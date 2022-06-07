package owmii.powah.lib.client.particle;//package owmii.powah.lib.client.particle;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.particle.IParticleRenderType;
//import net.minecraft.client.particle.Particle;
//import net.minecraft.client.renderer.ActiveRenderInfo;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//import owmii.powah.lib.Lollipop;
//import owmii.powah.lib.client.util.ColorUtil;
//import owmii.powah.lib.util.math.V3d;
//
//@OnlyIn(Dist.CLIENT)
//public class Effect extends Particle {
//    protected final Minecraft mc = Minecraft.getInstance();
//
//    protected Texture texture = Effect.GLOW_SMALL;
//    protected int textureID;
//
//    protected V3d origin;
//    protected V3d to;
//    protected boolean blend;
//    protected boolean noDepth;
//    protected boolean bright;
//
//    protected boolean dynamicColor;
//    protected int color0;
//    protected int color1;
//
//    protected double speed;
//    protected int alphaMode;
//    protected int scaleMode;
//    protected float scaleFactor;
//
//    public Effect(Texture texture, World world, V3d origin) {
//        this(world, origin.x, origin.y, origin.z);
//    }
//
//    public Effect(World world, V3d origin) {
//        this(world, origin.x, origin.y, origin.z);
//    }
//
//    public Effect(Texture texture, World world, BlockPos origin) {
//        this(world, origin.getX(), origin.getY(), origin.getZ());
//    }
//
//    public Effect(World world, BlockPos origin) {
//        this(world, origin.getX(), origin.getY(), origin.getZ());
//    }
//
//    public Effect(Texture texture, World world, double x, double y, double z) {
//        this(world, x, y, z);
//    }
//
//    public Effect(World world, double x, double y, double z) {
//        super(world, x, y, z);
//        this.origin = origin();
//        this.to = origin();
//        this.canCollide = false;
//        this.particleAlpha = 0.0F;
//        this.noDepth = true;
//        color(0xffffff);
//    }
//
//    @Override
//    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
//        render(partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
//    }
//
//    public void render(float partialTicks, double rotX, double rotZ, double rotYZ, double rotXY, double rotXZ) {
//        double x = this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX;
//        double y = this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY;
//        double z = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ;
//        if (render(partialTicks, x, y, z)) {
//            render(partialTicks, x, y, z, rotX, rotZ, rotYZ, rotXY, rotXZ);
//        }
//    }
//
//    public void render(float partialTicks, double x, double y, double z, double rotX, double rotZ, double rotYZ, double rotXY, double rotXZ) {
//        double d0 = 0.1F * this.scaleFactor;
//        int i = getBrightnessForRender(partialTicks);
//        int j = i >> 16 & 65535;
//        int k = i & 65535;
//        Vec3d[] posVec = new Vec3d[]{new Vec3d(-rotX * d0 - rotXY * d0, -rotZ * d0, -rotYZ * d0 - rotXZ * d0),
//                new Vec3d(-rotX * d0 + rotXY * d0, rotZ * d0, -rotYZ * d0 + rotXZ * d0),
//                new Vec3d(rotX * d0 + rotXY * d0, rotZ * d0, rotYZ * d0 + rotXZ * d0),
//                new Vec3d(rotX * d0 - rotXY * d0, -rotZ * d0, rotYZ * d0 - rotXZ * d0)};
//        if (this.particleAngle != 0.0F) {
//            ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
//            double d4 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
//            double d5 = MathHelper.cos((float) (d4 * 0.5F));
//            double d6 = MathHelper.sin((float) (d4 * 0.5F)) * renderInfo.getProjectedView().x;
//            double d7 = MathHelper.sin((float) (d4 * 0.5F)) * renderInfo.getProjectedView().y;
//            double d8 = MathHelper.sin((float) (d4 * 0.5F)) * renderInfo.getProjectedView().z;
//            Vec3d vec3d = new Vec3d(d6, d7, d8);
//            for (int l = 0; l < 4; ++l) {
//                posVec[l] = vec3d.scale(2.0D * posVec[l].dotProduct(vec3d))
//                        .add(posVec[l].scale(d5 * d5 - vec3d.dotProduct(vec3d)))
//                        .add(vec3d.crossProduct(posVec[l]).scale(2.0F * d5));
//            }
//        }
//        String textureSuffix = this.texture.frames > 1 ? "" + this.textureID : "";
//        this.mc.getTextureManager().bindTexture(new ResourceLocation(Lollipop.MOD_ID, "textures/particles/" + this.texture.name + textureSuffix + ".png"));
//        if (this.blend) GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferbuilder = tessellator.getBuffer();
//        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
//        bufferbuilder.pos(x + posVec[0].x, y + posVec[0].y, z + posVec[0].z).tex(1.0D, 1.0D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        bufferbuilder.pos(x + posVec[1].x, y + posVec[1].y, z + posVec[1].z).tex(1.0D, 0.0D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        bufferbuilder.pos(x + posVec[2].x, y + posVec[2].y, z + posVec[2].z).tex(0.0D, 0.0D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        bufferbuilder.pos(x + posVec[3].x, y + posVec[3].y, z + posVec[3].z).tex(0.0D, 1.0D).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        tessellator.draw();
//        if (this.blend)
//            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//    }
//
//    public boolean render(float partialTicks, double x, double y, double z) {
//        return true;
//    }
//
//    @Override
//    public void tick() {
//        this.prevPosX = this.posX;
//        this.prevPosY = this.posY;
//        this.prevPosZ = this.posZ;
//
//        if (this.texture.frames > 1 && !this.texture.randomize) {
//            this.textureID = this.age * this.texture.frames / this.maxAge;
//        }
//
//        float f0 = (float) this.age / (float) this.maxAge;
//        if (this.dynamicColor) {
//            color(ColorUtil.blend(this.color0, this.color1, f0));
//        }
//
//        this.particleAlpha = this.alphaMode == 1 ? (1.0F - f0) : this.particleAlpha;
//
//        if (this.scaleMode == 3) {
//            this.scaleFactor = MathHelper.sin((float) (f0 * Math.PI));
//        } else if (this.scaleMode == 2) {
//            this.scaleFactor *= this.scaleFactor;
//        } else if (this.scaleMode == 1) {
//            this.scaleFactor = 1.0F - f0;
//        }
//
//        if (!this.origin.found(this.to)) {
//            this.motionX = (this.to.x - this.posX) * this.speed;
//            this.motionY = (this.to.y - this.posY) * this.speed;
//            this.motionZ = (this.to.z - this.posZ) * this.speed;
//        }
//
//        if (this.particleGravity != 0.0D) {
//            this.motionY = (double) -this.particleGravity;
//        }
//
//        move(this.motionX, this.motionY, this.motionZ);
//        if (this.age++ > this.maxAge) {
//            setExpired();
//        }
//    }
//
//    public V3d origin() {
//        return new V3d(this.posX, this.posY, this.posZ);
//    }
//
//    public Effect color(int color) {
//        this.particleRed = (float) (color >> 16) / 255.0F;
//        this.particleGreen = (float) (color >> 8 & 255) / 255.0F;
//        this.particleBlue = (float) (color & 255) / 255.0F;
//        return this;
//    }
//
//    public Effect color(int from, int to) {
//        this.dynamicColor = true;
//        this.color0 = from;
//        this.color1 = to;
//        return color(from);
//    }
//
//    public Effect maxAge(int maxAge) {
//        this.maxAge = maxAge;
//        return this;
//    }
//
//    public Effect to(V3d to) {
//        this.to = to;
//        return this;
//    }
//
//    public Effect speed(double speed) {
//        this.speed = speed;
//        return this;
//    }
//
//    public Effect alpha(float alpha) {
//        return alpha(alpha, 0);
//    }
//
//    public Effect alpha(float alpha, int mode) {
//        this.particleAlpha = alpha;
//        this.alphaMode = mode;
//        return this;
//    }
//
//    /**
//     * Mod: 0 = normal <br />
//     * Mod: 1 = 1.0F - ageFactor <br />
//     * Mod: 2 = scale * scaleFactor <br />
//     * Mod: 3 = blob <br />
//     */
//
//    public Effect scale(float scale, int mode, float scaleFactor) {
//        this.scaleFactor = mode == 3 ? 0 : scale;
//        this.scaleMode = mode;
//        this.scaleFactor = scaleFactor;
//        return this;
//    }
//
//    public Effect blend() {
//        this.blend = true;
//        return this;
//    }
//
//    public Effect depth() {
//        this.noDepth = false;
//        return this;
//    }
//
//    public Effect collide() {
//        this.canCollide = true;
//        return this;
//    }
//
//    public Effect gravity(float gravity) {
//        this.particleGravity = gravity;
//        return this;
//    }
//
//    public boolean shouldDisableDepth() {
//        return this.noDepth;
//    }
//
//    public void spawn() {
//        spawn(1);
//    }
//
//    public void spawn(int count) {
//        for (int i = 0; i < count; i++) {
//            Effects.INSTANCE.spawn(this);
//        }
//    }
//
//    @Override
//    public int getBrightnessForRender(float partialTick) {
//        return this.bright ? 15728880 : super.getBrightnessForRender(partialTick);
//    }
//
//    @Override
//    public IParticleRenderType getRenderType() {
//        return IParticleRenderType.CUSTOM;
//    }
//
//    public static final Effect.Texture GLOW_SMALL = new Effect.Texture("glow_small");
//    public static final Effect.Texture GLOW_MID = new Effect.Texture("glow_mid");
//    public static final Effect.Texture GLOW_DENS = new Effect.Texture("glow_dens");
//    public static final Effect.Texture SQUARE = new Effect.Texture("square");
//    public static final Effect.Texture STAR = new Effect.Texture("star");
//
//    public static class Texture {
//        public final String name;
//        public final int frames;
//        public final boolean randomize;
//
//        public Texture(String name) {
//            this(name, 0, false);
//        }
//
//        public Texture(String name, int frames) {
//            this(name, frames, false);
//        }
//
//        public Texture(String name, int frames, boolean randomize) {
//            this.name = name;
//            this.frames = frames;
//            this.randomize = randomize;
//        }
//    }
//}
