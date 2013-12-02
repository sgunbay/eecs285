package com.eecs285.siegegame;

import java.awt.*;
import java.util.Random;

public class Player {

	public int id; // Player number.
	public String name; // Player name.
	
	private Integer gold; // Player's gold.
	private Integer income; // Player's income.
	
	Player(int id_in, String name_in){
	// Constructor.
		name = new String(name_in);
		id = id_in;
		gold = new Integer(0);
		income = new Integer(0);
	}
	
	public void updateIncome(){
	// Refresh the income of player.
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
		gold += accGold; // Receive gold from liberated cities.
	}
	
	public int getGold(){
		return gold;
	}
	
	public int getIncome(){
		return income;
	}
	
	public void subtractGold(int cost){
		assert(cost <= gold); // For deducting unit training costs.
		gold -= cost;		
	}
	
	public void refreshPlayer(){
		// Refreshes the player's income and gold, as well as the influence statuses of cities and army movements.
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
		for (int i = 0; i < Siege.numPlayers; ++i)
			Siege.players[i].updateIncome();
		gold = gold + income;
	}
	
	public void endTurn(){
		// Ends player's turn by both refreshing it and incrementing current player.
		Siege.players[Siege.currentPlayer].refreshPlayer();
		Siege.currentPlayer = (Siege.currentPlayer + 1)%Siege.numPlayers;
	}
	
}
