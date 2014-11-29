package xyz._5th.test.PaintBall;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import xyz._5th.gameapi.game.Game;

public class PaintListener implements Listener {
	
	@EventHandler
	public void shoot(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(Game.gameEquals(p, "PaintBall")){
			if(e.getAction().equals(Action.RIGHT_CLICK_AIR) && p.getItemInHand().getType().equals(Material.IRON_BARDING)){
				p.launchProjectile(Snowball.class);
			}
		}
	}
	
}
