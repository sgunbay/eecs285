package com.eecs285.siegegame;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static MiniServer[] allClients; // contains all connected clients

    // arrays to contain playerNames and whether players have chosen names yet
    static String[] playerNames = { "Player 0", "Player 1", "Player 2",
            "Player 3" };
    static boolean[] chosen = { false, false, false, false }; // if name chosen
    static boolean connectionAllowed = true; // if additional players can join
    static int numPlayer = 0;
    final static int MAX_PLAYERS = 4;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(45000);
        } catch (IOException e) {
            System.err.println("ERROR: Could not listen on port: 45000");
            System.exit(-1);
        }

        allClients = new MiniServer[MAX_PLAYERS];

        while (connectionAllowed && numPlayer < MAX_PLAYERS) {
            // accept connection and start a new MiniServer for that client
            Socket clientSocket = serverSocket.accept();
            MiniServer server = new MiniServer(clientSocket, numPlayer);
            allClients[numPlayer] = server;
            server.start();

            // announce start of server, increment number of connected players
            System.out.println("Server " + numPlayer + " started");
            numPlayer++;
        }

        // Delay sending of names array until all players have chosen
        while (!(chosen[0] && chosen[1] && chosen[2] && chosen[3]))
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

        // send player names to clients (using first client's output stream)
        System.out.println("Sending playerNames[] to all clients...");
        // allClients[0].oStream.writeObject(playerNames);
        allClients[0].broadcastToClients(playerNames);
        System.out.println("Done.");

    }
}

// connections allowed - int of num players not ready