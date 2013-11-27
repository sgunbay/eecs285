package com.eecs285.siegegame;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
//      while (true) {
//        String fromServer = in.readLine();
//        System.out.println("FROM SERVER: " + fromServer);
//        }
      
      Coord oneOne = new Coord(1, 1);
      Tile plains = new TilePlains(oneOne);
      mainFrame.updateGridSquare(oneOne, plains);

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
