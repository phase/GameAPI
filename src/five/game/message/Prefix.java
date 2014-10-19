package five.game.message;

import org.bukkit.ChatColor;

public class Prefix {
	
	ChatColor color;
	String prefix;
	
	/**
	 * Main contructor
	 * @param color - color of prefix
	 * @param prefix - actual 'prefix'
	 */
	public Prefix(ChatColor color, String prefix){
		this.color = color;
		this.prefix = prefix;
	}
	
	/**
	 * Gets color of prefix
	 * @return color
	 */
	public ChatColor getColor(){
		return color;
	}
	
	/**
	 * Gets string
	 * @return string
	 */
	public String getString(){
		return prefix;
	}
	
	/**
	 * Static generic "INFO" prefix.
	 * @return Prefix "Info"
	 */
	public static Prefix INFO(){
		return new Prefix(ChatColor.GREEN, "info");
	}
	
	/**
	 * Generic Time prefix used for countdowns
	 */
	public static Prefix TIME = new Prefix(ChatColor.DARK_PURPLE, "Time");
	
	
}
