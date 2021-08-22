package me.linoxgh.cratesenhanced.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {

    public static Component displayName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        Component displayName0 = (meta == null) ? null : meta.displayName();
        if (displayName0 == null) {
            String displayName = item.getI18NDisplayName();
            if (displayName != null) {
                return Component.text(displayName);
            } else {
                return Component.text(item.getType().toString().toLowerCase().replace("_", " "));
            }
        } else {
            return displayName0;
        }
    }

}
