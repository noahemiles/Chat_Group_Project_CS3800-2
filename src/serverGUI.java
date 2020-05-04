
import java.io.*;
import java.net.*;
import java.util.*;

public class serverGUI extends javax.swing.JFrame 
{
   ArrayList clientOutputStreams;
   private ArrayList<ServerThread> users;
   private String username = "";
   private static Server server;
   private static serverGUI gui;
   private String message = "";

    public serverGUI() 
    {
        initComponents();
    }
    
    protected void setChatText(String text) {
        System.out.println("This is testing:" + text);
        serverChatBox.append(text + "\n");
    }
    
    protected void getOnlineUsers() {
        users = server.onlineUsers();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        serverChatBox = new javax.swing.JTextArea();
        startButton = new javax.swing.JButton();
        endButton = new javax.swing.JButton();
        onlineUsersButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Server's frame");
        setName("server"); // NOI18N
        setResizable(false);

        serverChatBox.setEditable(false);
        serverChatBox.setColumns(20);
        serverChatBox.setRows(5);
        jScrollPane1.setViewportView(serverChatBox);

        startButton.setText("START");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        endButton.setText("END");
        endButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endButtonActionPerformed(evt);
            }
        });

        onlineUsersButton.setText("Online Users");
        onlineUsersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onlineUsersButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(endButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(onlineUsersButton, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(onlineUsersButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearButton)
                    .addComponent(endButton))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void endButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endButtonActionPerformed

        serverChatBox.append("Server stopping... \n");
        try {
            server.serverSocket.close();
        } catch(Exception e) {
            // do something, e.g. print e.getMessage()
        }
        
        
        serverChatBox.setText("");
    }//GEN-LAST:event_endButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        
        //ta_chat.setText("Server started...\n");
     
        Thread thread = new Thread() {
        public void run() {
                server.start(gui);
                }
            };
        thread.start();
        setChatText("Server started...\n");
    }//GEN-LAST:event_startButtonActionPerformed

    private void onlineUsersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onlineUsersButtonActionPerformed
        getOnlineUsers();
        serverChatBox.append("\n Online users : \n");
        for(int i = 0; i < server.getUserCount(); i++) {
            serverChatBox.append(users.get(i).getUsername() + "\n");
        }
    }//GEN-LAST:event_onlineUsersButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        
        serverChatBox.setText("");
    }//GEN-LAST:event_clearButtonActionPerformed

    public static void main(String args[]) 
    {
        gui = new serverGUI();
        server = new Server();
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                gui.setVisible(true);
            }
        });
        //server.start(gui);
        
    }
    
  
 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearButton;
    private javax.swing.JButton endButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton onlineUsersButton;
    private javax.swing.JTextArea serverChatBox;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
