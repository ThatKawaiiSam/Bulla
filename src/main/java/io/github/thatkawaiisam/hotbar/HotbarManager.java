package io.github.thatkawaiisam.hotbar;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class HotbarManager {

    private Set<Hotbar> hotbars = ConcurrentHashMap.newKeySet();

    private JavaPlugin plugin;
    private ItemListeners listeners;

    public HotbarManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    public void setup() {
        listeners = new ItemListeners(this);
        plugin.getServer().getPluginManager().registerEvents(listeners, plugin);
    }

    public void cleanup() {
        HandlerList.unregisterAll(listeners);
        listeners = null;
    }

    public void addHotbar(Hotbar hotbar) {
        hotbars.add(hotbar);
    }

    public void removeHotbar(Hotbar hotbar) {
        hotbars.remove(hotbar);
    }

}
