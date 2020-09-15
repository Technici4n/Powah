package owmii.powah.client.book;

import owmii.lib.client.util.Text;
import owmii.lib.client.wiki.Icon;
import owmii.lib.client.wiki.Wiki;
import owmii.lib.client.wiki.page.GridPage;
import owmii.lib.client.wiki.page.Info;
import owmii.lib.client.wiki.page.panel.CraftingPanel;
import owmii.lib.client.wiki.page.panel.EnergyPanel;
import owmii.lib.client.wiki.page.panel.WelcomePanel;
import owmii.powah.block.Blcks;
import owmii.powah.client.book.centent.IMG;
import owmii.powah.config.Configs;
import owmii.powah.item.Itms;

public class PowahBook {
    public static final Wiki WIKI = new Wiki(Itms.REG);

    static {
        WIKI
                .e("generators", new Icon(Blcks.MAGMATOR_NIOTIC), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("furnator", e -> e.s(s -> s.p(new Info(IMG.FURNATOR, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("magmator", e -> e.s(s -> s.p(new Info(IMG.MAGMATOR, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("thermo_generator", e -> e.s(s -> s.p(new Info(IMG.THERMO, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("solar_panel", e -> e.s(s -> s.p(new Info(IMG.SOLAR_PANEL, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("reactor", e -> e.s(s -> s.p(new Info(IMG.REACTOR, 2, s).next(new Info(IMG.REACTOR_GUI, 2, s).next(new Info(IMG.REACTOR_GUI, 3, s))), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                        , new WelcomePanel(as))))
                .e("storage_transfer", new Icon(Blcks.ENERGY_CELL_BLAZING), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("energy_cell", e -> e.s(s -> s.p(new Info(IMG.ENERGY_CELL, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("energy_cable", e -> e.s(s -> s.p(new Info(IMG.ENERGY_CABLE, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("ender_cell", e -> e.s(s -> s.p(new Info(IMG.ENDER_CELL, 2, s).next(new Info(IMG.ENDER_CELL, 2, s)), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("ender_gate", e -> e.s(s -> s.p(new Info(IMG.ENDER_GATE, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                        , new WelcomePanel(as))))
                .e("energy_blocks", new Icon(Blcks.PLAYER_TRANSMITTER_NIOTIC), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("energizing", Blcks.ENERGIZING_ORB, e -> e.s(s -> s.p(new Info(IMG.ENERGIZING, 2, new Object[][]{{Text.toRange(Configs.ENERGIZING.range.get())}, {}}, s).next(new Info(IMG.ENERGIZING, s)), new CraftingPanel<>(s).next(new EnergyPanel<>(Blcks.ENERGIZING_ROD_STARTER, s).next(new CraftingPanel<>(Blcks.ENERGIZING_ROD_STARTER, s))))))
                                .e("player_transmitter", e -> e.s(s -> s.p(new Info(IMG.PLAYER_TRANSMITTER, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("energy_hopper", e -> e.s(s -> s.p(new Info(IMG.ENERGY_HOPPER, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("energy_discharger", e -> e.s(s -> s.p(new Info(IMG.ENERGY_DISCHARGER, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                        , new WelcomePanel(as))))
                .e("items", new Icon(Itms.BATTERY_NITRO), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("battery", e -> e.s(s -> s.p(new Info(s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("wrench", Itms.WRENCH, e -> e.s(s -> s.p(new Info(4, s), new CraftingPanel<>(s))))
                                .e("charged_snowball", Itms.CHARGED_SNOWBALL, e -> e.s(s -> s.p(new Info(s), new CraftingPanel<>(s))))
                                .e("player_aerial_pearl", Itms.PLAYER_AERIAL_PEARL, e -> e.s(s -> s.p(new Info(2, s), new CraftingPanel<>(s))))
                                .e("binding_card", Itms.BINDING_CARD, e -> e.s(s -> s.p(new Info(2, s), new CraftingPanel<>(s))))
                                .e("binding_card_dim", Itms.BINDING_CARD_DIM, e -> e.s(s -> s.p(new Info(2, s), new CraftingPanel<>(s))))
                                .e("lens_of_ender", Itms.LENS_OF_ENDER, e -> e.s(s -> s.p(new Info(s), new CraftingPanel<>(s))))
                        , new WelcomePanel(as))))
                .e("materials", new Icon(Itms.URANINITE), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("uraninite", Blcks.URANINITE_ORE_DENSE, e -> e.s(s -> s.p(new Info(IMG.URANINITE, 2, s), new WelcomePanel(as))))
                                .e("dry_ice", Blcks.DRY_ICE, e -> e.s(s -> s.p(new Info(IMG.DRY_ICE, s), new WelcomePanel(as))))
                        , new WelcomePanel(as))));
    }

    public static void register() {
    }
}
