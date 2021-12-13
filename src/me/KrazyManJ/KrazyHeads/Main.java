package me.KrazyManJ.KrazyHeads;

import me.KrazyManJ.KrazyHeads.Core.HeadAPI;
import me.KrazyManJ.KrazyHeads.GUIs.BrowseGUI;
import me.KrazyManJ.KrazyHeads.GUIs.SelectorGUI;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {
    private static Main instance;

    public static Main getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();
        HeadAPI.initializeHeads();
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender,@Nonnull Command command,@Nonnull String label,@Nonnull String[] args) {
        if (sender instanceof Player player){
            if (args.length == 0){
                new SelectorGUI(player);
            }
            else if (args.length == 1 && args[0].equals("reload") && player.hasPermission("krazyheads.admin")){
                HeadAPI.initializeHeads();
                player.sendMessage("Heads successfully reloaded!");
            }
            else if (args.length == 2 && args[0].equals("category") && EnumUtils.isValidEnum(HeadAPI.Category.class, args[1].replace("-", "_").toUpperCase())) {
                new BrowseGUI(player, HeadAPI.Category.valueOf(args[1].replace("-", "_").toUpperCase()));
            }
            else if (args.length <= 2 && args[0].equals("search")){
                String search = String.join(" ",Arrays.copyOfRange(args, 1, args.length));
                new BrowseGUI(player, search);
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender,@Nonnull Command command,@Nonnull String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) suggestions = suggestByInput(args[0], Arrays.asList("search","category","reload"));
        else if (args.length == 2) {
            if (args[0].equals("category")){
                for (HeadAPI.Category cat : HeadAPI.Category.values())
                    suggestions.add(WordUtils.capitalize(cat.toString().replace("_", "-").toLowerCase()));
                return suggestByInput(args[1], suggestions);
            }
        }
        return suggestions;
    }

    @Override public void onDisable() {
        super.onDisable();
    }

    public List<String> suggestByInput(String input, List<String> suggestions) {
        return (!input.equals(""))
                ? suggestions.stream().filter(f -> StringUtils.containsIgnoreCase(f, input)).collect(Collectors.toList())
                : suggestions;
    }
    public static void log(Level l, String t){
        instance.getLogger().log(l, ChatColor.translateAlternateColorCodes('&', t));
    }
}
