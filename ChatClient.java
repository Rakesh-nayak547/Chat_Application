/*import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {
            System.out.println("Connected to the server!");

            // Input and output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Thread for reading messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = input.readLine()) != null) {
                        System.out.println("Server: " + serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Server disconnected.");
                }
            });

            readThread.start();

            // Sending messages to the server
            String clientMessage;
            while ((clientMessage = consoleInput.readLine()) != null) {
                output.println(clientMessage);
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private static JTextArea chatArea;
    private static JTextField inputField;
    private static JButton sendButton, clearButton;
    private static PrintWriter output;

    public static void main(String[] args) {
        // Frame setup
        JFrame frame = new JFrame("Chat Client");
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

        try (Socket socket = new Socket("localhost", 12345)) {
            chatArea.append("Connected to the server!\n");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            String serverMessage;
            while ((serverMessage = input.readLine()) != null) {
                chatArea.append("Server: " + serverMessage + "\n");
            }
        } catch (IOException e) {
            chatArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    private static void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            chatArea.append("Client: " + message + "\n");
            output.println(message);
            inputField.setText("");
        }
    }
}