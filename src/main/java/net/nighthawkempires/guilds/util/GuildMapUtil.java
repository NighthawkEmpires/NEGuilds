package net.nighthawkempires.guilds.util;

import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.GuildModel;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.inventivetalent.mapmanager.controller.MapController;
import org.inventivetalent.mapmanager.wrapper.MapWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class GuildMapUtil {

    private static Chunk topLeft;
    private static Chunk center;

    public static void sendGuildMap0(Player player) {
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

    public static void sendGuildMap(Player player) {
        MapWrapper mapWrapper = NEGuilds.getMapManager().wrapImage(getGuildMapImage(player));
        MapController mapController = mapWrapper.getController();

        mapController.addViewer(player);
        mapController.sendContent(player);

        mapController.showInHand(player);
    }

    private static BufferedImage getGuildMapImage(Player player) {
        // Create the image
        BufferedImage guildMap = new BufferedImage(120, 120, BufferedImage.TYPE_INT_RGB);

        // Decide the the min and max
        int minX = player.getLocation().getChunk().getX() - 14;
        int maxX = player.getLocation().getChunk().getX() + 15;
        int minZ = player.getLocation().getChunk().getZ() - 14;
        int maxZ = player.getLocation().getChunk().getZ() + 15;
        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                // Color to draw
                Color color = Color.gray;

                // Get guild info at location
                Chunk chunk = player.getWorld().getChunkAt(x, z);
                Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
                if (opGuild.isPresent()) {
                    GuildModel guild = opGuild.get();
                    // TODO symbol = guild.getColor() + guild.getName().substring(0, 1).toUpperCase();
                    color = ColorUtil.getColor(guild.getColor());
                }

                // Draw slot
                int drawX = x * 4;
                int drawZ = z * 4;
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        guildMap.setRGB(drawX + i, drawZ + j, color.getRGB());
                    }
                }
            }
        }

        return guildMap;
    }
}
