# Bulla
> Simple hotbar utility.

---

## Code Examples
The below code is taken from a a moderation plugin which utilises the library for the staff items portion.
```java
package io.github.thatkawaiisam.modsuite.modules.staffmode;

import io.github.thatkawaiisam.bulla.Bulla;
import io.github.thatkawaiisam.bulla.ClickableItem;
import io.github.thatkawaiisam.bulla.Hotbar;
import io.github.thatkawaiisam.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class StaffModeHotbar extends Hotbar {

    public StaffModeHotbar(Bulla bulla, String id) {
        super(bulla, id);
        initItems();
    }

    @Override
    public HashMap<Integer, ClickableItem> itemsToApply(Player player) {
        HashMap<Integer, ClickableItem> items = new HashMap<>();
        items.put(0, getCachedItem("compass"));
        items.put(1, getCachedItem("inspect"));
        items.put(2, getCachedItem("carpet"));
        items.put(7, getCachedItem("onlineStaff"));
        items.put(8, getCachedItem("appear"));
        return items;
    }

    /**
     * Add cached items.
     */
    private void initItems() {
        addCachedItem("compass", new ClickableItem(
                player -> {
                    // Empty Click Handler
                },
                new ItemBuilder().material(Material.COMPASS).title("&bTeleport Compass").build(),
                false,
                false
        ));
        addCachedItem("onlineStaff", new ClickableItem(
                player -> {
                    // Open online staff
                },
                new ItemBuilder().material(Material.SKULL_ITEM).title("&bOnline Staff").build(),
                false,
                false
        ));
        addCachedItem("disappear", new ClickableItem(
                player -> {
                    applyToPlayer(player, getCachedItem("appear"), 8);
                },
                new ItemBuilder().material(Material.INK_SACK).title("&bDisappear").durability((short) 10).build(),
                false,
                false
        ));
        addCachedItem("appear", new ClickableItem(
                player -> {
                    applyToPlayer(player, getCachedItem("disappear"), 8);
                },
                new ItemBuilder().material(Material.INK_SACK).title("&bAppear").durability((short) 8).build(),
                false,
                false
        ));
    }
}
```

---

## Contributing
When contributing, please create a pull request with the branch named as follows ``<feature/fix>/<title>``.

To compile, run the maven command: ``mvn clean install``

To run unit tests, run the maven command ``mvn test``

---

## Contact

- Discord: ThatKawaiiSam#2882
- Telegram: ThatKawaiiSam