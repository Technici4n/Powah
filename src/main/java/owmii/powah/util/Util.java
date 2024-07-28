package owmii.powah.util;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import owmii.powah.lib.logistics.fluid.Tank;

public class Util {
    public static boolean anyMatch(int[] arr, int value) {
        return Arrays.stream(arr).anyMatch(i -> i == value);
    }

    public static boolean anyMatch(long[] arr, long value) {
        return Arrays.stream(arr).anyMatch(i -> i == value);
    }

    public static int safeInt(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    public static int safeInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long safeLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return 0L;
        }
    }

    static final NavigableMap<Long, String> SUFFIXES = new TreeMap<>();

    static {
        SUFFIXES.put(1_000L, "k");
        SUFFIXES.put(1_000_000L, "M");
        SUFFIXES.put(1_000_000_000L, "B");
        SUFFIXES.put(1_000_000_000_000L, "T");
        SUFFIXES.put(1_000_000_000_000_000L, "P");
        SUFFIXES.put(1_000_000_000_000_000_000L, "E");
    }

    public static MutableComponent formatTankContent(Tank tank) {
        return formatTankContent(tank.getFluidAmount(), tank.getCapacity());
    }

    public static MutableComponent formatTankContent(long amount, long capacity) {
        return Component.translatable("info.lollipop.mb.stored",
                Util.addCommas(amountToMillibuckets(amount)),
                Util.numFormat(amountToMillibuckets(capacity))).withStyle(ChatFormatting.DARK_GRAY);
    }

    public static int bucketAmount() {
        return 1000;
    }

    public static long amountToMillibuckets(long amount) {
        var result = amount * 1000 / Util.bucketAmount();
        if (result == 0 && amount != 0) {
            return amount >= 0 ? 1 : -1;
        }
        return result;
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public static String numFormat(long value) {
        if (value == Long.MIN_VALUE)
            return numFormat(Long.MIN_VALUE + 1);
        if (value < 0)
            return "-" + numFormat(-value);
        if (value < 1000)
            return Long.toString(value);

        Map.Entry<Long, String> e = SUFFIXES.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String addCommas(long value) {
        return NumberFormat.getInstance(Locale.ROOT).format(value);
    }

    public static String getModName(String modId) {
        return getModInfo(modId).getDisplayName();
    }

    public static String getModVersion(String modId) {
        return getModInfo(modId).getVersion().toString();
    }

    private static IModInfo getModInfo(String modId) {
        return ModList.get().getModContainerById(modId).get().getModInfo();
    }
}
