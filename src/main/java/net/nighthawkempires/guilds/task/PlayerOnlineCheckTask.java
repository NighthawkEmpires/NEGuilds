package net.nighthawkempires.guilds.task;

import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerOnlineCheckTask implements Runnable {

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());
            if (user.getPower() >= 10) {
                user.setPower(10);
            } else {
                user.setPower(user.getPower() + 1);
            }
        }
    }
}
