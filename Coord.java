package com.eecs285.siegegame;

public class Coord {

	// TESTED AND APPROVED
	
	// A very basic pair of integers for coordinates.
	public int row;
	public int col;
	Coord(int row_in, int col_in){
		row = row_in;
		col = col_in;
	}
	
	public String toString(){		
		return "(" + row + ", " + col + ")";
	}
	
}
