package io.github.thatkawaiisam.hotbar;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class Hotbar {

    private String id;
    private Set<ClickableItem> clickableItems = new HashSet<>();
    private HotbarManager manager;

    public Hotbar(HotbarManager manager, String id) {
        this.manager = manager;
        this.id = id;
    }

    public void addItem(ClickableItem item) {
        clickableItems.add(item);
    }

    public void removeItem(ClickableItem item) {
        clickableItems.remove(item);
    }

    public void applyToPlayer(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ClickableItem item : clickableItems) {
                    if (item.getApplySlot() < 0) {
                        continue;
                    }
                    player.getInventory().setItem(item.getApplySlot(), item.getItemStack());
                }
            }
        }.runTaskLater(manager.getPlugin(), 1);
        player.updateInventory();
    }
}
