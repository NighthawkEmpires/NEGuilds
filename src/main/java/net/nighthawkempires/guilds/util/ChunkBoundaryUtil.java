package net.nighthawkempires.guilds.util;

import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ChunkBoundaryUtil {

    public static List<UUID> showingBoundaries = Lists.newArrayList();

    public static void startChunkBoundaries(Player player) {
        showingBoundaries.add(player.getUniqueId());
    }

    public static void stopChunkBoundaries(Player player) {
        showingBoundaries.remove(player.getUniqueId());
    }

    public static void showChunkBoundaries(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        Location wall1, wall2, wall3, wall4;
        for (int y = player.getLocation().getBlockY() - 5; y < player.getLocation().getBlockY() + 10; y++) {
            for (int wall = 0; wall < 15; wall++) {
                wall1 = chunk.getBlock(wall, y, 0).getLocation();
                wall2 = chunk.getBlock(15, y, wall).getLocation();
                wall3 = chunk.getBlock(15 - wall, y, 15).getLocation();
                wall4 = chunk.getBlock(0, y, 15 - wall).getLocation();
                if (wall1.getBlock().getType() == Material.AIR) {
                    player.spawnParticle(Particle.DRIP_WATER, wall1, 1);
                }
                if (wall2.getBlock().getType() == Material.AIR) {
                    player.spawnParticle(Particle.DRIP_WATER, wall2, 1);
                }
                if (wall3.getBlock().getType() == Material.AIR) {
                    player.spawnParticle(Particle.DRIP_WATER, wall3, 1);
                }
                if (wall4.getBlock().getType() == Material.AIR) {
                    player.spawnParticle(Particle.DRIP_WATER, wall4, 1);
                }
            }
        }
    }
}
