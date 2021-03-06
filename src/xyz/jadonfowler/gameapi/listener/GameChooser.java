package xyz.jadonfowler.gameapi.listener;

import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.jadonfowler.gameapi.GameAPI;
import xyz.jadonfowler.gameapi.game.*;
import xyz.jadonfowler.gameapi.message.*;

/**
 * Handles Game choosing in the Lobby/Hub.
 * 
 * @author Phase
 *
 */
public class GameChooser implements Listener {

    @EventHandler public void EntityInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        Entity n = e.getRightClicked();
        if (!Game.isInGame(p)) {
            if (n.getWorld().getName().equalsIgnoreCase("world")) {
                if (n instanceof LivingEntity) {
                    LivingEntity en = (LivingEntity) n;
                    String game = en.getCustomName();
                    if (Game.gameExist(ChatColor.stripColor(game))) {
                        openInventory(p, Game.getGame(ChatColor.stripColor(game)));
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler public void damageGame(EntityDamageEvent e) {
        if (FreezeTask.getMobs().contains(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler public void ChooseGame(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (Game.isInGame(p)) return;
            if (Game.gameExist(ChatColor.stripColor(e.getInventory().getName()))) {
                if (p.getWorld().getName().equalsIgnoreCase("world")) {
                    e.setCancelled(true);
                    int s = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()
                            .split(" ")[1]));
                    Arena a = Arena.getArena(s);
                    if (a.getState() == ArenaState.POST_GAME) {
                        MessageManager.sendMessage(p, Prefix.INFO(), "That game is restarting!");
                    }
                    else a.addPlayer(p);
                }
            }
        }
    }

    @SuppressWarnings("deprecation") public static void openInventory(Player p, Game g) {
        Inventory i = Bukkit.createInventory(null, getSize(g.getArenas().size()),
                GameAPI.getRandomColor() + "§l" + g.getName());
        int c = 0;
        for (Arena a : g.getArenas()) {
            byte color = getColor(a.getState());
            ItemStack wool = new ItemStack(Material.WOOL, 1, (short) 0, color);
            ItemMeta woolMeta = wool.getItemMeta();
            woolMeta.setDisplayName(GameAPI.getRandomColor() + g.getName() + " " + a.getId());
            List<String> s = new ArrayList<String>();
            s.add(a.getState().getString());
            s.add("§7Players: §d" + a.getPlayers().size());
            if (a.getState() == ArenaState.IN_GAME)
                s.add("§dMap: §e" + a.getCurrentMap().getName() + "§d by: §e" + a.getCurrentMap().getCreator());
            woolMeta.setLore(s);
            wool.setItemMeta(woolMeta);
            i.setItem(c, wool);
            c++;// :3
        }
        p.openInventory(i);
    }

    private static byte getColor(ArenaState state) {
        if (state == ArenaState.PRE_GAME) return 5;
        return 14;
    }

    private static int getSize(int size) {
        if (size < 10) return 9;
        else if (size < 19) return 18;
        else if (size < 28) return 27;
        else if (size < 37) return 36;
        else if (size < 46) return 45;
        else return 54;
    }
}
