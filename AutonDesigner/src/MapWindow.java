import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.java.games.input.*;

public class MapWindow implements WindowListener, ActionListener {
	static JDialog dialog;
	JLabel imgLabel;
	BufferedImage img;
	Graphics2D g2d;
	JScrollPane instructionScrollPane;
	JTextArea instructionText;
	BorderLayout layout;
	JFileChooser fileChooser;
	JPanel buttonsPanel;
	ArrayList<Shape> mapShapes;// = MapReader.fromfile(new File("map.txt"));

	private enum Actions {
		LOAD_MAP, LOAD_INSTRUCTIONS, SAVE_INSTRUCTIONS
	}

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
		instructionText.setMaximumSize(new Dimension(401, height + 1));
		instructionText.setBackground(Color.LIGHT_GRAY);
		instructionScrollPane = new JScrollPane(instructionText);// handles text area scrolling
		instructionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		instructionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		// buttons
		buttonsPanel = new JPanel();
		JButton mapLoadButton = new JButton("Load Map");
		mapLoadButton.setActionCommand(Actions.LOAD_MAP.name());
		mapLoadButton.setToolTipText("Load map file");
		mapLoadButton.addActionListener(this);
		JButton instructionsSaveButton = new JButton("Save Instructions");
		instructionsSaveButton.setActionCommand(Actions.SAVE_INSTRUCTIONS.name());
		instructionsSaveButton.setToolTipText("Save robot movement instructions");
		instructionsSaveButton.addActionListener(this);
		buttonsPanel.add(mapLoadButton);
		buttonsPanel.add(instructionsSaveButton);
		// janky stuff so the log can scroll to bottom
		// add elements to window and finalize setup
		dialog.add(imgLabel, BorderLayout.CENTER);
		dialog.add(instructionText, BorderLayout.LINE_END);
		dialog.add(buttonsPanel, BorderLayout.PAGE_END);
		dialog.pack();
		dialog.addWindowListener(this);
		dialog.setVisible(true);
		g2d.setPaint(Color.black);
		g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
		// browse for file and draw
		fileChooser = new JFileChooser();
		FileNameExtensionFilter fileTypeFilter = new FileNameExtensionFilter("text files", "txt");
		fileChooser.setFileFilter(fileTypeFilter);
		// File mapFile;
		// int returnVal = fileChooser.showOpenDialog(mapFile);
		// Setup Joysticks
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller joystick = null;
		System.out.println(ce.getControllers().length + " devices found");
		for (int i = 0; i < ce.getControllers().length; i++) {
			System.out.println(ce.getControllers()[i].getType().toString());
			System.out.println("-" + ce.getControllers()[i].getName());
			if (ce.getControllers()[i].getType().toString() == "Stick") {
				joystick = ce.getControllers()[i];
				System.out.println("Found Joystick");
			}
		}
		//
		while (joystick != null) {
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

	public void mapFileOpen() {
		int returnVal = fileChooser.showOpenDialog(dialog);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println(fileChooser.getSelectedFile().getPath());
			mapShapes = MapReader.fromfile(fileChooser.getSelectedFile());
			MapDrawer.drawObjects(g2d, mapShapes, Color.GREEN, 1);
		}
	}
	
	public void saveInstructions(){
		//File generatedFile = new File();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand() == Actions.LOAD_MAP.name()) {
			mapFileOpen();
		} else if (arg0.getActionCommand() == Actions.LOAD_INSTRUCTIONS.name()) {

		} else if (arg0.getActionCommand() == Actions.SAVE_INSTRUCTIONS.name()) {

		}
		else{
			System.err.println("Unknown action has been preformed");
		}
	}

}
