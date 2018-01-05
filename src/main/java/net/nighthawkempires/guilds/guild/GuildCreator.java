package net.nighthawkempires.guilds.guild;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileManager;
import net.nighthawkempires.core.file.FileType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class GuildCreator {

    private GuildModel guild;
    private FileManager fileManager;

    public GuildCreator(GuildModel guild) {
        this.guild = guild;
        this.fileManager = NECore.getFileManager();

        if (!fileManager.isFileLoaded(guild.getUUID().toString())) {
            fileManager.loadFile(guild.getUUID().toString(), FileType.GUILD_FILE);
        }
    }

    public void create(String name, UUID leader) {
        getGuildFile().set("name", name);
        getGuildFile().set("members", Lists.newArrayList(leader.toString()));
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
