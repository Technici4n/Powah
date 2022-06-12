package owmii.powah.config.v2;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import owmii.powah.config.v2.types.CableConfig;
import owmii.powah.config.v2.types.EnderConfig;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.config.v2.types.ChargingConfig;
import owmii.powah.config.v2.values.TieredChannelValues;
import owmii.powah.config.v2.values.TieredEnergyValues;
import owmii.powah.lib.logistics.energy.Energy;

// TODO: reduce value duplication
// TODO: magmator fluids, heat blocks, coolant fluids
@Config(name = "powah")
public class PowahConfig implements ConfigData {
	public final General general = new General();
	public final WorldGen worldgen = new WorldGen();
	public final Generators generators = new Generators();
	public final EnergyDevices devices = new EnergyDevices();

	public static class General {
		public boolean player_aerial_pearl = true;
		public boolean dimensional_binding_card = true;
		public boolean lens_of_ender = true;
	}
	
	public static class WorldGen {
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
}
