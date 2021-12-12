package me.KrazyManJ.KrazyHeads.GUIs;

import me.KrazyManJ.KrazyHeads.HeadAPI;
import me.KrazyManJ.KrazyHeads.ItemUtils;
import me.KrazyManJ.KrazyHeads.Main;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SelectorGUI implements Listener {
    private Inventory inv;

    public SelectorGUI(Player player){
        this.inv = Bukkit.createInventory(null, 45, "Selector");
        int[] slots = new int[]{11,12,13,14,15,20,21,22,23,24};
        int slot = 0;
        for (HeadAPI.Category cat : HeadAPI.Category.values()){
            inv.setItem(slots[slot], ItemUtils.createItem(Material.PLAYER_HEAD,"&f"+WordUtils.capitalize(cat.getId())));
            slot++;
        }
        for (int i = 0; i < 45; i++) if (inv.getItem(i) == null) inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        player.openInventory(inv);
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if (event.getClickedInventory() == inv && event.getCurrentItem() != null){
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();

            if (item.getType().equals(Material.PLAYER_HEAD)){
                ItemMeta meta = item.getItemMeta();
                HandlerList.unregisterAll(this);
                new BrowseGUI(player, HeadAPI.Category.valueOf(ChatColor.stripColor(meta.getDisplayName()).toUpperCase()));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if (event.getInventory() == inv) {
            inv = null;
            HandlerList.unregisterAll(this);
        }
    }
}