package xyz._5th.gameapi.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz._5th.gameapi.main.GameAPI;

public class LobbyManager implements Listener {
	
	private static Map<UUID, Integer> LobbyList = new HashMap<UUID, Integer>();
	private static int MaxLobbyNumber = 54;
	public static final String InventoryName = "§5§l§nLobby Switcher";
	
	@SuppressWarnings("deprecation")
	public static void changeLobby(Player p, int i, boolean teleport){
		LobbyList.put(p.getUniqueId(), i);
		for(Player o : Bukkit.getOnlinePlayers()){
			p.showPlayer(o);
			if(LobbyList.containsKey(o.getUniqueId())){
				if(LobbyList.get(o.getUniqueId()) != i){
					p.hidePlayer(o);
					o.hidePlayer(p);
				}
				else{
					o.showPlayer(p);
				}
			}else{
				p.hidePlayer(o);
				o.hidePlayer(p);
			}
		}
		p.setExp(0);
		p.setLevel(getLobby(p));
		if(teleport){
			p.teleport(GameAPI.getSpawnLocation());
			p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 7);
		}
	}
	
	public static void giveRandomLobby(Player p){
		Random r = new Random();
		int i = r.nextInt(MaxLobbyNumber)+1;
		changeLobby(p, i, false);
	}
	
	public static int getPlayersInLobby(int i){
		List<UUID> us = new ArrayList<UUID>();
		for(UUID u : LobbyList.keySet())
			if(LobbyList.get(u) == i)
				us.add(u);
		return us.size();
	}
	
	public static int getLobby(Player p){
		return LobbyList.get(p.getUniqueId());
	}
	
	@SuppressWarnings("deprecation")
	public static void openInventory(Player p){
		Inventory i = Bukkit.createInventory(null, MaxLobbyNumber, InventoryName);
		for(int n = 1; n <= MaxLobbyNumber; n++){
			ItemStack lby = new ItemStack(Material.WOOL, n, (short) 0, getData(getPlayersInLobby(n)));
			if(n == getLobby(p))
				lby.setType(Material.GOLD_BLOCK);
			ItemMeta lbym = lby.getItemMeta();
			lbym.setDisplayName("§d§lLobby: §6§l"+n);
			List<String> s = new ArrayList<String>();
			s.add("§8Players: §7" + getPlayersInLobby(n));
			lbym.setLore(s);
			lby.setItemMeta(lbym);
			i.setItem(n-1, lby);
		}
		p.openInventory(i);
	}
	
	/**
	 * Used for Wool color determined by how many players are in Lobby
	 */
	private static byte getData(int p){
		if(p == 0)
			return 10;
		else if(p<11)
			return 6;
		else if(p<21)
			return 4;
		else if(p<31)
			return 1;
		else
			return 14;
	}
	
	@EventHandler
	public void watchClick(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(p.getItemInHand().getType().equals(Material.WATCH) && p.getLocation().getWorld().getName().equalsIgnoreCase("world"))
			openInventory(p);
	}
	
	
}
