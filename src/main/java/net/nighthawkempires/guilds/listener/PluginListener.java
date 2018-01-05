package net.nighthawkempires.guilds.listener;

import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import static net.nighthawkempires.guilds.NEGuilds.*;

public class PluginListener implements Listener {

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        if (event.getPlugin().equals(getPlugin())) {
            NEGuilds.getGuildManager().loadGuilds();

            for (Player player : Bukkit.getOnlinePlayers()) {
                getUserManager().loadUser(new User(player.getUniqueId()));
            }
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(NEGuilds.getPlugin())) {
            NEGuilds.getGuildManager().saveGuilds();

            for (Player player : Bukkit.getOnlinePlayers()) {
                NEGuilds.getUserManager().saveUser(NEGuilds.getUserManager().getUser(player.getUniqueId()));
            }
        }
    }
}
