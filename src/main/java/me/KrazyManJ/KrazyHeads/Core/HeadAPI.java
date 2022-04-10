package me.KrazyManJ.KrazyHeads.Core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.KrazyManJ.KrazyHeads.Main;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.logging.Level;

public class HeadAPI {
    private static final HashMap<HeadCategory, List<ItemStack>> data = new HashMap<>();

    public static List<ItemStack> getCategoryHeads(HeadCategory cat){
        return data.get(cat);
    }
    public static List<ItemStack> getHeadsBySearch(String input){
        List<ItemStack> result = new ArrayList<>();
        for (HeadCategory cat : HeadCategory.values()){
            for (ItemStack item : data.get(cat)){
                if (StringUtils.containsIgnoreCase(Objects.requireNonNull(item.getItemMeta()).getDisplayName(), input)) result.add(item);
            }
        }
        return result;
    }
    public static int getHeadsCount(){
        return data.size();
    }
    public static ItemStack randomItemFromCategory(HeadCategory cat){
        List<ItemStack> coll = data.get(cat);
        return coll.get((int) (Math.random() * coll.size()));
    }

    public static JsonArray fetchAPI(HeadCategory category) {
        try {
            URL url = new URL("https://minecraft-heads.com/scripts/api.php?cat=" + category.getId()+"&tags=true");
            URLConnection request = url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            return root.getAsJsonArray();
        }
        catch (IOException e) {
            e.printStackTrace();
            return new JsonArray();
        }
    }
    public static void initializeHeads(){
        for (HeadCategory cat : HeadCategory.values()){
            new BukkitRunnable() {
                @Override public void run() {
                    List<ItemStack> s = new LinkedList<>();
                    for (JsonElement elem : fetchAPI(cat)){
                        JsonObject obj = (JsonObject) elem;
                        String value = obj.get("value").getAsString();
                        String name = obj.get("name").getAsString();
                        ItemStack head = customPlayerHead(value);
                        ItemMeta meta = head.getItemMeta();
                        assert meta != null;
                        meta.setDisplayName(ChatColor.RESET + name);
                        head.setItemMeta(meta);
                        s.add(head);
                    }
                    data.put(cat, s);
                }
            }.runTaskAsynchronously(Main.getInstance());
            Main.log(Level.INFO, "&aSuccessfully loaded category &x&8&0&e&b&3&4\""+cat.getId()+"\"&a!");
        }
    }
    public static ItemStack customPlayerHead(String base64){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", base64));
        Field profileField;
        try {
            assert meta != null;
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }
}
