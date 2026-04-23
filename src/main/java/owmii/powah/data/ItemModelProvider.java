package owmii.powah.data;

import java.util.Locale;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.ItemLike;
import owmii.powah.Powah;
import owmii.powah.components.PowahComponents;
import owmii.powah.item.Itms;

public class ItemModelProvider extends ModelSubProvider {
    public ItemModelProvider(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        super(blockModels, itemModels);
    }

    @Override
    protected void register() {
        for (var tier : Itms.BATTERY.getTiers()) {
            flatSingleLayer(Itms.BATTERY.get(tier), "item/battery_" + tier.name().toLowerCase(Locale.ROOT));
        }
        flatSingleLayer(Itms.BOOK, "item/book");
        flatSingleLayer(Itms.WRENCH, "item/wrench");
        flatSingleLayer(Itms.CAPACITOR_BASIC_TINY, "item/capacitor_basic_tiny");
        flatSingleLayer(Itms.CAPACITOR_BASIC, "item/capacitor_basic");
        flatSingleLayer(Itms.CAPACITOR_BASIC_LARGE, "item/capacitor_basic_large");
        flatSingleLayer(Itms.CAPACITOR_HARDENED, "item/capacitor_hardened");
        flatSingleLayer(Itms.CAPACITOR_BLAZING, "item/capacitor_blazing");
        flatSingleLayer(Itms.CAPACITOR_NIOTIC, "item/capacitor_niotic");
        flatSingleLayer(Itms.CAPACITOR_SPIRITED, "item/capacitor_spirited");
        flatSingleLayer(Itms.CAPACITOR_NITRO, "item/capacitor_nitro");
        flatSingleLayer(Itms.AERIAL_PEARL, "item/aerial_pearl");
        flatSingleLayer(Itms.PLAYER_AERIAL_PEARL, "item/player_aerial_pearl");
        flatSingleLayer(Itms.BLANK_CARD, "item/blank_card");
        card(Itms.BINDING_CARD, "item/binding_card", "item/binding_card_bound");
        card(Itms.BINDING_CARD_DIM, "item/binding_card_dim", "item/binding_card_dim_bound");
        flatSingleLayer(Itms.LENS_OF_ENDER, "item/lens_of_ender");
        flatSingleLayer(Itms.PHOTOELECTRIC_PANE, "item/photoelectric_pane");
        flatSingleLayer(Itms.THERMOELECTRIC_PLATE, "item/thermoelectric_plate");
        flatSingleLayer(Itms.DIELECTRIC_PASTE, "item/dielectric_paste");
        flatSingleLayer(Itms.DIELECTRIC_ROD, "item/dielectric_rod");
        flatSingleLayer(Itms.DIELECTRIC_ROD_HORIZONTAL, "item/dielectric_rod_horizontal");
        itemModels.declareCustomModelItem(Itms.DIELECTRIC_CASING.get());
        flatSingleLayer(Itms.ENERGIZED_STEEL, "item/steel_energized");
        flatSingleLayer(Itms.BLAZING_CRYSTAL, "item/crystal_blazing");
        flatSingleLayer(Itms.NIOTIC_CRYSTAL, "item/crystal_niotic");
        flatSingleLayer(Itms.SPIRITED_CRYSTAL, "item/crystal_spirited");
        flatSingleLayer(Itms.NITRO_CRYSTAL, "item/crystal_nitro");
        flatSingleLayer(Itms.ENDER_CORE, "item/ender_core");
        flatSingleLayer(Itms.CHARGED_SNOWBALL, "item/charged_snowball");
        flatSingleLayer(Itms.URANINITE_RAW, "item/uraninite_raw");
        flatSingleLayer(Itms.URANINITE, "item/uraninite_crystal");
    }

    private void card(ItemLike item, String texture, String boundTexture) {
        var model = ModelTemplates.FLAT_ITEM.create(item.asItem(),
                TextureMapping.layer0(new Material(Powah.id(texture))),
                itemModels.modelOutput);
        var boundModel = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item.asItem(), "_bound"),
                TextureMapping.layer0(new Material(Powah.id(boundTexture))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item.asItem(), ItemModelUtils.conditional(
                ItemModelUtils.hasComponent(PowahComponents.BOUND_PLAYER),
                ItemModelUtils.plainModel(boundModel),
                ItemModelUtils.plainModel(model)));
    }

    private void flatSingleLayer(ItemLike item, String texture) {
        var model = ModelTemplates.FLAT_ITEM.create(item.asItem(), TextureMapping.layer0(new Material(Powah.id(texture))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item.asItem(), ItemModelUtils.plainModel(model));
    }
}
