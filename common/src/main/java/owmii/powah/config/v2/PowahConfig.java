package owmii.powah.config.v2;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonPrimitive;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.DeserializationException;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import owmii.powah.api.PowahAPI;
import owmii.powah.config.v2.types.CableConfig;
import owmii.powah.config.v2.types.EnderConfig;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.config.v2.types.ChargingConfig;
import owmii.powah.config.v2.values.TieredChannelValues;
import owmii.powah.config.v2.values.TieredEnergyValues;
import owmii.powah.lib.logistics.energy.Energy;

import java.util.Map;

// TODO: reduce value duplication
// TODO: magmator fluids, heat blocks, coolant fluids
@Config(name = "powah")
public class PowahConfig implements ConfigData {
	@Comment("World generation config options.")
	public final WorldGen worldgen = new WorldGen();
	@Comment("Other general config options.")
	public final General general = new General();
	@Comment("Configuration of generators.")
	public final Generators generators = new Generators();
	@Comment("Configuration of other energy devices.")
	public final EnergyDevices devices = new EnergyDevices();

	public static class General {
		@Comment("Enable this to get Player Aerial Pearl by right clicking a Zombie or Husk with a Aerial Pearl.")
		public boolean player_aerial_pearl = true;
		@Comment("Enable this to get Dimensional Binding card by right clicking an Enderman or Endermite with a Binding card.")
		public boolean dimensional_binding_card = true;
		@Comment("Enable this to get Lens Of Ender by right clicking an Enderman or Endermite with a Photoelectric Pane.")
		public boolean lens_of_ender = true;

		@Comment("List of fluids used in the Magmator.")
		public final Map<ResourceLocation, Integer> magmatic_fluids = Map.of(
				new ResourceLocation("minecraft:lava"), 10000
		);
		@Comment("List of coolant fluids used in the Reactor and the Thermo Generator.")
		public final Map<ResourceLocation, Integer> coolant_fluids = Map.of(
				new ResourceLocation("minecraft:water"), 1
		);
		@Comment("List of heat source blocks used under Thermo Generator.")
		public final Map<ResourceLocation, Integer> heat_blocks = Map.of(
				new ResourceLocation("minecraft:lava"), 1000,
				new ResourceLocation("minecraft:magma_block"), 800,
				new ResourceLocation("powah:blazing_crystal_block"), 2800
		);
	}
	
	public static class WorldGen {
		@Comment("Enable this to disable worldgen entirely. If true, the other options have no effect.")
		public boolean disable_all = false;
		public int poor_uraninite_veins_per_chunk = 8;
		public int uraninite_veins_per_chunk = 6;
		public int dense_uraninite_veins_per_chunk = 3;
		public int dry_ice_veins_per_chunk = 9;
	}

	public static class Generators {
		public final GeneratorConfig furnators = new GeneratorConfig(
				new TieredEnergyValues(10000L, 100000L, 500000L, 900000L, 2000000L, 5000000L, 10000000L),
				new TieredEnergyValues(80L, 200L, 700L, 1200L, 5000L, 7000L, 10000L),
				new TieredEnergyValues(15L, 70L, 180L, 400L, 900L, 2000L, 4800L)
		);
		@ConfigEntry.BoundedDiscrete(max = Energy.MAX)
		public final long furnator_energy_per_fuel_tick = 30;
		public final GeneratorConfig magmators = new GeneratorConfig(
				new TieredEnergyValues(10000L, 100000L, 500000L, 900000L, 2000000L, 5000000L, 10000000L),
				new TieredEnergyValues(80L, 200L, 700L, 1200L, 5000L, 7000L, 10000L),
				new TieredEnergyValues(15L, 70L, 180L, 400L, 900L, 2000L, 4800L)
		);
		public final GeneratorConfig reactors = new GeneratorConfig(
				new TieredEnergyValues(1000000L, 4000000L, 10000000L, 18000000L, 30000000L, 50000000L, 80000000L),
				new TieredEnergyValues(10000L, 50000L, 160000L, 100000L, 140000L, 400000L, 800000L),
				new TieredEnergyValues(100L, 400L, 840L, 1200L, 2700L, 3800L, 5400L)
		);
		public final GeneratorConfig solar_panels = new GeneratorConfig(
				new TieredEnergyValues(5000L, 25000L, 100000L, 500000L, 1000000L, 2500000L, 8000000L),
				new TieredEnergyValues(50L, 150L, 300L, 700L, 1000L, 2000L, 4000L),
				new TieredEnergyValues(5L, 12L, 40L, 90L, 220L, 500L, 1200L)
		);
		public final GeneratorConfig thermo_generators = new GeneratorConfig(
				new TieredEnergyValues(5000L, 25000L, 100000L, 500000L, 1000000L, 2500000L, 8000000L),
				new TieredEnergyValues(50L, 150L, 300L, 700L, 1000L, 2000L, 4000L),
				new TieredEnergyValues(3L, 8L, 18L, 42L, 90L, 200L, 800L)
		);
	}

