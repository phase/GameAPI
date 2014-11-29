package xyz._5th.gameapi.game;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import xyz._5th.gameapi.lobby.LobbyManager;
import xyz._5th.gameapi.main.GameAPI;
import xyz._5th.gameapi.message.MessageManager;
import xyz._5th.gameapi.message.Prefix;

public class Arena {

	public static ArrayList<Arena> ArenaList = new ArrayList<Arena>();
	ArrayList<UUID> Players;
	ArrayList<Team> Teams;

	ArrayList<Map> Maps;
	Map currentMap;
	Team winner;
	ArenaState gameState;
	String name;
	String desc;
	ArrayList<String> preGameNotes;
	int id;
	GameRunnable gameRunnable;
	ArrayList<Integer> countdowns;
	
	/**
	 * Main Contructor for the Arena.
	 * @param id - Number for Arena
	 * @param name - Name for Arena
	 * @param desc - Dscription about Arena
	 * @param preGameNotes - Notes for before the game, usually the same for all Arenas in one Game.
	 * @param teams - Teams for Arena.
	 * @param maps - Maps for Arena.
	 * @param r - GameRunnable for Arena
	 */
	public Arena(int id, String name, String desc,
			ArrayList<String> preGameNotes, ArrayList<Team> teams,
			ArrayList<Map> maps, GameRunnable r) {
		this.Teams = teams;
		this.Maps = maps;
		this.name = name;
		this.desc = desc;
		this.preGameNotes = preGameNotes;
		this.gameState = ArenaState.PRE_GAME;
		this.Players = new ArrayList<UUID>();
		this.id = id;
		this.gameRunnable = r;
		ArenaList.add(this);
	}

	/**
	 * Adds a player to the Arena.
	 * @param p - Player to add
	 */
	@SuppressWarnings("deprecation")
	public void addPlayer(Player p) {
		if (gameState.equals(ArenaState.PRE_GAME)) {
			Players.add(p.getUniqueId());
			p.teleport(GameAPI.getGameLobby());
			p.setHealth(20d);
			p.setFoodLevel(20);
			p.setFireTicks(0);
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			p.setGameMode(GameMode.SURVIVAL);
			LobbyManager.changeLobby(p, -1 * id, false);
			for (Player o : Bukkit.getOnlinePlayers())
				if (!Players.contains(o.getUniqueId())) {
					p.hidePlayer(o);
					o.hidePlayer(p);
				} else {
					p.showPlayer(o);
					o.showPlayer(p);
				}
			giveRandomTeam(p);
			checkStart();
		}
	}

	/**
	 * Gives random Team to a Player
	 * @param p - Player to give Team to.
	 */
	private void giveRandomTeam(Player p) {
		Team t = null;
		for (Team te : Teams) {
			if (t == null || te.getPlayers().size() < t.getPlayers().size()) {
				t = te;
			}
		}
		t.addPlayer(p);
	}

	/**
	 * Checks if game can start, and starts the countdown if it can.
	 */
	private void checkStart() {
		countDown();
//		int max = 0;
//		for (Team t : Teams)
//			max += t.getMaxPlayers();
//		if (max == getPlayers().size()) {
//			broadcastMessage(Prefix.INFO(),
//					"We have acquired enough players to start the game!");
//			countDown();
//		}
	}

	/**
	 * Removes a player from the Arena.
	 * @param p - Player to remove
	 */
	public void removePlayer(Player p) {
		Players.remove(p.getUniqueId());
		p.teleport(GameAPI.getSpawnLocation());
		p.setHealth(20d);
		p.setFoodLevel(20);
		p.setFireTicks(0);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		LobbyManager.giveRandomLobby(p);
		if (Players.size() < 1)
			stop();

	}

	/**
	 * Checks if a Player is in the Arena
	 * @param p - Player to check
	 * @return if Player is in the Arena
	 */
	public boolean isInArena(Player p) {
		return Players.contains(p.getUniqueId());
	}

