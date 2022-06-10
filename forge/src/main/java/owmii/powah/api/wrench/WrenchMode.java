package owmii.powah.api.wrench;

public enum WrenchMode {
    CONFIG,
    LINK,
    ROTATE;

    public boolean config() {
        return this == CONFIG;
    }

    public boolean link() {
        return this == LINK;
    }

    public boolean rotate() {
        return this == ROTATE;
    }
}
