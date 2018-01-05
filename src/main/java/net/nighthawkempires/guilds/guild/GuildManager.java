package net.nighthawkempires.guilds.guild;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileType;
import net.nighthawkempires.guilds.NEGuilds;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import static net.nighthawkempires.core.NECore.getFileManager;

public class GuildManager {

    private ConcurrentMap<UUID, Guild> guildMap;

    public GuildManager() {
        guildMap = Maps.newConcurrentMap();
    }

    public ConcurrentMap<UUID, Guild> getGuildMap() {
        return guildMap;
    }

    public List<Guild> getGuilds() {
        List<Guild> guilds = Lists.newArrayList();
        guilds.addAll(getGuildMap().values());
        return guilds;
    }

    public void loadGuilds() {
        try {
            for (File file : NECore.getFileManager().getGuildDirectory().listFiles()) {
                String preuuid = file.getName().replaceAll(".yml", "");

                UUID uuid = UUID.fromString(preuuid);
                loadGuild(new Guild(uuid));
            }
        } catch (NullPointerException ignored) {
        }
    }

    public void saveGuilds() {
        for (Guild guild : getGuilds()) {
            this.saveGuild(guild);
            if (getGuildMap().values().contains(guild)) {
                getGuildMap().remove(guild.getUUID(), guild);
            }
        }

        for (Guild guild : getGuildMap().values()) {
            saveGuild(guild);
        }
    }

    public Guild getGuild(UUID uuid) {
        if (getGuildMap().containsKey(uuid)) {
            return getGuildMap().get(uuid);
        }
        loadGuild(new Guild(uuid));
        return getGuildMap().get(uuid);
    }

    public Guild getGuild(String name) {
        for (Guild guild : getGuilds()) {
            if (guild.getName().toLowerCase().equals(name.toLowerCase())) {
                return guild;
            }
        }
        return null;
    }

    public void createGuild(Guild guild, String name, UUID leader) {
        if (guildExists(guild.getUUID())) return;
        GuildCreator creator = new GuildCreator(guild);
        creator.create(name, leader);
        loadGuild(guild);
    }

    public void loadGuild(Guild guild) {
        if (guildLoaded(guild.getUUID())) return;
        if (!guildExists(guild.getUUID())) return;
        GuildLoader loader = new GuildLoader(guild);
        loader.load();
        getGuildMap().put(guild.getUUID(), guild);
    }

    public void saveGuild(Guild guild) {
        GuildSaver saver = new GuildSaver(guild);
        saver.save();
        getGuildMap().remove(guild.getUUID());
    }

    public void deleteGuild(UUID uuid) {
        NECore.getFileManager().deleteFile(uuid.toString(), FileType.GUILD_FILE);
        for (Guild guild : getGuilds()) {
            if (guild.getRelations().keySet().size() != 0) {
                if (guild.getRelations().keySet().contains(guildMap.get(uuid))) {
                    guild.getRelations().remove(guildMap.get(uuid));
                }
            }
        }
        getGuildMap().remove(uuid);
        NECore.getLoggers().info(NEGuilds.getPlugin(), "Deleted guild: " + uuid.toString() + ".");
        NECore.getFileManager().deleteFile(uuid.toString(), FileType.GUILD_FILE);
    }

    public boolean guildExists(UUID uuid) {
        return NECore.getFileManager().fileExists(uuid.toString(), FileType.GUILD_FILE);
    }

    public boolean guildExists(String name) {
        for (Guild guild : getGuilds()) {
            if (guild.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean guildLoaded(UUID uuid) {
        return getGuildMap().containsKey(uuid);
    }

    public FileConfiguration getGuildFile(UUID uuid) {
        if (!getFileManager().isFileLoaded(uuid.toString())) {
            getFileManager().loadFile(uuid.toString(), FileType.GUILD_FILE);
        }
        return getFileManager().getFile(uuid.toString());
    }

    public void saveGuildFile(UUID uuid) {
        try {
            NECore.getFileManager().saveFile(uuid.toString(), true);
        } catch (Exception e) {

        }
    }
}
