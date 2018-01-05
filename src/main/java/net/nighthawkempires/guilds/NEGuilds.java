package net.nighthawkempires.guilds;

import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.server.Server;
import net.nighthawkempires.guilds.command.GuildCommand;
import net.nighthawkempires.guilds.data.GuildData;
import net.nighthawkempires.guilds.guild.GuildRegistry;
import net.nighthawkempires.guilds.guild.GuildTag;
import net.nighthawkempires.guilds.listener.GuildListener;
import net.nighthawkempires.guilds.listener.InventoryListener;
import net.nighthawkempires.guilds.listener.PlayerListener;
import net.nighthawkempires.guilds.listener.PluginListener;
import net.nighthawkempires.guilds.scoreboard.GuildScoreboards;
import net.nighthawkempires.guilds.user.User;
import net.nighthawkempires.guilds.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NEGuilds extends JavaPlugin {

    private static Plugin plugin;
    private static NEGuilds instance;

    private static GuildRegistry guildRegistry;
    private static PluginManager pluginManager;
    private static UserManager userManager;

    private static InventoryListener inventoryListener;

    private static GuildData guildData;

    public void onEnable() {
        if (NECore.getSettings().server != Server.SUR) {
            NECore.getLoggers().warn(this, "This plugin can only be ran on the Survival Server.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;
        instance = this;

        guildRegistry = new GuildRegistry();
        pluginManager = Bukkit.getPluginManager();
        userManager = new UserManager();

        inventoryListener = new InventoryListener();

        guildData = new GuildData();

        NECore.getChatFormat().add(new GuildTag());
        NECore.getScoreboardManager().addScoreboard(new GuildScoreboards());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                User user = getUserManager().getUser(player.getUniqueId());

                if (user.getPower() == 10) {
                    user.setPower(10);
                } else {
                    user.setPower(user.getPower() + 1);
                }
            }
        }, 6000, 6000);

        registerCommands();
        registerListeners();
    }


    public void onDisable() {
    }

    private void registerCommands() {
        this.getCommand("guilds").setExecutor(new GuildCommand());
    }

    private void registerListeners() {
        getPluginManager().registerEvents(new GuildListener(), this);
        getPluginManager().registerEvents(inventoryListener, this);
        getPluginManager().registerEvents(new PlayerListener(), this);
        getPluginManager().registerEvents(new PluginListener(), this);
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static NEGuilds getInstance() {
        return instance;
    }

    public static GuildRegistry getGuildRegistry() {
        return guildRegistry;
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static InventoryListener getInventoryListener() {
        return inventoryListener;
    }

    public static GuildData getGuildData() {
        return guildData;
    }
}
