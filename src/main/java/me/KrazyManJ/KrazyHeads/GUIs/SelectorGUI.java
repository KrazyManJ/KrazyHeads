package me.KrazyManJ.KrazyHeads.GUIs;

import com.google.common.base.Charsets;
import me.KrazyManJ.KrazyHeads.Core.HeadAPI;
import me.KrazyManJ.KrazyHeads.Core.HeadCategory;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SelectorGUI implements Listener {
    private Inventory inv;
    private final BukkitTask runnable;


    public SelectorGUI(Player player){
        this.inv = Bukkit.createInventory(null, 45, "Selector");
        int[] slots = new int[]{11,12,13,14,15,20,21,22,23,24};
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                int slot = 0;
                for (HeadCategory cat : HeadCategory.values()){
                    inv.setItem(slots[slot], ItemUtils.createItem(HeadAPI.randomItemFromCategory(cat),"&f"+WordUtils.capitalize(cat.getId())));
                    slot++;
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
        inv.setItem(40, statusItem());
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
                assert meta != null;
                new BrowseGUI(player, HeadCategory.valueOf(ChatColor.stripColor(meta.getDisplayName()).replace("-", "_").toUpperCase()));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if (event.getInventory() == inv) {
            runnable.cancel();
            inv = null;
            HandlerList.unregisterAll(this);
        }
    }

    private ItemStack statusItem(){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.nameUUIDFromBytes("OfflinePLayer:KrazyManJ".getBytes(Charsets.UTF_8))));
        meta.setDisplayName(ItemUtils.colorize("         &e&lStatus:"));
        List<String> string = new ArrayList<>();
        string.add(ItemUtils.colorize("&r"));
        string.add(ItemUtils.colorize("  &7Total heads: &e"+HeadAPI.getHeadsCount()+"&r &r "));
        string.add(ItemUtils.colorize("  &7Total categories: &e"+HeadCategory.values().length+"&r &r "));
        string.add(ItemUtils.colorize("&r"));
        meta.setLore(string);
        item.setItemMeta(meta);
        return item;
    }
}
