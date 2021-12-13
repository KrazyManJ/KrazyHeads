package me.KrazyManJ.KrazyHeads.GUIs;

import me.KrazyManJ.KrazyHeads.Core.HeadAPI;
import me.KrazyManJ.KrazyHeads.ItemUtils;
import me.KrazyManJ.KrazyHeads.Main;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BrowseGUI implements Listener {
    private Inventory inv;
    private int page;

    private int maxPage;
    private final List<ItemStack> heads;

    public BrowseGUI(Player player, HeadAPI.Category cat){
        this.inv = Bukkit.createInventory(null, 54,"KrazyHeads - "+ WordUtils.capitalize(cat.getId()));
        this.heads = HeadAPI.getCategoryHeads(cat);
        createGUI(player);
    }
    public BrowseGUI(Player player, String input){
        this.inv = Bukkit.createInventory(null, 54,"KrazyHeads - Searching");
        this.heads = HeadAPI.getHeadsBySearch(input);
        createGUI(player);
    }

    private void createGUI(Player player){
        this.page = 1;
        this.maxPage = (int)Math.ceil(heads.size()/45F);
        setPage(page);
        player.openInventory(inv);
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }


    private void setPage(int page){
        this.page = page;
        int headCounter = (page-1)*45;
        for (int i = 0; i < 45; i++) {
            if (headCounter >= heads.size()) inv.setItem(i,new ItemStack(Material.AIR));
            else {
                inv.setItem(i,heads.get(headCounter));
                headCounter++;
            }
        }
        makeGuiControlBar();
    }


    private void makeGuiControlBar(){
        for (int i = 45; i < 54; i++) inv.setItem(i,new ItemStack(Material.AIR));
        inv.setItem(45, closeItem());
        if (this.page != 1) inv.setItem(47, previousPageItem());
        if (this.page != this.maxPage) inv.setItem(51, nextPageItem());
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if (event.getClickedInventory() == inv && event.getCurrentItem() != null) {
            ItemStack clicked = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();

            if (clicked.getType().equals(Material.PLAYER_HEAD)) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                player.getInventory().addItem(clicked);
            }
            else if (clicked.equals(closeItem())){
                new SelectorGUI(player);
            }
            else if (clicked.equals(nextPageItem())) {
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 2);
                setPage(page+1);
            }
            else if (clicked.equals(previousPageItem())) {
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 2);
                if (page != 1) setPage(page-1);
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




    private ItemStack closeItem(){
        return ItemUtils.createItem(new ItemStack(Material.BARRIER), "&x&f&f&0&0&0&0&lClose");
    }

    private ItemStack nextPageItem(){
        return ItemUtils.createItem(new ItemStack(Material.ARROW), "&7Next page");
    }

    private ItemStack previousPageItem(){
        return ItemUtils.createItem(new ItemStack(Material.ARROW), "&7Previous page");
    }


}
