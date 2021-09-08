package io.github.thatkawaiisam.bulla;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class Bulla {

    private JavaPlugin plugin;
    private ItemListeners listeners;

    private Set<Hotbar> hotbars = ConcurrentHashMap.newKeySet();

    /**
     * Bulla.
     *
     * @param plugin instance.
     */
    public Bulla(JavaPlugin plugin) {
        this.plugin = plugin;
        this.setup();
    }

    /**
     * Setup.
     */
    public void setup() {
        listeners = new ItemListeners(this);
        plugin.getServer().getPluginManager().registerEvents(listeners, plugin);
    }

    /**
     * Cleanup.
     */
    public void cleanup() {
        HandlerList.unregisterAll(listeners);
        listeners = null;
    }

    /**
     * Add hotbar to Bulla.
     *
     * @param hotbar to add.
     */
    public void addHotbar(Hotbar hotbar) {
        hotbars.add(hotbar);
    }

    /**
     * Remove hotbar from Bulla.
     *
     * @param hotbar to remove.
     */
    public void removeHotbar(Hotbar hotbar) {
        hotbars.remove(hotbar);
    }

    /**
     * Delayed Inventory Update.
     *
     * @param player to update inventory of.
     */
    public void delayedUpdateInventory(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(this.plugin, 2);
    }

}
