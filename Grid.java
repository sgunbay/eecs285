package com.eecs285.siegegame;

import java.io.*;

public class Grid {

	// TESTED AND APPROVED
	
	public static int max_row = 30;
	public static int max_col = 40;
	public int rows;
	public int cols;
	private Tile grid[][];
	
	public void initialize(){
		// Initialize everything to Plains.
		grid = new Tile[rows][cols];
		for (int i = 0; i < rows; ++i){
			for (int j = 0; j < cols; ++j){
				grid[i][j] = new TilePlains(new Coord(i,j));
			}
		}				
	}
	
	Grid(int size){
	// A MORE basic constructor.
		rows = Math.min(size,max_row);
		cols = Math.min(Math.min(size,max_col),7*rows/3);
		initialize();
	}
	
	Grid(int row_in, int col_in){
	// Basic constructor.
		rows = Math.min(row_in,max_row);
		cols = Math.min(Math.min(col_in,max_col),7*rows/3);
		initialize();
	}
	
	public void load(File file) throws Exception{
		// Load from a map file.
		BufferedReader istream = new BufferedReader(new FileReader(file.getPath()));
		String s = istream.readLine();
			
		String []firstline = s.split(" ");
		rows = Integer.valueOf(firstline[0]);
		cols = Integer.valueOf(firstline[1]);
		initialize();
		
		int lines = Integer.valueOf(firstline[2]);
		for (int i = 0; i < lines; ++i){
			s = istream.readLine();
			firstline = s.split(" ");
			int row = Integer.valueOf(firstline[1]);
			int col = Integer.valueOf(firstline[2]);
			
			if (firstline[0].equalsIgnoreCase("F"))
				setTile(new Coord(row,col),new TileForest(new Coord(row,col)));
			else if (firstline[0].equalsIgnoreCase("M"))
				setTile(new Coord(row,col),new TileMountain(new Coord(row,col)));
			else if (firstline[0].equalsIgnoreCase("D"))
				setTile(new Coord(row,col),new TileMuddy(new Coord(row,col)));
			else if (firstline[0].equalsIgnoreCase("W"))
				setTile(new Coord(row,col),new TileWater(new Coord(row,col)));
			else if (firstline[0].equalsIgnoreCase("R")){
				int sz = 0;
				if (firstline[3].equalsIgnoreCase("S"))
					sz = TileResource.SMALL;
				else if (firstline[3].equalsIgnoreCase("M"))
					sz = TileResource.MEDIUM;
				else if (firstline[3].equalsIgnoreCase("L"))
					sz = TileResource.LARGE;				
				setTile(new Coord(row,col),new TileResource(sz,new Coord(row,col)));
			}	
			else if (firstline[0].equalsIgnoreCase("C"))			
				setTile(new Coord(row,col),new TileCity(Integer.valueOf(firstline[3]),new Coord(row,col)));
			else
				System.out.println("Invalid line.");		
		}
				
	}
	
	public void save(File file) throws Exception{
		// Save to a map file.
		BufferedWriter ostream = new BufferedWriter(new FileWriter(file.getPath()));
		int count = 0;
		for (int i = 0; i < rows; ++i){
			for (int j = 0; j < cols; ++j){
				if (!getTile(new Coord(i,j)).name.equalsIgnoreCase("plains"))	
					++count;
			}
		}
		ostream.write(rows + " " + cols + " " + count + "\n");
		for (int i = 0; i < rows; ++i){
			for (int j = 0; j < cols; ++j){
				Tile t = getTile(new Coord(i,j));
				if (t.name.equalsIgnoreCase("plains"))	
					continue;
				ostream.write(Character.toUpperCase(getTile(new Coord(i,j)).name.charAt(0)) + " " + i + " " + j);
				if (t.isResource())
					ostream.write(" " + t.income);
				else if (t.isCity())
					ostream.write(" " + t.owner);
				ostream.write("\n");		
			}
		}
		
		
	}
	
	public Tile getTile(Coord coord){
		return grid[coord.row][coord.col];		
	}
	
	public void setTile(Coord coord, Tile tile){
		grid[coord.row][coord.col] = tile;
	}
	
	public Army getOccupantAt(Coord coord){
		return grid[coord.row][coord.col].getOccupant();		
	}
	
	public void setOccupantAt(Coord coord, Army a){
		grid[coord.row][coord.col].setOccupant(a);		
	}
	
	public boolean withinBounds(Coord coord){
		return (coord.row >= 0 && coord.col >= 0 && coord.row < rows && coord.col < cols);	
	}
	
	public String toString(){
	// Print the entire grid as a string.
		String s = new String();
		for (int i = 0; i < rows; ++i){
			for (int j = 0; j < cols; ++j)
				s = s.concat(getTile(new Coord(i,j)).name.charAt(0) + " ");
			s = s.concat("\n");
		}	
		return s;
	}

}
