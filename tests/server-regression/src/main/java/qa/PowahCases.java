package qa;

import it.unimi.dsi.fastutil.longs.*;
import java.lang.reflect.*;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import owmii.powah.block.cable.*;

public final class PowahCases {
  static Map map;
  static Method add, remove;
  static ServerLevel level;
  static BlockPos pos = new BlockPos(0, 100, 0);

  static CableBlockEntity cable() {
    var c =
        new CableBlockEntity(
            pos,
            owmii.powah.block.Blcks.ENERGY_CABLE
                .get(owmii.powah.block.Tier.STARTER)
                .defaultBlockState());
    c.setLevel(level);
    return c;
  }

  static Long2ObjectMap<CableBlockEntity> chunk() {
    return (Long2ObjectMap<CableBlockEntity>)
        ((Long2ObjectMap) map.get(level)).get(net.minecraft.world.level.ChunkPos.pack(pos));
  }

  static void call(Method m, Object o) throws Exception {
    m.invoke(null, o);
  }

  public static void run(ServerLevel l) throws Exception {
    level = l;
    Field f = CableNet.class.getDeclaredField("loadedCables");
    f.setAccessible(true);
    map = (Map) f.get(null);
    add = CableNet.class.getDeclaredMethod("addCable", CableBlockEntity.class);
    remove = CableNet.class.getDeclaredMethod("removeCable", CableBlockEntity.class);
    add.setAccessible(true);
    remove.setAccessible(true);
    Object prior = map.remove(level);
    try {
      Checks.test(
          "powah_normal_add_remove",
          () -> {
            map.remove(level);
            var c = cable();
            call(add, c);
            call(remove, c);
            Checks.require(!map.containsKey(level), "Level map leaked");
          });
      Checks.test(
          "powah_duplicate_registration_idempotent",
          () -> {
            map.remove(level);
            var c = cable();
            call(add, c);
            call(add, c);
            Checks.require(chunk().get(pos.asLong()) == c, "Lost cable");
          });
      Checks.test(
          "powah_missing_unregister_no_throw",
          () -> {
            map.remove(level);
            var c = cable();
            call(add, c);
            var missing =
                new CableBlockEntity(
                    pos.offset(3, 0, 0),
                    owmii.powah.block.Blcks.ENERGY_CABLE
                        .get(owmii.powah.block.Tier.STARTER)
                        .defaultBlockState());
            missing.setLevel(level);
            call(remove, missing);
            Checks.require(chunk().get(pos.asLong()) == c, "Removed unrelated cable");
          });
      Checks.test(
          "powah_stale_callback_keeps_replacement",
          () -> {
            map.remove(level);
            var old = cable();
            call(add, old);
            var newer = cable();
            chunk().put(pos.asLong(), newer);
            call(remove, old);
            Checks.require(chunk().get(pos.asLong()) == newer, "Deleted replacement");
          });
      Checks.test(
          "powah_conflict_does_not_mutate_registry",
          () -> {
            map.remove(level);
            var old = cable();
            call(add, old);
            try {
              call(add, cable());
              throw new AssertionError("Expected live conflict rejection");
            } catch (InvocationTargetException ex) {
              Checks.require(ex.getCause() instanceof RuntimeException, "Wrong failure");
            }
            Checks.require(
                chunk().get(pos.asLong()) == old, "Conflicting add replaced owner before throwing");
          });
    } finally {
      map.remove(level);
      if (prior != null) map.put(level, prior);
    }
  }
}
