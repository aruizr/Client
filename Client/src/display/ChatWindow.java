package display;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import application.ClientController;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class ChatWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField messageField;
	private JTextPane chatField;
	private JScrollPane scrollChatField;
	private JList<String> contactList;
	private DefaultListModel<String> contactListModel;
	private JScrollPane scrollPane;
	private JTextField txtContactList;
	private JTextField currentContactField;
	
	public ChatWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				initialize();
			}
		});
	}

	public void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//Exit application
				ClientController.getInstance().exit();
			}
		});
		setResizable(false);
		setTitle("Chat");
		setBackground(Color.GRAY);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 800, 469);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnProfileMenu = new JMenu("Profile");
		menuBar.add(mnProfileMenu);
		
		JMenuItem mntmChangePassword = new JMenuItem("Change password");
		mntmChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Change password
				ClientController.getInstance().changePassword();
			}
		});
		mnProfileMenu.add(mntmChangePassword);
		
		JMenuItem mntmRemoveAccount = new JMenuItem("Remove account");
		mntmRemoveAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Remove account
				ClientController.getInstance().removeAccount();
			}
		});
		mnProfileMenu.add(mntmRemoveAccount);
		
		JSeparator separator = new JSeparator();
		mnProfileMenu.add(separator);
		
		JMenuItem mntmLogout = new JMenuItem("Log out");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO logout
				ClientController.getInstance().logout();
			}
		});
		mnProfileMenu.add(mntmLogout);
		
		JSeparator separator_1 = new JSeparator();
		mnProfileMenu.add(separator_1);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientController.getInstance().exit();
			}
		});
		mnProfileMenu.add(mntmExit);
		
		JMenu mnContacts = new JMenu("Contacts");
		menuBar.add(mnContacts);
		
		JMenuItem mntmAddContact = new JMenuItem("Add contact");
		mntmAddContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientController.getInstance().addUser();
			}
		});
		mnContacts.add(mntmAddContact);
		
		JMenuItem mntmRemoveContact = new JMenuItem("Remove contact");
		mntmRemoveContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientController.getInstance().removeUser();
			}
		});
		mnContacts.add(mntmRemoveContact);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollChatField = new JScrollPane();
		scrollChatField.setBounds(10, 14, 580, 364);
		contentPane.add(scrollChatField);
		
		currentContactField = new JTextField();
		currentContactField.setFont(new Font("Tahoma", Font.BOLD, 11));
		scrollChatField.setColumnHeaderView(currentContactField);
		currentContactField.setEditable(false);
		currentContactField.setBackground(Color.LIGHT_GRAY);
		currentContactField.setColumns(10);
		
		chatField = new JTextPane();
		scrollChatField.setViewportView(chatField);
		chatField.setContentType("text/html");
		chatField.setEditable(false);
		
		messageField = new JTextField();
		messageField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Send message
				messageUser();
			}
		});
		messageField.setBounds(10, 389, 481, 21);
		contentPane.add(messageField);
		messageField.setColumns(10);
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//send message
				messageUser();
			}
		});
		sendButton.setBounds(501, 389, 89, 23);
		contentPane.add(sendButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(600, 14, 184, 396);
		contentPane.add(scrollPane);
		
		txtContactList = new JTextField();
		scrollPane.setColumnHeaderView(txtContactList);
		txtContactList.setEditable(false);
		txtContactList.setBackground(Color.LIGHT_GRAY);
		txtContactList.setText("Contact list");
		txtContactList.setColumns(10);
		
		contactListModel = new DefaultListModel<String>();
		
		contactList = new JList<String>(contactListModel);
		scrollPane.setViewportView(contactList);
		contactList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!contactList.isSelectionEmpty()) ClientController.getInstance().updateWindow();
			}
		});
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	/**
	 * Displays in the Window's chat field the inserted String, if the String is null the chat field will appear empty.
	 * @param text
	 */
	public synchronized void updateChatField(String text) {
		if (text == null) chatField.setText("");
		else {
			chatField.setText(text);
			scrollChatField.getVerticalScrollBar().setValue(scrollChatField.getVerticalScrollBar().getMaximum());
		}
	}
	
	/**
	 * The specified value will appear selected in the Window's contact list only if the list contains the value and the value is not null.
	 * @param key
	 */
	public synchronized void select(String key) {
		if (key != null) {
			if (contactListModel.contains(key)) {
				contactList.setSelectedValue(key, true);
				currentContactField.setText(contactList.getSelectedValue());
			}
		}
	}
	
	public synchronized void clearSelection() {
		contactList.clearSelection();
		currentContactField.setText(contactList.getSelectedValue());
	}
	
	/**
	 * @return The selected value from the contact list, null if the list is empty or there is no selection.
	 */
	public synchronized String getSelectedUser() {
		if (contactListModel.isEmpty()) return null;
		return contactList.getSelectedValue();
	}
	
	/**
	 * Replaces the contact list with the values in the inserted Collection only if the Collection is not null.
	 * @param names
	 */
	public synchronized void updateContactList(Collection<String> names) {
		if (names != null) {
			contactListModel.clear();
			for (String name : names) {
				contactListModel.addElement(name);
			}
		}
	}
	
	public DefaultListModel<String> getContactListModel() {
		return contactListModel;
	}
	
	private synchronized void messageUser() {
		if (!messageField.getText().isEmpty()) {
			ClientController.getInstance().messageUser(messageField.getText());
			messageField.setText("");
		}
	}
}
