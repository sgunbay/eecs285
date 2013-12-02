package com.eecs285.siegegame;

import java.io.DataOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Random;

import com.eecs285.siegegame.ActionParser.ActionType;

public class Siege {

    // These are required for the classes to work properly:
	public static MainGameFrame mainFrame;
    public static Grid grid;
    public static int numCities;
    public static int numPlayers;
    public static int currentPlayer;
    public static Player[] players;
    public static String[] playerNames;
    public static Random rnd = new Random(10000);

    // required for networking functionality
    // static BufferedReader in;
    static DataOutputStream out;
    static ObjectInputStream in;
    static int portNum = 45000;
    private final static String IPaddress = "67.194.55.95"; // server IP
    
    public static int attemptTrain(Tile city, String unitType){
    	if (unitType.equalsIgnoreCase("Basic") && city.trainUnitBasic())
			return 1;	
		else if (unitType.equalsIgnoreCase("Explorer") && city.trainUnitExplorer())
			return 1;
		else if (unitType.equalsIgnoreCase("Attacker") && city.trainUnitAttacker())
			return 1;
		else if (unitType.equalsIgnoreCase("Rusher") && city.trainUnitRusher())
			return 1;
		else
			return 0;
    }

    public static void main(String args[]) throws Exception {
        // initialize connection to server (including IO streams)
        initServerConnection();

        File map = new File("src/Resources/test0.map");

        grid = new Grid(1);
        grid.load(map);
        
        final int rows = grid.rows;
        final int cols = grid.cols;
       
        mainFrame = new MainGameFrame(grid);

        
        //get player names array from server
        playerNames = (String[]) in.readObject();
        playerNames = fixDuplicates(playerNames);
        
        numPlayers = playerNames.length;
        players = new Player[numPlayers];
        for(int i = 0; i < playerNames.length; i++)
            players[i] = new Player(i,playerNames[i]);       
        
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
                parser.getFirstPlayer();
                break;
            case ATTACK_CITY:
                System.out.println("Attack City");
                attacker = parser.getFirstCoordinate();
                city = parser.getSecondCoordinate();
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
                mainFrame.printNarration(players[currentPlayer].name + " ends turn.");
                players[currentPlayer].endTurn();
                mainFrame.updateAllGridSquares();
                mainFrame.updatePlayer();
                break;
            case LOSE_UNITS:
                System.out.println("Lose Units");
                break;
            case MERGE_ARMY:
                System.out.println("Merge Army");
                break;
            case MOVE_ARMY:
                Coord me = parser.getFirstCoordinate();
                Coord victim = parser.getSecondCoordinate();
            	if (grid.getOccupantAt(me).attemptMove(victim))
            		mainFrame.printNarration(fromServer);
            	else
            		System.out.println("Invalid move attempted.");
            	mainFrame.updateGridSquare(me);
            	mainFrame.updateGridSquare(victim);
            	
                break;
            case RECRUIT:
            	city = parser.getFirstCoordinate();
            	int numUnits = parser.getNumUnits();
            	String unitType = parser.getTypeUnits();
            	
            	Tile t = grid.getTile(city);
            	int count = 0;
            	for (int i = 0; i < numUnits; ++i)
            		count += attemptTrain(t,unitType);
            	
            	grid.setTile(city,t);
            	mainFrame.updateGridSquare(city);
            	mainFrame.printNarration(playerNames[currentPlayer] + " trains " + count + " " + unitType + " unit(s) at city " + city);
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
            	System.out.println("Starting game...");
            	for (int i = 0; i < numPlayers; ++i)
            		players[i].endTurn();
            	for (int i = 0; i < grid.rows; ++i){
            		for (int j = 0; j < grid.cols; ++j){
            			Tile tile = grid.getTile(new Coord(i,j));
            			if (tile.owner >= numPlayers){
            				grid.setTile(new Coord(i,j), new TileCity(-1,new Coord(i,j)));	
                			System.out.println(grid.getTile(new Coord(i,j)).getColor());
            			}
            		}
            	}
            	mainFrame.updateAllGridSquares();
            	mainFrame.printNonePanel();
            	mainFrame.updatePlayer();
            	
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
                    || input.charAt(i) == ')' || input.charAt(i) == ',' 
                    || input.charAt(i) == '.' || input.charAt(i) == '!')
                temp += input.charAt(i);
        }
        return temp;
    }
}
