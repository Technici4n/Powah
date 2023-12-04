package owmii.powah.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import owmii.powah.item.Itms;

public class ChargedSnowballEntity extends ThrowableItemProjectile {

    public ChargedSnowballEntity(Level worldIn, LivingEntity livingEntityIn) {
        super(Entities.CHARGED_SNOWBALL.get(), livingEntityIn, worldIn);
    }

    public ChargedSnowballEntity(EntityType<ChargedSnowballEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Itms.CHARGED_SNOWBALL.get();
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) result).getEntity();
            int i = entity instanceof Blaze ? 3 : 0;
            entity.hurt(level().damageSources().thrown(this, getOwner()), (float) i);

        }

        if (level() instanceof ServerLevel) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(level());
            if (lightningboltentity != null) {
                lightningboltentity.moveTo(Vec3.atBottomCenterOf(blockPosition()));
                lightningboltentity.setCause(getOwner() instanceof ServerPlayer sp ? sp : null);
                level().addFreshEntity(lightningboltentity);
            }
        }

        if (!level().isClientSide) {
            level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}
