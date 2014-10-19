package five.game.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import five.game.game.Arena;
import five.game.game.Game;
import five.game.listener.ChatCommand;
import five.game.listener.FreezeTask;
import five.game.listener.GameChooser;
import five.game.listener.HubCommand;
import five.game.listener.LeaveListener;
import five.game.listener.RespawnListener;
import five.game.survivalist.Survivalist;
import five.game.woolchaser.WoolChaser;
import five.main.FifthDimension;

public class GameAPI extends JavaPlugin {

	static Plugin instance;

	File f;
	FileConfiguration config;

	/**
	 * Main enable method
	 */
	public void onEnable() {
		instance = this;
		PluginManager p = Bukkit.getPluginManager();
		p.registerEvents(new HubCommand(), this);
		p.registerEvents(new ChatCommand(), this);
		p.registerEvents(new GameChooser(), this);
		p.registerEvents(new RespawnListener(), this);
		p.registerEvents(new LeaveListener(), this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance,
				new FreezeTask(), 0, 1);
		f = new File(FifthDimension.getFilePath() + "game/main/mobs.yml");
		config = YamlConfiguration.loadConfiguration(f);
		getMobs();
		Survivalist.run();
		new WoolChaser();
	}

	/**
	 * Gets mobs from config and freezes them
	 */
	private void getMobs() {
		List<String> s = config.getStringList("Mobs");
		List<UUID> u = new ArrayList<UUID>();
		for (String t : s)
			u.add(UUID.fromString(t));
		for (Entity e : FifthDimension.getMainWorld().getEntities())
			if (u.contains(e.getUniqueId()))
				FreezeTask.addMob(e, e.getLocation());
	}

	/**
	 * Main disable method
	 */
	public void onDisable() {
		List<String> s = new ArrayList<String>();
		for (Entity e : FreezeTask.getMobs())
			if (!e.isDead())
				s.add(e.getUniqueId().toString());
		config.set("Mobs", s);
		try {
			config.save(f);
		} catch (IOException ex) {
		}
		for(Game g : Game.GameList){
			for(Arena a : g.getArenas()){
				a.stop();
			}
		}
	}

	/** <b>TODO</b> make a game lobby! */
	public static Location getGameLobby() {
		return FifthDimension.getJoinLocation();
	}

	/** Gets the main instace of the GameAPI */
	public static Plugin getInstance() {
		return instance;
	}

}
