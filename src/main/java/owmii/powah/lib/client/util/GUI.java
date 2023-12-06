package owmii.powah.lib.client.util;

public class GUI {
    public static boolean isMouseOver(double mx, double my, int x, int y, float width, float height) {
        return mx >= x && my >= y && mx < x + width && my < y + height;
    }
}
