package xyz._5th.gameapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import xyz._5th.gameapi.game.Arena;
import xyz._5th.gameapi.game.Game;
import xyz._5th.gameapi.main.GameAPI;
/**
 * Respawns Player at the Player's Team spawnpoint
 * @author Phase
 */
public class RespawnListener implements Listener {
	
	@EventHandler
	public void Respawn(PlayerRespawnEvent e){
		final Player p = e.getPlayer();
		if(Game.isInGame(p)){
			final Arena a = Arena.getArena(p);
			e.setRespawnLocation(a.getSpawn(p));
			Bukkit.getScheduler().scheduleSyncDelayedTask(GameAPI.getInstance(), new Runnable(){public void run(){
				p.teleport(a.getSpawn(p));
			}}, 1);
		}
	}
	
}
