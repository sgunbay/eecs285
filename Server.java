package com.eecs285.siegegame;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    
    static MiniServer[] allClients; //contains all connected clients
    static String[] playerNames = {"Player 0", "Player 1", "Player 2", "Player 3"}; //contains all player names
    static boolean connectionAllowed = true; //if additional players can join
    static int numPlayer = 0;
    final static int MAX_PLAYERS = 4;

    public static void main(String[] args) throws IOException {        
        
        ServerSocket serverSocket = null;
        
        try {
            serverSocket = new ServerSocket(45000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 45000");
            System.exit(-1);
        }

        allClients = new MiniServer[MAX_PLAYERS];    
        
        while(connectionAllowed && numPlayer < MAX_PLAYERS) {
            
            //accept connection and start a new MiniServer for that client
            Socket clientSocket = serverSocket.accept();
            MiniServer mini = new MiniServer(clientSocket, numPlayer);
            allClients[numPlayer] = mini;
            mini.start();
    
            System.out.println("Server " + numPlayer + " started"); 
            numPlayer++;
        }       
    }
}
