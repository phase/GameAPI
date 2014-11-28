GameAPI
=======

Pretty amazing GameAPI that combines the OCTC &amp; Mineplex game system.

Uses the Spigot API for some 1.8 stuff, like Titles and other Packets.

Uses an Arena Object system like other Minigames, but has a Game Object system for each game. 

To join a game, you do /game <MobType> <GameName> , and this will spawn a mob with the name of the game, and when you right-click that mob, it opens up an inventory full of wool, each piece representing an Arena. The color of the wool depends on how many players on in that arena. Click the wool to join the game! (The Arenas are hard-coded into the you make, so no editing in the config. You could write a way to do that, but that would take a while. Wait, that's a pretty good idea. I should do that.)


Structure
---------
The structure of a Game isn't that hard to comprehend.
```
Game
- Arenas
    - Maps
    - Teams
- Listeners
```

But the code implementation can be quite confusing.
```java
ArrayList<String> pre = new ArrayList<String>();
ArrayList<Arena> as = new ArrayList<Arena>();
ArrayList<Listener> lis = new ArrayList<Listener>();
ArrayList<Team> ts = new ArrayList<Team>();
ArrayList<Map> maps = new ArrayList<Map>();

Team p = Team.PLAYERS(-1);
lis.add(new PaintListener());

HashMap<Team, Location> spawns = new HashMap<Team, Location>();
spawns.put(p, new Location(Bukkit.getWorld("testmap"), 0, 64, 0));

maps.add(new Map("TestMap", "testmap", "Mojang", spawns));

as.add(new Arena(1, "PaintBall", "Shoot your paintballs at everyone!", pre, ts, maps, new GameRunnable(){
	public void start(){}public void stop(){}public void win(Team team){}}));

Game game = new Game("PaintBall", "Shoot your paintballs at everyone!", pre, as, lis);

```
Yes, that may seem confusing, but.... yeah, it's pretty weird... I'll find some way to make it better. Right now it just uses a bunch of ArrayLists.
