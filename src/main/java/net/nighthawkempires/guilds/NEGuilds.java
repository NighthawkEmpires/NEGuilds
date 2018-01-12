package net.nighthawkempires.guilds;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileDirectory;
import net.nighthawkempires.core.server.Server;
import net.nighthawkempires.guilds.command.GuildCommand;
import net.nighthawkempires.guilds.guild.GuildTag;
import net.nighthawkempires.guilds.guild.GuildTempData;
import net.nighthawkempires.guilds.guild.registry.*;
import net.nighthawkempires.guilds.listener.*;
import net.nighthawkempires.guilds.scoreboard.GuildScoreboards;
import net.nighthawkempires.guilds.task.ChunkBoundaryTask;
import net.nighthawkempires.guilds.task.PlayerOnlineCheckTask;
import net.nighthawkempires.guilds.user.registry.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NEGuilds extends JavaPlugin {

    private static Plugin plugin;

    private static MongoDatabase mongoDatabase;
    private static UserRegistry userRegistry;
    private static GuildRegistry guildRegistry;

    private static PluginManager pluginManager;

    private static InventoryListener inventoryListener;

    private static GuildTempData guildData;

    private static ChunkBoundaryTask boundaryTask;

    public void onEnable() {
        if (NECore.getSettings().server != Server.SUR) {
            NECore.getLoggers().warn(this, "This plugin can only be ran on the Survival Server.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;

        if (NECore.getSettings().mongoEnabledGuilds) {
            try {
                String hostname = NECore.getSettings().mongoHostnameGuilds;
                String username = NECore.getSettings().mongoUsernameGuilds;
                String password = NECore.getSettings().mongoPasswordGuilds;
                ServerAddress address = new ServerAddress(hostname, 27017);
                MongoCredential credential =
                        MongoCredential.createCredential(username, "ne_guilds", password.toCharArray());
                mongoDatabase =
                        new MongoClient(address, credential, new MongoClientOptions.Builder().build())
                                .getDatabase("ne_guilds");
                userRegistry = new MUserRegistry(mongoDatabase);
                guildRegistry = new MGuildRegistry(mongoDatabase);
                NECore.getLoggers().info("MongoDB enabled.");
            } catch (Exception oops) {
                oops.printStackTrace();
                NECore.getLoggers().warn("MongoDB connection failed. Disabling plugin.");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }

        if (!NECore.getSettings().mongoEnabledGuilds) {
            userRegistry = new FUserRegistry(FileDirectory.GUILD_DIRECTORY.getDirectory().getPath());
            guildRegistry = new FGuildRegistry(FileDirectory.GUILD_DIRECTORY.getDirectory().getPath());
            NECore.getLoggers().info("Json file saving enabled.");
        }

        pluginManager = Bukkit.getPluginManager();

        inventoryListener = new InventoryListener();

        guildData = new GuildTempData();

        boundaryTask = new ChunkBoundaryTask();

        NECore.getChatFormat().add(new GuildTag());
        NECore.getScoreboardManager().addScoreboard(new GuildScoreboards());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PlayerOnlineCheckTask(), 240, 240);

        BukkitRunnable runnable = boundaryTask;
        runnable.runTaskTimer(this, 0, 10);

        registerCommands();
        registerListeners();

        userRegistry.loadAllFromDb();
        guildRegistry.loadAllFromDb();
    }


    public void onDisable() {
        // NOTHING
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

    public static UserRegistry getUserRegistry() {
        return userRegistry;
    }

    public static GuildRegistry getGuildRegistry() {
        return guildRegistry;
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static InventoryListener getInventoryListener() {
        return inventoryListener;
    }

    public static GuildTempData getGuildTempData() {
        return guildData;
    }
}
