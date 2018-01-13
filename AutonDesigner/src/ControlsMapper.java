import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class ControlsMapper implements WindowListener, ActionListener {
	static JDialog dialog;
	static JPanel UIHeader;
	static JPanel UIBody;
	static JPanel UIFooter;
	static JComboBox<Controller> devices;
	static JComboBox<Object> deviceDropdown;
	static ControllerEnvironment ce;
	static JLabel deviceTitle;
	static JLabel componentTitle;
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
		devices = new JComboBox<Controller>(ce.getControllers());
		JButton selectDevice = new JButton();
		selectDevice.setText("select");
		selectDevice.setActionCommand("controller_select");
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
		deviceTitle = new JLabel();
		deviceTitle.setText(device.toString());
		deviceTitle.setFont(new Font("Dialog", Font.PLAIN, 20));
		UIBody.add(deviceTitle);
		this.device=device;
		deviceDropdown = new JComboBox<Object>(device.getComponents());
		UIBody.add(deviceDropdown);
		JButton selectDevice = new JButton();
		selectDevice.setActionCommand("button_select");
		selectDevice.addActionListener(cm2);
		selectDevice.setText("Select");
		UIBody.add(selectDevice);
		dialog.pack();
	}
	
	private void displayComponent(Object object){
		ControlsMapper cm3 = new ControlsMapper();
		UIFooter.removeAll();
		componentTitle = new JLabel();
		componentTitle.setText(object.toString());
		componentTitle.setFont(new Font("Dialog", Font.PLAIN, 20));
		UIFooter.add(componentTitle);
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
		if(arg0.getActionCommand().toString()=="controller_select"){
			displayDevice((Controller) devices.getSelectedItem());
		}
		else if(arg0.getActionCommand().toString()=="button_select"){
			displayComponent(deviceDropdown.getSelectedItem());
		}

	}
}
