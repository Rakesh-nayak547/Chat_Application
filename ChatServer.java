
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatServer {
    private static JTextArea chatArea;
    private static JTextField inputField;
    private static JButton sendButton, clearButton;
    private static PrintWriter output;

    public static void main(String[] args) {
        // Frame setup
        JFrame frame = new JFrame("Chat Server");
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        inputField = new JTextField();
        sendButton = new JButton("Send");
        clearButton = new JButton("Clear");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        // Layout setup
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.add(clearButton, BorderLayout.NORTH);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Clear button functionality
        clearButton.addActionListener(e -> chatArea.setText(""));

        // Send button functionality
        sendButton.addActionListener(e -> sendMessage());

        inputField.addActionListener(e -> sendMessage());

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            chatArea.append("Waiting for a client to connect...\n");

            Socket clientSocket = serverSocket.accept();
            chatArea.append("Client connected!\n");

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientMessage;
            while ((clientMessage = input.readLine()) != null) {
                chatArea.append("Client: " + clientMessage + "\n");
            }
        } catch (IOException e) {
            chatArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    private static void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            chatArea.append("Server: " + message + "\n");
            output.println(message);
            inputField.setText("");
        }
    }
}
