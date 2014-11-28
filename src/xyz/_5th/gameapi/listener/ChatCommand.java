package xyz._5th.gameapi.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz._5th.gameapi.main.GameAPI;
import xyz._5th.gameapi.message.MessageManager;
import xyz._5th.gameapi.message.Prefix;

public class ChatCommand implements Listener{
	
	/**
	 * No idea why this is in this class, but it is. Will be moved later.
	 */
	@EventHandler
	public void chatCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(!p.isOp()) return;
		String c = e.getMessage().split(" ")[0];
		String[] args = e.getMessage().split(" ");
		if(c.startsWith("/game")){
			e.setCancelled(true);
			if(args.length != 3){
				m(p, "/game <type> <name>");
				return;
			}
			
			String mobName = args[1].replace(' ', '_').toUpperCase();
			EntityType type = null;
			try{type = EntityType.valueOf(mobName);
			}catch(IllegalArgumentException exp){m(p, "That is not a mob.");}
			if(type != null){
				Entity n = p.getWorld().spawnEntity(p.getLocation(), type);
				if(n instanceof LivingEntity){
					LivingEntity en = (LivingEntity) n;
					en.setCustomName(GameAPI.getRandomColor()+"§l"+args[2]);
					en.setCustomNameVisible(true);
					en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 100000));
					FreezeTask.addMob(en, p.getLocation());
				}
			}
		}
		
	}
	
	public void m(Player p, String s){
		MessageManager.sendMessage(p, new Prefix(ChatColor.GREEN, "Game"), s);
	}
	
}
