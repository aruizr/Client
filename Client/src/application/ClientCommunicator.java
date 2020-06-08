package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;

import domain.InetUtils;
import domain.Message;
import domain.Settings;

public class ClientCommunicator {
	
	//private final static String DEFAULT_SERVER_ADDRESS = "192.168.1.133";
	//private final static int DEFAULT_SERVER_PORT = 4444;
	
	private Socket serverConnection;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String currentAddress;

	/**
	 * Tries to connect to the Server and starts a new listening Thread.
	 * @throws IOException If there was a problem connecting to the Server.
	 */
	public ClientCommunicator() throws IOException {
		serverConnection = new Socket(Settings.serverAddress, Settings.serverPort);
		out = new ObjectOutputStream(serverConnection.getOutputStream());
		out.flush();
		in = new ObjectInputStream(serverConnection.getInputStream());
		if (Settings.localTest) currentAddress = Inet4Address.getLocalHost().getHostAddress();
		else currentAddress = InetUtils.getPublicAddress();
		listen();
	}
	
	/**
	 * Adds the source address to the Message and sends it to the Server.
	 * @param message
	 * @throws IOException If there was a problem sending the Message
	 * @throws IllegalArgumentException If the argument is null.
	 */
	public void send(Message message) throws IOException {
		if (message == null) throw new IllegalArgumentException("Argument can't be null.");
		message.setSourceAddress(currentAddress);
		out.writeObject(message);
	}
	
	public void close() throws IOException {
		serverConnection.close();
	}

	private void listen() {
		Thread listener = new Thread() {
			public void run() {
				while (!serverConnection.isClosed()) {
					//TODO receive message
					try {
						ClientController.getInstance().messageReceived((Message) in.readObject());
					} catch (ClassNotFoundException e) {
						//Not a message
						e.printStackTrace();
					} catch (IOException e) {
						//TODO connection closed
						
					}
				}
			}
		};
		listener.start();
	}
}
