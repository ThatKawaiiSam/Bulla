package io.github.thatkawaiisam.hotbar;

import com.google.common.collect.ImmutableSet;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class ItemListeners implements Listener {

    private static Set<Action> actions = ImmutableSet.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

    private HotbarManager hotbarManager;

    public ItemListeners(HotbarManager hotbarManager) {
        this.hotbarManager = hotbarManager;
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onItemDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        for (Hotbar hotbar : hotbarManager.getHotbars()) {
            for (ClickableItem items : hotbar.getClickableItems()) {
                /* Check that the itemstack is similar */
                if (items.getItemStack().isSimilar(event.getItemDrop().getItemStack())) {
                    if (items.isDroppable()) {
                        continue;
                    }
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (player.getItemInHand() == null
                || player.getItemInHand().getType() == Material.AIR
                || !actions.contains(event.getAction())) {
            return;
        }

        for (Hotbar hotbar : hotbarManager.getHotbars()) {
            for (ClickableItem items : hotbar.getClickableItems()) {
                if (items.getItemStack().isSimilar(player.getItemInHand())) {
                    event.setCancelled(true);
                    event.setUseItemInHand(Event.Result.DENY);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    player.updateInventory();
                    if (items.getClickHandler() == null) {
                        continue;
                    }
                    items.getClickHandler().click(player);
                }
            }
        }
    }

}
