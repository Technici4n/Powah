package owmii.powah.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraftforge.network.NetworkHooks;
import owmii.powah.item.Itms;

public class ChargedSnowballEntity extends ThrowableItemProjectile {
    public ChargedSnowballEntity(Level p_i50159_2_) {
        super(Entities.CHARGED_SNOWBALL, p_i50159_2_);
    }

    public ChargedSnowballEntity(Level worldIn, LivingEntity livingEntityIn) {
        super(Entities.CHARGED_SNOWBALL, livingEntityIn, worldIn);
    }

    public ChargedSnowballEntity(EntityType<ChargedSnowballEntity> snowballEntityEntityType, Level world) {
        super(snowballEntityEntityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Itms.CHARGED_SNOWBALL;
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) result).getEntity();
            int i = entity instanceof Blaze ? 3 : 0;
            entity.hurt(DamageSource.thrown(this, func_234616_v_()), (float) i);

        }

        if (this.level instanceof ServerLevel) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level);
            if (lightningboltentity != null) {
                lightningboltentity.moveTo(Vec3.atBottomCenterOf(blockPosition()));
                lightningboltentity.setCause(func_234616_v_() instanceof ServerPlayer ? (ServerPlayer) func_234616_v_() : null);
                this.level.addFreshEntity(lightningboltentity);
            }
        }

        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.remove();
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
