package owmii.powah.lib.client.wiki;

import owmii.powah.lib.client.wiki.page.panel.Panel;

public class Section {
    private final Entry parent;
    private Page page;
    private Page panel;

    public Section(Entry parent) {
        this.parent = parent;
    }

    public Section p(Page page, Panel panel) {
        this.page = page;
        this.panel = panel;
        return this;
    }

    public Entry getEntry() {
        return this.parent;
    }

    public Page getPage() {
        return this.page;
    }

    public Page getPanel() {
        return this.panel;
    }
}
