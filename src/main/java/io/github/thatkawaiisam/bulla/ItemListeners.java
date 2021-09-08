package io.github.thatkawaiisam.bulla;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemListeners implements Listener {

    private final Bulla bulla;

    /**
     * Item Listeners.
     *
     * @param bulla instance.
     */
    public ItemListeners(Bulla bulla) {
        this.bulla = bulla;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        for (Hotbar hotbar : bulla.getHotbars()) {
            for (ClickableItem items : hotbar.getCachedItems().values()) {
                // Check that the ItemStack is similar.
                if (items.getItemStack().isSimilar(event.getItemDrop().getItemStack())) {
                    if (items.isDroppable()) {
                        continue;
                    }
                    event.setCancelled(true);
                    bulla.delayedUpdateInventory(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        boolean update = false;

        for (Hotbar hotbar : bulla.getHotbars()) {
            for (ClickableItem items : hotbar.getCachedItems().values()) {
                // Current Item.
                if (items.getItemStack().isSimilar(event.getCurrentItem())) {
                    if (!items.isMoveable()) {
                        event.setResult(Event.Result.DENY);
                        event.setCancelled(true);
                        update = true;
                    }
                }
                // Cursor.
                if (items.getItemStack().isSimilar(event.getCursor())) {
                    if (!items.isMoveable()) {
                        event.setResult(Event.Result.DENY);
                        event.setCancelled(true);
                        update = true;
                    }
                }
                // Number.
                if (event.getHotbarButton() != -1
                        && items.getItemStack().isSimilar(player.getInventory().getItem(event.getHotbarButton()))) {
                    if (!items.isMoveable()) {
                        event.setResult(Event.Result.DENY);
                        event.setCancelled(true);
                        update = true;
                    }
                }
            }
        }

        if (update) {
            bulla.delayedUpdateInventory(player);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (player.getItemInHand() == null
                || player.getItemInHand().getType() == Material.AIR) {
            return;
        }

        for (Hotbar hotbar : bulla.getHotbars()) {
            for (ClickableItem items : hotbar.getCachedItems().values()) {
                if (items.getItemStack().isSimilar(player.getItemInHand())) {
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (player.getItemInHand() == null
                || player.getItemInHand().getType() == Material.AIR
                || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        for (Hotbar hotbar : bulla.getHotbars()) {
            for (ClickableItem items : hotbar.getCachedItems().values()) {
                if (items.getItemStack().isSimilar(player.getItemInHand())) {
                    event.setCancelled(true);
                    event.setUseItemInHand(Event.Result.DENY);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    player.updateInventory();
                    if (items.getClickHandler() == null) {
                        continue;
                    }
                    items.getClickHandler().click(player);
                    return;
                }
            }
        }
    }

}
