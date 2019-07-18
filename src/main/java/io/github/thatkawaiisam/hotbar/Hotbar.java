package io.github.thatkawaiisam.hotbar;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public abstract class Hotbar {

    private String id;
    private ConcurrentHashMap<String, ClickableItem> cachedItems = new ConcurrentHashMap<>();
    private JavaPlugin javaPlugin;

    public Hotbar(JavaPlugin javaPlugin, String id) {
        this.javaPlugin = javaPlugin;
        this.id = id;
    }

    public void addCachedItem(String id, ClickableItem item) {
        cachedItems.put(id, item);
    }

    public void removeCachedItem(String id) {
        cachedItems.remove(id);
    }

    public ClickableItem getCachedItem(String id) {
        return cachedItems.get(id);
    }

    public void applyToPlayer(Player player, ClickableItem item, int slot) {
        player.getInventory().setItem(slot, null);
        player.getInventory().setItem(slot, item.getItemStack());
        new BukkitRunnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(javaPlugin, 2);
    }

    public void applyToPlayer(Player player, boolean clearHotbar) {
        if (clearHotbar) {
            for (int i = 0; i < 8; i++) {
                player.getInventory().setItem(i, null);
            }
        }
        HashMap<Integer, ClickableItem> itemsToApply = itemsToApply(player);
        for (Integer slot : itemsToApply.keySet()) {
            player.getInventory().setItem(slot, itemsToApply.get(slot).getItemStack());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(javaPlugin, 2);
    }

    public abstract HashMap<Integer, ClickableItem> itemsToApply(Player player);

}
