package owmii.lib.client.wiki.page.panel;

import owmii.lib.client.wiki.Page;
import owmii.lib.client.wiki.Section;

import javax.annotation.Nullable;

public class Panel extends Page {
    public Panel(Section parent) {
        this("", parent);
    }

    public Panel(String name, Section parent) {
        super(name, parent);
    }

    public Panel next(@Nullable Panel next) {
        return (Panel) super.next(next);
    }
}
