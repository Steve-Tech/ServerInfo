package me.stevetech.serverinfo;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerInfo extends JavaPlugin implements Listener {
    TextComponent info;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        loadText(null);

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info(getDescription().getName() + ' ' + getDescription().getVersion() + " has been Enabled");
    }

    @Override
    public void onDisable() {
        saveConfig();

        getLogger().info(getDescription().getName() + ' ' + getDescription().getVersion() + " has been Disabled");
    }

    @SuppressWarnings("unchecked")
    private void loadText(Player player) {
        List<String> configText = (List<String>) getConfig().getList("message");
        ChatColor hoverColor = ChatColor.valueOf(getConfig().getString("hover-color"));
        info = new TextComponent();
        for (int i = 0; i < configText.size(); i++) {
            String message = (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) ?
                    PlaceholderAPI.setPlaceholders(player, configText.get(i)) : configText.get(i);
            Pattern pattern = Pattern.compile("(\\[.*?\\] ?\\(.*?\\))");
            Matcher matcher = pattern.matcher(message);

            int lastEnd = 0;
            while (matcher.find()) {
                String[] halves = matcher.group().split("\\] ?\\(");
                String text = halves[0].substring(1);
                String action = halves[1].substring(0, halves[1].length()-1);
                halves = null;

                info.addExtra(ChatColor.translateAlternateColorCodes('&', message.substring(lastEnd, matcher.start())));

                if (action.startsWith("COMMAND ")) {
                    TextComponent textShown = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
                    textShown.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, action.substring(8)));
                    if (getConfig().getBoolean("show-hover"))
                        textShown.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverColor + "Run Command: " + action.substring(8))));
                    info.addExtra(textShown);

                } else if (action.startsWith("SUGGEST ")) {
                    TextComponent textShown = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
                    textShown.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, action.substring(8)));
                    if (getConfig().getBoolean("show-hover"))
                        textShown.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverColor + "Suggest Command: " + action.substring(8))));
                    info.addExtra(textShown);

                } else if (action.startsWith("COPY ")) {
                    TextComponent textShown = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
                    textShown.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, action.substring(5)));
                    if (getConfig().getBoolean("show-hover"))
                        textShown.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverColor + "Copy Text: " + action.substring(5))));
                    info.addExtra(textShown);

                } else if (action.startsWith("LINK ")) {
                    TextComponent textShown = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
                    textShown.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, action.substring(5)));
                    if (getConfig().getBoolean("show-hover"))
                        textShown.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverColor + "Open URL: " + action.substring(5))));
                    info.addExtra(textShown);

                } else {
                    TextComponent textShown = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
                    textShown.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, action));
                    if (getConfig().getBoolean("show-hover"))
                        textShown.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverColor + "Open URL: " + action)));
                    info.addExtra(textShown);
                }


                lastEnd = matcher.end();
            }
            info.addExtra(ChatColor.translateAlternateColorCodes('&', message.substring(lastEnd)));

            if (i < configText.size()-1) info.addExtra(ChatColor.RESET + "\n");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("info") && sender.hasPermission("serverinfo.info")) {
            if (getConfig().getBoolean("refresh-on-send")) {
                getServer().getScheduler().runTaskAsynchronously(this, () -> {
                    loadText((Player) sender);
                    sender.spigot().sendMessage(info);
                });
            }
            else {
                sender.spigot().sendMessage(info);
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase("serverinfo") && sender.hasPermission("serverinfo.reload")
                && args.length == 1 && args[0].equals("reload")) {
            reloadConfig();
            loadText(null);
            sender.sendMessage("Reloaded Config.");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (getConfig().getBoolean("show-on-join") || getConfig().getBoolean("first-join-only")) {
            Player player = event.getPlayer();
            if (player.hasPermission("serverinfo.info") && (!getConfig().getBoolean("first-join-only") ||
                    (getConfig().getBoolean("first-join-only") && !player.hasPlayedBefore()))) {
                getServer().getScheduler().runTaskLater(this, () -> {
                    if (getConfig().getBoolean("refresh-on-send")) {
                        getServer().getScheduler().runTaskAsynchronously(this, () -> {
                            loadText(player);
                            player.spigot().sendMessage(info);
                        });
                    } else player.spigot().sendMessage(info);
                }, 1L);
            }
        }
    }
}
