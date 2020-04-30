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

    /*
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
        client.initialSetup();
        client.outgoingChat();
    }*/

    // Starts TCP connection with server
    protected void start() {
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
    protected void initialSetup(clientGUI gui, String username){
        try {
            outToServer.println(username);
            gui.setChatText(inFromServer.readLine());
            gui.setChatText("> You can begin sending messages...");

            ChatListener cl = new ChatListener(gui);
            cl.start();
            isConnected = true;
        } catch (IOException e) {
            System.out.println("Failed to connect user to the chatroom...");
        }
    }

    // Begin an endless loop of reading and sending the user's messages
    protected void outgoingChat(String receivedMsg) {
        if(isConnected) {
            try {
                if(receivedMsg == null){
                }
                //Received sign-out message
                else if(receivedMsg.equals(".")) {
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
    protected void close() {
        try {
            isConnected = false;
            inFromServer.close();
            outToServer.close();
            socket.close();
        } catch (Exception e) {}
    }

    // Continuously listens for other user's messages
    class ChatListener extends Thread {
        clientGUI g;
        public ChatListener(clientGUI g){
            this.g = g;
        }
        public void run() {
            String receivedMsg;
            while(isConnected) {
                try {
                    receivedMsg = inFromServer.readLine();  
                    System.out.println(receivedMsg);
                    g.setChatText(receivedMsg);
                    g.setView();

                } catch (Exception e) {
                    System.out.println("Server has encountered an error. Disconnecting...");
                    close();
                }
            }
        }
    }
}

