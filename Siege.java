package com.eecs285.siegegame;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Random;

import com.eecs285.siegegame.ActionParser.ActionType;

public class Siege {

    // These are required for the classes to work properly:
    public static Grid grid;
    public static int numCities;
    public static int currentPlayer;
    public static Player[] players;
    public static String[] playerNames;
    public static Random rnd = new Random(10000);

    // required for networking functionality
    // static BufferedReader in;
    static DataOutputStream out;
    static ObjectInputStream in;
    static int portNum = 45000;
    private final static String IPaddress = "127.0.0.1"; // server IP
    
    

    public static void main(String args[]) throws Exception {
        // initialize connection to server (including IO streams)
        initServerConnection();

        final int rows = 25;
        final int cols = 40;

        grid = new Grid(rows, cols);
        grid.initialize();

        MainGameFrame mainFrame = new MainGameFrame(grid);
        mainFrame.printNarration("Jordan added a bit of functionality");
        mainFrame.printNarration("Jordan tested the functionality");
        mainFrame.printNarration("Jordan is making sure the scrollbar works");
        for (int i = 0; i < 50; i++)
            mainFrame.printNarration("Filling in 50 lines...");
        mainFrame.printNarration("Jordan ended turn");
        
        //get player names array from server
        playerNames = (String[]) in.readObject();
        playerNames = fixDuplicates(playerNames);
        for(int i = 0; i < 4; i++) {
            System.out.println("playerNames[" + i + "] = " + playerNames[i]);
        }
        
        //create player objects here
        
        
        

        while (true) {
            // Get string from server
            // String fromServer = in.readLine();
            String fromServer = (String) in.readObject();
            fromServer = Siege.parseServerString(fromServer);
            System.out.println("FROM SERVER: " + fromServer);

            // Instantiate ActionParser
            ActionParser parser = new ActionParser(fromServer);

            // Get action type from string and initialize variables
            ActionType atype = parser.getActionType();
            Coord attacker = null, target = null;
            Coord city = null, resource = null;

            // If atype is null, then there was an issue reading the string
            if (atype == null) {
                System.out.println("ERROR: atype is null");
                System.exit(-1);
            }

            // perform action depending on what server string specifies
            switch (atype) {
            case ATTACK_ARMY:
                System.out.println("Attack Army");
                attacker = parser.getFirstCoordinate();
                target = parser.getSecondCoordinate();
                System.out.println("attacking army at (" + attacker.row + ", "
                        + attacker.col + ")");
                System.out.println("target army at (" + target.row + ", "
                        + target.col + ")");

                parser.getFirstPlayer();

                break;
            case ATTACK_CITY:
                System.out.println("Attack City");
                attacker = parser.getFirstCoordinate();
                city = parser.getSecondCoordinate();
                System.out.println("attacking army at (" + attacker.row + ", "
                        + attacker.col + ")");
                System.out.println("target city at (" + city.row + ", "
                        + city.col + ")");

                break;
            case CAPTURE_CITY:
                System.out.println("Capture City");
                city = parser.getFirstCoordinate();

                break;
            case CAPTURE_RESOURCE:
                System.out.println("Capture Resource");
                resource = parser.getFirstCoordinate();

                break;
            case CITY_LIBERATED:
                System.out.println("Liberate City");
                city = parser.getFirstCoordinate();

                break;
            case CITY_UNDER_SIEGE:
                System.out.println("Siege City");
                city = parser.getFirstCoordinate();

                break;
            case END_TURN:
                System.out.println("End Turn");
                players[currentPlayer].endTurn();                
                
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
                resource = parser.getFirstCoordinate();

                break;
            case RESOURCE_UNDER_CONFLICT:
                System.out.println("Resource Contested");
                resource = parser.getFirstCoordinate();

                break;
            case PLAYER_DEFEATED:
                System.out.println("Player Defeated");
                break;
            case PLAYER_WINS:
                System.out.println("Player Wins");
                break;
            case NAME_CHANGE:
                System.out.println("Name change occured");
                break;
            case WAITING_FOR_PLAYERS:
                System.out.println("Waiting for players...");
                break;
            case STARTING_GAME:
                System.out.println("Starting game");
                break;
            case IS_READY:
                System.out.println("Is ready");
                break;
            default:
                System.out
                        .println("ERROR: Action did not specify a known ActionType");
                System.exit(-1);
                break;
            }
        }
    }

    private static String[] fixDuplicates(String[] names) {
        // if duplicates exist, append 2nd occurrence with a 2, 3rd w/ 3, etc
        for(int i = 0; i < names.length - 1; i++) {
            String curName = names[i];
            int numOccurrences = 1;
            for(int j = i + 1; j < names.length; j++) 
                if(names[j] == curName) {
                    numOccurrences++;
                    names[j] = names[j].concat(" " + numOccurrences);
                }                    
        }
        return names;        
    }

    private static void initServerConnection() throws Exception {
        // create a connection to the server and initialize IO streams
        Socket cSocket = new Socket(IPaddress, portNum);
        out = new DataOutputStream(cSocket.getOutputStream());
        in = new ObjectInputStream(cSocket.getInputStream());
    }

    public static void sendToServer(String data) throws Exception {
        // send the string parameter to the server
        data += '\n';
        out.writeChars(data);
    }

    public static String parseServerString(String input) {
        String temp = "";
        for (int i = 0; i < input.length(); i++) {
            if (Character.isLetterOrDigit(input.charAt(i))
                    || input.charAt(i) == ' ' || input.charAt(i) == '('
                    || input.charAt(i) == ')' || input.charAt(i) == ',')
                temp += input.charAt(i);
        }
        return temp;
    }
}
