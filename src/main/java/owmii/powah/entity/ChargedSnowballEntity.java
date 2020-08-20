package owmii.powah.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import owmii.powah.item.Itms;

public class ChargedSnowballEntity extends ProjectileItemEntity {
    public ChargedSnowballEntity(World p_i50159_2_) {
        super(Entities.CHARGED_SNOWBALL, p_i50159_2_);
    }

    public ChargedSnowballEntity(World worldIn, LivingEntity livingEntityIn) {
        super(Entities.CHARGED_SNOWBALL, livingEntityIn, worldIn);
    }

    public ChargedSnowballEntity(EntityType<ChargedSnowballEntity> snowballEntityEntityType, World world) {
        super(snowballEntityEntityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Itms.CHARGED_SNOWBALL;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) result).getEntity();
            int i = entity instanceof BlazeEntity ? 3 : 0;
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, func_234616_v_()), (float) i);

        }

        if (this.world instanceof ServerWorld) {
            LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.world);
            if (lightningboltentity != null) {
                lightningboltentity.moveForced(Vector3d.copyCenteredHorizontally(getPosition()));
                lightningboltentity.setCaster(func_234616_v_() instanceof ServerPlayerEntity ? (ServerPlayerEntity) func_234616_v_() : null);
                this.world.addEntity(lightningboltentity);
            }
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte) 3);
            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
