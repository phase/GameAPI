package xyz.jadonfowler.gameapi.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.jadonfowler.gameapi.game.Arena;
import xyz.jadonfowler.gameapi.game.Game;
/**
 * Checks if Player leaves
 * @author Phase
 *
 */
public class LeaveListener implements Listener{
	@EventHandler
	public void leaveGame(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(Game.isInGame(p)){
			Arena.getArena(p).removePlayer(p);
		}
	}

}
