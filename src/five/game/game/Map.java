package five.game.game;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class Map {
	
	String name;
	String world;
	String creator;
	HashMap<Team, Location> Spawns;
	boolean isLoaded;
	
	/**
	 * Creates a map for the players to play on.
	 * @param name of the map
	 * @param world name of the world
	 * @param creator name of the author
	 * @param Spawns used for spawning 
	 */
	public Map(String name, String world, String creator, HashMap<Team, Location> Spawns){
		this.name = name;
		this.world = world;
		this.creator = creator;
		this.Spawns = Spawns;
		this.isLoaded = false;
	}
	
	/**
	 * Loads world
	 */
	public void loadWorld(){
		if(isLoaded) unloadWorld();
		Bukkit.createWorld(new WorldCreator(world));
		isLoaded = true;
	}
	
	/**
	 * Unloads world, doesnt save any changes. Allows for Players to destroy
	 * terrain and it not be saved.
	 */
	public void unloadWorld(){
		if(!isLoaded) return;
		Bukkit.unloadWorld(world, false);
		isLoaded = false;
	}
	
	/**
	 * @return if world is loaded
	 */
	public boolean isLoaded(){
		return isLoaded;
	}
	
	/**
	 * @return name of map
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return author of the map
	 */
	public String getCreator(){
		return creator;
	}
	
	/**
	 * @return name of the world to load, not the Map name.
	 */
	public String getWorldName(){ 
		return world;
	}
	
	/**
	 * <b>MAY BE NULL! USE getWorldName()!</b>
	 * @return The World instace, may be <b>null</b>
	 */
	public World getWorld(){
		return Bukkit.getWorld(world);
	}
	
	/**
	 * @return HashMap of Teams & Spawn Locations
	 */
	public HashMap<Team, Location> getSpawns(){
		return Spawns;
	}
}
