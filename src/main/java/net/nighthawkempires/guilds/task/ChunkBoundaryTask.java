package net.nighthawkempires.guilds.task;

import net.nighthawkempires.guilds.util.ChunkBoundaryUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ChunkBoundaryTask extends BukkitRunnable {

    public void run() {
        for (UUID uuid : ChunkBoundaryUtil.showingBoundaries) {
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                Player player = Bukkit.getPlayer(uuid);
                ChunkBoundaryUtil.showChunkBoundaries(player);
            } else {
                ChunkBoundaryUtil.showingBoundaries.remove(uuid);
            }
        }
    }
}
