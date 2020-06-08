package application;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import display.ChatWindow;
import display.LoginWindow;

public class WindowManager {
	
	private LoginWindow loginWindow;
	private ChatWindow chatWindow;
	private JFrame currentFrame;

	public WindowManager() {
		loginWindow = new LoginWindow();
		loginWindow.setVisible(true);
		currentFrame = loginWindow;
	}
	
	public void startChat() {
		if (chatWindow == null) chatWindow = new ChatWindow();
	}
	
	public void openChat() {
		chatWindow.setVisible(true);
		chatWindow.setLocationRelativeTo(loginWindow);
		loginWindow.clearTextFields();
		loginWindow.setEnabled(false);
		loginWindow.setVisible(false);
		currentFrame = chatWindow;
	}
	
	public void restart() {
		loginWindow.setVisible(true);
		loginWindow.setEnabled(true);
		loginWindow.setLocationRelativeTo(null);
		chatWindow.setVisible(false);
		chatWindow.clearSelection();
		currentFrame = loginWindow;
	}
	
	public void dialog(String title, String text, int type) {
		JOptionPane.showMessageDialog(currentFrame, text, title, type);
	}
	
	public void infoDialog(String text) {
		dialog("Information", text, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void infoDialog(String title, String text) {
		dialog(title, text, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void exceptionDialog(Exception e) {
		dialog("Error", "An error ocurred: "+e.getMessage(), JOptionPane.ERROR_MESSAGE);
	}
	
	public void errorDialog(String text) {
		dialog("Error", text, JOptionPane.ERROR_MESSAGE);
	}
	
	public void errorDialog(String title, String text) {
		dialog(title, text, JOptionPane.ERROR_MESSAGE);
	}
	
	public boolean confirmDialog(String title, String text, int type) {
		return JOptionPane.showConfirmDialog(currentFrame, text, title, type) == JOptionPane.YES_OPTION;
	}
	
	public boolean confirmDialog(String text) {
		return JOptionPane.showConfirmDialog(currentFrame, text) == JOptionPane.YES_OPTION;
	}
	
	public String getUserNameDialog() {
		return JOptionPane.showInputDialog(chatWindow, "Insert user name.", "Add User", JOptionPane.QUESTION_MESSAGE);
	}
	
	public String getPasswordDialog() {
		JPasswordField passwordField = new JPasswordField();
		if (JOptionPane.showConfirmDialog(currentFrame, passwordField, "Insert new password", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			return new String(passwordField.getPassword());
		}
		return null;
	}
	
	public String getContactSelection() {
		JList<String> list = new JList<String>(chatWindow.getContactListModel());
		list.clearSelection();
		JOptionPane.showConfirmDialog(currentFrame, list, "Select user", JOptionPane.OK_CANCEL_OPTION);
		return list.getSelectedValue();
	}
	
	public ChatWindow getChatWindow() {
		return chatWindow;
	}
}
