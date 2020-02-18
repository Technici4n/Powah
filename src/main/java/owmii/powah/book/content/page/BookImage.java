package owmii.powah.book.content.page;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;

public class BookImage {
    private ResourceLocation location;
    private int width;
    private int height;

    BookImage(ResourceLocation location, int width, int height) {
        this.location = location;
        this.width = width;
        this.height = height;
    }

    public static BookImage create(String path, int width, int height) {
        return new BookImage(GameData.checkPrefix("textures/gui/book/" + path + ".png", true), width, height);
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
