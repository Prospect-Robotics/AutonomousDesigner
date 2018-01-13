import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
/*
 * THIS ISN'T USED ANYMORE BUT I'M STILL GOING TO USE SOME OF THE CODE OUT OF IT SO JUST IGNORE THIS FOR NOW
 */
public class ModeSelect implements ActionListener {
	public enum Modes {
		MAP_VIEW {

			@Override
			public void openWindow() {
				MapWindow.showUI();
			}
			
		};
		public abstract void openWindow();	
	}

	public static void showUI() {
		// overall window setup
		ModeSelect modeSelect = new ModeSelect();
		JDialog dialog = new JDialog();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		dialog.setLayout(layout);
		// header
		JLabel title = new JLabel();
		title.setText("Select Mode:");
		title.setFont(new Font("Dialog", Font.PLAIN, 20));
		// buttons
		JPanel buttonsArea = new JPanel();
		buttonsArea.setLayout(new BoxLayout(buttonsArea, BoxLayout.Y_AXIS));
		for (Modes mode : Modes.values()) {
			JButton button = new JButton(mode.toString());
			button.addActionListener(modeSelect);
			buttonsArea.add(button);
		}
		// combine items & do final setup
		dialog.add(title, BorderLayout.PAGE_START);
		dialog.add(buttonsArea, BorderLayout.CENTER);
		dialog.pack();
		dialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(Modes.valueOf(arg0.getActionCommand())!=null){
			Modes.valueOf(arg0.getActionCommand()).openWindow();
		}
	}
}
