package io.github.thatkawaiisam.hotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemListeners implements Listener {

    private HotbarManager hotbarManager;

    public ItemListeners(HotbarManager hotbarManager) {
        this.hotbarManager = hotbarManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        for (Hotbar hotbar : hotbarManager.getHotbars()) {
            for (ClickableItem items : hotbar.getCachedItems().values()) {
                /* Check that the itemstack is similar */
                if (items.getItemStack().isSimilar(event.getItemDrop().getItemStack())) {
                    if (items.isDroppable()) {
                        continue;
                    }
                    event.setCancelled(true);
                    delayedUpdateInventory(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        boolean update = false;

        for (Hotbar hotbar : hotbarManager.getHotbars()) {
            for (ClickableItem items : hotbar.getCachedItems().values()) {
                // Current Item
                if (items.getItemStack().isSimilar(event.getCurrentItem())) {
                    if (!items.isMoveable()) {
                        event.setResult(Event.Result.DENY);
                        event.setCancelled(true);
                        update = true;
                    }
                }
                // Cursor
                if (items.getItemStack().isSimilar(event.getCursor())) {
                    if (!items.isMoveable()) {
                        event.setResult(Event.Result.DENY);
                        event.setCancelled(true);
                        update = true;
                    }
                }
            }
        }

        if (update) {
            delayedUpdateInventory(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (player.getItemInHand() == null
                || player.getItemInHand().getType() == Material.AIR
                || event.getAction() != Action.RIGHT_CLICK_AIR
                || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        for (Hotbar hotbar : hotbarManager.getHotbars()) {
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

    private void delayedUpdateInventory(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(hotbarManager.getPlugin(), 2);
    }

}
