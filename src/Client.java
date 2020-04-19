import java.io.*;
import java.net.*;

public class Client {
    final int PORT = 2468;
    final String HOSTNAME = "localhost";
    Socket socket;
    private PrintWriter outToServer;
    private BufferedReader inFromServer;
    private BufferedReader inFromUser;
    private boolean isConnected = false;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
        client.initialSetup();
        client.outgoingChat();
    }

    // Starts TCP connection with server
    private void start() {
        try {
            socket = new Socket(HOSTNAME, PORT);
            outToServer = new PrintWriter(socket.getOutputStream(), true);
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Failed to establish connection...");
        }
    }

    // Prompts user for username and sends to server, waits for confirmation message
    private void initialSetup() {
        System.out.println("You are attempting to connect to the chatroom...");
        System.out.print("Enter a username: ");
        try {
            String username = inFromUser.readLine();
            outToServer.println(username); 
            System.out.println(inFromServer.readLine());  // Welcome message
            System.out.println("> You can begin sending messages...");
            
            ChatListener cl = new ChatListener();
            cl.start();
            isConnected = true;
        } catch (IOException e) {
            System.out.println("Failed to connect user to the chatroom...");
        }
    }

    // Begin an endless loop of reading and sending the user's messages
    private void outgoingChat() {
        String receivedMsg;
        while(isConnected) {
            try {
                receivedMsg = inFromUser.readLine();
                // Received sign-out message
                if(receivedMsg.equals(".")) {
                    isConnected = false;
                    outToServer.println(receivedMsg);
                    socket.shutdownOutput();
                    receivedMsg = inFromServer.readLine();
                    System.out.println(receivedMsg);
                    outToServer.close();
                    socket.close();
                }
                // Received chat message
                else {
                    // Send to server
                    outToServer.println(receivedMsg);
                }
            } catch (Exception e) {
                System.out.println("Error in sending message process...");
            }
        }
    }
    
    // Close connection and socket
    private void close() {
        try {
            isConnected = false;
            inFromServer.close();
            outToServer.close();
            socket.close();
        } catch (Exception e) {}
    }

    // Continuously listens for other user's messages
    class ChatListener extends Thread {
        public void run() {
            String receivedMsg;
            while(isConnected) {
                try {
                    receivedMsg = inFromServer.readLine();  
                    System.out.println(receivedMsg);
                } catch (Exception e) {
                    System.out.println("Server has encountered an error. Disconnecting...");
                    close();
                }
            }
        }
    }
}

