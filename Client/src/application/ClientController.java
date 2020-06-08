package application;

import java.io.IOException;

import javax.swing.JOptionPane;

import domain.Formatter;
import domain.Message;
import domain.MessageType;
import domain.Settings;
import domain.User;

public class ClientController {
	
	private static ClientController instance;
	
	private ClientCommunicator clientCommunicator;
	private WindowManager windowManager;
	private User currentUser;
	
	public static void main(String[] args) {
		getInstance();
	}
	
	public static synchronized ClientController getInstance() {
		if (instance == null) instance = new ClientController();
		return instance;
	}

	public ClientController() {
		windowManager = new WindowManager();
		try {
			Settings.loadProperties();
			clientCommunicator = new ClientCommunicator();
		} catch (IOException e) {
			windowManager.exceptionDialog(e);
		}
	}
	
	//Message handling methods------------------------------------------------------------------------------------------------------
	
	public void messageReceived(Message message) {		
		switch (message.getMessageType()) {
		case LOGIN_REPLY:
			loginReply(message);
			break;
			
		case ADDING_REQUEST:
			addingRequest(message);
			break;
			
		case ADDING_FORWARDING:
			addingForwarding(message);
			break;
			
		case ADDING_REPLY:
			addingReply(message);
			break;
			
		case ADDING_REMOVE:
			addingRemove(message);
			break;
			
		case USER_MESSAGE:
			userMessage(message);
			break;
			
		case USER_MESSAGE_FORWARDING:
			messageForwarding(message);
			break;
			
		case USER_UPDATE_REQUEST:
			updateRequest(message);
			break;
			
		case SERVER_MESSAGE:
			serverMessage(message);
			break;

		default:
			break;
		}
	}

	private void loginReply(Message message) {
		if (message.getCondition()) {
			windowManager.startChat();
			currentUser = (User) message.getAdditionalData();
			windowManager.infoDialog(message.getContent());
			windowManager.openChat();
			updateWindow();
			send(new Message(MessageType.LOGIN_CONFIRMATION));
		}
		else windowManager.errorDialog(message.getContent());
	}
	
	private void addingRequest(Message message) {
		Message reply = new Message(MessageType.ADDING_REPLY);
		reply.setDestinationName(message.getSourceName());
		if (windowManager.confirmDialog("Adding request received", "User "+message.getSourceName()+" wants to add you, accept?", JOptionPane.YES_NO_OPTION)) {
			currentUser.addContact(message.getSourceName());
			updateWindow();
			reply.setCondition("true");
		}
		else reply.setCondition("false");
		send(reply);
	}

	private void addingForwarding(Message message) {
		if (message.getCondition()) windowManager.infoDialog(message.getContent());
		else windowManager.errorDialog(message.getContent());
	}

	private void addingReply(Message message) {
		if (message.getCondition()) {
			windowManager.infoDialog("User "+message.getSourceName()+" has accepted your adding request!");
			currentUser.addContact(message.getSourceName());
			updateWindow();
		}
		else windowManager.infoDialog("User "+message.getSourceName()+" has rejected your adding request.");
	}
	
	private void addingRemove(Message message) {
		if (currentUser.hasContact(message.getSourceName())) {
			windowManager.infoDialog("User "+message.getSourceName()+" has removed you. You can no longer chat with "+message.getSourceName()+" :(");
			currentUser.removeContact(message.getSourceName());
			updateWindow();
		}
	}

	private void userMessage(Message message) {
		if (currentUser.hasContact(message.getSourceName())) {
			currentUser.addToChat(message.getSourceName(), formatUserMessage(message));
			updateWindow();
		}
	}

	private void messageForwarding(Message message) {
		if (message.getCondition()) {
			currentUser.addToChat(message.getSourceName(), formatSelfMessage(message));
			updateWindow();
		}
		else windowManager.errorDialog(message.getContent());
	}

	private void updateRequest(Message message) {
		sendUpdate();
	}

	private void serverMessage(Message message) {
		windowManager.infoDialog("Message from server", message.getContent());
	}
	
	//Action event methods--------------------------------------------------------------------------------------------------------------

	public void loginRequest(String name, String password) {
		Message request;
		if (name == null || name.length() == 0) windowManager.errorDialog("User name not valid");
		else if (password.length() < 4) windowManager.errorDialog("Password must be at least 4 characters long.");
		else {
			request = new Message(MessageType.LOGIN_REQUEST);
			request.setSourceName(name);
			request.setContent(password);
			send(request);
		}
	}
	
