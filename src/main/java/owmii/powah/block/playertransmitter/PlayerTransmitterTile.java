package owmii.powah.block.playertransmitter;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import owmii.lib.block.AbstractBlock;
import owmii.lib.block.TileBase;
import owmii.lib.compat.curios.CuriosCompat;
import owmii.lib.energy.Energy;
import owmii.lib.util.Player;
import owmii.lib.util.Stack;
import owmii.lib.util.Text;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;
import owmii.powah.item.BindingCardItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class PlayerTransmitterTile extends TileBase.EnergyStorage<Tier, PlayerTransmitterBlock> {
    public PlayerTransmitterTile(Tier variant) {
        super(ITiles.PLAYER_TRANSMITTER, variant);
        this.inv.add(1);
    }

    public PlayerTransmitterTile() {
        this(Tier.STARTER);
    }

    @Override
    protected boolean postTicks(World world) {
        if (!this.energy.hasEnergy()) return false;
        long extracted = 0;
        for (ItemStack stack : this.inv.getStacks()) {
            if (stack.getItem() instanceof BindingCardItem) {
                BindingCardItem bindingCard = (BindingCardItem) stack.getItem();
                Optional<ServerPlayerEntity> playerEntity = bindingCard.getPlayer(stack);
                if (playerEntity.isPresent()) {
                    ServerPlayerEntity player = playerEntity.get();
                    if (bindingCard.isMultiDim(stack) || player.dimension.equals(world.dimension.getType())) {
                        long charging = getBlock().getChargingSpeed();
                        for (ItemStack stack1 : Player.invStacks(player)) {
                            long amount = Math.min(charging, getEnergyStored());
                            int received = Energy.receive(stack1, amount, false);
                            extracted += extractEnergy(received, false, null);
                        }
                        for (ItemStack stack1 : CuriosCompat.getAllStacks(player)) {
                            long amount = Math.min(charging, getEnergyStored());
                            int received = Energy.receive(stack1, amount, false);
                            extracted += extractEnergy(received, false, null);
                        }
                    }
                }
            }
        }
        return extracted > 0;
    }

    @Override
    public void getListedEnergyInfo(List<String> list) {
        super.getListedEnergyInfo(list);
        list.add(TextFormatting.GRAY + I18n.format("info.powah.charging.speed", TextFormatting.DARK_GRAY + Text.numFormat(getBlock().getChargingSpeed())));
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        return index == 0 && Stack.getTagOrEmpty(stack).hasUniqueId("BoundPlayerId");
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && side.equals(getBlockState().get(AbstractBlock.FACING));
    }
}
