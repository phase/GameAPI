package xyz.jadonfowler.gameapi;

import java.io.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jadonfowler.gameapi.game.*;
import xyz.jadonfowler.gameapi.listener.*;
import xyz.jadonfowler.gameapi.lobby.LobbyManager;

public class GameAPI extends JavaPlugin {

    static Plugin instance;

    File f;

    FileConfiguration config;

    private static Location joinLocation = new Location(Bukkit.getWorld("world"), 488.5, 8.0, -303.5);

    private static Location spawnLocation = new Location(Bukkit.getWorlds().get(0), 0, 64, 0);

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
        p.registerEvents(new LobbyManager(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new FreezeTask(), 0, 1);
        f = new File("plugins/GameAPI/game/main/mobs.yml");
        config = YamlConfiguration.loadConfiguration(f);
        getMobs();
    }

    /**
     * Gets mobs from config and freezes them
     */
    private void getMobs() {
        List<String> s = config.getStringList("Mobs");
        List<UUID> u = new ArrayList<UUID>();
        for (String t : s)
            u.add(UUID.fromString(t));
        for (Entity e : Bukkit.getWorlds().get(0).getEntities())
            if (u.contains(e.getUniqueId())) FreezeTask.addMob(e, e.getLocation());
    }

    /**
     * Main disable method
     */
    public void onDisable() {
        List<String> s = new ArrayList<String>();
        for (Entity e : FreezeTask.getMobs())
            if (!e.isDead()) s.add(e.getUniqueId().toString());
        config.set("Mobs", s);
        try {
            config.save(f);
        }
        catch (IOException ex) {}
        for (Game g : Game.GameList) {
            for (Arena a : g.getArenas()) {
                a.stop();
            }
        }
    }

    /** <b>TODO</b> make a game lobby! */
    public static Location getGameLobby() {
        return joinLocation;
    }

    /** Gets the main instace of the GameAPI */
    public static Plugin getInstance() {
        return instance;
    }

    /**
     * Gets a random color
     *
     * @return Random ChatColor
     */
    public static ChatColor getRandomColor() {
        Random r = new Random();
        int i = r.nextInt(10) + 1;
        switch (i) {
        case 1:
            return ChatColor.RED;
        case 2:
            return ChatColor.LIGHT_PURPLE;
        case 3:
            return ChatColor.BLUE;
        case 4:
            return ChatColor.DARK_PURPLE;
        case 5:
            return ChatColor.GREEN;
        case 6:
            return ChatColor.GOLD;
        case 7:
            return ChatColor.AQUA;
        case 8:
            return ChatColor.YELLOW;
        case 9:
            return ChatColor.WHITE;
        case 10:
            return ChatColor.GRAY;
        default:
            return ChatColor.GREEN;
        }
    }

    /**
     * Get Join Location
     *
     * @return Join Location
     */
    public static Location getJoinLocation() {
        return joinLocation;
    }

    /**
     * Get Spawn Location
     *
     * @return Spawn Location
     */
    public static Location getSpawnLocation() {
        return spawnLocation;
    }

    /**
     * Gets a Player Name from a UUID, only does
     *
     * @param uuid
     * @return
     */
    public static String getPlayerName(UUID uuid) {
        return Bukkit.getPlayer(uuid).getName();
    }
}
