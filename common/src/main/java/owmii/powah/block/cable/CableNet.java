package owmii.powah.block.cable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

class CableNet {
	private static final Map<Level, Map<BlockPos, CableTile>> loadedCables = new WeakHashMap<>();

	static void addCable(CableTile cable) {
		var previousCable = loadedCables.computeIfAbsent(cable.getLevel(), l -> new HashMap<>()).put(cable.getBlockPos().immutable(), cable);

		if (previousCable != null) {
			throw new RuntimeException("Cable added to position %s, but there was already one there?".formatted(cable.getBlockPos()));
		}

		updateAdjacentCables(cable);
	}

	static void removeCable(CableTile cable) {
		var levelMap = loadedCables.get(cable.getLevel());
		if (levelMap.remove(cable.getBlockPos()) != cable) {
			throw new RuntimeException("Removed wrong cable from position %s".formatted(cable.getBlockPos()));
		}
		if (levelMap.size() == 0) {
			loadedCables.remove(cable.getLevel());
		}

		updateAdjacentCables(cable);
	}

	static void updateAdjacentCables(CableTile cable) {
		var levelMap = loadedCables.get(cable.getLevel());
		if (levelMap == null) {
			return;
		}

		for (var direction : Direction.values()) {
			var adjPos = cable.getBlockPos().relative(direction);
			var adjCable = levelMap.get(adjPos);

			if (adjCable != null && adjCable.net != null) {
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
				var adjCable = levelMap.get(adjPos);

				if (adjCable != null && cables.add(adjCable)) {
					queue.add(adjCable);
				}
			}
		}

		var net = new CableNet(new ArrayList<>(cables));
		for (var tile : net.cableList) {
			tile.net = net;
		}
	}

	List<CableTile> cableList;

	CableNet(List<CableTile> cableList) {
		this.cableList = cableList;
	}
}
