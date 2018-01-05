package net.nighthawkempires.guilds.guild;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileManager;
import net.nighthawkempires.core.file.FileType;
import net.nighthawkempires.core.utils.ChunkUtil;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.relation.RelationType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class GuildLoader {

    private Guild guild;
    private FileManager fileManager;

    public GuildLoader(Guild guild) {
        this.guild = guild;
        this.fileManager = NECore.getFileManager();

        if (!fileManager.isFileLoaded(guild.getUUID().toString())) {
            fileManager.loadFile(guild.getUUID().toString(), FileType.GUILD_FILE);
        }
    }

    public Guild getGuild() {
        return guild;
    }

    public void load() {
        getGuild().setName(getGuildFile().getString("name"));
        getGuild().setDescription(getGuildFile().getString("description", ""));
        getGuild().setColor(ChatColor.valueOf(getGuildFile().getString("color", ChatColor.DARK_GRAY.name()).toUpperCase()));
        List<UUID> members = Lists.newArrayList();
        if (!getGuildFile().getStringList("members").isEmpty()) {
            for (String string : getGuildFile().getStringList("members")) {
                members.add(UUID.fromString(string));
            }
        }
        getGuild().setMembers(members);
        List<UUID> invites = Lists.newArrayList();
        if (getGuildFile().isSet("invites")) {
            if (!getGuildFile().getStringList("invites").isEmpty()) {
                for (String string : getGuildFile().getStringList("invites")) {
                    invites.add(UUID.fromString(string));
                }
            }
        }
        getGuild().setInvites(invites);
        List<Chunk> territory = Lists.newArrayList();
        if (getGuildFile().isSet("territory")) {
            if (!getGuildFile().getStringList("territory").isEmpty()) {
                for (String string : getGuildFile().getStringList("territory")) {
                    territory.add(ChunkUtil.getChunk(string));
                }
            }
        }
        getGuild().setTerritory(territory);
        ConcurrentMap<Guild, RelationType> relations = Maps.newConcurrentMap();
        getGuild().setRelations(relations);
        if (getGuildFile().isSet("relations")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(NEGuilds.getPlugin(), () -> {
                for (String string : getGuildFile().getConfigurationSection("relations").getKeys(false)) {
                    UUID uuid = UUID.fromString(string);
                    if (!NEGuilds.getGuildManager().guildExists(uuid)) {
                        return;
                    }
                    Guild guild = NEGuilds.getGuildManager().getGuild(uuid);
                    RelationType type = RelationType.valueOf(getGuildFile().getString("relations." + string).toUpperCase());
                    this.getGuild().getRelations().put(guild, type);
                }
            }, 100);
        }

        if (getGuildFile().isSet("home")) {
            ConfigurationSection section = getGuildFile().getConfigurationSection("home");
            String world = section.getString("world");
            int x = section.getInt("cord-x"), y = section.getInt("cord-y"), z = section.getInt("cord-z");
            float yaw = Float.parseFloat(section.getString("yaw")), pitch = Float.parseFloat(section.getString("pitch"));
            Location home = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            getGuild().setHome(home);
        } else {
            getGuild().setHome(null);
        }
    }

    private FileConfiguration getGuildFile() {
        if (!fileManager.isFileLoaded(guild.getUUID().toString())) {
            fileManager.loadFile(guild.getUUID().toString(), FileType.GUILD_FILE);
        }
        return fileManager.getFile(guild.getUUID().toString());
    }

    public void saveGuildFile(boolean clear) {
        fileManager.saveFile(guild.getUUID().toString(), clear);
    }
}
