import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.File;
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

public class MapWindow implements WindowListener, ActionListener, MouseListener, KeyListener {
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
	static double robotAngle = 0;
	static Point.Double robotPos = new Point.Double(100, 100);
	static ArrayList<Shape> currentPath = new ArrayList<Shape>();
	// ui colors
	static Color background = new Color(0xffffff);
	static Color mapLines = new Color(0x000000);
	static Color pathLines = new Color(0xff0000);
	//robot info
	static double length = 34;
	static double width = 40;

	private enum Buttons {
		LOAD_MAP("Load Map", buttonsPanel) {
			@Override
			public void onClick() {
				int returnVal = openDialog.showOpenDialog(dialog);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println(openDialog.getSelectedFile().getPath());
					mapShapes = MapReader.fromfile(openDialog.getSelectedFile());
					MapDrawer.drawObjects(g2d, mapShapes, mapLines, background, 1);
				}
			}
		},
		LOAD_INSTRUCTIONS("Load Instruction", buttonsPanel) {
			@Override
			public void onClick() {
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
				int retrival = openDialog.showSaveDialog(dialog);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						FileWriter fw = new FileWriter(openDialog.getSelectedFile());
						instructionText.write(fw);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		},
		PLOT_LINES("Plot Lines", instructionButtonsPanel) {
			@Override
			public void onClick() {
				MapDrawer.drawObjects(g2d, mapShapes, mapLines, background, 1);
				ArrayList<Shape> shapes = new ArrayList<Shape>();
				Scanner in = new Scanner(instructionText.getText());
				while (in.hasNext()) {
					ArrayList<Point.Double> pointslist = new ArrayList<Point.Double>();
					String line = in.nextLine();
					String[] points = line.split(" ");
					for (String str : points) {
						String[] xy = str.split(",");
						Point.Double point = new Point.Double(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
						pointslist.add(point);
					}
					Path2D.Double path = new Path2D.Double();
					path.moveTo(pointslist.get(0).getX(), pointslist.get(0).getY());
					for (int i = 1; i < pointslist.size(); i++) {
						path.lineTo(pointslist.get(i).getX(), pointslist.get(i).getY());
					}
					// path.closePath();
					shapes.add(path);
				}
				in.close();
				currentPath.clear();
				for (Shape s : shapes) {
					currentPath.add(s);
				}
				MapDrawer.drawObjects(g2d, shapes, pathLines, new Color(1f, 0f, 0f, .5f), 2);

			}
		},
		TRACE_PATH("Trace", instructionButtonsPanel) {
			@Override
			public void onClick() {
				for (Shape s : currentPath) {
					//((Path2D.Double) s).
					//Robot r = new Robot(path., startAngle, length, width);
				}
			}
		},
		SAVE_IMAGE("Export Image", instructionButtonsPanel) {
			@Override
			public void onClick() {
				
			}
		},
		CLEAR("Clear", instructionButtonsPanel) {

			@Override
			public void onClick() {
				g2d.setBackground(background);
				g2d.clearRect(0, 0, img.getWidth(), img.getHeight());
				MapDrawer.drawObjects(g2d, mapShapes, mapLines, background, 1);
				dialog.repaint();
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
		imgLabel.addMouseListener(mapWindow);

		// add elements to window and finalize setup
		dialog.add(imgLabel, BorderLayout.CENTER);
		dialog.add(instructionPanel, BorderLayout.LINE_END);
		dialog.add(buttonsPanel, BorderLayout.PAGE_END);
		dialog.pack();
		dialog.addWindowListener(mapWindow);
		dialog.addKeyListener(mapWindow);
		dialog.setVisible(true);
		g2d.setBackground(background);
		g2d.clearRect(0, 0, img.getWidth(), img.getHeight());
		// File open/save dialog
		openDialog = new JFileChooser();
		FileNameExtensionFilter fileTypeFilter = new FileNameExtensionFilter("text files", "txt");
		openDialog.setFileFilter(fileTypeFilter);
		openDialog.setPreferredSize(new Dimension(600, 500));

		// load default map
		mapShapes = MapReader.fromfile(new File("map.txt"));
		MapDrawer.drawObjects(g2d, mapShapes, mapLines, background, 1);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.printf("%s,%s\n", e.getX(), e.getY());
		if (e.isShiftDown()) {
			instructionText.append(String.format("%s,%s ", e.getX(), e.getY()));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
