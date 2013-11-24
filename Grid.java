package eecs285.proj4;

import java.io.*;

public class Grid {

	int rows;
	int cols;
	Tile grid[][];
	
	public void initialize(int rows, int cols){
		grid = new Tile[rows][cols];
		for (int i = 0; i < rows; ++i){
			for (int j = 0; j < cols; ++j){
				grid[i][j] = new TilePlains();
			}
		}				
	}
	
	Grid(int size){
		initialize(size,size);	
		rows = size;
		cols = size;
	}
	
	Grid(int row_in, int col_in){
		initialize(row_in,col_in);	
		rows = row_in;
		cols = col_in;
	}
	
	public void load(File file){
		initialize(rows,cols);
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
}