	public static class EnergyDevices {
		public final EnergyConfig batteries = new EnergyConfig(
				new TieredEnergyValues(100_000L, 1000_000L, 4000_000L, 10_000_000L, 25_000_000L, 60_000_000L, 140_000_000L),
				new TieredEnergyValues(200L, 2500L, 8000L, 20_000L, 50_000L, 100_000L, 400_000L)
		);
		public final CableConfig cables = new CableConfig(
				new TieredEnergyValues(100, 1400, 5000, 18000, 40000, 90000, 200000)
		);
		public final EnergyConfig dischargers = new EnergyConfig(
				new TieredEnergyValues(100_000L, 1000_000L, 4000_000L, 10_000_000L, 25_000_000L, 60_000_000L, 140_000_000L),
				new TieredEnergyValues(200L, 2500L, 8000L, 20_000L, 50_000L, 100_000L, 400_000L)
		);
		public final EnderConfig ender_cells = new EnderConfig(
				new TieredEnergyValues(200L, 2500L, 8000L, 20_000L, 50_000L, 100_000L, 400_000L),
				new TieredChannelValues()
		);
		public final EnderConfig ender_gates = new EnderConfig(
				new TieredEnergyValues(100, 1400, 5000, 18000, 40000, 90000, 200000),
				new TieredChannelValues()
		);
		public final EnergyConfig energy_cells = new EnergyConfig(
				new TieredEnergyValues(100_000L, 1000_000L, 4000_000L, 10_000_000L, 25_000_000L, 60_000_000L, 140_000_000L),
				new TieredEnergyValues(200L, 2500L, 8000L, 20_000L, 50_000L, 100_000L, 400_000L)
		);
		public final EnergyConfig energizing_rods = new EnergyConfig(
				new TieredEnergyValues(10000, 100000, 250000, 800000, 1500000, 4000000, 10000000),
				new TieredEnergyValues(10, 50, 120, 300, 700, 1200, 3000)
		);
		@ConfigEntry.BoundedDiscrete(min = 1, max = 32)
		public final int energizing_range = 4;
		public final ChargingConfig hoppers = new ChargingConfig(
				new TieredEnergyValues(100_000L, 1000_000L, 3000_000L, 5_000_000L, 10_000_000L, 15_000_000L, 40_000_000L),
				new TieredEnergyValues(500L, 2500L, 8000L, 20_000L, 50_000L, 100_000L, 400_000L),
				new TieredEnergyValues(100, 1000, 3000, 8000, 12000, 20000, 50000)
		);
		public final ChargingConfig player_transmitters = new ChargingConfig(
				new TieredEnergyValues(100_000L, 1000_000L, 3000_000L, 5_000_000L, 10_000_000L, 15_000_000L, 40_000_000L),
				new TieredEnergyValues(500L, 2500L, 8000L, 20_000L, 50_000L, 100_000L, 400_000L),
				new TieredEnergyValues(100, 1000, 3000, 8000, 12000, 20000, 50000)
		);
	}

	@Override
	public void validatePostLoad() throws ValidationException {
		// TODO: proper validation here one day?

		PowahAPI.MAGMATIC_FLUIDS.clear();
		PowahAPI.MAGMATIC_FLUIDS.putAll(general.magmatic_fluids);

		PowahAPI.COOLANT_FLUIDS.clear();
		PowahAPI.COOLANT_FLUIDS.putAll(general.coolant_fluids);

		PowahAPI.HEAT_SOURCES.clear();
		PowahAPI.HEAT_SOURCES.putAll(general.heat_blocks);
	}

	public static ConfigHolder<PowahConfig> register() {
		return AutoConfig.register(PowahConfig.class, (cfg, cfgClass) -> {
			var janksonBuilder = Jankson.builder();
			// Resource Location
			janksonBuilder.registerDeserializer(String.class, ResourceLocation.class, (string, marshaller) -> {
				try {
					return new ResourceLocation(string);
				} catch (ResourceLocationException exception) {
					throw new DeserializationException("Not a valid resource location: " + string, exception);
				}
			});
			janksonBuilder.registerSerializer(ResourceLocation.class, (resLoc, marshaller) -> {
				return new JsonPrimitive(resLoc.toString());
			});

			return new JanksonConfigSerializer<>(cfg, cfgClass, janksonBuilder.build());
		});
	}
}
