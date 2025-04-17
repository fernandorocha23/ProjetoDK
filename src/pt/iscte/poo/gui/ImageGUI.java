package pt.iscte.poo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
// import java.awt.event.MouseEvent;
// import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.utils.Point2D;

/**
 * @author POO 2024
 * <p>
 * ImageGUI, manages and displays in its main window a grid of
 * square images (implementations of ImageTile) of the same size. The
 * default image-size is 48x48 and the default grid-size is 10x10.
 * <p>
 * This class is observable and will send an update message everytime a
 * key is pressed.
 * <p>
 * ImageGUI also includes a ticket that notifies the observable on time-based interactions.
 * <p>
 * The images that ImageTiles refer to MUST be in a folder called
 * "images" directly under the project folder.

 * <p>
 * ImageTile is required to provide the name of the image (e.g.
 * "XxxX") and its position (in tile coordinates, with 0,0 in the top
 * left corner and increasing to the right in the horizontal axis and
 * downwards in the vertical axis). ImageMatrixGUI will look for an
 * image with that name in the "images" folder (e.g. "XxxX.png") and draw
 * that image in the window.
 * <p>
 * ImageMatrixGUI implements the CoisasQueMexem pattern.
 **/

/**
 * @author lmmn
 */

//Changed to local Observer-Observed pattern 27-Set-2018
public class ImageGUI extends Observed {

    private static final int LABEL_HEIGHT = 20;

    private static final long TICK_TIME = 500;

	private static ImageGUI INSTANCE;

    static final String IMAGE_DIR = "images";

    final int tileWidth = 48;
    final int tileHeight = 48;
    
    private int width = 480;
    private int height = 480;

    private JFrame frame;
    private JPanel panel;
    private JLabel info;

    private Map<String, ImageIcon> imageDB = new HashMap<>();

    private List<ImageTile> images = new ArrayList<>();

    // private Point2D lastMouseCoordinate;
    private boolean mouseClicked;

    private int lastKeyPressed;
    private boolean keyPressed;
    private boolean windowClosed = false; // Added 25-oct-2022

    private int maxLevel;

	private KeyWatcher keywatcher; // added dec 2022

	private Ticker ticker; // added jul 2024
	
	private int ticks = 0; // added jul 2024

    private ImageGUI() {
        init();
    }

