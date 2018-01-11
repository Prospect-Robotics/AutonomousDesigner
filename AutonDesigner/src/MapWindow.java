import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class MapWindow implements WindowListener {
	JDialog dialog;
	JPanel panel;
	JLabel imgLabel;
	BufferedImage img;
	Graphics2D g2d;
	JScrollPane scrollPane;
	JTextArea text;
	GridBagLayout layout;
	public MapWindow() {
		int width=888;
		int height=360;
		dialog = new JDialog();//main window
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//main window close instructions
		layout = new GridBagLayout();//layout, sets up where window components are
		panel = new JPanel();//panel, makes stuff work with layout
		panel.setLayout(layout);
		img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);//image drawn to for map
		g2d = img.createGraphics();//graphics interface for map
		imgLabel = new JLabel(new ImageIcon(img));//contains map, used to more easily get mouse coordinates
		text = new JTextArea();//text area containing instructions
		text.setEditable(true);
		text.setMinimumSize(new Dimension(50,width));
		scrollPane = new JScrollPane(text);//handles text area scrolling
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(imgLabel);
		panel.add(text);
		dialog.add(panel);
		dialog.pack();
		dialog.addWindowListener(this);
		dialog.setVisible(true);
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
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
}
