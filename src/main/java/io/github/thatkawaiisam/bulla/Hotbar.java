package io.github.thatkawaiisam.bulla;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public abstract class Hotbar {

    private final Bulla bulla;

    private String id;
    private Map<String, ClickableItem> cachedItems = new ConcurrentHashMap<>();

    /**
     * Hotbar.
     *
     * @param id of hotbar.
     * @param bulla instance.
     */
    public Hotbar(String id, Bulla bulla) {
        this.bulla = bulla;
        this.id = id;
    }

    /**
     * Add cached item to hotbar.
     *
     * @param id of item.
     * @param item to add.
     */
    public void addCachedItem(String id, ClickableItem item) {
        cachedItems.put(id, item);
    }

    /**
     * Remove cached item from hotbar.
     *
     * @param id of item.
     */
    public void removeCachedItem(String id) {
        cachedItems.remove(id);
    }

    /**
     * Get cached item from hotbar.
     *
     * @param id of item.
     * @return item.
     */
    public ClickableItem getCachedItem(String id) {
        return cachedItems.get(id);
    }

    /**
     * Apply clickable item to player.
     *
     * @param player to apply to.
     * @param item to give.
     * @param slot to place item.
     */
    public void applyToPlayer(Player player, ClickableItem item, int slot) {
        player.getInventory().setItem(slot, null);
        player.getInventory().setItem(slot, item.getItemStack());
        bulla.delayedUpdateInventory(player);
    }

    /**
     * Apply hotbar items to player.
     *
     * @param player to apply hotbar to.
     * @param clear whether to clear.
     */
    public void applyToPlayer(Player player, boolean clear) {
        if (clear) {
            for (int i = 0; i < 8; i++) {
                player.getInventory().setItem(i, null);
            }
        }
        Map<Integer, ClickableItem> itemsToApply = itemsToApply(player);
        for (Integer slot : itemsToApply.keySet()) {
            player.getInventory().setItem(slot, itemsToApply.get(slot).getItemStack());
        }
        bulla.delayedUpdateInventory(player);
    }

    /**
     * Get a mapping of items to apply to a player.
     *
     * @param player to apply to.
     * @return mapping of items with slots.
     */
    public abstract Map<Integer, ClickableItem> itemsToApply(Player player);

}
