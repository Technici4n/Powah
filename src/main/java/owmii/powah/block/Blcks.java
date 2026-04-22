package owmii.powah.block;

import static owmii.powah.lib.block.Properties.deepslate;
import static owmii.powah.lib.block.Properties.metal;
import static owmii.powah.lib.block.Properties.metalNoSolid;
import static owmii.powah.lib.block.Properties.rock;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import owmii.powah.Powah;
import owmii.powah.block.cable.CableBlock;
import owmii.powah.block.discharger.EnergyDischargerBlock;
import owmii.powah.block.ender.EnderCellBlock;
import owmii.powah.block.ender.EnderGateBlock;
import owmii.powah.block.energizing.EnergizingOrbBlock;
import owmii.powah.block.energizing.EnergizingRodBlock;
import owmii.powah.block.energycell.EnergyCellBlock;
import owmii.powah.block.furnator.FurnatorBlock;
import owmii.powah.block.hopper.EnergyHopperBlock;
import owmii.powah.block.magmator.MagmatorBlock;
import owmii.powah.block.reactor.ReactorBlock;
import owmii.powah.block.solar.SolarBlock;
import owmii.powah.block.thermo.ThermoBlock;
import owmii.powah.block.transmitter.PlayerTransmitterBlock;
import owmii.powah.item.CreativeTabs;
import owmii.powah.item.Itms;
import owmii.powah.lib.block.PowahBaseBlock;
import owmii.powah.lib.item.PowahBlockItem;
import owmii.powah.lib.registry.TieredBlockReg;

public class Blcks {

    public static final DeferredRegister.Blocks DR = DeferredRegister.createBlocks(Powah.MOD_ID);

