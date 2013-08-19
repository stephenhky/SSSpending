package home.kwyho.google.ss.finance.gui;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class JAuthenticationPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5261163066202408731L;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JPasswordField jPassword;
	private JTextField jGmailAddr;
	
	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		/*
		JFrame frame = new JFrame();
		frame.getContentPane().add(new JAuthenticationPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		*/
		JAuthenticationPanel panel = new JAuthenticationPanel();
		int option = JOptionPane.showConfirmDialog(null, panel, "Authentication", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			System.out.println(panel.getGmailAddr()+" | "+panel.getPassword());
		} else {
			System.out.println("Cancelled");
		}
	}
	
	public JAuthenticationPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 100));
			{
				setLayout(null);
				{
					jLabel1 = new JLabel();
					add(jLabel1);
					jLabel1.setText("GMail Address");
					jLabel1.setBounds(12, 18, 144, 15);
				}
				{
					jLabel2 = new JLabel();
					add(jLabel2);
					jLabel2.setText("Password");
					jLabel2.setBounds(12, 51, 144, 15);
				}
				{
					jGmailAddr = new JTextField();
					add(jGmailAddr);
					jGmailAddr.setText("kwanyuetho@gmail.com");
					jGmailAddr.setBounds(162, 15, 211, 22);
				}
				{
					jPassword = new JPasswordField();
					add(jPassword);
					jPassword.setText("sosad");
					jPassword.setBounds(162, 48, 211, 22);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getGmailAddr() {
		return jGmailAddr.getText();
	}
	
	public String getPassword() {
		return new String(jPassword.getPassword());
	}

}
