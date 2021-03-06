package domain;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

	public static final String DEFAULT_PROPERTIES_FILE = "config.properties";
	
	public static boolean localTest;
	public static String serverAddress;
	public static int serverPort;
	
	public static void loadProperties() throws IOException {
		Properties properties;
		FileInputStream in = new FileInputStream(DEFAULT_PROPERTIES_FILE);
		properties = new Properties();
		properties.load(in);
		localTest = Boolean.parseBoolean(properties.getProperty("localTest"));
		serverAddress = properties.getProperty("serverAddress");
		serverPort = Integer.parseInt(properties.getProperty("serverPort"));
	}	
}
