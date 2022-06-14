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
import owmii.powah.config.v2.annotations.DoubleRange;
import owmii.powah.config.v2.annotations.LongRange;
import owmii.powah.config.v2.types.CableConfig;
import owmii.powah.config.v2.types.EnderConfig;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.config.v2.types.ChargingConfig;
import owmii.powah.config.v2.values.TieredChannelValues;
import owmii.powah.lib.logistics.energy.Energy;

import java.lang.reflect.Field;
import java.util.Map;

import static owmii.powah.config.v2.DefaultEnergies.*;

@Config(name = "powah")
public class PowahConfig implements ConfigData {
	@Comment("World generation config options.")
	public final WorldGen worldgen = new WorldGen();
	@Comment("Other general config options.")
	public final General general = new General();
	@Comment("Configuration of energy values for generators.")
	public final Generators generators = new Generators();
	@Comment("Configuration of energy values for other devices.")
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
		@Comment("Energy produced per fuel tick in the Furnator.")
		@LongRange(min = 1, max = Integer.MAX_VALUE)
		public long energy_per_fuel_tick = energyPerFuelTick();
		@LongRange(min = 1, max = 32)
		public int energizing_range = 4;
		@Comment("Multiplier to the required energy applied after an energizing recipe is read.\nUse this to adjust the cost of ALL energizing recipes.")
		@DoubleRange(min = 0.001, max = 1000)
		public double energizing_energy_ratio = energizingRatio();
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
				generatorCapacity(),
				generatorTransfer(),
				activeProduction()
		);
		public final GeneratorConfig magmators = new GeneratorConfig(
				generatorCapacity(),
				generatorTransfer(),
				activeProduction()
		);
		public final GeneratorConfig reactors = new GeneratorConfig(
				reactorCapacity(),
				reactorTransfer(),
				reactorProduction()
		);
		public final GeneratorConfig solar_panels = new GeneratorConfig(
				generatorCapacity(),
				generatorTransfer(),
				passiveProduction()
		);
		public final GeneratorConfig thermo_generators = new GeneratorConfig(
				generatorCapacity(),
				generatorTransfer(),
				passiveProduction()
		);
	}

	public static class EnergyDevices {
		public final EnergyConfig batteries = new EnergyConfig(
				batteryCapacity(),
				batteryTransfer()
		);
		public final CableConfig cables = new CableConfig(
				cableTransfer()
		);
		public final EnergyConfig dischargers = new EnergyConfig(
				batteryCapacity(),
				batteryTransfer()
		);
		public final EnderConfig ender_cells = new EnderConfig(
				batteryTransfer(),
				TieredChannelValues.getDefault()
		);
		public final EnderConfig ender_gates = new EnderConfig(
				cableTransfer(),
				TieredChannelValues.getDefault()
		);
		public final EnergyConfig energy_cells = new EnergyConfig(
				batteryCapacity(),
				batteryTransfer()
		);
		public final EnergyConfig energizing_rods = new EnergyConfig(
				energizingCapacity(),
				energizingTransfer()
		);
		public final ChargingConfig hoppers = new ChargingConfig(
				batteryCapacity(),
				batteryTransfer(),
				chargingTransfer()
		);
		public final ChargingConfig player_transmitters = new ChargingConfig(
				batteryCapacity(),
				batteryTransfer(),
				chargingTransfer()
		);
	}

	@Override
	public void validatePostLoad() throws ValidationException {
		try {
			validateObject(this);
		} catch (ReflectiveOperationException roe) {
			throw new ValidationException("Failed to validate Powah config", roe);
		}
		// TODO: proper validation here one day?

		PowahAPI.MAGMATIC_FLUIDS.clear();
		PowahAPI.MAGMATIC_FLUIDS.putAll(general.magmatic_fluids);

		PowahAPI.COOLANT_FLUIDS.clear();
		PowahAPI.COOLANT_FLUIDS.putAll(general.coolant_fluids);

		PowahAPI.HEAT_SOURCES.clear();
		PowahAPI.HEAT_SOURCES.putAll(general.heat_blocks);
	}

	private static void validateObject(Object object) throws ValidationException, ReflectiveOperationException {
		for (var field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);

			var value = field.get(object);

			if (value instanceof Number number) {
				var doubleRange = field.getAnnotation(DoubleRange.class);
				if (doubleRange != null) {
					if (number.doubleValue() > doubleRange.max() || number.doubleValue() < doubleRange.min()) {
						throw new ValidationException("Expected double entry %s = %f to be in range [%f, %f]".formatted(
								field.getName(),
								number.doubleValue(),
								doubleRange.min(),
								doubleRange.max()
						));
					}
				}
				var longRange = field.getAnnotation(LongRange.class);
				if (longRange != null) {
					if (number.longValue() > longRange.max() || number.longValue() < longRange.min()) {
						throw new ValidationException("Expected long entry %s = %d to be in range [%d, %d]".formatted(
								field.getName(),
								number.longValue(),
								longRange.min(),
								longRange.max()
						));
					}
				}
			} else if (value != null && value.getClass().getPackageName().startsWith("owmii.powah.config")) {
				validateObject(value);
			}
		}
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
