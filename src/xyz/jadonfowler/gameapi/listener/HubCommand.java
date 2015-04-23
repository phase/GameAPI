package xyz._5th.gameapi.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import xyz._5th.gameapi.game.Arena;
import xyz._5th.gameapi.game.Game;
import xyz._5th.gameapi.main.GameAPI;

/**
 * Main /hub command.
 * @author Phase
 *
 */
public class HubCommand implements Listener {

	
	@EventHandler
	public void command(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(e.getMessage().split(" ")[0].equalsIgnoreCase("/hub")){
			if(Game.isInGame(p)){
				Arena a = Arena.getArena(p);
				a.removePlayer(p);
			}else
				p.teleport(GameAPI.getJoinLocation());
		}
			
	}

}
