package me.KrazyManJ.KrazyHeads;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
    public static ItemStack createItem(ItemStack item, String displayname){
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(colorize(displayname));
        item.setItemMeta(meta);
        return item;
    }
    public static String colorize(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
