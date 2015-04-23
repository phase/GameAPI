package xyz.jadonfowler.gameapi.game;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import xyz.jadonfowler.gameapi.message.MessageManager;
import xyz.jadonfowler.gameapi.message.Prefix;

public class Team {
	
	ArrayList<UUID> PlayersOnTeam = new ArrayList<UUID>();
	
	String TeamName;
	int MaxPlayers;
	ChatColor Color;
	int Score;
	
	/**
	 * Creates a new Team.
	 * @param Name of the Team.
	 * @param MaxPlayers of the Team. Set to -1 for infinite.
	 * @param Color of the Team.
	 */
	public Team(String name, int MaxPlayers, ChatColor color){
		this.TeamName = name;
		this.MaxPlayers = MaxPlayers;
		this.Color = color;
	}
	
	/**
	 * Adds Player to Team
	 * @param p - Player to add
	 * @return The team for no reason, just usefullness.
	 */
	public Team addPlayer(Player p){
		if(!isOnTeam(p)){
			if(MaxPlayers == -1 || PlayersOnTeam.size() < MaxPlayers)
				PlayersOnTeam.add(p.getUniqueId());
		}else{
			MessageManager.sendMessage(p, Prefix.INFO(), "You are already on the " + getName() + " team!");
		}
		return this;
	}
	
	/**
	 * Removes Player from Team
	 * @param p - Player to remove
	 * @return The team for no reason, just usefullness.
	 */
	public Team removePlayer(Player p){
		if(isOnTeam(p)){
			PlayersOnTeam.remove(p.getUniqueId());
		}else{
			MessageManager.sendMessage(p, Prefix.INFO(), "You aren't on the " + getName() + " team!");
		}
		return this;
	}
	
	/**
	 * Checks if Player is on Team
	 * @param p - Player
	 * @return if Player is on Team
	 */
	public boolean isOnTeam(Player p){
		return PlayersOnTeam.contains(p.getUniqueId());
	}
	
	/**
	 * @return name of Team
	 */
	public String getName(){
		return TeamName;
	}
	
	/**
	 * @return Max number of players that can join team, if -1, all players can join.
	 */
	public int getMaxPlayers(){
		return MaxPlayers;
	}
	
	/**
	 * Get color of the Team
	 * @return Color of the team as a ChatColor
	 */
	public ChatColor getColor(){
		return Color;
	}
	
	/**
	 * Gets Players on Team
	 * @return List of Players' UUIDs
	 */
	public ArrayList<UUID> getPlayers(){
		return PlayersOnTeam;
	}
	
	/**
	 * Sets score of Team, not all games might use this.
	 * @return Score of team
	 */
	public int setScore(int s){
		Score = s;
		return Score;
	}
	
	/**
	 * Gets score of Team, not all games might use this.
	 * @return Score of Team
	 */
	public int getScore(){
		return Score;
	}
	
	/**
	 * Add score from team
	 * @param s - how many to add
	 */
	public void addScore(int s){
		Score += s;
	}
	
	/**
	 * Minus score from team.
	 * @param s - how many to subtract
	 */
	public void minusScore(int s){
		Score -= s;
	}
	
	/**
	 * Resets score to 0
	 */
	public void resetScore(){
		setScore(0);
	}
	
	/**
	 * Static method for generic Players Team
	 * @param i - Max number of players. 
	 * @return Team "Players"
	 */
	public static Team PLAYERS(int i){
		return new Team("Players", i, ChatColor.YELLOW);
	}
	
}
