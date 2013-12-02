package com.eecs285.siegegame;

import java.awt.*;
import java.util.Random;

public class Player {

	public int id;
	public Color color;
	public String name;
	
	private Integer gold;
	private Integer income;
	
	Player(int id_in, String name_in){
		name = new String(name_in);
		id = id_in;
		gold = new Integer(0);
		income = new Integer(0);
	}
	
	public void updateIncome(){
		income = 0;
		for (int i = 0; i < Siege.grid.rows; ++i){
			for (int j = 0; j < Siege.grid.cols; ++j){
				Tile target = Siege.grid.getTile(new Coord(i,j));
				if (target.isCity() && (target.owner == id) && (target.infers == 0)){
					income += target.income;
				}
				else if (target.isResource() && (target.owner == id) && (target.infers == 0)){
					income += target.income;
				}
			}
		}
	}
	
	public void transferAccGold(int accGold){
		gold += accGold;
	}
	
	public int getGold(){
		return gold;
	}
	
	public int getIncome(){
		return income;
	}
	
	public void subtractGold(int cost){
		assert(cost <= gold);
		gold -= cost;		
	}
	
	public void refreshPlayer(){
		
		for (int i = 0; i < Siege.grid.rows; ++i){
			for (int j = 0; j < Siege.grid.cols; ++j){
				Tile target = Siege.grid.getTile(new Coord(i,j));
				target.infers = 0;
				if (target.isResource())
					target.owner = -1;
				Siege.grid.setTile(new Coord(i,j), target);
			}
		}
		
		for (int i = 0; i < Siege.grid.rows; ++i){
			for (int j = 0; j < Siege.grid.cols; ++j){
				Tile target = Siege.grid.getTile(new Coord(i,j));
				target.refreshTile();
				Siege.grid.setTile(new Coord(i,j), target);
			}
		}
		for (int i = 0; i < 4; ++i)
			Siege.players[i].updateIncome();
		gold = gold + income;
	}
	
	public void endTurn(){
		Siege.players[Siege.currentPlayer].refreshPlayer();
		Siege.currentPlayer = (Siege.currentPlayer + 1)%Siege.numPlayers;
	}
	
}
