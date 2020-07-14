package owmii.powah.block.magmator;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import owmii.lib.block.AbstractGeneratorBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.generator.MagmatorConfig;
import owmii.powah.inventory.MagmatorContainer;

import javax.annotation.Nullable;

public class MagmatorBlock extends AbstractGeneratorBlock<Tier, MagmatorConfig> {
    public MagmatorBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setDefaultState();
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    @Override
    public MagmatorConfig getConfig() {
        return Configs.MAGMATOR;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MagmatorTile(this.variant);
    }

    @Nullable
    @Override
    public <T extends AbstractTileEntity> AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof MagmatorTile) {
            return new MagmatorContainer(id, inventory, (MagmatorTile) te);
        }
        return null;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof MagmatorTile) {
            MagmatorTile magmator = (MagmatorTile) tile;
            if (FluidUtil.interactWithFluidHandler(player, hand, magmator.getTank())) {
                magmator.sync();
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }

    @Override
    protected boolean isPlacerFacing() {
        return true;
    }

    @Override
    protected boolean hasLitProp() {
        return true;
    }
}