    public static final TieredBlockReg ENERGY_CELL = new TieredBlockReg(DR, "energy_cell",
            (variant, props) -> new EnergyCellBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.values());
    public static final TieredBlockReg ENDER_CELL = new TieredBlockReg(DR, "ender_cell",
            (variant, props) -> new EnderCellBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.getNormalVariants());
    public static final TieredBlockReg ENERGY_CABLE = new TieredBlockReg(DR, "energy_cable",
            (variant, props) -> new CableBlock(metalNoSolid(props, 2.0f, 20.0f).noCollision(), variant), Tier.getNormalVariants());
    public static final TieredBlockReg ENDER_GATE = new TieredBlockReg(DR, "ender_gate",
            (variant, props) -> new EnderGateBlock(metalNoSolid(props, 2.0f, 20.0f).noCollision().noLootTable(), variant), Tier.getNormalVariants());
    public static final DeferredBlock<Block> ENERGIZING_ORB = DR.registerBlock("energizing_orb",
            props -> new EnergizingOrbBlock(metalNoSolid(props, 2.0f, 20.0f)));
    public static final TieredBlockReg ENERGIZING_ROD = new TieredBlockReg(DR, "energizing_rod",
            (variant, props) -> new EnergizingRodBlock(metalNoSolid(props, 2.0f, 20.0f).noCollision().noLootTable(), variant),
            Tier.getNormalVariants());
    public static final TieredBlockReg FURNATOR = new TieredBlockReg(DR, "furnator",
            (variant, props) -> new FurnatorBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.getNormalVariants());
    public static final TieredBlockReg MAGMATOR = new TieredBlockReg(DR, "magmator",
            (variant, props) -> new MagmatorBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.getNormalVariants());
    public static final TieredBlockReg THERMO_GENERATOR = new TieredBlockReg(DR, "thermo_generator",
            (variant, props) -> new ThermoBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.getNormalVariants());
    public static final TieredBlockReg SOLAR_PANEL = new TieredBlockReg(DR, "solar_panel",
            (variant, props) -> new SolarBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.getNormalVariants());
    public static final TieredBlockReg REACTOR = new TieredBlockReg(DR, "reactor",
            (variant, props) -> new ReactorBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.getNormalVariants());
    public static final TieredBlockReg PLAYER_TRANSMITTER = new TieredBlockReg(DR, "player_transmitter",
            (variant, props) -> new PlayerTransmitterBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.getNormalVariants());
    public static final TieredBlockReg ENERGY_HOPPER = new TieredBlockReg(DR, "energy_hopper",
            (variant, props) -> new EnergyHopperBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.getNormalVariants());
    public static final TieredBlockReg ENERGY_DISCHARGER = new TieredBlockReg(DR, "energy_discharger",
            (variant, props) -> new EnergyDischargerBlock(metalNoSolid(props, 2.0f, 20.0f), variant), Tier.getNormalVariants());
    public static final DeferredBlock<Block> ENERGIZED_STEEL = DR.registerBlock("energized_steel_block",
            props -> new Block(metal(props, 2.0f, 20.0f)));
    public static final DeferredBlock<Block> BLAZING_CRYSTAL = DR.registerBlock("blazing_crystal_block",
            props -> new Block(metal(props, 2.0f, 20.0f)));
    public static final DeferredBlock<Block> NIOTIC_CRYSTAL = DR.registerBlock("niotic_crystal_block", props -> new Block(metal(props, 2.0f, 20.0f)));
    public static final DeferredBlock<Block> SPIRITED_CRYSTAL = DR.registerBlock("spirited_crystal_block",
            props -> new Block(metal(props, 2.0f, 20.0f)));
    public static final DeferredBlock<Block> NITRO_CRYSTAL = DR.registerBlock("nitro_crystal_block", props -> new Block(metal(props, 2.0f, 20.0f)));
    public static final DeferredBlock<Block> URANINITE = DR.registerBlock("uraninite_block", props -> new Block(metal(props, 2.0f, 20.0f)));
    public static final DeferredBlock<Block> DEEPSLATE_URANINITE_ORE_POOR = DR.registerBlock("deepslate_uraninite_ore_poor",
            props -> new DropExperienceBlock(ConstantInt.of(0), deepslate(props)));
    public static final DeferredBlock<Block> DEEPSLATE_URANINITE_ORE = DR.registerBlock("deepslate_uraninite_ore",
            props -> new DropExperienceBlock(ConstantInt.of(0), deepslate(props)));
    public static final DeferredBlock<Block> DEEPSLATE_URANINITE_ORE_DENSE = DR.registerBlock("deepslate_uraninite_ore_dense",
            props -> new DropExperienceBlock(ConstantInt.of(0), deepslate(props)));
    public static final DeferredBlock<Block> URANINITE_ORE_POOR = DR.registerBlock("uraninite_ore_poor",
            props -> new DropExperienceBlock(ConstantInt.of(0), rock(props, 3.0f, 8.0f)));
    public static final DeferredBlock<Block> URANINITE_ORE = DR.registerBlock("uraninite_ore",
            props -> new DropExperienceBlock(ConstantInt.of(0), rock(props, 3.2f, 8.0f)));
    public static final DeferredBlock<Block> URANINITE_ORE_DENSE = DR.registerBlock("uraninite_ore_dense",
            props -> new DropExperienceBlock(ConstantInt.of(0), rock(props, 4.0f, 8.0f)));
    public static final DeferredBlock<Block> DRY_ICE = DR.registerBlock("dry_ice", props -> new Block(rock(props, 2.0f, 8.0f)));

    static {
        for (var entry : DR.getEntries()) {
            Itms.DR.registerItem(entry.getId().getPath(), props -> {
                props.useBlockDescriptionPrefix();
                BlockItem blockItem;
                Block block = entry.get();
                if (block instanceof PowahBaseBlock<?> iBlock) {
                    blockItem = iBlock.getBlockItem(props, CreativeTabs.MAIN_KEY);
                } else {
                    blockItem = new PowahBlockItem<>(block, props, CreativeTabs.MAIN_KEY);
                }
                return blockItem;
            });
        }
    }

}
