package owmii.powah.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ElectricParticle extends SpriteTexturedParticle {
    protected ElectricParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
        this.maxAge = 3;
    }

    @Override
    public void tick() {
        if (this.age++ >= this.maxAge) {
            setExpired();
        }
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite p_i50747_1_) {
            this.spriteSet = p_i50747_1_;
        }

        @Nullable
        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ElectricParticle particle = new ElectricParticle(worldIn, x, y, z);
            particle.selectSpriteRandomly(this.spriteSet);
            return particle;
        }
    }
}
