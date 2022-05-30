package owmii.lib.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import owmii.lib.item.Stacks;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class Player {
    public static boolean isFake(PlayerEntity player) {
        return player instanceof FakePlayer;
    }

    public static Optional<ServerPlayerEntity> get(UUID uuid) {
        return Optional.ofNullable(Server.get().getPlayerList().getPlayerByUUID(uuid));
    }

    public static Optional<ServerPlayerEntity> get(String name) {
        return Optional.ofNullable(Server.get().getPlayerList().getPlayerByUsername(name));
    }

    public static boolean hasItem(PlayerEntity player, Item item) {
        return !getItem(player, item).isEmpty();
    }

    public static boolean hasItem(PlayerEntity player, ItemStack stack) {
        return !getItem(player, stack).isEmpty();
    }

    public static ItemStack getItem(PlayerEntity player, Item item) {
        return getItem(player, new ItemStack(item));
    }

    public static ItemStack getItem(PlayerEntity player, ItemStack stack) {
        for (ItemStack stack1 : invStacks(player)) {
            if (stack1.isItemEqual(stack)) {
                return stack1;
            }
        }
        return ItemStack.EMPTY;
    }

    public static Stacks invStacks(PlayerEntity player) {
        Stacks stacks = Stacks.create();
        PlayerInventory inventory = player.inventory;
        Stream.of(inventory.mainInventory, inventory.armorInventory, inventory.offHandInventory).forEach(stacks::addAll);
        return stacks;
    }
}
