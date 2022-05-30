package owmii.lib.block;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public interface IOwnable {
    @Nullable
    GameProfile getOwner();

    default void setOwner(PlayerEntity owner) {
        setOwner(owner.getGameProfile());
    }

    void setOwner(@Nullable GameProfile owner);
}
