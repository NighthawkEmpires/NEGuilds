package net.nighthawkempires.guilds;

import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.server.Server;
import net.nighthawkempires.guilds.command.GuildCommand;
import net.nighthawkempires.guilds.data.GuildData;
import net.nighthawkempires.guilds.guild.GuildRegistry;
import net.nighthawkempires.guilds.guild.GuildTag;
import net.nighthawkempires.guilds.listener.*;
import net.nighthawkempires.guilds.scoreboard.GuildScoreboards;
import net.nighthawkempires.guilds.user.User;
import net.nighthawkempires.guilds.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.mapmanager.MapManagerPlugin;
import org.inventivetalent.mapmanager.manager.MapManager;

public class NEGuilds extends JavaPlugin {

    private static Plugin plugin;
    static NEGuilds instance;

    private static GuildRegistry guildRegistry;
    private static PluginManager pluginManager;
    private static UserManager userManager;
    private static MapManager mapManager;

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

        guildRegistry = new GuildRegistry(NECore.getFileManager().getGuildDirectory().getPath());
        pluginManager = Bukkit.getPluginManager();
        userManager = new UserManager();
        mapManager = ((MapManagerPlugin) Bukkit.getPluginManager().getPlugin("MapManager")).getMapManager();

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

        guildRegistry.loadAllFromDb();

        for (Player player : Bukkit.getOnlinePlayers()) {
            getUserManager().loadUser(new User(player.getUniqueId()));
        }
    }


    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            NEGuilds.getUserManager().saveUser(NEGuilds.getUserManager().getUser(player.getUniqueId()));
        }
    }

    private void registerCommands() {
        this.getCommand("guilds").setExecutor(new GuildCommand());
    }

    private void registerListeners() {
        getPluginManager().registerEvents(new GuildListener(), this);
        getPluginManager().registerEvents(inventoryListener, this);
        getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static GuildRegistry getGuildRegistry() {
        return guildRegistry;
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static MapManager getMapManager() {
        return mapManager;
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
