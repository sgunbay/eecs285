package com.eecs285.siegegame;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.eecs285.siegegame.ActionParser.ActionType;

public class Siege {

    // These are required for the classes to work properly:
    public static Grid grid;
    public static int numCities;
    public static int currentPlayer;
    public static Player[] players;

    // required for networking functionality
    static BufferedReader in;
    static DataOutputStream out;
    static int portNum = 45000;
    private final static String IPaddress = "127.0.0.1"; // server IP

    public static void main(String args[]) throws Exception {
        // initialize connection to server (including IO streams)
        initServerConnection();

        final int rows = 25;
        final int cols = 40;

        Tile[][] mapTiles = new Tile[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                mapTiles[i][j] = new TilePlains(new Coord(i, j));

        MainGameFrame mainFrame = new MainGameFrame("Siege", rows, cols,
                mapTiles);
        mainFrame.printNarration("Jordan added a bit of functionality");
        mainFrame.printNarration("Jordan tested the functionality");
        mainFrame.printNarration("Jordan is making sure the scrollbar works");
        for (int i = 0; i < 50; i++)
            mainFrame.printNarration("Filling in 50 lines...");
        mainFrame.printNarration("Jordan ended turn");

        Coord oneOne = new Coord(1, 1);
        Tile plains = new TilePlains(oneOne);
        mainFrame.updateGridSquare(oneOne, plains);

        // testing max
        // sendToServer("someone attacks someone else");
        sendToServer("Player's army at (1, 2) attacks player's army/city at (3, 4)");

        while (true) {
            // Get string from server
            String fromServer = in.readLine();
            // Remove unwanted spaces in string
            fromServer = trimServerString(fromServer);

            // Get action type from string and initialize variables
            ActionType atype = ActionParser.getActionType(fromServer);
            Coord attacker = null, target = null;
            Coord city = null, resource = null;

            // If atype is null, then there was an issue reading the string
            if (atype == null) {
                System.out.println("ERROR: atype is null");
                System.exit(-1);
            }

            // perform action depending on what server string specifies
            switch (atype) {
            case ATTACK:
                System.out.println("Attack");
                attacker = ActionParser.getFirstCoordinate(fromServer);
                target = ActionParser.getSecondCoordinate(fromServer);
                System.out.println("attacker at (" + attacker.row + ", "
                        + attacker.col + ")");
                System.out.println("target at (" + target.row + ", "
                        + target.row + ")");

                break;
            case CAPTURE_CITY:
                System.out.println("Capture City");
                city = ActionParser.getFirstCoordinate(fromServer);

                break;
            case CAPTURE_RESOURCE:
                System.out.println("Capture Resource");
                resource = ActionParser.getFirstCoordinate(fromServer);

                break;
            case CITY_LIBERATED:
                System.out.println("Liberate City");
                city = ActionParser.getFirstCoordinate(fromServer);

                break;
            case CITY_UNDER_SIEGE:
                System.out.println("Siege City");
                city = ActionParser.getFirstCoordinate(fromServer);

                break;
            case END_TURN:
                System.out.println("End Turn");
                break;
            case LOSE_UNITS:
                System.out.println("Lose Units");
                break;
            case MERGE_ARMY:
                System.out.println("Merge Army");
                break;
            case MOVE_ARMY:
                System.out.println("Move Army");
                break;
            case RECRUIT:
                System.out.println("Recruit");
                break;
            case RESOURCE_RECAPTURED:
                System.out.println("Recapture Resource");
                resource = ActionParser.getFirstCoordinate(fromServer);

                break;
            case RESOURCE_UNDER_CONFLICT:
                System.out.println("Resource Contested");
                resource = ActionParser.getFirstCoordinate(fromServer);

                break;
            case PLAYER_DEFEATED:
                System.out.println("Player Defeated");
                break;
            case PLAYER_WINS:
                System.out.println("Player Wins");
                break;
            default:
                System.out
                        .println("ERROR: Action did not specify a known ActionType");
                System.exit(-1);
                break;
            }
        }
    }

    private static String trimServerString(String fromServer) {
        // string from server has 3 space chars between each usable char
        // need to trim the spaces to use string.contains() method

        String usableData = "";
        for (int i = 0; i < fromServer.length(); i++)
            if ((i + 1) % 4 == 0) // every fourth character is useful
                usableData += String.valueOf(fromServer.charAt(i));

        return usableData;
    }

    private static void initServerConnection() throws Exception {
        // create a connection to the server and initialize IO streams
        Socket cSocket = new Socket(IPaddress, portNum);
        out = new DataOutputStream(cSocket.getOutputStream());
        DataInputStream dis = new DataInputStream(cSocket.getInputStream());
        in = new BufferedReader(new InputStreamReader(dis));
    }

    public static void sendToServer(String data) throws IOException {
        // send the string parameter to the server
        data += '\n';
        out.writeChars(data);
    }
}
