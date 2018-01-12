package net.nighthawkempires.guilds;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileDirectory;
import net.nighthawkempires.core.server.Server;
import net.nighthawkempires.guilds.command.GuildCommand;
import net.nighthawkempires.guilds.guild.*;
import net.nighthawkempires.guilds.listener.*;
import net.nighthawkempires.guilds.scoreboard.GuildScoreboards;
import net.nighthawkempires.guilds.task.ChunkBoundaryTask;
import net.nighthawkempires.guilds.user.User;
import net.nighthawkempires.guilds.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NEGuilds extends JavaPlugin {

    private static Plugin plugin;

    private static MongoDatabase mongoDatabase;
    private static GuildRegistry guildRegistry;

    private static PluginManager pluginManager;
    private static UserManager userManager;

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
            String hostname = NECore.getSettings().mongoHostnameGuilds;
            String username = NECore.getSettings().mongoUsernameGuilds;
            String password = NECore.getSettings().mongoPasswordGuilds;
            ServerAddress address = new ServerAddress(hostname, 27017);
            MongoCredential credential =
                    MongoCredential.createCredential(username, "ne_guilds", password.toCharArray());
            mongoDatabase =
                    new MongoClient(address, credential, new MongoClientOptions.Builder().build())
                            .getDatabase("ne_guilds");
            guildRegistry = new MGuildRegistry(mongoDatabase, 0);
            NECore.getLoggers().info("MongoDB enabled.");
        } else {
            guildRegistry = new FGuildRegistry(FileDirectory.GUILD_DIRECTORY.getDirectory().getPath());
            NECore.getLoggers().info("Json file saving enabled.");
        }

        pluginManager = Bukkit.getPluginManager();
        userManager = new UserManager();

        inventoryListener = new InventoryListener();

        guildData = new GuildTempData();

        boundaryTask = new ChunkBoundaryTask();

        NECore.getChatFormat().add(new GuildTag());
        NECore.getScoreboardManager().addScoreboard(new GuildScoreboards());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                User user = getUserManager().getUser(player.getUniqueId());

                if (user.getPower() >= 10) {
                    user.setPower(10);
                } else {
                    user.setPower(user.getPower() + 1);
                }
            }
        }, 6000, 6000);

        BukkitRunnable runnable = boundaryTask;
        runnable.runTaskTimer(this, 0, 10);

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

    public static UserManager getUserManager() {
        return userManager;
    }

    public static InventoryListener getInventoryListener() {
        return inventoryListener;
    }

    public static GuildTempData getGuildTempData() {
        return guildData;
    }
}
