package owmii.lib.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import owmii.lib.compat.botania.BotaniaCompat;

public class Magnet {
    public static final String PREVENT_REMOTE_MOVEMENT = "PreventRemoteMovement";
    public static final String ALLOW_MACHINE_REMOTE_MOVEMENT = "AllowMachineRemoteMovement";

    /**
     * Checks if the entity can be collected by a magnet, vacuum or similar.
     *
     * @param entity    The Entity in question
     * @param automated true if the magnet does not require a player to operate
     * @see <a href="https://github.com/comp500/Demagnetize#for-mod-developers">Demagnetize: For mod developers</a>
     */
    @SuppressWarnings("RedundantIfStatement")
    public static boolean canCollect(Entity entity, boolean automated) {
        if (!entity.isAlive()) {
            return false;
        }

        // Demagnetize standard
        CompoundNBT persistentData = entity.getPersistentData();
        if (persistentData.contains(PREVENT_REMOTE_MOVEMENT)) {
            if (!(automated && persistentData.contains(ALLOW_MACHINE_REMOTE_MOVEMENT))) {
                return false;
            }
        }

        // Mod compatibility
        if (BotaniaCompat.preventCollect(entity)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the entity should be collected manually.
     * e.g. Player using a magnet that collects items and experience.
     */
    public static boolean canCollectManual(Entity entity) {
        return canCollect(entity, false);
    }

    /**
     * Checks if the entity should be collected with automation.
     * e.g. An block that collect items or experience.
     */
    public static boolean canCollectAutomated(Entity entity) {
        return canCollect(entity, true);
    }
}
