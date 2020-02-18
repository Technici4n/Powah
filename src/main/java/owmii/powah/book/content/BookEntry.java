package owmii.powah.book.content;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.GameData;
import owmii.lib.util.FML;
import owmii.powah.book.content.page.BookPage;

import java.util.ArrayList;
import java.util.List;

public class BookEntry {
    private String title = "";
    private BookIcon icon = new BookIcon();
    private List<BookPage> pages = new ArrayList<>();
    private int main;

    BookEntry() {
    }

    public static BookEntry create(String title) {
        final BookEntry entry = new BookEntry();
        entry.title = "book." + FML.getActiveID() + ".title." + title;
        return entry;
    }

    public BookEntry icon(IItemProvider item) {
        return icon(new ItemStack(item));
    }

    public BookEntry icon(ItemStack icon) {
        this.icon = BookIcon.create(icon);
        return this;
    }

    public BookEntry icon(String path) {
        this.icon = BookIcon.create(GameData.checkPrefix("textures/gui/book/" + path + ".png", true));
        return this;
    }

    public boolean hasIcon() {
        return !this.icon.isEmpty();
    }

    public BookIcon getIcon() {
        return this.icon;
    }

    public BookEntry add(BookPage page) {
        this.pages.add(page);
        return this;
    }

    public BookPage get(int i) {
        return this.pages.get(i);
    }

    public String getTitle() {
        return this.title;
    }

    public int getMain() {
        return this.main;
    }

    public BookEntry setMain(int main) {
        this.main = main;
        return this;
    }

    public int size() {
        return this.pages.size();
    }

    public List<BookPage> getPages() {
        return this.pages;
    }
}
