package owmii.powah.compat.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

public record SolidCoolant(ResourceLocation id, Item item, int amount, int coldness) {
    public static Collection<SolidCoolant> getAll() {
        List<SolidCoolant> result = new ArrayList<>();

        for (var entry : PowahAPI.SOLID_COOLANTS.entrySet()) {
            var id = entry.getKey();
            var pair = entry.getValue();
            int amount = pair.getLeft();
            int coldness = pair.getRight();

            var item = BuiltInRegistries.ITEM.get(id);
            if (item != Items.AIR) {
                var recipeId = Powah.id("coolants/solid/" + id.getNamespace() + "/" + id.getPath());
                result.add(new SolidCoolant(recipeId, item, amount, coldness));
            }
        }

        return result;
    }

}
