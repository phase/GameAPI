package five.game.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
/**
 * 
 * Freezes the Mobs in place
 * @author Phase
 *
 */
public class FreezeTask implements Runnable {

	private static Map<Entity, Location> frozenMobs = new HashMap<Entity, Location>();

	@Override
	public void run() {
		for(Entry<Entity, Location> current : frozenMobs.entrySet()) {
			if(!current.getKey().isDead())
				current.getKey().teleport(current.getValue());
		}
	}

	public static void addMob(Entity mob, Location loc) {
		frozenMobs.put(mob, loc);
	}

	public static void removeMob(Entity mob) {
		if(frozenMobs.containsKey(mob)) {
			frozenMobs.remove(mob);
		}
	}

	public static Set<Entity> getMobs() {
		return frozenMobs.keySet();
	}

}