    /**
     * @return Access to the CoisasQueMexem instance of ImageMatrixGUI
     */
    public static ImageGUI getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ImageGUI();
        return INSTANCE;
    }

    /**
     * Setter for the name of the frame
     *
     * @param name Name of application (will be displayed as a frame title in the
     *             top left corner)
     */

    public void setName(final String name) {
        frame.setTitle(name); // Corrected 2-Mar-2016
    }

    private void init() {
        frame = new JFrame();
        panel = new DisplayWindow();
        info = new JLabel();

        panel.setPreferredSize(new Dimension(width, height));
        info.setPreferredSize(new Dimension(width, LABEL_HEIGHT));
//		panel.setPreferredSize(new Dimension(N_SQUARES_WIDTH * SQUARE_SIZE, N_SQUARES_HEIGHT * SQUARE_SIZE));
//		info.setPreferredSize(new Dimension(N_SQUARES_WIDTH * SQUARE_SIZE, LABEL_HEIGHT));
        info.setBackground(Color.BLACK);
        frame.add(panel);
        frame.add(info, BorderLayout.NORTH);
        frame.pack();
        frame.setResizable(false); // Added 27-Feb-2018
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.addWindowListener(new WindowAdapter() { // Added 25-oct-2022
            public void windowClosing(WindowEvent e) {
            	windowClosed = true;
            	notifyObservers();
            }
        });
        
        //sets the IconImage on windows, doesn't work on macOS
        ImageIcon icon = new ImageIcon("icon/Game_Icon.png");
        frame.setIconImage(icon.getImage());

        initImages();

        keywatcher = new KeyWatcher(); 
        keywatcher.start();

//		new MouseWatcher().start();

		ticker = new Ticker();
		ticker.start();

        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                lastKeyPressed = e.getKeyCode();
                keyPressed = true;
                releaseObserver();
            }
        });
    }

    synchronized void releaseObserver() {
        notifyAll();
    }

    synchronized void waitForKey() throws InterruptedException {
        while (!keyPressed) {
            wait();
        }
        notifyObservers();
        keyPressed = false;
    }

    synchronized void waitForClick() throws InterruptedException {
        while (!mouseClicked) {
            wait();
        }
        notifyObservers();
        mouseClicked = false;
    }

    synchronized void tick() throws InterruptedException {
        ticks = getTicks() + 1;
        notifyObservers();
    }

    private void initImages() {
        File dir = new File(IMAGE_DIR);
        if (dir == null) {
            //throw new FileNotFoundException();
            System.err.println("Images dir not found");
            return;
        }
        for (File f : dir.listFiles()) {
            assert (f.getName().lastIndexOf('.') != -1);
            imageDB.put(f.getName().substring(0, f.getName().lastIndexOf('.')),
                    new ImageIcon(IMAGE_DIR + "/" + f.getName()));
        }
    }

    /**
     * Make the window visible.
     */
    public void go() {
        frame.setVisible(true);
    }

    /**
     * Add a new set of images to the main window.
     *
     * @param newImages images to be added to main window
     * @throws IllegalArgumentException if no image with that name (and a suitable extension) is
     *                                  found the images folder
     */

    public void addImages(final List<? extends ImageTile> newImages) {
        synchronized (images) { // Added 16-Mar-2016
            if (newImages == null)
                throw new IllegalArgumentException("Null list");
            if (newImages.size() == 0)
                return;
            for (ImageTile i : newImages) {
                if (i == null)
                    throw new IllegalArgumentException("Null image");
                if (!imageDB.containsKey(i.getName())) {
                    throw new IllegalArgumentException("No such image in DB " + i.getName());
                }
                addImage(i);
            }
        }
    }

    // Added 2-Mar-2016

    /**
     * Removes the image given as a parameter.
     * <p>
     * Does nothing if there is no match.
     *
     * @param image to be removed (must be the exact same Object and not a copy)
     */

    public void removeImage(final ImageTile image) {
        if (image == null)
            throw new IllegalArgumentException("Null list");
        synchronized (images) { // Added 16-Mar-2016
            images.remove(image);
        }
    }

    // Added 2-Out-2017

    /**
     * Removes the image given as a parameter.
     * <p>
     * Does nothing if there is no match.
     *
     * @param images_to_remove images to be removed (must be the exact same Object and not a copy)
     */

    public void removeImages(final List<? extends ImageTile> images_to_remove) {
        if (images_to_remove == null)
            throw new IllegalArgumentException("Null list");
        synchronized (images) {
            images.removeAll(images_to_remove);
        }
    }

    // Added 2-Mar-2016

    /**
     * Adds image to main window
     *
     * @param image to be added
     */
    public void addImage(final ImageTile image) {
        synchronized (images) { // Added 16-Mar-2016
            if (image == null)
                throw new IllegalArgumentException("Null image");
            if (image.getName() == null)
                throw new IllegalArgumentException("Null image name");
            if (image.getPosition() == null)
                throw new IllegalArgumentException("Null image position");
            if (image.getLayer() >= maxLevel)
                maxLevel = image.getLayer() + 1;
            if (!imageDB.containsKey(image.getName())) {
                throw new IllegalArgumentException("No such image in DB " + image.getName());
            }
            if (images.contains(image)) {
                System.err.println("Attempting to register repeated image: " + image + " (ignoring)");
                return;
            }
            images.add(image);
        }
    }

    /**
     * Clear all images displayed in main window.
     */
    public void clearImages() {
        synchronized (images) { // Added 16-Mar-2016
            images.clear();
        }
    }

    /**
     * Set a message on the status bar.
     *
     * @param message message to be added to status bar
     *
     */

    public void setStatusMessage(String message) {
    	info.setHorizontalAlignment(SwingConstants.LEFT);
    	info.setVerticalAlignment(SwingConstants.CENTER);
        info.setText(message);
    }
   
    public void showMessage(String title, String message) {
		JOptionPane.showMessageDialog(panel, message, title, JOptionPane.DEFAULT_OPTION);
    }
    
    public String showInputDialog(String title, String message) {
    	return JOptionPane.showInputDialog(panel, message, title, JOptionPane.DEFAULT_OPTION);
    }

    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
    
    @SuppressWarnings("serial") // Added 2-Mar-2016
    private class DisplayWindow extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            // System.out.println("Thread " + Thread.currentThread() + " repainting");
            synchronized (images) { // Added 16-Mar-2016
                for (int j = 0; j != maxLevel; j++)
                    for (ImageTile i : images) {
                        if (i.getLayer() == j) {
                        	Image img = imageDB.get(i.getName()).getImage();
                        	g.drawImage(img, i.getPosition().getX()*tileWidth, i.getPosition().getY()*tileHeight, frame);
                        }
                    }
            }
        }
    }

    private class KeyWatcher extends Thread {
        private boolean end = false; // added dec 2022

		public void run() {
            try {
                while (!end)
                    waitForKey();
            } catch (InterruptedException e) {
            }
        }

		public void end() { // added dec 2022
			end = true;
		}
    }

	private class Ticker extends Thread {
		
		public void run() {
			try {
				while (true) {
					sleep(TICK_TIME);
					tick();
				}
			} catch (InterruptedException e) {
			}
		}
	}

    /**
     * Force scheduling of a new window paint (this may take a while, it does
     * not necessarily happen immediately after this instruction is issued)
     */
    public void update() {
        frame.repaint();
    }

    /**
     * Terminate window GUI
     */
    public void dispose() {
        images.clear();
        imageDB.clear();
        frame.dispose();
        keywatcher.end(); // added dec 2022
    }

    /**
     * Grid dimensions
     *
     * @return the width and height of the image grid
     */
    public Dimension getGridDimension() {
//		return new Dimension(N_SQUARES_WIDTH, N_SQUARES_HEIGHT);
        return new Dimension(width, height);
    }

    public void setSize(int i, int j) {
        width = i;
        height = j;
        if (INSTANCE != null) {
            //This is a workaround to allow dynamic resizing
            panel.setPreferredSize(new Dimension(width, height + ImageGUI.LABEL_HEIGHT));
            info.setPreferredSize(new Dimension(width, ImageGUI.LABEL_HEIGHT));
            //frame.setSize(INSTANCE.frame.getPreferredSize());
            frame.pack();
            frame.setResizable(false); // Added 27-Feb-2018
        }
    }

    public boolean isWithinBounds(Point2D p) {
        return p.getX() >= 0 && p.getY() >= 0 && p.getX() <= width && p.getY() <= height;
    }

    public synchronized int keyPressed() {
        return lastKeyPressed;
    }

    public synchronized boolean wasWindowClosed() { // Added 25-out-2022
        return windowClosed;
    }
    
    public String askUser(String question) { // Added 25-out-2022
    	return JOptionPane.showInputDialog(question);
    }

	public boolean wasKeyPressed() {
		return keyPressed;
	}

	public int getTicks() {
		return ticks;
	}
    
}
