package com.eecs285.siegegame;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MiniServer extends Thread {
    private Socket socket = null;
    int clientNum;
    BufferedReader iStream;
    DataOutputStream oStream;

    public MiniServer(Socket socket, int numClient) {
        super("MiniServer");
        clientNum = numClient;
        this.socket = socket;
    }

    public void run() {
        // Announce connection of player
        System.out.println("Player " + clientNum + " Connected");
        
        // get IO functionality to client
        try {
            DataInputStream is = new DataInputStream(socket.getInputStream());
            iStream = new BufferedReader(new InputStreamReader(is));
            oStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("ERROR: getting InputStream or OutputStream");
        }
        
        //first, get name from user
        String data;
        boolean namePicked = false;
        while(!namePicked) {
            if((data = getInput(iStream)) != null) {          
                String usableData = "";
                for(int i = 0; i < data.length(); i++) {
                    if(Character.isLetterOrDigit(data.charAt(i)))
                        usableData += data.charAt(i);
                }
                
                if(usableData.contains("NAME")) {
                    usableData = usableData.substring(4);  
                    Server.playerNames[clientNum] = usableData;
                    System.out.println("Player " + clientNum + " has changed their name to " + usableData);
                    broadcastToClients("Player " + clientNum + " has changed their name to " + usableData + '\n');
                    namePicked = true;                            
                }
            }
        }
        
        
        //for(int i = 0; i < Server.MAX_PLAYERS; i++)
        //    System.out.println(Server.playerNames[i]);
        
        
        
        while (true) {
            // if data has been received from client
            if ((data = getInput(iStream)) != null) {                
                System.out.println(data);
                data += '\n';
                broadcastToClients(data);
            }
        }
    }    

    // helpful methods
    public String getInput(BufferedReader istream) {
        String receivedData = null;
        try {
            receivedData = istream.readLine();
        } catch (IOException e) {
            System.out.println("ERROR: getting data");
            System.exit(-1);
        }
        return receivedData;
    }

    public void broadcastToClients(String data) {
        for (int i = 0; i < Server.allClients.length; i++) {
            if (Server.allClients[i] == null)
                continue;
            try {
                Server.allClients[i].oStream.writeChars(data);
            } catch (Exception e) {
                System.out.println("ERROR: writing to server: " + i);
            }
        }
    }
}
