package io.github.thatkawaiisam.bulla;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data @AllArgsConstructor
public class ClickableItem {

    private ClickHandler clickHandler;
    private ItemStack itemStack;
    private boolean droppable, moveable, placeable;

}
