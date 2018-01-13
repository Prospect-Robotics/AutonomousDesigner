import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.OptionalDouble;
import java.util.Scanner;
import java.util.function.Supplier;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class MapWindow implements WindowListener, ActionListener {
	static JDialog dialog;
	static JLabel imgLabel;
	static BufferedImage img;
	static Graphics2D g2d;
	static JPanel instructionPanel;
	static JScrollPane instructionScrollPane;
	static JTextArea instructionText;
	static JPanel instructionButtonsPanel;
	static BorderLayout layout;
	static JFileChooser openDialog;
	static JPanel buttonsPanel;
	static ArrayList<Shape> mapShapes = new ArrayList<Shape>();
	static FileNameExtensionFilter filterScript = new FileNameExtensionFilter("Autonomous Designer Script", "atds");
	static FileNameExtensionFilter filterMap = new FileNameExtensionFilter("Autonomous Designer Map", "atdm");
	static double robotAngle = 0;
	static Point.Double robotPos = new Point.Double(100, 100);
	static FakeBot robot = new FakeBot(robotPos, robotAngle, 30, 20);

	private enum Buttons {
		LOAD_MAP("Load Map", buttonsPanel) {
			@Override
			public void onClick() {
				openDialog.setFileFilter(filterMap);
				int returnVal = openDialog.showOpenDialog(dialog);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println(openDialog.getSelectedFile().getPath());
					mapShapes = MapReader.fromfile(openDialog.getSelectedFile());
					MapDrawer.drawObjects(g2d, mapShapes, Color.GREEN, Color.BLACK, 1);
				}
			}
		},
		LOAD_INSTRUCTIONS("Load Instruction", buttonsPanel) {
			@Override
			public void onClick() {
				openDialog.setFileFilter(filterScript);
				int returnVal = openDialog.showOpenDialog(dialog);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						FileReader reader = new FileReader(openDialog.getSelectedFile());
						instructionText.read(reader, null);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		},
		SAVE_INSTRUCTIONS("Save Instruction", buttonsPanel) {
			@Override
			public void onClick() {
				openDialog.setFileFilter(filterScript);
				int retrival = openDialog.showSaveDialog(dialog);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						FileWriter fw = new FileWriter(openDialog.getSelectedFile() + ".atdm");
						instructionText.write(fw);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		},
		PLAYBACK_INSTRUCTIONS("Play", instructionButtonsPanel) {
			@Override
			public void onClick() {
				MapDrawer.drawObjects(g2d, mapShapes, Color.GREEN, Color.BLACK, 1);
				MapDrawer.drawRobot(g2d, robot, Color.magenta, 1);
				
			}
		},
		OPEN_KEYMAPPER("Open Controlls Mapper", buttonsPanel) {

			@Override
			public void onClick() {
				ControlsMapper.showUI();
			}

		};
		private String label;
		private JPanel panel;

		Buttons(String label, JPanel panel) {
			this.label = label;
			this.panel = panel;
		}

		public abstract void onClick();
	}

	public static void showUI() {
		MapWindow mapWindow = new MapWindow();
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
		instructionPanel = new JPanel();
		instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
		instructionText = new JTextArea(1, 1);// text area containing instructions
		instructionText.setEditable(true);
		instructionText.setBackground(Color.LIGHT_GRAY);
		instructionScrollPane = new JScrollPane(instructionText);// handles text area scrolling
		instructionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		instructionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		instructionPanel.add(instructionScrollPane);
		instructionButtonsPanel = new JPanel();
		instructionPanel.setPreferredSize(new Dimension(400, height));
		instructionButtonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		instructionPanel.add(instructionButtonsPanel);
		// buttons
		buttonsPanel = new JPanel();
		for (Buttons b : Buttons.values()) {
			JButton button = new JButton(b.toString());
			button.setText(b.label);
			button.addActionListener(mapWindow);
			button.setActionCommand(b.toString());
			b.panel.add(button);
		}

		// add elements to window and finalize setup
		dialog.add(imgLabel, BorderLayout.CENTER);
		dialog.add(instructionPanel, BorderLayout.LINE_END);
		dialog.add(buttonsPanel, BorderLayout.PAGE_END);
		dialog.pack();
		dialog.addWindowListener(mapWindow);
		dialog.setVisible(true);
		g2d.setPaint(Color.black);
		g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
		// File open/save dialog
		openDialog = new JFileChooser();
		FileNameExtensionFilter fileTypeFilter = new FileNameExtensionFilter("text files", "txt");
		openDialog.setFileFilter(fileTypeFilter);
		openDialog.setPreferredSize(new Dimension(600, 500));
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
				for (Component c : joystick.getComponents()) {
					System.out.println(" " + c);
					System.out.println(c.getIdentifier());
				}
			}
		}
		//
		while (joystick != null) {
			if (joystick != null) {
				joystick.poll();
				EventQueue queue = joystick.getEventQueue();
				Event event = new Event();
				while (queue.getNextEvent(event)) {
					switch (event.getComponent().getName()) {
					default:
						System.out.println(event.getComponent().getName());
						System.out.println(event.getComponent().getPollData());
						break;
					case ("X Axis"):
						break;
					case ("Y Axis"):
						break;
					case ("Z Rotation"):
						robotAngle += event.getValue();
						System.out.println(robotAngle);
						MapDrawer.drawObjects(g2d, mapShapes, Color.GREEN, Color.BLACK, 1);
						break;
					}

				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void playInstructions(){
		Supplier<Exception> noDefaultSpeed = () -> new Exception("You forgot to set the default speed!");
		try (Scanner instructionsFeed = new Scanner(instructionText.getText())) {
			OptionalDouble defaultSpeed = OptionalDouble.empty();
			while (instructionsFeed.hasNext()) {
				String command = instructionsFeed.next().toLowerCase();
				switch (command) {
				case "drive":
					double distance = instructionsFeed.nextDouble();
					double speed = instructionsFeed.hasNextDouble() ? instructionsFeed.nextDouble() : defaultSpeed.orElseThrow(noDefaultSpeed);
					break;
				case "rotate":
					double degrees = instructionsFeed.nextDouble();
					speed = instructionsFeed.hasNextDouble() ? instructionsFeed.nextDouble() : defaultSpeed.orElseThrow(noDefaultSpeed);
					break;
				case "cdrive":
					degrees = instructionsFeed.nextDouble();
					// double turnRadiusAsWheelSpeedRatio =
					break;
				case "cfg":
					break;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {

	}

	@Override
	public void windowIconified(WindowEvent arg0) {

	}

	@Override
	public void windowOpened(WindowEvent arg0) {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			Buttons.valueOf(arg0.getActionCommand()).onClick();
		} catch (java.lang.IllegalArgumentException e) {

		}
	}
}
