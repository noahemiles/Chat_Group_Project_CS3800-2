import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    private ArrayList<ServerThread> userList = new ArrayList<ServerThread>();
    private int userCount = 0;
    final int PORT = 2468;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    // Setup socket, accept connection, spawn and add thread
    private void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is waiting for connections...");
            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted...");
                ServerThread st = new ServerThread(this, clientSocket);
                userList.add(st);
                userCount++;
                st.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Locate client in the userList by username
    private int findClientByName(String username) {
        for(int i = 0; i < userCount; i++) {
            if((userList.get(i).getUsername()).equalsIgnoreCase(username)) {
                return i;
            }
        }
        return -1;
    }

    // Removes user from userList
    synchronized void removeUser(String username) {
        int index = findClientByName(username);
        userList.remove(index);
        userCount--;
    }

    // Sends message to all users by looping through userList
    // If client is disconnected (not a normal logout), remove that client from userList
    synchronized void sendMsgToAll(String msg) {
        for(int i = 0; i < userCount; i++) {
            ServerThread st = userList.get(i);
            if(st.getIsConnected()) {
                st.sendMsg(msg);
            }
        }
    }
}

