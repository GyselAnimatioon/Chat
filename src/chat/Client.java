// Package muss natürlich angepasst werden
package chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import sun.awt.image.ToolkitImage;

public class Client {

    JFrame clientFrame;
    JFrame loginFrame;
    JPanel clientPanel;
    JPanel loginPanel;
    JTextArea textArea_Messages;
    JTextField textField_ClientMessage;
    JTextField loginUsername;
    JButton button_SendMessage;
    JButton loginSubmit;
    JTextField textField_Username;
    JScrollPane scrollPane_Messages;
    Socket client;
    PrintWriter writer;
    BufferedReader reader;
    int login = 0;
    String name = "Unnamed";

    public static void main(String[] args) {
        Client c = new Client();
        c.loginGUI(c);
    }

    public void loginGUI(Client c) {
        c.createGUI();
        loginFrame = new JFrame("Login");
        loginFrame.setSize(400, 100);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        loginPanel = new JPanel();
        loginUsername = new JTextField(30);
        loginUsername.setText(System.getProperty("user.name"));
        loginUsername.setEditable(true);
        loginUsername.addKeyListener(new SendPressEnterListener());
        loginSubmit = new JButton("Starten");
        SendButtonListener start = new SendButtonListener();
        loginSubmit.addActionListener(start);

        loginPanel.add(loginUsername);
        loginPanel.add(loginSubmit);

        loginFrame.getContentPane().add(BorderLayout.CENTER, loginPanel);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
        loginFrame.pack();
    }

    public void createGUI() {
        clientFrame = new JFrame("Mein Chat");
        clientFrame.setSize(800, 600);
        clientFrame.setLocationRelativeTo(null);
        clientFrame.setResizable(false);
        //clientFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icon.png")));

        clientPanel = new JPanel();

        textArea_Messages = new JTextArea();
        textArea_Messages.setEditable(false);

        textField_ClientMessage = new JTextField(38);
        textField_ClientMessage.addKeyListener(new SendPressEnterListener());

        button_SendMessage = new JButton("Send");
        button_SendMessage.addActionListener(new SendButtonListener());

        textField_Username = new JTextField(10);

        scrollPane_Messages = new JScrollPane(textArea_Messages);
        scrollPane_Messages.setPreferredSize(new Dimension(700, 500));
        scrollPane_Messages.setMinimumSize(new Dimension(700, 500));
        scrollPane_Messages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_Messages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        if (!connectToServer()) {
            // Connect-Label anzeigen ob verbunden oder nicht...
        }
        Thread t = new Thread(new MessagesFromServerListener());
        t.start();
        clientPanel.add(scrollPane_Messages);
        clientPanel.add(textField_Username);
        clientPanel.add(textField_ClientMessage);
        clientPanel.add(button_SendMessage);
        clientFrame.getContentPane().add(BorderLayout.CENTER, clientPanel);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setVisible(true);
        //clientFrame.pack();
    }

    public boolean connectToServer() {
        try {
            client = new Socket("192.168.43.142", 5555);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintWriter(client.getOutputStream());
            appendTextMessages("Netzwerkverbindung hergestellt");
            writer.println(System.getProperty("user.name") + " hat den Chatraum betreten.");
            writer.flush();
            return true;
        } catch (Exception e) {
            appendTextMessages("Netzwerkverbindung konnte nicht hergestellt werden");
            return false;
        }
    }

    public void sendMessageToServer() {
        if (!textField_Username.getText().equals("")) {
            this.name = textField_Username.getText();
        }
        if (!textField_ClientMessage.getText().equals("")) {
            writer.println(name + ": " + textField_ClientMessage.getText());
            writer.flush();
        }
        textField_ClientMessage.setText("");
        textField_ClientMessage.requestFocus();
    }

    public void appendTextMessages(String message) {
        textArea_Messages.append(message + "\n");
    }

    public class SendPressEnterListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent arg0) {
            if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                if (login == 0) {
                    if (!loginUsername.getText().equals("")) {
                        name = loginUsername.getText();
                    }
                    loginFrame.dispose();
                    login = 1;
                    textField_ClientMessage.requestFocus();
                } else {
                    sendMessageToServer();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent arg0) {
        }

        @Override
        public void keyTyped(KeyEvent arg0) {
        }
    }

    public class SendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (login == 0) {
                if (!loginUsername.getText().equals("")) {
                    name = loginUsername.getText();
                }
                loginFrame.dispose();
                login = 1;
                textField_ClientMessage.requestFocus();
            } else {
                sendMessageToServer();
            }
        }
    }

    public class MessagesFromServerListener implements Runnable {

        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    appendTextMessages(message);
                    textArea_Messages.setCaretPosition(textArea_Messages.getText().length());
                    clientFrame.toFront();
                }
            } catch (IOException e) {
                appendTextMessages("Nachricht konnte nicht empfangen werden!");
                e.printStackTrace();
            }
        }
    }
}
