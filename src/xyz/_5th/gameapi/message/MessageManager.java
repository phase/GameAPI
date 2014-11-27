package xyz._5th.gameapi.message;

import org.bukkit.entity.Player;

public class MessageManager {
	
	/**
	 * Sends message to Player
	 * @param p - Player
	 * @param prefix - Prefix before message
	 * @param m - message
	 */
	public static void sendMessage(Player p, Prefix prefix, String m){
		String message = prefix.getColor() + "§l" + prefix.getString().toUpperCase() + " §9》§7 " + m;
		p.sendMessage(message);
	}
	
}
