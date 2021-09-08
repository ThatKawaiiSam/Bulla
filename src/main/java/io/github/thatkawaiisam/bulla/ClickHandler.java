package io.github.thatkawaiisam.bulla;

import org.bukkit.entity.Player;

public interface ClickHandler {

    /**
     * On click of item.
     *
     * @param player that executed the click.
     */
    void click(Player player);

}
