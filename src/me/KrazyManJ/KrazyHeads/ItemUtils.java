package me.KrazyManJ.KrazyHeads;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
    public static ItemStack createItem(Material mat, String displayname){
        ItemStack returnment = new ItemStack(mat);
        ItemMeta meta = returnment.getItemMeta();
        meta.setDisplayName(colorize(displayname));
        returnment.setItemMeta(meta);
        return returnment;
    }
    public static String colorize(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
