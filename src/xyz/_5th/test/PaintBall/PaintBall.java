package xyz._5th.test.PaintBall;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import xyz._5th.gameapi.game.Arena;
import xyz._5th.gameapi.game.Game;
import xyz._5th.gameapi.game.GameRunnable;
import xyz._5th.gameapi.game.Map;
import xyz._5th.gameapi.game.Team;

public class PaintBall {
	
	public PaintBall(){
		ArrayList<String> pre = new ArrayList<String>();
		ArrayList<Arena> as = new ArrayList<Arena>();
		ArrayList<Listener> lis = new ArrayList<Listener>();
		ArrayList<Team> ts = new ArrayList<Team>();
		ArrayList<Map> maps = new ArrayList<Map>();
		Team p = Team.PLAYERS(-1);
		lis.add(new PaintListener());
		HashMap<Team, Location> spawns = new HashMap<Team, Location>();
		spawns.put(p, new Location(Bukkit.getWorld("testmap"), 0, 64, 0));
		maps.add(new Map("TestMap", "testmap", "Mojang", spawns));
		as.add(new Arena(1, "PaintBall", "Shoot your paintballs at everyone!", pre, ts, maps, new GameRunnable(){
			public void start(){}public void stop(){}public void win(Team team){}}));
		Game game = new Game("PaintBall", "Shoot your paintballs at everyone!", pre, as, lis);
	}
	
}