	public void exit() {
		if (windowManager.confirmDialog("Exit the application?")) {
			if (currentUser == null) System.exit(0);
			sendUpdate();
			try {
				clientCommunicator.close();
				System.exit(0);
			} catch (IOException e) {
				windowManager.exceptionDialog(e);
			}
		}
	}

	public void messageUser(String text) {
		Message message;
		if (currentUser.hasContact(windowManager.getChatWindow().getSelectedUser())) {
			message = new Message(MessageType.USER_MESSAGE);
			message.setDestinationName(windowManager.getChatWindow().getSelectedUser());
			message.setContent(text);
			send(message);
		}
	}
	
	public void addUser() {
		Message request;
		String name = windowManager.getUserNameDialog();
		if (name == null || name.isEmpty()) return; 
		if (!currentUser.getName().equals(name)) {
			request = new Message(MessageType.ADDING_REQUEST);
			request.setDestinationName(name);
			send(request);
		}
		else windowManager.errorDialog("You can't add yourself you fucking dumbass.");
	}
	
	public void removeUser() {
		Message message;
		String name = windowManager.getContactSelection();
		if (name != null) {
			if (windowManager.confirmDialog("Remove "+name+" from your contacts?")) {
				currentUser.removeContact(name);
				updateWindow();
				message = new Message(MessageType.ADDING_REMOVE);
				message.setDestinationName(name);
				send(message);
			}
		}
	}
	
	public void logout() {
		if (windowManager.confirmDialog("Log out of "+currentUser.getName()+" profile?")) {
			sendUpdate();
			send(new Message(MessageType.USER_LOGOUT));
			restartApp();
		}
	}
	
	public void changePassword() {
		String password = windowManager.getPasswordDialog();
		if (password == null || password.isEmpty()) return;
		if (currentUser.getPassword().equals(password)) {
			windowManager.errorDialog("The password inserted is the same as the previous one.");
			changePassword();
		}
		else if (password.length() < 4) {
			windowManager.errorDialog("Passwords must be at least 4 characters long.");
			changePassword();
		}
		else {
			currentUser.setPassword(password);
			sendUpdate();
			windowManager.infoDialog("You password has been changed.");
		}
	}
	
	public void removeAccount() {
		if (windowManager.confirmDialog("Are you sure to delete your user account?")) {
			send(new Message(MessageType.USER_LOGOUT));
			send(new Message(MessageType.USER_REMOVE));
			restartApp();
		}
	}
	
	//Public auxiliar methods-------------------------------------------------------------------------------------------------------
	
	public void updateWindow() {
		String selectedUser = windowManager.getChatWindow().getSelectedUser();
		windowManager.getChatWindow().clearSelection();
		windowManager.getChatWindow().updateContactList(currentUser.getContacts());
		windowManager.getChatWindow().select(selectedUser);
		windowManager.getChatWindow().updateChatField(currentUser.getChat(selectedUser));
	}
	
	//Private auxiliar methods------------------------------------------------------------------------------------------------------
	
	/**
	 * Adds the current user name to the Message and tries to send it, if there is a problem displays a dialog.
	 * @param message
	 */
	private void send(Message message) {
		try {
			if (currentUser != null) message.setSourceName(currentUser.getName());
			clientCommunicator.send(message);
		} catch (IOException e) {
			windowManager.exceptionDialog(e);
		}
	}
	
	private String formatMessage(String name, String time, String text) {
		return Formatter.colorText(Formatter.GREY, Formatter.italic("["+time+"]"))+
				Formatter.space(1)+
				name+
				Formatter.space(2)+
				Formatter.colorText(Formatter.LIGHT_BLACK, text)+Formatter.lineBreak();
	}
	
	private String formatUserMessage(Message message) {
		return formatMessage(Formatter.colorText(Formatter.GREEN, Formatter.bold(message.getSourceName())), message.getTimeStamp(null), message.getContent());
	}
	
	private String formatSelfMessage(Message message) {
		return formatMessage(Formatter.colorText(Formatter.BLUE, Formatter.bold("You")), message.getTimeStamp(null), message.getContent());
	}
	
	private void sendUpdate() {
		Message update = new Message(MessageType.USER_UPDATE);
		update.setAdditionalData(currentUser);
		send(update);
	}
	
	private void restartApp() {
		currentUser = null;
		windowManager.restart();
	}
}
