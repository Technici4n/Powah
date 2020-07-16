package owmii.powah.book.content.page;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

public interface IBookInfo {
    @OnlyIn(Dist.CLIENT)
    Map<String, Object[]> getBookInfo(ItemStack stack, Map<String, Object[]> lines);
}
