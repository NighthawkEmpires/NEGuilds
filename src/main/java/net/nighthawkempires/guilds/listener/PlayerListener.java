package net.nighthawkempires.guilds.listener;

import net.nighthawkempires.core.utils.MathUtil;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.user.UserModel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

import static net.nighthawkempires.guilds.NEGuilds.getUserRegistry;


public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserModel user = getUserRegistry().getUser(player.getUniqueId());

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
}
