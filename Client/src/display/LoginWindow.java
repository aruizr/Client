package display;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import application.ClientController;

import javax.swing.JPasswordField;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;
	
	public LoginWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				initialize();
			}
		});
	}

	public void initialize() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 290, 160);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Insert username:");
		lblNewLabel.setBounds(10, 11, 254, 14);
		contentPane.add(lblNewLabel);
		
		usernameField = new JTextField();
		usernameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.grabFocus();
			}
		});
		usernameField.setBounds(10, 36, 254, 20);
		contentPane.add(usernameField);
		usernameField.setColumns(10);
		
		JLabel lblInsertPassword = new JLabel("Insert password:");
		lblInsertPassword.setBounds(10, 67, 254, 14);
		contentPane.add(lblInsertPassword);
		
		passwordField = new JPasswordField();
		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Create login request
				ClientController.getInstance().loginRequest(usernameField.getText(), new String(passwordField.getPassword()));
			}
		});
		passwordField.setBounds(10, 92, 254, 20);
		contentPane.add(passwordField);
		
		setLocationRelativeTo(null);
	}
	
	public synchronized void clearTextFields() {
		usernameField.grabFocus();
		usernameField.setText(null);
		passwordField.setText(null);
	}
}
