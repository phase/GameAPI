package five.game.survivalist;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import five.game.game.Arena;
import five.game.game.Game;
import five.game.game.GameRunnable;
import five.game.game.Map;
import five.game.game.Team;
import five.game.survivalist.listener.MainListener;

public class Survivalist {

	public static void run() {
		ArrayList<String> pre = new ArrayList<String>();
		pre.add("Can you survive?");
		pre.add("You can break & place blocks to gather resources!");
		pre.add("Be the last on standing to win!");

		ArrayList<Team> teams = new ArrayList<Team>();
		teams.add(new Team("Players", 1, ChatColor.YELLOW));

		HashMap<Team, Location> spawns = new HashMap<Team, Location>();
		spawns.put(new Team("Players", 1, ChatColor.YELLOW), new Location(
				Bukkit.getWorld("testMap"), 0, 64, 0));

		ArrayList<Map> maps = new ArrayList<Map>();
		maps.add(new Map("TestMap", "testMap", "Bukkit Team", spawns));

		ArrayList<Arena> arenas = new ArrayList<Arena>();
		arenas.add(new Arena(123, "Survivalist", "Can you survive?", pre, teams,
				maps, new GameRunnable() {
					public void start() {
					}

					public void stop() {
					}
					public void win(Team team) {}
				}
		));

		ArrayList<Listener> ls = new ArrayList<Listener>();
		ls.add(new MainListener());
		Game g = new Game("Survivalist", "Can you survive?", pre, arenas, ls);
		g.useTime(true);
		g.setTime(10);
	}

}
