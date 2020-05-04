import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");
    private final Server server;
    private final Socket clientSocket;
    private BufferedReader inFromClient;
    private PrintWriter outToClient;
    private String username;
    private boolean isConnected;
    serverGUI g;
    
    // Constructor
    public ServerThread(Server server, Socket clientSocket, serverGUI sgui) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.g = sgui;
        try {
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Failed to establish connection...");
        }
    }

    @Override
    public void run() {
        // Setup for a new user
        handleNewUser();
        
        String receivedMsg;
        // Keep reading messages till socket/stream is closed
        while (isConnected) {
            try {
                receivedMsg = inFromClient.readLine();
                // Received sign-out message
                if(receivedMsg.equals(".")) {
                    isConnected = false;
                    sendMsg("> You have exited the chatroom!");
                    close();
                }
                // Chat message
                else {
                    String formattedMsg = formatMsg(receivedMsg);
                    server.sendMsgToAll(formattedMsg);
                }   
            } catch (IOException e) {
                System.out.println("Failed to read message. Client socket has been closed abnormally...");
                // Close socket
                close();
            }
        }
    }

    // Sets the username and send a welcome message
    private void handleNewUser() {
        try {
            username = inFromClient.readLine();
            sendMsg("> Welcome to the chatroom, " + username + "!");
            server.sendMsgToAll("> " + username + " has connected to the chatroom!");
            isConnected = true;
        } catch (IOException e) {
            System.out.println("Failed to connect user to the chatroom...");
        }
    }

    // Takes a string and formats it to include username and timestamp
    private String formatMsg(String msg) {
        String formattedMsg;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String timestamp = SDF.format(ts);
        formattedMsg = timestamp + " - " + username + ": " + msg;
        return formattedMsg;
    }

    // Send the message to the client, if fail let parent server know
    void sendMsg(String msg) {
        try {
            //gui.setChatText("Attempting to send this message: " + msg);
            System.out.println("Attempting to send this message: " + msg); //--------------------TEST-----------------------
            g.setChatText("Attempting to send this message: " + msg);
            // Client is connected, send message
            outToClient.println(msg);
            System.out.println("Message has been sent!"); //--------------------TEST-----------------------
            g.setChatText("Message has been sent!");
        } catch (Exception e) {
            // Found a client that was disconnected without logout
            // Remove the user server's userList and close connection
            close();
        } 
    }

    // Close connection and socket
    private void close() {
        try{
            isConnected = false;
            server.removeUser(username);
            server.sendMsgToAll("> " + username + " has exited the chatroom!");
            inFromClient.close();
            outToClient.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Failed to close connection and socket...");
        }
    }

    // Gets username
    public String getUsername() {
        return username;
    }
    
    // Sets username
    public void setUsername(String username) {
        this.username = username;
    }   

    // Gets connected to chatroom status
    boolean getIsConnected() {
        return isConnected;
    }
}