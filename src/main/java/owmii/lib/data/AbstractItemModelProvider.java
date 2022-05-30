package owmii.lib.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

public abstract class AbstractItemModelProvider extends ItemModelProvider {
    public AbstractItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    protected void add(Block block) {
        String name = name(block);
        withExistingParent(name, modLoc("block/" + name));
    }

    protected void add(Item item, ModelFile parent) {
        String name = name(item);
        add(item, parent, "item/" + name);
    }

    protected void add(Item item, ModelFile parent, String texture) {
        String name = name(item);
        getBuilder(name).parent(parent).texture("layer0", modLoc(texture));
    }

    protected String name(IItemProvider provider) {
        return Objects.requireNonNull(provider.asItem().getRegistryName()).getPath();
    }
}
