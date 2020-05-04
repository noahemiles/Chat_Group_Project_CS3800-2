import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.lang.reflect.Method;

public class Server {
    private ArrayList<ServerThread> userList = new ArrayList<ServerThread>();
    private int userCount = 0;
    final int PORT = 2468;
    ServerThread st;
    ServerSocket serverSocket;
/*
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
*/
    // Setup socket, accept connection, spawn and add thread
    protected void start(serverGUI gui) {
        
        try {
            serverSocket = new ServerSocket(PORT);
            gui.setChatText("Server is waiting for connections...");
            System.out.println("Server is waiting for connections...");
            while(true) {
                Socket clientSocket = serverSocket.accept();
                gui.setChatText("Connection accepted...");
                st = new ServerThread(this, clientSocket, gui);
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
    
    public ArrayList<ServerThread> onlineUsers() {
        return userList;
    }
    
    public int getUserCount() {
        return userCount;
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

