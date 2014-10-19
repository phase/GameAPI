package five.game.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import five.game.game.Arena;
import five.game.game.ArenaState;
import five.game.game.Game;
import five.main.FifthDimension;
/**
 * Handles Game choosing in the Lobby/Hub.
 * @author Phase
 *
 */
public class GameChooser implements Listener {

	@EventHandler
	public void EntityInteract(PlayerInteractEntityEvent e){
		Player p = e.getPlayer();
		Entity n = e.getRightClicked();
		if(!Game.isInGame(p)){
			if(n.getWorld().equals(FifthDimension.getMainWorld())){
				if(n instanceof LivingEntity){
					LivingEntity en = (LivingEntity) n;
					String game = en.getCustomName();
					if(Game.gameExist(ChatColor.stripColor(game))){
						openInventory(p, Game.getGame(ChatColor.stripColor(game)));
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void damageGame(EntityDamageEvent e){
		if(FreezeTask.getMobs().contains(e.getEntity()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void ChooseGame(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player){
			Player p = (Player) e.getWhoClicked();
			if(Game.isInGame(p)) return;
			if(Game.gameExist(ChatColor.stripColor(e.getInventory().getName()))){
				if(p.getWorld().equals(FifthDimension.getMainWorld())){
					e.setCancelled(true);
					
						int s = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]));
						Arena a = Arena.getArena(s);
						if(a.getState() == ArenaState.PRE_GAME)
							a.addPlayer(p);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void openInventory(Player p, Game g) {
		Inventory i = Bukkit.createInventory(null, getSize(g.getArenas().size()), FifthDimension.getRandomColor()+"§l"+g.getName());
		int c = 0;
		for(Arena a : g.getArenas()){
			byte color = getColor(a.getState());
			ItemStack wool = new ItemStack(Material.WOOL, 1, (short) 0, color);
			ItemMeta woolMeta = wool.getItemMeta();
			woolMeta.setDisplayName(FifthDimension.getRandomColor()+g.getName() + " " + a.getId());
			List<String> s = new ArrayList<String>();
			s.add(a.getState().getString());
			s.add("§7Players: §d"+a.getPlayers().size());
			if(a.getState() == ArenaState.IN_GAME)
				s.add("§dMap: §e" + a.getCurrentMap().getName() + "§d by: §e" + a.getCurrentMap().getCreator());
			woolMeta.setLore(s);
			wool.setItemMeta(woolMeta);
			i.setItem(c, wool);
			c++;//:3
		}
		p.openInventory(i);
	}

	private static byte getColor(ArenaState state) {
		if(state == ArenaState.PRE_GAME)
			return 5;
		return 14;
	}

	private static int getSize(int size) {
		if(size < 10)
			return 9;
		else if(size < 19)
			return 18;
		else if(size < 28)
			return 27;
		else if(size < 37)
			return 36;
		else if(size < 46)
			return 45;
		else
			return 54;
	}
	
}
