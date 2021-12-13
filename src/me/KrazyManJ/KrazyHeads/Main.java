package me.KrazyManJ.KrazyHeads;

import me.KrazyManJ.KrazyHeads.Core.HeadAPI;
import me.KrazyManJ.KrazyHeads.GUIs.BrowseGUI;
import me.KrazyManJ.KrazyHeads.GUIs.SelectorGUI;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player player) {
            new SelectorGUI(player);
            return false;
        }
        if (sender instanceof Player player) {
            if (args[0].equals("search")){
                String search = String.join(" ",Arrays.copyOfRange(args, 1, args.length));
                new BrowseGUI(player, search);
            }
            else new BrowseGUI(player, HeadAPI.Category.valueOf(args[0].replace("-", "_").toUpperCase()));
        }
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equals("krazyheads")){
            if (args.length == 1) {
                List<String> suggestions = new ArrayList<>();
                for (HeadAPI.Category cat : HeadAPI.Category.values())
                    suggestions.add(WordUtils.capitalize(cat.toString().replace("_", "-").toLowerCase()));
                return suggestByInput(args[0], suggestions);
            }
            else return new ArrayList<>();
        }
        return super.onTabComplete(sender, command, alias, args);
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
