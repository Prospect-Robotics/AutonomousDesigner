import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.java.games.input.*;

public class MapWindow implements WindowListener {
	JDialog dialog;
	JLabel imgLabel;
	BufferedImage img;
	Graphics2D g2d;
	JScrollPane instructionScrollPane;
	JTextArea instructionText;
	static JScrollPane logScrollPane;
	static JTextArea logText;
	BorderLayout layout;
	static JLabel logScrollHelper;
	ArrayList<Shape> mapShapes = MapReader.fromfile(new File("map.txt"));

	public MapWindow() {
		System.out.println("There will be a few errors from jinput but just ignore them because it still works");
		int width = 888;
		int height = 360;
		// setup main window and layout
		dialog = new JDialog();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		layout = new BorderLayout();// helps set where window components are
		dialog.setLayout(layout);
		// setup map view
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);// image drawn to for map
		g2d = img.createGraphics();// graphics interface for map
		imgLabel = new JLabel(new ImageIcon(img));// contains map, used to more easily get mouse coordinates
		// setup instruction edit area
		instructionText = new JTextArea(1, 1);// text area containing instructions
		instructionText.setEditable(true);
		instructionText.setMinimumSize(new Dimension(100, height));// configure size and scaling for text area
		instructionText.setPreferredSize(new Dimension(400, height));
		instructionText.setMaximumSize(new Dimension(9999, height));
		instructionText.setBackground(Color.LIGHT_GRAY);
		instructionScrollPane = new JScrollPane(instructionText);// handles text area scrolling
		instructionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		instructionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		// setup error/warning log
		logText = new JTextArea(1, 1);// text area containing instructions
		// logText.setEditable(false);
		// logT ext.setMinimumSize(new Dimension(width,25));//configure size and
		// scaling for text area
		// logText.setPreferredSize(new Dimension(width,100));
		// logText.setMaximumSize(new Dimension(999,999));
		logScrollPane = new JScrollPane();// handles text area scrolling
		logScrollPane.setMinimumSize(new Dimension(width, 25));// configure size and scaling for text area
		logScrollPane.setPreferredSize(new Dimension(width, 100));
		logScrollPane.setMaximumSize(new Dimension(999, 999));
		logScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		logScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		logScrollPane.setViewportView(logText);
		//janky stuff so the log can scroll to bottom
		logScrollHelper = new JLabel();
		logScrollHelper.setMinimumSize(new Dimension(25,25));
		logScrollHelper.setPreferredSize(new Dimension(50,50));
		logScrollHelper.setMaximumSize(new Dimension(100,100));
		logScrollHelper.setBackground(Color.green);
		logScrollPane.add(logScrollHelper);
		// add elements to window and finalize setup
		dialog.add(imgLabel, BorderLayout.CENTER);
		dialog.add(instructionText, BorderLayout.LINE_END);
		dialog.add(logScrollPane, BorderLayout.PAGE_END);
		dialog.pack();
		dialog.addWindowListener(this);
		dialog.setVisible(true);
		g2d.setPaint(Color.black);
		g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
		MapDrawer.drawObjects(g2d, mapShapes, Color.GREEN, 1);
		// Setup Joysticks
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller joystick = null;
		for (int i = 0; i < ce.getControllers().length; i++) {
			log(ce.getControllers()[i].getName());
			if (ce.getControllers()[i].getType().toString() == "Stick") {
				joystick = ce.getControllers()[i];
				log("Found Joystick");
			}
		}
		//
		while (joystick!=null) {
			if (joystick != null) {
				EventQueue queue = joystick.getEventQueue();
				Event event = new Event();
				while (queue.getNextEvent(event)) {
					System.out.println(event.getComponent());
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void log(String text) {
		logText.append("\n"+text);
		logScrollPane.scrollRectToVisible(logScrollHelper.getBounds());
	}

}
