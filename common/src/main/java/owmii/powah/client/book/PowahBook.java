package owmii.powah.client.book;

import owmii.powah.Powah;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.client.wiki.Icon;
import owmii.powah.lib.client.wiki.Wiki;
import owmii.powah.lib.client.wiki.page.GridPage;
import owmii.powah.lib.client.wiki.page.Info;
import owmii.powah.lib.client.wiki.page.panel.CraftingPanel;
import owmii.powah.lib.client.wiki.page.panel.EnergyPanel;
import owmii.powah.lib.client.wiki.page.panel.WelcomePanel;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;
import owmii.powah.client.book.centent.IMG;
import owmii.powah.item.Itms;

public class PowahBook {
    public static final Wiki WIKI = new Wiki();

    static {
        WIKI
                .e("generators", new Icon(Blcks.MAGMATOR.get(Tier.NIOTIC)), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("furnator", e -> e.s(s -> s.p(new Info(IMG.FURNATOR, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("magmator", e -> e.s(s -> s.p(new Info(IMG.MAGMATOR, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("thermo_generator", e -> e.s(s -> s.p(new Info(IMG.THERMO, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("solar_panel", e -> e.s(s -> s.p(new Info(IMG.SOLAR_PANEL, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("reactor", e -> e.s(s -> s.p(new Info(IMG.REACTOR, 2, s).next(new Info(IMG.REACTOR_GUI, 2, s).next(new Info(IMG.REACTOR_GUI, 3, s))), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                        , new WelcomePanel(as))))
                .e("storage_transfer", new Icon(Blcks.ENERGY_CELL.get(Tier.BLAZING)), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("energy_cell", e -> e.s(s -> s.p(new Info(IMG.ENERGY_CELL, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("energy_cable", e -> e.s(s -> s.p(new Info(IMG.ENERGY_CABLE, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("ender_cell", e -> e.s(s -> s.p(new Info(IMG.ENDER_CELL, 2, s).next(new Info(IMG.ENDER_CELL, 2, s)), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("ender_gate", e -> e.s(s -> s.p(new Info(IMG.ENDER_GATE, 2, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                        , new WelcomePanel(as))))
                .e("energy_blocks", new Icon(Blcks.PLAYER_TRANSMITTER.get(Tier.SPIRITED)), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("energizing", Blcks.ENERGIZING_ORB.get(), e -> e.s(s -> s.p(new Info(IMG.ENERGIZING, 2, new Object[][]{{Text.toRange(Powah.config().devices.energizing_range)}, {}}, s).next(new Info(IMG.ENERGIZING, s)), new CraftingPanel<>(s).next(new EnergyPanel<>(Blcks.ENERGIZING_ROD.get(Tier.STARTER), s).next(new CraftingPanel<>(Blcks.ENERGIZING_ROD.get(Tier.STARTER), s))))))
                                .e("player_transmitter", e -> e.s(s -> s.p(new Info(IMG.PLAYER_TRANSMITTER, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("energy_hopper", e -> e.s(s -> s.p(new Info(IMG.ENERGY_HOPPER, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("energy_discharger", e -> e.s(s -> s.p(new Info(IMG.ENERGY_DISCHARGER, s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                        , new WelcomePanel(as))))
                .e("items", new Icon(Itms.BATTERY.get(Tier.NITRO)), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("battery", e -> e.s(s -> s.p(new Info(s), new EnergyPanel<>(s).next(new CraftingPanel<>(s)))))
                                .e("wrench", Itms.WRENCH.get(), e -> e.s(s -> s.p(new Info(4, s), new CraftingPanel<>(s))))
                                .e("charged_snowball", Itms.CHARGED_SNOWBALL.get(), e -> e.s(s -> s.p(new Info(s), new CraftingPanel<>(s))))
                                .e("player_aerial_pearl", Itms.PLAYER_AERIAL_PEARL.get(), e -> e.s(s -> s.p(new Info(2, s), new CraftingPanel<>(s))))
                                .e("binding_card", Itms.BINDING_CARD.get(), e -> e.s(s -> s.p(new Info(2, s), new CraftingPanel<>(s))))
                                .e("binding_card_dim", Itms.BINDING_CARD_DIM.get(), e -> e.s(s -> s.p(new Info(2, s), new CraftingPanel<>(s))))
                                .e("lens_of_ender", Itms.LENS_OF_ENDER.get(), e -> e.s(s -> s.p(new Info(s), new CraftingPanel<>(s))))
                        , new WelcomePanel(as))))
                .e("materials", new Icon(Itms.URANINITE.get()), ae -> ae.s(as -> as.p(new GridPage(as)
                                .e("uraninite", Blcks.URANINITE_ORE_DENSE.get(), e -> e.s(s -> s.p(new Info(IMG.URANINITE, 2, s), new WelcomePanel(as))))
                                .e("dry_ice", Blcks.DRY_ICE.get(), e -> e.s(s -> s.p(new Info(IMG.DRY_ICE, s), new WelcomePanel(as))))
                        , new WelcomePanel(as))));
    }

    public static void register() {
    }
}
