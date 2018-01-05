package net.nighthawkempires.guilds.guild;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileManager;
import net.nighthawkempires.core.file.FileType;
import net.nighthawkempires.core.utils.ChunkUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.UUID;

public class GuildSaver {

    private Guild guild;
    private FileManager fileManager;

    public GuildSaver(Guild guild) {
        this.guild = guild;
        this.fileManager = NECore.getFileManager();

        if (!fileManager.isFileLoaded(guild.getUUID().toString())) {
            fileManager.loadFile(guild.getUUID().toString(), FileType.GUILD_FILE);
        }
    }

    public Guild getGuild() {
        return guild;
    }

    public void save() {
        getGuildFile().set("name", guild.getName());
        getGuildFile().set("description", guild.getDescription());
        getGuildFile().set("color", guild.getColor().name());
        List<String> members = Lists.newArrayList();
        for (UUID uuid : guild.getMembers()) {
            members.add(uuid.toString());
        }
        getGuildFile().set("members", members);
        List<String> invites = Lists.newArrayList();
        for (UUID uuid : guild.getInvites()) {
            invites.add(uuid.toString());
        }
        getGuildFile().set("invites", invites);
        List<String> chunks = Lists.newArrayList();
        for (Chunk chunk : guild.getTerritory()) {
            chunks.add(ChunkUtil.getChunkString(chunk));
        }
        getGuildFile().set("territory", chunks);
        getGuildFile().set("relations", null);
        if (getGuild().getRelations().size() != 0) {
            for (Guild guild : getGuild().getRelations().keySet()) {
                getGuildFile().set("relations." + guild.getUUID(), getGuild().getRelations().get(guild).name());
            }
        }
        if (getGuild().getHome() != null) {
            Location home = getGuild().getHome();
            getGuildFile().set("home.world", home.getWorld().getName());
            getGuildFile().set("home.cord-x", home.getBlockX());
            getGuildFile().set("home.cord-y", home.getBlockY());
            getGuildFile().set("home.cord-z", home.getBlockZ());
            getGuildFile().set("home.yaw", String.valueOf(home.getYaw()));
            getGuildFile().set("home.pitch", String.valueOf(home.getPitch()));
        } else {
            getGuildFile().set("home", null);
        }
        saveGuildFile(true);
    }

    public FileConfiguration getGuildFile() {
        if (!fileManager.isFileLoaded(guild.getUUID().toString())) {
            fileManager.loadFile(guild.getUUID().toString(), FileType.GUILD_FILE);
        }
        return fileManager.getFile(guild.getUUID().toString());
    }

    public void saveGuildFile(boolean clear) {
        fileManager.saveFile(guild.getUUID().toString(), clear);
    }
}
