package owmii.lib.registry;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import owmii.lib.block.IBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class Registry<T extends IForgeRegistryEntry<T>> {
    private final LinkedHashMap<ResourceLocation, List<T>> objects;
    private final Class<T> clazz;
    private final String id;
    private boolean frozen;

    public Registry(Class<T> clazz, String id) {
        this(clazz, new LinkedHashMap<>(), id);
    }

    public Registry(Class<T> clazz, Registry<T> registry, String id) {
        this(clazz, registry.objects, id);
    }

    public Registry(Class<T> clazz, LinkedHashMap<ResourceLocation, List<T>> objects, String id) {
        this.clazz = clazz;
        this.id = id;
        this.objects = new LinkedHashMap<>(objects);
    }

    public <E extends Entity> EntityType<E> register(String name, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, int updateInterval, int range, boolean sendVelocity) {
        EntityType<E> entityType = EntityType.Builder.of(factory, classification).sized(width, height).setUpdateInterval(updateInterval).setTrackingRange(range).setShouldReceiveVelocityUpdates(sendVelocity).build(name);
        register(name, (T) entityType);
        return entityType;
    }

    public <E extends BlockEntity> BlockEntityType<E> registerTile(String name, BlockEntityType.BlockEntitySupplier<E> factory, Block... blocks) {
        BlockEntityType<E> type = BlockEntityType.Builder.of(factory, blocks).build(null);
        register(name, (T) type);
        return type;
    }

    public <O extends T, V extends Enum<V> & IVariant<V>> O register(String name, O o, V v) {
        final O t = register(name + "_" + v.getName(), o, false);
        ResourceLocation key = new ResourceLocation(this.id, name);
        List<T> sibling = getSiblings(key);
        sibling.add(t);
        this.objects.put(key, sibling);
        return t;
    }

    public <O extends T> O register(String name, O o) {
        return register(name, o, true);
    }

    private <O extends T> O register(String name, O o, boolean flag) {
        o.setRegistryName(new ResourceLocation(this.id, name));
        if (o instanceof IRegistryObject) {
            ((IRegistryObject) o).setRegistry(this);
        }
        if (flag) {
            this.objects.put(o.getRegistryName(), Lists.newArrayList(o));
        }
        return o;
    }

    public List<T> getSiblings(String name) {
        return getSiblings(new ResourceLocation(this.id, name));
    }

    public List<T> getSiblings(ResourceLocation key) {
        return this.objects.getOrDefault(key, new ArrayList<>());
    }

    public void init() {
        if (this.frozen) return;
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(this.clazz, this::registerAll);
        this.frozen = true;
    }

    public void registerAll(RegistryEvent.Register<T> event) {
        forEach(t -> event.getRegistry().register(t));
    }

    public Registry<Item> getBlockItems(@Nullable CreativeModeTab group) {
        Registry<Item> reg = new Registry<>(Item.class, this.id);
        forEach(object -> {
            if (object instanceof Block) {
                Block block = (Block) object;
                ResourceLocation rl = block.getRegistryName();
                if (block instanceof IBlock) {
                    Item im = ((IBlock) object).getBlockItem(new Item.Properties(), group);
                    boolean flag = true;
                    if (block instanceof IVariantEntry) {
                        IVariantEntry v = (IVariantEntry) block;
                        if (!(v.getVariant() instanceof IVariant.Single)) { // TODO: optimise
                            ResourceLocation key = v.getSiblingsKey(v);
                            final Item t = reg.register(key.getPath() + "_" + v.getVariant().getName(), im, false);
                            List<Item> sibling = reg.getSiblings(key);
                            sibling.add(t);
                            reg.objects.put(key, sibling);
                            flag = false;
                        }
                    }
                    if (flag)
                        reg.register(rl.getPath(), im, true);
                } else {
                    Item.Properties properties = new Item.Properties();
                    if (group != null) properties.tab(group);
                    reg.register(rl.getPath(), new BlockItem((Block) object, properties));
                }
            }
        });
        return reg;
    }

    public void forEach(Consumer<T> action) {
        this.objects.forEach((rl, ts) -> ts.forEach(action));
    }

    public <V extends Enum<V> & IVariant<V>> VarReg<V, T> getVar(String name, VarReg.VarObject<V, T> varObject, V[] variants) {
        return new VarReg<>(name, varObject, variants, this);
    }

    public LinkedHashMap<ResourceLocation, List<T>> getObjects() {
        return this.objects;
    }

    public String getId() {
        return this.id;
    }

}
