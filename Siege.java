package com.eecs285.siegegame;

import java.awt.Color;
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
	public static Player []players;
// -- Sinan

    static BufferedReader in;
    static DataOutputStream out;
    static int portNum = 45000;
    
    //change this to whatever IP address the server has
    private final static String IPaddress = "127.0.0.1"; // localhost
                                                         

    public static void main(String args[]) throws Exception {
//    initialize connection to server (including IO streams)
//    initServerConnection();

      final int rows = 25;
      final int cols = 40;
      
      Tile[][] mapTiles = new Tile[rows][cols];
      for (int i = 0; i < rows; i++)
        for (int j = 0; j < cols; j++)
          mapTiles[i][j] = new TilePlains(new Coord(i, j));
      
      MainGameFrame mainFrame = new MainGameFrame("Siege", rows, cols, mapTiles);
      mainFrame.printNarration("Jordan added a bit of functionality");
      mainFrame.printNarration("Jordan tested the functionality");
      mainFrame.printNarration("Jordan is making sure the scrollbar works");
      for (int i = 0; i < 50; i++)
        mainFrame.printNarration("Filling in 50 lines...");
      mainFrame.printNarration("Jordan ended turn");

      
      Coord oneOne = new Coord(1, 1);
      Tile plains = new TilePlains(oneOne);
      mainFrame.updateGridSquare(oneOne, plains);
      
//    while (true) {
//    String fromServer = in.readLine();
//    System.out.println("FROM SERVER: " + fromServer);
//    }
      
      //max testing ActionParser
      /*
      String test = "something sinister attacks something good";
      ActionType atype = ActionParser.getActionType(test);
      System.out.println(test);
      if(atype == ActionType.ATTACK) System.out.println("Attack");
      
      test = "Player 1 captures city from Player 2";
      atype = ActionParser.getActionType(test);
      System.out.println(test);
      if(atype == ActionType.CAPTURE_CITY) System.out.println("Capture city");
      
      test = "asdfajsdflkja ends turn";
      atype = ActionParser.getActionType(test);
      System.out.println(test);
      if(atype == ActionType.END_TURN) System.out.println("End turn");
      
      Coord coord = ActionParser.getFirstCoordinate("Army at (6, 1) attacked army at (19,51)");
      System.out.println("row = " + coord.row + "   col = " + coord.col);
      coord = ActionParser.getSecondCoordinate("Army at (6, 1) attacked army at (19,51)");
      System.out.println("row = " + coord.row + "   col = " + coord.col); 
      */
    }

    private static void initServerConnection() throws Exception {
        Socket cSocket = new Socket(IPaddress, portNum);
        out = new DataOutputStream(cSocket.getOutputStream());
        DataInputStream dis = new DataInputStream(cSocket.getInputStream());
        in = new BufferedReader(new InputStreamReader(dis));
    }

    public static void sendToServer(String data) throws IOException {
        data += '\n';
        out.writeChars(data);
    }
}
