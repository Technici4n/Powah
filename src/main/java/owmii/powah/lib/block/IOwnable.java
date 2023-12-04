package owmii.powah.lib.block;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface IOwnable {
    @Nullable
    GameProfile getOwner();

    default void setOwner(Player owner) {
        setOwner(owner.getGameProfile());
    }

    void setOwner(@Nullable GameProfile owner);
}
