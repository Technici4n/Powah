package owmii.powah.lib.client.wiki.page.panel;

import org.jetbrains.annotations.Nullable;
import owmii.powah.lib.client.wiki.Page;
import owmii.powah.lib.client.wiki.Section;

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
