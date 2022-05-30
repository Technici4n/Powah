package owmii.lib.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class Server {
    public static long ticks;

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ticks++;
        }
    }

    public static <T extends WorldSavedData> T getData(Supplier<T> supplier) {
        return getData(supplier, World.OVERWORLD);
    }

//    public static <T extends WorldSavedData> T getData(Supplier<T> supplier, Dimension dim) {
//        return getData(supplier, dim.func_236063_b_());
//    }

//    public static <T extends WorldSavedData> T getData(Supplier<T> supplier, DimensionType dim) {
//        return getData(supplier, dim.getId());
//    }

    @SuppressWarnings("unchecked")
    public static <T extends WorldSavedData> T getData(Supplier<T> supplier, @Nullable RegistryKey<World> dim) {
        Optional<ServerWorld> world = getWorld(dim);
        final T[] data = (T[]) new WorldSavedData[]{supplier.get()};
        world.ifPresent(serverWorld -> {
            DimensionSavedDataManager dataManager = serverWorld.getSavedData();
            data[0] = dataManager.get(supplier, supplier.get().getName());
            if (data[0] == null) {
                data[0] = supplier.get();
                data[0].markDirty();
                dataManager.set(data[0]);
            }
        });
        return data[0];
    }

//    public static Optional<ServerWorld> getWorld(int dimId) {
//        return getWorld(DimensionType.getById(dimId));
//    }

    public static Optional<ServerWorld> getWorld(ResourceLocation dimName) {
        return getWorld(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, dimName));
    }

    public static Optional<ServerWorld> getWorld(@Nullable RegistryKey<World> dim) {
        return dim == null ? Optional.empty() : Optional.ofNullable(get().getWorld(dim));
    }

    public static boolean hasPlayers() {
        return !get().getPlayerList().getPlayers().isEmpty();
    }

    public static boolean isSinglePlayer() {
        return !isMultiPlayer();
    }

    public static boolean isMultiPlayer() {
        return get().isDedicatedServer();
    }

    public static MinecraftServer get() {
        return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
    }
}