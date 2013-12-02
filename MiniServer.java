package com.eecs285.siegegame;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MiniServer extends Thread {
    private Socket socket = null;
    String playerName;
    int clientNum;
    BufferedReader iStream;
    //DataOutputStream oStream;
    ObjectOutputStream oStream;

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
            //oStream = new DataOutputStream(socket.getOutputStream());
            oStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("ERROR: getting InputStream or OutputStream");
        }

        // first, get name from user
        String data;
        boolean namePicked = false;
        while (!namePicked) {
            if ((data = getInput(iStream)) != null) {
                String usableData = "";
                for (int i = 0; i < data.length(); i++) {
                    if (Character.isLetterOrDigit(data.charAt(i)))
                        usableData += data.charAt(i);
                }

                if (usableData.contains("NAME")) {                    
                    usableData = usableData.substring(4);
                    Server.playerNames[clientNum] = usableData;
                    Server.chosen[clientNum] = true;
                    

                    

                    playerName = usableData;
                    System.out.println("Player " + clientNum
                            + " has changed their name to " + usableData);
                    namePicked = true;
                }
            }
        }

        while (true) {
            // if data has been received from client, send it to all clients
            if ((data = getInput(iStream)) != null) {
                System.out.println(data);
                data += '\n';
                broadcastToClients(data);
            }
            
            // sleep to prevent overheating of processor in case 
            // clients disconnect (not important to game)
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {}
        }
    }

    // get data from clients
    public String getInput(BufferedReader istream) {
        String receivedData = null;
        try {
            receivedData = istream.readLine();
        } catch (IOException e) {
            System.err.println("ERROR: Getting data from clients. Quitting.");
            System.exit(-1);
        }
        return receivedData;
    }

    // send the object parameter (String or String[]) to all clients 
    public void broadcastToClients(Object data) {
        for (int i = 0; i < Server.allClients.length; i++) {
            if (Server.allClients[i] == null)
                continue;
            try {
                //Server.allClients[i].oStream.writeChars(data);
                Server.allClients[i].oStream.writeObject(data);
            } catch (Exception e) {
                System.err.println("ERROR: writing to server: " + i);
            }
        }
    }    
}
