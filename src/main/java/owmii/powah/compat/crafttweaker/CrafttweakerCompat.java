package owmii.powah.compat.crafttweaker;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CrafttweakerCompat {
    public static final String ID = "crafttweaker";
    private static int loaded;

    public static void setup() {
        if (isLoaded()) {
            BufferedWriter out = null;
            try {
                Path dir = FMLPaths.GAMEDIR.get();
                Path energyPath = Paths.get(dir.toAbsolutePath().toString(), "scripts/powah");
                try {
                    Files.createDirectories(energyPath);
                } catch (Exception ignored) {
                }

                File scriptFile = new File(dir.toString() + "/scripts/powah/energizing.zs");

                if (scriptFile.exists()) {
                    return;
                }

                out = new BufferedWriter(new FileWriter(scriptFile));
                try {

                    out.write("import mods.powah.Energizing;");
                    out.newLine();
                    out.newLine();
                    out.write("// Example method to add Energizing recipe");
                    out.newLine();
                    out.write("// Arguments: output, energy (max = 2147483647), inputs (max = 6 items/tags)");
                    out.newLine();
                    out.write("// Energizing.addRecipe(<item:powah:dielectric_paste>, 1000, [<tag:minecraft:coals>, <item:minecraft:clay>]);");
                    out.newLine();
                    out.newLine();
                    out.write("// Example method to remove Energizing recipe");
                    out.newLine();
                    out.write("// Arguments: output");
                    out.newLine();
                    out.write("// Energizing.removeRecipe(<item:powah:energised_steel>);");
                    out.newLine();
                    out.newLine();
                    out.write("// Example method to clear all Energizing recipes");
                    out.newLine();
                    out.write("// Energizing.clearAll();");

                } catch (IOException ignored) {
                } finally {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isLoaded() {
        if (loaded == 0) loaded = ModList.get().isLoaded(ID) ? 1 : -1;
        return loaded == 1;
    }
}
