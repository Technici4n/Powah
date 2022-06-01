package owmii.lib.util.dev;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.IModInfo;
import owmii.lib.registry.Registry;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DevUtil {
    public static void genItemModels(Registry<? extends Item> registry) {
        registry.forEach(item -> {
            DevUtil.genItemModel(item.getRegistryName());
        });
    }

    public static void genItemModel(@Nullable ResourceLocation location) {
        if (location != null) {
            String domain = location.getNamespace();
            String name = location.getPath();
            IModInfo info = ModList.get().getModFileById(domain).getMods().get(0);
            String jsonString = "{\n  \"parent\": \"item/generated\",\n  \"textures\": {\n\t\"layer0\": \"" + domain + ":item/" + name + "\"\n  }\n}";
            Path path = Paths.get(FMLPaths.GAMEDIR.get().toAbsolutePath().toString(), "../../../" + info.getDisplayName() + "/src/main/resources/assets/" + domain + "/models/item/" + name + ".json");
            if (!path.toFile().exists()) {
                try {
                    Files.createDirectories(path.getParent());
                    try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                        bufferedwriter.write(jsonString);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