	/**
	 * Starts the game.
	 */
	public void start() {
		if (gameState.equals(ArenaState.PRE_GAME)) {
			currentMap = getRandomMap();
			currentMap.loadWorld();
			if (gameRunnable != null)
				gameRunnable.start();
			startMessage();

			gameState = ArenaState.IN_GAME;
			if (Game.getGame(this).usesTime()) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						GameAPI.getInstance(), new Runnable() {
							public void run() {
								if (getState() == ArenaState.IN_GAME)
									stop();
							}
						}, Game.getGame(this).getTime() * 20 * 60);
				int time = Game.getGame(this).getTime();
				ArrayList<Integer> countdowns = new ArrayList<Integer>();
				for (int x = 1; x < time; x++) {
					int i = Bukkit.getScheduler().scheduleSyncDelayedTask(
							GameAPI.getInstance(),
							new CountdownRunnable(this, x), (time-x)*60*20);
					countdowns.add(i);
				}
				this.countdowns = countdowns;
			}
			teleportPlayers();
		}
	}

	private void teleportPlayers() {
		try {
			for (UUID u : Players) {
				Player p = Bukkit.getPlayer(u);
				Location l = currentMap.getSpawns().get(getTeam(p));
				p.teleport(l);
			}
		} catch (Exception e) {
			currentMap.loadWorld();
			teleportPlayers();
		}
	}

	/**
	 * Stops the game.
	 */
	public void stop() {
		if (gameState.equals(ArenaState.IN_GAME)) {
			gameState = ArenaState.POST_GAME;
			stopMessage();
			for(Team t : Teams)
				t.resetScore();
			if(gameRunnable != null){
				gameRunnable.stop();
				gameRunnable.win(winner);
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(
					GameAPI.getInstance(), new Runnable() {
						public void run() {
							for (UUID u : Players) {
								Player p = Bukkit.getPlayer(u);
								p.teleport(GameAPI.getGameLobby());
							}
							gameState = ArenaState.PRE_GAME;
							currentMap.unloadWorld();
							checkStart();
						}
					}, 7 * 20l);
			for(int i : this.countdowns)
				Bukkit.getScheduler().cancelTask(i);
		}
	}

	/**
	 * Terrible way to do the pre-game countdown, will change soon.
	 */
	public void countDown() {
		broadcastMessage(Prefix.INFO(), "Game starting in 1 minute!");
		s(new Runnable() {
			public void run() {
				broadcastMessage(Prefix.INFO(), "Game starting in 50 seconds!");
				s(new Runnable() {
					public void run() {
						broadcastMessage(Prefix.INFO(),
								"Game starting in 40 seconds!");
						s(new Runnable() {
							public void run() {
								broadcastMessage(Prefix.INFO(),
										"Game starting in 30 seconds!");
								s(new Runnable() {
									public void run() {
										broadcastMessage(Prefix.INFO(),
												"Game starting in 20 seconds!");
										s(new Runnable() {
											public void run() {
												broadcastMessage(Prefix.INFO(),
														"Game starting in 10 seconds!");
												s(new Runnable() {
													public void run() {
														start();
													}
												}, 10 * 20);
											}
										}, 10 * 20);
									}
								}, 10 * 20);
							}
						}, 10 * 20);
					}
				}, 10 * 20);
			}
		}, 10 * 20);
	}
	
	/**
	 * Terrible way to do the pre-game countdown, will change soon.
	 */
	private void s(Runnable r, long delay) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameAPI.getInstance(), r,
				delay);
	}

	/**
	 * @return a random map that is not the current map & is not being played by
	 *         another map, allows for different arenas to use the same map.
	 */
	private Map getRandomMap() {
		Random r = new Random();
		Map m = Maps.get(r.nextInt(Maps.size()));
		for(Arena a : Game.getGame(this).getArenas())
			if(a.getCurrentMap() == m && a != this)
				return getRandomMap();
		if (m == currentMap && Maps.size() != 1)
			return getRandomMap();
		return m;
	}
	/**
	 * Message before the game starts.
	 */
	private void startMessage() {
		for (UUID u : Players) {
			Player p = Bukkit.getPlayer(u);
			p.sendMessage("§5§l-----------------");
			p.sendMessage(" ");
			p.sendMessage("§aGame§3: §c§l" + getName());
			p.sendMessage(" ");
			for (String s : preGameNotes)
				p.sendMessage("   §9-§3" + s);
			p.sendMessage(" ");
			p.sendMessage("§eMap: §3§l" + currentMap.getName() + " §eby§5§l "
					+ currentMap.getCreator());
			p.sendMessage(" ");
			p.sendMessage("§5§l------------------");
			MessageManager.sendTitle(p, 1 * 20, 5 * 20, 1 * 20, GameAPI.getRandomColor() + getGame().getName(), ChatColor.GREEN + "Get Ready");
		}
	}

	/**
	 * Message when game ends.
	 */
	private void stopMessage() {
		for (Team t : Teams)
			if (winner == null || winner.getScore() < t.getScore())
				winner = t;
		for (UUID u : Players) {
			Player p = Bukkit.getPlayer(u);
			p.sendMessage("§5§l-----------------");
			p.sendMessage(" ");
			p.sendMessage("§aGame§3:§c§l" + getName());
			p.sendMessage(" ");
			if (winner.getPlayers().size() == 1 && GameAPI.getPlayerName(winner.getPlayers().get(0)) != null){
				p.sendMessage(winner.getColor() + "§lThe winner is "
						+ GameAPI.getPlayerName(winner.getPlayers().get(0)) + "!");
				MessageManager.sendTitle(p, 1 * 20, 5 * 20, 1 * 20, winner.getColor() + Bukkit.getPlayer(winner.getPlayers().get(0)).getName(), winner.getColor() + "won the game!");
			}
			else{
				p.sendMessage(winner.getColor() + "§lThe winner is "
						+ winner.getName() + "!");
				MessageManager.sendTitle(p, 1 * 20, 5 * 20, 1 * 20, winner.getColor() + winner.getName(), winner.getColor() + "won the game!");
			}
			p.sendMessage(" ");
			p.sendMessage("§eMap: §3§l" + currentMap.getName() + " §eby§5§l "
					+ currentMap.getCreator());
			p.sendMessage(" ");
			p.sendMessage("§5§l-----------------");
		}
	}
	
	/**
	 * Broadcast a message to all the players in the game.
	 * @param prefix - Prefix for the message
	 * @param m - Message to send
	 */
	public void broadcastMessage(Prefix prefix, String m) {
		for (UUID u : Players)
			MessageManager.sendMessage(Bukkit.getPlayer(u), prefix, m);
	}

	/**
	 * Gets the name of the arena, used for inventories and whatnot.
	 * @return the name of the arena
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the Team that the Player is on. 
	 * @param p - Player you want the team for.
	 * @return the Team the Player is on.
	 */
	public Team getTeam(Player p) {
		for (Team t : Teams)
			if (t.getPlayers().contains(p.getUniqueId()))
				return t;
		return null;
	}

	public Team getTeam(String name){
		for (Team t : Teams)
			if (t.getName().equalsIgnoreCase(name))
				return t;
		return null;
	}
	
	/**
	 * Gets the List of Players.
	 * @return list of Players' UUIDs
	 */
	public ArrayList<UUID> getPlayers() {
		return Players;
	}

	/**
	 * Static method to check if Player is in any Arena
	 * @param p - Player to check
	 * @return if Player's in an Arena
	 */
	public static boolean isInGame(Player p) {
		return getArena(p) == null;
	}

	/**
	 * Static method to get the Arena of the player
	 * @param p - Player
	 * @return Arena Player is in
	 */
	public static Arena getArena(Player p) {
		if (Game.isInGame(p)) 
			for (Arena a : Game.getGame(p).getArenas()) 
				if (a.getPlayers().contains(p.getUniqueId())) 
					return a;
		return null;
	}

	/**
	 * Gets number id of the Arena
	 * @return id of arena
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets current map that has been played, is about to be played, or is being played.
	 * @return Current Map
	 */
	public Map getCurrentMap() {
		return currentMap;
	}

	/**
	 * Gets spawn of Player
	 * @param p - Player
	 * @return the Location for Player's team spawn.
	 */
	public Location getSpawn(Player p) {
		return getCurrentMap().getSpawns().get(getTeam(p));
	}

	/**
	 * Get ArenaState
	 * @return ArenaState
	 */
	public ArenaState getState() {
		return gameState;
	}

	/**
	 * Static method to get an Arena by id
	 * @param id - Id of Arena
	 * @return Arena by id
	 */
	public static Arena getArena(int id) {
		for (Arena a : ArenaList)
			if (a.getId() == id)
				return a;
		return null;
	}

	/**
	 * Setter for GameRunnable
	 * @param r - GameRunnable
	 */
	public void setRunnable(GameRunnable r) {
		this.gameRunnable = r;
	}
	
	/**
	 * Gets game that this Arena is a part of
	 * @return Arena's Game
	 */
	public Game getGame(){
		return Game.getGame(this);
	}
}
