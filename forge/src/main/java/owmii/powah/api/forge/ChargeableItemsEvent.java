package owmii.powah.api.forge;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.List;

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
