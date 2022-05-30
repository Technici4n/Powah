package owmii.lib.logistics;

public interface IRedstoneInteract {
    Redstone getRedstoneMode();

    void setRedstoneMode(Redstone mode);

    default void nextRedstoneMode() {
        setRedstoneMode(getRedstoneMode().next());
    }
}
