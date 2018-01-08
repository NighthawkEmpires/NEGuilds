package net.nighthawkempires.guilds.util;

import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.GuildModel;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GuildMapUtil {

    private final static int IMAGE_SIZE = 2_048;

    private static Chunk topLeft;
    private static Chunk center;

    public static void sendGuildMap(Player player) {
        StringBuilder builder = new StringBuilder();
        int lineCounter = 0;
        int lines = 0;
        int z = player.getLocation().getChunk().getZ() - 4;
        for (int x = player.getLocation().getChunk().getX() - 24; x <= player.getLocation().getChunk().getX() + 24; x++) {
            if (lineCounter == 49) {
                lineCounter = 0;
                builder.append("\n");
                z = z + 1;
            }
            String symbol = ChatColor.GRAY + "-";
            Chunk chunk = player.getWorld().getChunkAt(x, z);
            Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
            if (opGuild.isPresent()) {
                GuildModel guild = opGuild.get();
                symbol = guild.getColor() + guild.getName().substring(0, 1).toUpperCase();
            }

            if (player.getLocation().getChunk() == chunk) {
                symbol = ChatColor.DARK_GRAY + "Y";
                center = chunk;
            }

            if (center != null) {
                int topX = center.getX() - 24;
                int topZ = center.getZ() - 4;
                if (chunk.getX() == topX && chunk.getZ() == topZ) {
                    topLeft = chunk;
                }
            }

            builder.append(symbol);
            lineCounter++;
            lines++;
            if (lines == 10) {
                break;
            }
        }

        player.sendMessage(Lang.HEADER.getServerHeader());
        player.sendMessage(ChatColor.DARK_GRAY + "Map" + ChatColor.GRAY + ": Guilds");
        player.sendMessage(Lang.FOOTER.getMessage());
        player.sendMessage(builder.toString());
        player.sendMessage(Lang.FOOTER.getMessage());
    }
}
