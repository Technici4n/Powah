package owmii.powah.block.cable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CableNet {
    // Level -> ChunkPos -> BlockPos -> Tile
    private static final Map<Level, Long2ObjectMap<Long2ObjectMap<CableTile>>> loadedCables = new WeakHashMap<>();

    static void addCable(CableTile cable) {
        var chunkPos = ChunkPos.asLong(cable.getBlockPos());
        var previousCable = loadedCables.computeIfAbsent(cable.getLevel(), l -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(chunkPos, l -> new Long2ObjectOpenHashMap<>())
                .put(cable.getBlockPos().asLong(), cable);

        if (previousCable != null) {
            throw new RuntimeException("Cable added to position %s, but there was already one there?".formatted(cable.getBlockPos()));
        }

        updateAdjacentCables(cable);
    }

    static void removeCable(CableTile cable) {
        // No need to repeat this if the cable isn't part of a net anyway
        if (cable.net == null) {
            return;
        }

        var levelMap = loadedCables.get(cable.getLevel());
        if (levelMap == null) {
            return; // The full-chunk cleanup already took care of the entire level
        }
        var chunkPos = ChunkPos.asLong(cable.getBlockPos());
        var chunkMap = levelMap.get(chunkPos);
        if (chunkMap == null) {
            return; // The full-chunk cleanup already took care of the entire chunk
        }
        if (chunkMap.remove(cable.getBlockPos().asLong()) != cable) {
            throw new RuntimeException("Removed wrong cable from position %s".formatted(cable.getBlockPos()));
        }
        if (chunkMap.isEmpty()) {
            levelMap.remove(chunkPos);
        }
        if (levelMap.isEmpty()) {
            loadedCables.remove(cable.getLevel());
        }

        updateAdjacentCables(cable);
    }

    @Nullable
    private static CableTile getCableAt(Long2ObjectMap<Long2ObjectMap<CableTile>> levelMap, BlockPos pos) {
        var chunkMap = levelMap.get(ChunkPos.asLong(pos));
        if (chunkMap == null) {
            return null;
        }
        return chunkMap.get(pos.asLong());
    }

    static void updateAdjacentCables(CableTile cable) {
        var levelMap = loadedCables.get(cable.getLevel());
        if (levelMap == null) {
            return;
        }

        for (var direction : Direction.values()) {
            var adjPos = cable.getBlockPos().relative(direction);
            var adjCable = getCableAt(levelMap, adjPos);

            if (adjCable != null && cable.canConnectTo(adjCable) && adjCable.net != null) {
                adjCable.net.cableList.forEach(c -> c.net = null);
            }
        }
    }

    static void calculateNetwork(CableTile cable) {
        var levelMap = Objects.requireNonNull(loadedCables.get(cable.getLevel()), "No level map?");

        // Here we go again...
        var cables = new LinkedHashSet<CableTile>();
        var queue = new ArrayDeque<CableTile>();
        cables.add(cable);
        queue.add(cable);

        while (!queue.isEmpty()) {
            var cur = queue.pop();

            for (var direction : Direction.values()) {
                var adjPos = cur.getBlockPos().relative(direction);
                var adjCable = getCableAt(levelMap, adjPos);

                if (adjCable != null && cable.canConnectTo(adjCable) && cables.add(adjCable)) {
                    queue.add(adjCable);
                }
            }
        }

        var insertionGuard = new MutableBoolean();
        var net = new CableNet(new ArrayList<>(cables));
        for (var tile : net.cableList) {
            tile.net = net;
            tile.netInsertionGuard = insertionGuard;
        }
    }

    List<CableTile> cableList;

    CableNet(List<CableTile> cableList) {
        this.cableList = cableList;
    }

    public static void removeChunk(Level level, ChunkAccess chunk) {
        var levelMap = loadedCables.get(level);
        if (levelMap == null) {
            return; // No cables in this level
        }
        var chunkMap = levelMap.remove(chunk.getPos().toLong());
        if (chunkMap == null) {
            return; // No cables in this chunk anyway
        }
        if (levelMap.isEmpty()) {
            loadedCables.remove(level); // No more cables in the level, so might as well remove it
        }
        for (var cable : chunkMap.values()) {
            cable.net = null;
        }
        for (var cable : chunkMap.values()) {
            updateAdjacentCables(cable);
        }
    }
}
