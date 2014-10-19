package five.game.survivalist.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import five.game.game.Game;

public class MainListener implements Listener {

	@EventHandler
	public void switchWeapon(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(Game.isInGame(p)){
			if(Game.getGame(p).getName().equalsIgnoreCase("Survivalist")){
				ItemStack i = p.getItemInHand();
				if(i.getType() == Material.STONE_SWORD && e.getAction() == Action.RIGHT_CLICK_AIR)
					i.setType(Material.BOW);
				else if(i.getType() == Material.BOW && e.getAction() == Action.LEFT_CLICK_AIR)
					i.setType(Material.STONE_SWORD);
			}
		}
	}

}
