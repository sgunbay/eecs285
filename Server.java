package com.eecs285.siegegame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// The wrapper for the actual servers that the clients connect to
// Written by Max


public class Server {
    // shit
    static MiniServer[] allClients; // contains all connected clients

    static String[] playerNames; // contains all player names
    static boolean[] chosen; // if name chosen
    static boolean[] ready; // players ready?
    static boolean connectionAllowed = true; // if additional players can join
    static int numPlayer = 0;
    static int MAX_PLAYERS;

    public static void main(String[] args) throws IOException {

        // get number of players from command line argument
        MAX_PLAYERS = Integer.valueOf(args[0]);

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(45000);
        } catch (IOException e) {
            System.err.println("ERROR: Could not listen on port: 45000");
            System.exit(-1);
        }

        // Instantiate arrays
        allClients = new MiniServer[MAX_PLAYERS];
        chosen = new boolean[MAX_PLAYERS];
        ready = new boolean[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++)
            chosen[i] = ready[i] = false;

        // initialize player names to Player 0, Player 1, etc
        playerNames = new String[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++)
            playerNames[i] = "Player " + i;

        // allow connections until 2 - 4 players have connected to the server
        // and all connected clients are ready
        while (numPlayer < MAX_PLAYERS) {
            System.out.println("Connection still allowed");

            // accept connection and start a new MiniServer for that client
            Socket clientSocket = serverSocket.accept();
            MiniServer server = new MiniServer(clientSocket, numPlayer);
            allClients[numPlayer] = server;
            server.start();

            // announce start of server, increment number of connected players
            System.out.println("Server " + numPlayer + " started");
            numPlayer++;
        }

        System.out.println("CONNECTIONS NOT ALLOWED");

        // Delay sending of names array until all players have chosen
        boolean allChosen = false;
        while (!allChosen) {
            allChosen = true;
            for (int i = 0; i < MAX_PLAYERS; ++i) {
                if (!chosen[i])
                    allChosen = false;
            }
            try {
                // wait to prevent overheating of processor
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }

        // send player names to clients (using first client's output stream)
        System.out.println("Sending playerNames[] to all clients...");
        allClients[0].broadcastToClients(playerNames);
        System.out.println("Done.");

        System.out.println("Waiting for all players to be ready");
        allClients[0]
                .broadcastToClients("Waiting for all players to be ready...");

        // Wait for all players to hit Ready button
        while (!checkReady())
            try {
            	Thread.sleep(1);
            } catch (InterruptedException e) {
            }

        // print friendly message
        System.out.println("All players ready. Starting game!");
        allClients[0].broadcastToClients("All players ready. "
                + "Starting the game now. Good luck, have fun!");
    }

    private static boolean checkReady() {
        // if all connected are ready, prevent additional connections
        int counter = 0;
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (ready[i])
                counter++;
        }

        if (counter == MAX_PLAYERS)
            return true;
        return false;
    }
}