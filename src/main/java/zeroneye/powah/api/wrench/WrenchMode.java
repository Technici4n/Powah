package zeroneye.powah.api.wrench;

public enum WrenchMode {
    WRENCH,
    LINK,
    ROTATE;

    public boolean wrench() {
        return this == WRENCH;
    }

    public boolean link() {
        return this == LINK;
    }

    public boolean rotate() {
        return this == ROTATE;
    }
}
