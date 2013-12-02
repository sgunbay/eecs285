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
		grid = new Tile[rows][cols];
		for (int i = 0; i < rows; ++i){
			for (int j = 0; j < cols; ++j){
				grid[i][j] = new TilePlains(new Coord(i,j));
			}
		}				
	}
	
	Grid(int size){
		rows = Math.min(size,max_row);
		cols = Math.min(Math.min(size,max_col),7*rows/3);
		initialize();
	}
	
	Grid(int row_in, int col_in){
		rows = Math.min(row_in,max_row);
		cols = Math.min(Math.min(col_in,max_col),7*rows/3);
		initialize();
	}
	
	public void load(File file){
		BufferedReader istream;
		String s;
		try {
			istream = new BufferedReader(new FileReader(file.getPath()));
			s = istream.readLine();
			// IMPLEMENT!!
		
		
		}
		catch (Exception e){System.out.println("Error: An exception was thrown."); System.exit(1);}
		
		
		initialize();
		// IMPLEMENT!!		
	}
	
	public void save(File file){
		// IMPLEMENT!!
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
		String s = new String();
		for (int i = 0; i < rows; ++i){
			for (int j = 0; j < cols; ++j)
				s = s.concat(getTile(new Coord(i,j)).name.charAt(0) + " ");
			s = s.concat("\n");
		}	
		return s;
	}

}
