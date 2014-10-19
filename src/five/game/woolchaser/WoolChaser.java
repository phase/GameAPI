package five.game.woolchaser;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import five.game.game.Arena;
import five.game.game.Game;
import five.game.game.Map;
import five.game.game.Team;
import five.game.woolchaser.listener.WoolListener;

public class WoolChaser {

	
	public WoolChaser() {
		ArrayList<String> pre = new ArrayList<String>();
		pre.add("Capture the Wool from the opposing side!");
		pre.add("Capture both Wool blocks to win!");

		ArrayList<Team> teams = new ArrayList<Team>();
		Team oranges = new Team("Oranges", -1, ChatColor.GOLD);
		Team apples = new Team("Apples", -1, ChatColor.GREEN);
		teams.add(oranges);
		teams.add(apples);

		HashMap<Team, Location> spawns = new HashMap<Team, Location>();
		spawns.put(oranges, new Location(Bukkit.getWorld("myth1"), 0, 24, -163));
		spawns.put(apples, new Location(Bukkit.getWorld("myth1"), 0, 24, -162));

		ArrayList<Map> maps = new ArrayList<Map>();
		Map mythicalmarsh = new Map("Mythical Marsh", "myth1",
				"Captian_Elliott, rockymine, & Lieutenantpants", spawns);
		maps.add(mythicalmarsh);
		
		ArrayList<Listener> listeners = new ArrayList<Listener>();
		listeners.add(new WoolListener());
		
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		Arena a1 = new Arena(1, "WoolChaser", "Capture the wool!", pre, teams, maps, null);
		arenas.add(a1);
		
		Game g = new Game("WoolChaser", "Capture the wool!", pre, arenas, listeners);
		g.useTime(true);
		g.setTime(90);
	}

	public static void checkWin(Arena a) {
		if (a.getTeam("Oranges").getScore() == 2
				|| a.getTeam("Apples").getScore() == 2)
			a.stop();
	}
}
