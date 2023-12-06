package owmii.powah;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

/**
 * Fired when gathering items to charge in a player.
 * You can add stacks to the list to make them chargeable by Powah.
 */
public class ChargeableItemsEvent extends Event {
    private final Player player;
    private final List<ItemStack> items = new ArrayList<>();

    public ChargeableItemsEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public List<ItemStack> getItems() {
        return items;
    }
}
