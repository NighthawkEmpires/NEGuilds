package net.nighthawkempires.guilds.listener;

import net.nighthawkempires.core.utils.MathUtil;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

import static net.nighthawkempires.guilds.NEGuilds.getUserManager;


public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (getUserManager().getUserMap().containsKey(player.getUniqueId())) {
            getUserManager().getUserMap().remove(player.getUniqueId());
        }

        User user = new User(player.getUniqueId());

        if (!getUserManager().userExists(player.getUniqueId())) {
            getUserManager().createUser(user);
        } else {
            getUserManager().loadUser(user);
        }

        user = getUserManager().getUser(player.getUniqueId());

        Optional<GuildModel> opGuild = user.getGuild();
        if (opGuild.isPresent()) {
            if (!opGuild.get().getMembers().contains(player.getUniqueId())) {
                opGuild.get().addMember(player.getUniqueId());
            }
        }

        if (MathUtil.greaterThan(user.getPower(), 10)) {
            user.setPower(10);
        } else if (MathUtil.lessThan(user.getPower(), 0)) {
            user.setPower(0);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user =
                (getUserManager().userLoaded(player.getUniqueId()) ? getUserManager().getUser(player.getUniqueId()) :
                        getUserManager().getTempUser(player.getUniqueId()));

        getUserManager().saveUser(user);
        if (getUserManager().getUserMap().containsKey(player.getUniqueId())) {
            getUserManager().getUserMap().remove(player.getUniqueId());
        }
    }
}
