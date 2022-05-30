package owmii.lib.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.Function;

public class Config {
    public static final Marker MARKER = new MarkerManager.Log4jMarker("Config");

    public static String createConfigDir(String path) {
        try {
            Path configDir = Paths.get(FMLPaths.CONFIGDIR.get()
                    .toAbsolutePath().toString(), path);
            Files.createDirectories(configDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static void registerCommon(ForgeConfigSpec spec) {
        register(ModConfig.Type.COMMON, spec);
    }

    public static void registerCommon(ForgeConfigSpec spec, String path) {
        register(ModConfig.Type.COMMON, spec, path);
    }

    public static void registerServer(ForgeConfigSpec spec) {
        register(ModConfig.Type.SERVER, spec);
    }

    public static void registerServer(ForgeConfigSpec spec, String path) {
        register(ModConfig.Type.SERVER, spec, path);
    }

    public static void registerClient(ForgeConfigSpec spec) {
        register(ModConfig.Type.CLIENT, spec);
    }

    public static void registerClient(ForgeConfigSpec spec, String path) {
        register(ModConfig.Type.CLIENT, spec, path);
    }

    public static void register(ModConfig.Type type, ForgeConfigSpec spec) {
        ModLoadingContext.get().registerConfig(type, spec);
    }

    public static void register(ModConfig.Type type, ForgeConfigSpec spec, String path) {
        ModLoadingContext.get().registerConfig(type, spec, path);
    }

    public static <T> Pair<T, ForgeConfigSpec> get(Function<ForgeConfigSpec.Builder, T> consumer) {
        return new ForgeConfigSpec.Builder().configure(consumer);
    }

    public static <T> Pair<T, ForgeConfigSpec> get(Function<ForgeConfigSpec.Builder, T> consumer, Collection<T> collection) {
        final Pair<T, ForgeConfigSpec> left = new ForgeConfigSpec.Builder().configure(consumer);
        collection.add(left.getLeft());
        return left;
    }
}
