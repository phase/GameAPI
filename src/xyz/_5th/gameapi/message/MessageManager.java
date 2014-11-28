package xyz._5th.gameapi.message;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PlayerConnection;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

import xyz._5th.gameapi.main.GameAPI;

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
		CraftPlayer craftPlayer = (CraftPlayer) player;

		if (!GameAPI.is1_8(player))
			return;

		IChatBaseComponent serializedTitle = ChatSerializer.a(TextConverter.convert(title));
		IChatBaseComponent serializedSubTitle = ChatSerializer.a(TextConverter.convert(subtitle));

		craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(
				ProtocolInjector.PacketTitle.Action.TIMES, fadeIn, stay, fadeOut));
		craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(
				ProtocolInjector.PacketTitle.Action.TITLE, serializedTitle));
		craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(
				ProtocolInjector.PacketTitle.Action.SUBTITLE, serializedSubTitle));
	}

	/**
	 * Sends title on player tablist
	 * 
	 * @param player
	 * @param header
	 * @param footer
	 */
	public static void sendTabTitle(Player player, String header, String footer) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		if(!GameAPI.is1_8(player)) return;
		PlayerConnection connection = craftPlayer.getHandle().playerConnection;

		header = header.replaceAll("%player%", player.getDisplayName());
		footer = footer.replaceAll("%player%", player.getDisplayName());

		IChatBaseComponent header2 = ChatSerializer.a(new StringBuilder().append("{'text': '")
				.append(header).append("'}").toString());
		IChatBaseComponent footer2 = ChatSerializer.a(new StringBuilder().append("{'text': '")
				.append(footer).append("'}").toString());
		connection.sendPacket(new ProtocolInjector.PacketTabHeader(header2, footer2));
	}

}
