package five.game.woolchaser.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import five.game.game.Arena;
import five.game.game.ArenaState;
import five.game.game.Game;
import five.game.game.Team;
import five.game.message.Prefix;
import five.game.woolchaser.WoolChaser;

public class WoolListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void breakwWool(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(Game.isInGame(p))
			if(Game.getGame(p).getName().equalsIgnoreCase("WoolChaser"))
				if(e.getBlock().getType().equals(Material.WOOL))
					if(Arena.getArena(p).getState() == ArenaState.IN_GAME){
						Team team = Arena.getArena(p).getTeam(p);
						if(team.getName().equalsIgnoreCase("Apples")){
							if(e.getBlock().getData() == (byte) 10 || e.getBlock().getData() == (byte) 13){
								e.setCancelled(true);
							}else if(e.getBlock().getData() == ((byte) 11 | (byte) 14)){
								Arena.getArena(p).broadcastMessage(Prefix.INFO(), team.getColor()+p.getName() + "§4 took the " + getWool(e.getBlock().getData())+"!");
							}
						}else if(team.getName().equalsIgnoreCase("Oranges")){
							if(e.getBlock().getData() == (byte) 10 || e.getBlock().getData() == (byte) 13){
								Arena.getArena(p).broadcastMessage(Prefix.INFO(), team.getColor()+p.getName() + "§4 took the " + getWool(e.getBlock().getData())+"!");
							}else if(e.getBlock().getData() == ((byte) 11 | (byte) 14)){
								e.setCancelled(true);
							}
						}else{
							e.setCancelled(true);
						}
					}else
						e.setCancelled(true);
				
	}

	private String getWool(byte data) {
		switch(data){
		case 13: return "Green Wool";
		case 11: return "Blue Wool";
		case 10: return "Purple Wool";
		case 14: return "Red Wool";
		default: return "Wool";
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void placeWool(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(Game.isInGame(p))
			if(Game.getGame(p).getName().equalsIgnoreCase("WoolChaser"))
				if(e.getBlock().getType().equals(Material.WOOL)){
					Block alter = e.getBlock().getLocation().clone().add(0, -1, 0).getBlock();
					Block placed = e.getBlock();
					Team team = Arena.getArena(p).getTeam(p);
					if(team.getName().equalsIgnoreCase("Oranges")){
						if(e.getBlock().getData() == (byte) 10 || e.getBlock().getData() ==(byte) 13){
							if(alter.getType() == Material.EMERALD_BLOCK && placed.getData() == (byte) 13){
								team.addScore(1);
								Arena.getArena(p).broadcastMessage(Prefix.INFO(), team.getColor()+p.getName() + "§4 placed the " + getWool(e.getBlock().getData())+"!");
								WoolChaser.checkWin(Arena.getArena(p));
							}else if(alter.getType() == Material.DIAMOND_BLOCK && placed.getData() == (byte) 10){
								team.addScore(1);
								Arena.getArena(p).broadcastMessage(Prefix.INFO(), team.getColor()+p.getName() + "§4 placed the " + getWool(e.getBlock().getData())+"!");
								WoolChaser.checkWin(Arena.getArena(p));
							}else
								e.setCancelled(true);
						}else if(e.getBlock().getData() == (byte) 11 || e.getBlock().getData() ==(byte) 14){
							e.setCancelled(true);
						}
					}else if(team.getName().equalsIgnoreCase("Apples")){
						if(e.getBlock().getData() == (byte) 10 || e.getBlock().getData() ==(byte) 13){
							e.setCancelled(true);
						}else if(e.getBlock().getData() == (byte) 11 || e.getBlock().getData() ==(byte) 14){
							if(alter.getType() == Material.IRON_BLOCK && placed.getData() == (byte) 11){
								team.addScore(1);
								Arena.getArena(p).broadcastMessage(Prefix.INFO(), team.getColor()+p.getName() + "§4 placed the" + getWool(e.getBlock().getData())+"!");
								WoolChaser.checkWin(Arena.getArena(p));
							}else if(alter.getType() == Material.GOLD_BLOCK && placed.getData() == (byte) 14){
								team.addScore(1);
								Arena.getArena(p).broadcastMessage(Prefix.INFO(), team.getColor()+p.getName() + "§4 placed the" + getWool(e.getBlock().getData())+"!");
								WoolChaser.checkWin(Arena.getArena(p));
							}else
								e.setCancelled(true);
						}
					}else{
						e.setCancelled(true);
					}
				}
	}

}
