package zeroneye.powah.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import zeroneye.powah.item.IItems;

public class ChargedSnowballEntity extends ProjectileItemEntity {
    public ChargedSnowballEntity(World p_i50159_2_) {
        super(IEntities.CHARGED_SNOWBALL, p_i50159_2_);
    }

    public ChargedSnowballEntity(World worldIn, LivingEntity livingEntityIn) {
        super(IEntities.CHARGED_SNOWBALL, livingEntityIn, worldIn);
    }

    public ChargedSnowballEntity(EntityType<ChargedSnowballEntity> snowballEntityEntityType, World world) {
        super(snowballEntityEntityType, world);
    }


    @Override
    protected Item getDefaultItem() {
        return IItems.CHARGED_SNOWBALL;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) result).getEntity();
            int i = entity instanceof BlazeEntity ? 3 : 0;
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), (float) i);
        }

        if (this.world instanceof ServerWorld) {
            LightningBoltEntity entity = new LightningBoltEntity(this.world, this.posX, this.posY, this.posZ, false);
            ((ServerWorld) this.world).addLightningBolt(entity);
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
