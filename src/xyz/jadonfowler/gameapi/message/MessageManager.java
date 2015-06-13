package xyz.jadonfowler.gameapi.message;

import org.bukkit.entity.Player;

public class MessageManager {

    /**
     * Sends message to Player
     * 
     * @param p
     *            - Player
     * @param prefix
     *            - Prefix before message
     * @param m
     *            - message
     */
    public static void sendMessage(Player p, Prefix prefix, String m) {
        String message = prefix.getColor() + "§l" + prefix.getString().toUpperCase() + " §9》§7 " + m;
        p.sendMessage(message);
    }

    /**
     * Sends a title to a player
     * 
     * @param player
     * @param fadeIn
     * @param stay
     * @param fadeOut
     * @param title
     * @param subtitle
     */
    public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        Title t = new Title(title, subtitle, fadeIn, stay, fadeOut);
        t.setTimingsToSeconds();
        t.send(player);
        t = null;
    }
}
