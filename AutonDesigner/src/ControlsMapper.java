import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class ControlsMapper implements WindowListener, ActionListener {
	static JDialog dialog;
	static JPanel UIHeader;
	static JPanel UIBody;
	static JPanel UIFooter;
	static JComboBox<Object> devices;
	static ControllerEnvironment ce;
	static JLabel title;
	static JPanel components;
	static Controller device;
	public static void showUI() {
		// initial setup
		ControlsMapper controlsMapper = new ControlsMapper();
		dialog = new JDialog();
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(controlsMapper);
		dialog.setLayout(new BorderLayout());
		dialog.setVisible(true);
		// main UI segments layout
		UIHeader = new JPanel();
		UIHeader.setBackground(Color.RED);
		UIBody = new JPanel();
		UIBody.setBackground(Color.GREEN);
		UIBody.setLayout(new BoxLayout(UIBody,BoxLayout.Y_AXIS));
		UIFooter = new JPanel();
		UIFooter.setBackground(Color.BLUE);
		dialog.add(UIHeader, BorderLayout.PAGE_START);
		dialog.add(UIFooter, BorderLayout.PAGE_END);
		dialog.add(UIBody, BorderLayout.CENTER);
		// device selection
		ce = ControllerEnvironment.getDefaultEnvironment();
		devices = new JComboBox<Object>(ce.getControllers());
		JButton selectDevice = new JButton();
		selectDevice.setText("select");
		selectDevice.setActionCommand("select");
		selectDevice.addActionListener(controlsMapper);
		UIHeader.add(devices);
		UIHeader.add(selectDevice);
	}

	private void displayDevice(Controller device) {
		ControlsMapper cm2 = new ControlsMapper();
		UIBody.removeAll();
		try {
			components.removeAll();
		} catch (NullPointerException e) {
		}
		title = new JLabel();
		title.setText(device.toString());
		title.setFont(new Font("Dialog", Font.PLAIN, 20));
		UIBody.add(title);
		this.device=device;
		JScrollPane componentScroll = new JScrollPane();
		componentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		components = new JPanel();
		components.setMinimumSize(new Dimension(1,1));
		components.setPreferredSize(new Dimension(300,500));
		components.setMaximumSize(new Dimension(500,500));
		components.setLayout(new FlowLayout());
		UIBody.add(componentScroll);
		componentScroll.setViewportView(components);
		for (net.java.games.input.Component c : device.getComponents()) {// used full name b/c awt also has component
			JButton b = new JButton();
			b.setText(c.getName());
			b.setActionCommand("CONTROLS"+c.getIdentifier().toString());
			b.addActionListener(cm2);
			components.add(b);
		}
		dialog.pack();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?", "Confirm", JOptionPane.YES_NO_OPTION);
		if (confirmed == JOptionPane.YES_OPTION) {
			dialog.dispose();
		}
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
		dialog.pack();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().toString()=="select"){
			displayDevice((Controller) devices.getSelectedItem());
		}
		else if(arg0.getActionCommand().toString().length()>=8 && arg0.getActionCommand().toString().substring(0,8).equals("CONTROLS")){
			String command = arg0.getActionCommand().toString().substring(8);
			System.out.println(command);
			System.out.println();
		}
	}
}
