package owmii.powah.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.lib.block.IBlock;
import owmii.lib.block.OreBlockBase;
import owmii.lib.block.Properties;
import owmii.powah.block.cable.EnergyCableBlock;
import owmii.powah.block.energizing.EnergizingOrbBlock;
import owmii.powah.block.energizing.EnergizingRodBlock;
import owmii.powah.block.energycell.EnergyCellBlock;
import owmii.powah.block.furnator.FurnatorBlock;
import owmii.powah.block.magmator.MagmatorBlock;
import owmii.powah.block.solarpanel.SolarPanelBlock;
import owmii.powah.block.thermo.ThermoGenBlock;
import owmii.powah.item.ItemGroups;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IBlocks {
    public static final List<BlockItem> BLOCK_ITEMS = new ArrayList<>();
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block ENERGY_CELL_STARTER = register("energy_cell_starter", new EnergyCellBlock(Properties.rockNoSolid(1.0f, 8.0f), Tier.STARTER));
    public static final Block ENERGY_CELL_BASIC = register("energy_cell_basic", new EnergyCellBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BASIC));
    public static final Block ENERGY_CELL_HARDENED = register("energy_cell_hardened", new EnergyCellBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.HARDENED));
    public static final Block ENERGY_CELL_BLAZING = register("energy_cell_blazing", new EnergyCellBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BLAZING));
    public static final Block ENERGY_CELL_NIOTIC = register("energy_cell_niotic", new EnergyCellBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NIOTIC));
    public static final Block ENERGY_CELL_SPIRITED = register("energy_cell_spirited", new EnergyCellBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.SPIRITED));
    public static final Block ENERGY_CELL_NITRO = register("energy_cell_nitro", new EnergyCellBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NITRO));
    public static final Block ENERGY_CELL_CREATIVE = register("energy_cell_creative", new EnergyCellBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.CREATIVE));

    public static final Block ENERGY_CABLE_STARTER = register("energy_cable_starter", new EnergyCableBlock(Properties.rockNoSolid(1.0f, 8.0f).doesNotBlockMovement(), Tier.STARTER));
    public static final Block ENERGY_CABLE_BASIC = register("energy_cable_basic", new EnergyCableBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.BASIC));
    public static final Block ENERGY_CABLE_HARDENED = register("energy_cable_hardened", new EnergyCableBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.HARDENED));
    public static final Block ENERGY_CABLE_BLAZING = register("energy_cable_blazing", new EnergyCableBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.BLAZING));
    public static final Block ENERGY_CABLE_NIOTIC = register("energy_cable_niotic", new EnergyCableBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.NIOTIC));
    public static final Block ENERGY_CABLE_SPIRITED = register("energy_cable_spirited", new EnergyCableBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.SPIRITED));
    public static final Block ENERGY_CABLE_NITRO = register("energy_cable_nitro", new EnergyCableBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.NITRO));

    public static final Block ENERGIZING_ORB = register("energizing_orb", new EnergizingOrbBlock(Properties.metalNoSolid(2.0f, 20.0f)));
    public static final Block ENERGIZING_ROD_STARTER = register("energizing_rod_starter", new EnergizingRodBlock(Properties.rockNoSolid(1.0f, 8.0f).doesNotBlockMovement(), Tier.STARTER));
    public static final Block ENERGIZING_ROD_BASIC = register("energizing_rod_basic", new EnergizingRodBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.BASIC));
    public static final Block ENERGIZING_ROD_HARDENED = register("energizing_rod_hardened", new EnergizingRodBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.HARDENED));
    public static final Block ENERGIZING_ROD_BLAZING = register("energizing_rod_blazing", new EnergizingRodBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.BLAZING));
    public static final Block ENERGIZING_ROD_NIOTIC = register("energizing_rod_niotic", new EnergizingRodBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.NIOTIC));
    public static final Block ENERGIZING_ROD_SPIRITED = register("energizing_rod_spirited", new EnergizingRodBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.SPIRITED));
    public static final Block ENERGIZING_ROD_NITRO = register("energizing_rod_nitro", new EnergizingRodBlock(Properties.metalNoSolid(2.0f, 20.0f).doesNotBlockMovement(), Tier.NITRO));

    public static final Block FURNATOR_STARTER = register("furnator_starter", new FurnatorBlock(Properties.rockNoSolid(1.0f, 8.0f), Tier.STARTER));
    public static final Block FURNATOR_BASIC = register("furnator_basic", new FurnatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BASIC));
    public static final Block FURNATOR_HARDENED = register("furnator_hardened", new FurnatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.HARDENED));
    public static final Block FURNATOR_BLAZING = register("furnator_blazing", new FurnatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BLAZING));
    public static final Block FURNATOR_NIOTIC = register("furnator_niotic", new FurnatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NIOTIC));
    public static final Block FURNATOR_SPIRITED = register("furnator_spirited", new FurnatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.SPIRITED));
    public static final Block FURNATOR_NITRO = register("furnator_nitro", new FurnatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NITRO));

    public static final Block MAGMATOR_STARTER = register("magmator_starter", new MagmatorBlock(Properties.rockNoSolid(1.0f, 8.0f), Tier.STARTER));
    public static final Block MAGMATOR_BASIC = register("magmator_basic", new MagmatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BASIC));
    public static final Block MAGMATOR_HARDENED = register("magmator_hardened", new MagmatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.HARDENED));
    public static final Block MAGMATOR_BLAZING = register("magmator_blazing", new MagmatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BLAZING));
    public static final Block MAGMATOR_NIOTIC = register("magmator_niotic", new MagmatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NIOTIC));
    public static final Block MAGMATOR_SPIRITED = register("magmator_spirited", new MagmatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.SPIRITED));
    public static final Block MAGMATOR_NITRO = register("magmator_nitro", new MagmatorBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NITRO));

    public static final Block THERMO_STARTER = register("thermo_generator_starter", new ThermoGenBlock(Properties.rockNoSolid(1.0f, 8.0f), Tier.STARTER));
    public static final Block THERMO_BASIC = register("thermo_generator_basic", new ThermoGenBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BASIC));
    public static final Block THERMO_HARDENED = register("thermo_generator_hardened", new ThermoGenBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.HARDENED));
    public static final Block THERMO_BLAZING = register("thermo_generator_blazing", new ThermoGenBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BLAZING));
    public static final Block THERMO_NIOTIC = register("thermo_generator_niotic", new ThermoGenBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NIOTIC));
    public static final Block THERMO_SPIRITED = register("thermo_generator_spirited", new ThermoGenBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.SPIRITED));
    public static final Block THERMO_NITRO = register("thermo_generator_nitro", new ThermoGenBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NITRO));

    public static final Block SOLAR_PANEL_STARTER = register("solar_panel_starter", new SolarPanelBlock(Properties.rockNoSolid(1.0f, 8.0f), Tier.STARTER));
    public static final Block SOLAR_PANEL_BASIC = register("solar_panel_basic", new SolarPanelBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BASIC));
    public static final Block SOLAR_PANEL_HARDENED = register("solar_panel_hardened", new SolarPanelBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.HARDENED));
    public static final Block SOLAR_PANEL_BLAZING = register("solar_panel_blazing", new SolarPanelBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.BLAZING));
    public static final Block SOLAR_PANEL_NIOTIC = register("solar_panel_niotic", new SolarPanelBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NIOTIC));
    public static final Block SOLAR_PANEL_SPIRITED = register("solar_panel_spirited", new SolarPanelBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.SPIRITED));
    public static final Block SOLAR_PANEL_NITRO = register("solar_panel_nitro", new SolarPanelBlock(Properties.metalNoSolid(2.0f, 20.0f), Tier.NITRO));

    public static final Block ENERGIZED_STEEL = register("energized_steel_block", new OreBlockBase(Properties.metal(2.0f, 20.0f)));
    public static final Block BLAZING_CRYSTAL = register("blazing_crystal_block", new OreBlockBase(Properties.metal(2.0f, 20.0f)));
    public static final Block NIOTIC_CRYSTAL = register("niotic_crystal_block", new OreBlockBase(Properties.metal(2.0f, 20.0f)));
    public static final Block SPIRITED_CRYSTAL = register("spirited_crystal_block", new OreBlockBase(Properties.metal(2.0f, 20.0f)));
    public static final Block NITRO_CRYSTAL = register("nitro_crystal_block", new OreBlockBase(Properties.metal(2.0f, 20.0f)));
    public static final Block URANINITE_ORE_POOR = register("uraninite_ore_poor", new OreBlockBase(Properties.rock(1.0f, 8.0f)));
    public static final Block URANINITE_ORE = register("uraninite_ore", new OreBlockBase(Properties.rock(1.0f, 8.0f)));
    public static final Block URANINITE_ORE_DENSE = register("uraninite_ore_dense", new OreBlockBase(Properties.rock(1.0f, 8.0f)));

    static <T extends net.minecraft.block.Block> T register(final String name, final T block) {
        final BlockItem itemBlock = ((IBlock) block).getBlockItem(new Item.Properties(), ItemGroups.MAIN);
        itemBlock.setRegistryName(name);
        block.setRegistryName(name);
        IBlocks.BLOCK_ITEMS.add(itemBlock);
        IBlocks.BLOCKS.add(block);
        return block;
    }

    @SubscribeEvent
    public static void onRegistry(final RegistryEvent.Register<Block> event) {
        IBlocks.BLOCKS.forEach(block -> event.getRegistry().register(block));
    }
}
