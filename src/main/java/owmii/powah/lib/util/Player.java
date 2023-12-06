package owmii.powah.lib.util;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.FakePlayer;
import owmii.powah.lib.item.Stacks;

public class Player {
    public static boolean isFake(net.minecraft.world.entity.player.Player player) {
        return player instanceof FakePlayer;
    }

    public static Optional<ServerPlayer> get(ServerLevel level, UUID uuid) {
        return Optional.ofNullable(level.getServer().getPlayerList().getPlayer(uuid));
    }

    public static Optional<ServerPlayer> get(ServerLevel level, String name) {
        return Optional.ofNullable(level.getServer().getPlayerList().getPlayerByName(name));
    }

    public static boolean hasItem(net.minecraft.world.entity.player.Player player, Item item) {
        return !getItem(player, item).isEmpty();
    }

    public static boolean hasItem(net.minecraft.world.entity.player.Player player, ItemStack stack) {
        return !getItem(player, stack).isEmpty();
    }

    public static ItemStack getItem(net.minecraft.world.entity.player.Player player, Item item) {
        return getItem(player, new ItemStack(item));
    }

    public static ItemStack getItem(net.minecraft.world.entity.player.Player player, ItemStack stack) {
        for (ItemStack stack1 : invStacks(player)) {
            if (ItemStack.isSameItem(stack1, stack)) {
                return stack1;
            }
        }
        return ItemStack.EMPTY;
    }

    public static Stacks invStacks(net.minecraft.world.entity.player.Player player) {
        Stacks stacks = Stacks.create();
        Inventory inventory = player.getInventory();
        Stream.of(inventory.items, inventory.armor, inventory.offhand).forEach(stacks::addAll);
        return stacks;
    }
}
