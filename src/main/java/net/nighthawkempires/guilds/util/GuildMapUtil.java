package net.nighthawkempires.guilds.util;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.GuildModel;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class GuildMapUtil {

    public static void sendGuildMap(Player player) {
        StringBuilder builder = new StringBuilder();
        List<GuildModel> guilds = Lists.newArrayList();
        int lines = 0;
        for (int z = player.getLocation().getChunk().getZ() - 3; z <= player.getLocation().getChunk().getZ() + 3; z++) {
            if (lines == 0) {
                builder.append(ChatColor.translateAlternateColorCodes('&', "&8___&7\\&6N&7/&8___"));
            } else if (lines == 1) {
                builder.append(ChatColor.translateAlternateColorCodes('&', "&8___&6W&7+&6E&8___"));
            } else if (lines == 2) {
                builder.append(ChatColor.translateAlternateColorCodes('&', "&8___&7/&6S&7\\&8___"));
            } else {
                builder.append(ChatColor.DARK_GRAY).append("_________");
            }
            for (int x = player.getLocation().getChunk().getX() - 15; x <= player.getLocation().getChunk().getX() + 15; x++) {
                String symbol = ChatColor.GRAY + "-";
                Chunk chunk = player.getWorld().getChunkAt(x, z);

                Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
                if (opGuild.isPresent()) {
                    GuildModel guild = opGuild.get();
                    if (!guilds.contains(guild)) {
                        guilds.add(guild);
                    }
                    symbol = guild.getColor() + guild.getName().substring(0, 1).toUpperCase();
                }

                if (player.getLocation().getChunk() == chunk) {
                    symbol = ChatColor.WHITE + "*";
                }

                builder.append(symbol);
            }
            builder.append(ChatColor.DARK_GRAY).append("__________\n");
            lines++;
        }

        player.sendMessage(Lang.HEADER.getServerHeader());
        player.sendMessage(ChatColor.DARK_GRAY + "Map" + ChatColor.GRAY + ": Guilds");
        player.sendMessage(Lang.FOOTER.getMessage());
        player.sendMessage(builder.toString());
        player.sendMessage(Lang.FOOTER.getMessage());

        StringBuilder guildBuilder = new StringBuilder();
        guildBuilder.append(ChatColor.WHITE).append("*").append(ChatColor.DARK_GRAY).append(" - ").append(ChatColor.WHITE).append("You").append(ChatColor.DARK_GRAY).append(", ");
        if (!guilds.isEmpty()) {
            for (GuildModel guildModel : guilds) {
                guildBuilder.append(guildModel.getColor()).append(guildModel.getName().substring(0, 1).toUpperCase()).append(ChatColor.DARK_GRAY).append(
                        " - ").append(guildModel.getColor()).append(guildModel.getName()).append(ChatColor.DARK_GRAY).append(", ");
            }
        }
        player.sendMessage(ChatColor.DARK_GRAY + "Keys" + ChatColor.GRAY + ": " + guildBuilder.toString().substring(0, guildBuilder.length() - 2));
        player.sendMessage(Lang.FOOTER.getMessage());
    }
}
