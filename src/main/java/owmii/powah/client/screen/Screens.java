package owmii.powah.client.screen;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.apache.commons.lang3.tuple.Pair;
import owmii.powah.client.book.PowahBook;
import owmii.powah.client.screen.container.CableScreen;
import owmii.powah.client.screen.container.DischargerScreen;
import owmii.powah.client.screen.container.EnderCellScreen;
import owmii.powah.client.screen.container.EnergyCellScreen;
import owmii.powah.client.screen.container.EnergyHopperScreen;
import owmii.powah.client.screen.container.FurnatorScreen;
import owmii.powah.client.screen.container.MagmatorScreen;
import owmii.powah.client.screen.container.PlayerTransmitterScreen;
import owmii.powah.client.screen.container.ReactorScreen;
import owmii.powah.client.screen.container.SolarScreen;
import owmii.powah.client.screen.container.ThermoScreen;
import owmii.powah.inventory.Containers;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.wiki.WikiScreen;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.client.wiki.Entry;
import owmii.powah.lib.client.wiki.Page;
import owmii.powah.lib.client.wiki.Section;
import owmii.powah.lib.client.wiki.page.GridPage;
import owmii.powah.lib.client.wiki.page.Info;
import owmii.powah.lib.client.wiki.page.panel.CraftingPanel;
import owmii.powah.lib.client.wiki.page.panel.EnergyPanel;
import owmii.powah.lib.client.wiki.page.panel.InfoBox;
import owmii.powah.lib.client.wiki.page.panel.ItemPanel;
import owmii.powah.lib.client.wiki.page.panel.WelcomePanel;
import owmii.powah.lib.logistics.energy.Energy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Screens {
    public static void register(RegisterMenuScreensEvent event) {
        event.register(Containers.ENERGY_CELL.get(), EnergyCellScreen::new);
        event.register(Containers.ENDER_CELL.get(), EnderCellScreen::new);
        event.register(Containers.FURNATOR.get(), FurnatorScreen::new);
        event.register(Containers.MAGMATOR.get(), MagmatorScreen::new);
        event.register(Containers.PLAYER_TRANSMITTER.get(), PlayerTransmitterScreen::new);
        event.register(Containers.ENERGY_HOPPER.get(), EnergyHopperScreen::new);
        event.register(Containers.CABLE.get(), CableScreen::new);
        event.register(Containers.REACTOR.get(), ReactorScreen::new);
        event.register(Containers.SOLAR.get(), SolarScreen::new);
        event.register(Containers.THERMO.get(), ThermoScreen::new);
        event.register(Containers.DISCHARGER.get(), DischargerScreen::new);
    }

    public static void openManualScreen() {
        for (var lang : List.of("en_us", "fr_fr", "ja_jp", "pt_br", "ru_ru", "tr_tr", "zh_cn", "zh_tw")) {
            try {
                var clientLang = ClientLanguage.loadFrom(Minecraft.getInstance().getResourceManager(), List.of(lang), false);

                class T {
                    String t(String key) {
                        return clientLang.getOrDefault(key, key);
                    }

                    public String translate(Component component) {
                        if (component.getContents() instanceof TranslatableContents translatableContents) {
                            return String.format(Locale.ROOT, t(translatableContents.getKey()), translatableContents.getArgs());
                        }
                        return component.getString();
                    }
                }
                var t = new T();
                var bookPath = Paths.get("Z:/Powah/guidebook");
                if (!"en_us".equals(lang)) {
                    bookPath = bookPath.resolve("_" + lang);
                }

                var position = 1;
                for (var category : PowahBook.WIKI.getCategories()) {
                    var mdFile = bookPath.resolve(category.getName() + "/index.md");
                    Files.createDirectories(mdFile.getParent());

                    var lines = new ArrayList<String>();
                    lines.add("---");
                    lines.add("navigation:");
                    lines.add("  title: " + t.t(category.getTransKey()));
                    if (category.getIcon() != null) {
                        lines.add("  icon: " + category.getIcon().getStack().getItem());
                    }
                    lines.add("  position: " + (position++));
                    lines.add("---");
                    lines.add("");
                    lines.add("# " + t.t(category.getTransKey()));

                    for (Section section : category.getSections()) {
                        switch (section.getPanel()) {
                            case WelcomePanel panel -> {
                            }
                            default -> throw new IllegalStateException("Unexpected value: " + section.getPanel());
                        }

                        var subPosition = 0;
                        switch (section.getPage()) {
                            case GridPage gridPage -> {
                                for (var entry : gridPage.getEntries()) {
                                    var subPageMdFile = mdFile.resolveSibling(entry.getName() + ".md");

                                    var subLines = new ArrayList<String>();
                                    subLines.add("---");
                                    subLines.add("navigation:");
                                    subLines.add("  title: " + t.t(entry.getTransKey()));
                                    subLines.add("  parent: " + category.getName() + "/index.md");
                                    if (entry.getIcon() != null) {
                                        subLines.add("  icon: " + entry.getIcon().getStack().getItem());
                                    }
                                    subLines.add("  position: " + (subPosition++));

                                    Set<ResourceLocation> items = new HashSet<>();
                                    for (var subSection : entry.getSections()) {
                                        for (var panel = subSection.getPanel(); panel != null; panel = panel.next()) {
                                            if (panel instanceof ItemPanel<?> itemPanel) {
                                                for (ItemLike item : itemPanel.getItems()) {
                                                    items.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
                                                }
                                            }
                                        }
                                    }
                                    if (!items.isEmpty()) {
                                        subLines.add("item_ids:");
                                        items.stream().sorted(Comparator.comparing(ResourceLocation::toString)).forEach(id ->
                                                subLines.add("  - " + id));
                                    }
                                    subLines.add("---");
                                    subLines.add("");
                                    subLines.add("# " + t.t(entry.getTransKey()));
                                    subLines.add("");

                                    for (var subSection : entry.getSections()) {
                                        switch (subSection.getPage()) {
                                            case Info info -> {
                                                Texture img = info.getImg();
                                                if (!img.isEmpty()) {
                                                    var lastSlash = img.getLocation().getPath().lastIndexOf('/');
                                                    var filename = img.getLocation().getPath().substring(lastSlash + 1);

                                                    if ("en_us".equals(lang)) {
                                                        try (var originalImage = Minecraft.getInstance().getResourceManager().open(
                                                                img.getLocation()
                                                        )) {
                                                            Path outputPath = subPageMdFile.resolveSibling(filename);
                                                            Files.createDirectories(outputPath.getParent());
                                                            try (var out = Files.newOutputStream(outputPath)) {
                                                                originalImage.transferTo(out);
                                                            }
                                                        }
                                                    }
                                                    subLines.add("![](./" + filename + ")");
                                                    subLines.add("");
                                                }

                                                int pp = 0;
                                                Page currentPage = info;
                                                while (currentPage != null && currentPage.prev() != null) {
                                                    Page prev = currentPage.prev();
                                                    if (prev instanceof Info) {
                                                        pp += ((Info) prev).getParagraphs();
                                                    }
                                                    currentPage = prev;
                                                }

                                                for (int i = 0; i < info.getParagraphs(); i++) {
                                                    String translatedText = t.t("wiki.powah." + entry.getName() + "_" + (i + pp));
                                                    var text = String.format(Locale.ROOT, translatedText, info.getArgs()[i]);
                                                    String[] words = text.split("\\s+");
                                                    StringBuilder paragraph = new StringBuilder();
                                                    for (int j = 0; j < words.length; j++) {
                                                        String w = words[j];
                                                        if (w.startsWith("<") && w.contains(":") && w.endsWith(">")) {
                                                            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(w.substring(1, w.length() - 1)));
                                                            paragraph.append("<ItemLink id=\"" + item + "\" /> ");
                                                        } else {
                                                            paragraph.append(w).append(" ");
                                                        }
                                                    }
                                                    subLines.add(paragraph.toString());
                                                    subLines.add("");
                                                }
                                            }
                                            default ->
                                                    throw new IllegalStateException("Unexpected value: " + subSection.getPage());
                                        }

                                        for (var panel = subSection.getPanel(); panel != null; panel = panel.next()) {
                                            switch (panel) {
                                                case EnergyPanel<?> energyPanel -> {
                                                    Table<Item, String, String> table = HashBasedTable.create();
                                                    for (var itemLike : energyPanel.getItems()) {
                                                        Item item = itemLike.asItem();
                                                        if (item instanceof InfoBox.IInfoBoxHolder infoBoxHolder) {
                                                            var infoBox = new InfoBox(0xff0000, 0x3d3d3d);
                                                            infoBox = infoBoxHolder.getInfoBox(item.getDefaultInstance(), infoBox);
                                                            for (var ibentry : infoBox.getLines().entrySet()) {
                                                                var col = ((TranslatableContents) ibentry.getKey().getContents()).getKey();
                                                                String cellValue = switch (col) {
                                                                    case "info.lollipop.capacity" -> "<powah:EnergyCapacity id=\"" + item + "\" />";
                                                                    case "info.lollipop.generates" -> "<powah:EnergyGeneration id=\"" + item + "\" />";
                                                                    case "info.lollipop.max.extract" -> "<powah:EnergyMaxIO id=\"" + item + "\" />";
                                                                    case "info.lollipop.max.io" -> "<powah:EnergyMaxIO id=\"" + item + "\" />";
                                                                    case "info.powah.generation.factor" -> "<powah:EnergyGenerationFactor id=\"" + item + "\" />";
                                                                    default ->
                                                                            throw new IllegalStateException("Unexpected value: " + col);
                                                                };
                                                                table.put(item, t.t(col), cellValue);
                                                            }
                                                        }
                                                    }

                                                    var maxItemWidth = table.rowKeySet().stream().map(Item::toString)
                                                            .mapToInt(i -> ("<ItemLink id=\"" + i + "\" />").length())
                                                            .max()
                                                            .orElse(0);
                                                    var maxWidths = table.columnMap().entrySet().stream().collect(
                                                            Collectors.toMap(
                                                                    Map.Entry::getKey,
                                                                    e -> e.getValue().values().stream().mapToInt(String::length).max().orElse(0)
                                                            )
                                                    );

                                                    subLines.add("| " + " ".repeat(maxItemWidth) + " |" + table.columnKeySet().stream().map(c -> Strings.padEnd(c, maxWidths.get(c), ' ')).collect(Collectors.joining(" | ", " ", " |")));
                                                    subLines.add("| " + "-".repeat(maxItemWidth) + " |" + table.columnKeySet().stream().map(c -> "-".repeat(maxWidths.get(c))).collect(Collectors.joining(" | ", " ", " |")));
                                                    for (var itemMapEntry : table.rowMap().entrySet()) {
                                                        subLines.add("| " + Strings.padEnd("<ItemLink id=\"" + itemMapEntry.getKey() + "\" />", maxItemWidth, ' ') + " |" + table.columnKeySet().stream().map(c -> Strings.padEnd(itemMapEntry.getValue().get(c), maxWidths.get(c), ' ')).collect(Collectors.joining(" | ", " ", " |")));
                                                    }
                                                    subLines.add("");
                                                }
                                                case CraftingPanel<?> craftingPanel -> {
                                                    subLines.add("<Row>");
                                                    for (ItemLike item : craftingPanel.getItems()) {
                                                        subLines.add("<RecipeFor id=\"" + item.asItem() + "\" />");
                                                    }
                                                    subLines.add("</Row>");
                                                }
                                                case WelcomePanel welcomePanel -> {
                                                }
                                                default ->
                                                        throw new IllegalStateException("Unexpected value: " + panel);
                                            }
                                        }
                                    }

                                    Files.writeString(subPageMdFile, String.join("\n", subLines));
                                }
                            }
                            default -> throw new IllegalStateException("Unexpected value: " + section.getPage());
                        }
                    }
                    Files.writeString(mdFile, String.join("\n", lines));

                }

                String indexPage = """
                        ---
                        navigation:
                          title: Powah
                          position: 0
                        ---
                        
                        # Powah
                        
                        """;
                for (var category : PowahBook.WIKI.getCategories()) {
                    indexPage += "- [" + t.t(category.getTransKey()) + "](./" + category.getName() + "/index.md)\n";
                }
                indexPage += "";

                Files.writeString(bookPath.resolve("index.md"), indexPage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        WikiScreen.open(PowahBook.WIKI.getCategories().get(0));
    }
}
