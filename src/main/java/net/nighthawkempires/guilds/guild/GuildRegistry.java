package net.nighthawkempires.guilds.guild;

import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.Registry;
import com.google.common.collect.ImmutableList;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.utils.ChunkUtil;
import net.nighthawkempires.guilds.NEGuilds;
import org.bukkit.Chunk;

import java.util.*;

public interface GuildRegistry extends Registry<GuildModel> {
    String NAME = "guilds";

    default GuildModel fromDataSection(String stringKey, DataSection data) {
        return new GuildModel(stringKey, data);
    }

    default Optional<GuildModel> getGuild(UUID uuid) {
        if (uuid == null) {
            return Optional.empty();
        }
        return fromKey(uuid.toString());
    }

    default Optional<GuildModel> getGuild(String name) {
        for (GuildModel guild : getRegisteredData().values()) {
            if (guild.getName().toLowerCase().equals(name.toLowerCase())) {
                return Optional.of(guild);
            }
        }
        return Optional.empty();
    }

    default Optional<GuildModel> getGuild(Chunk chunk) {
        if (!chunk.isLoaded()) {
            chunk.load();
        }
        for (GuildModel guildModel : getRegisteredData().values()) {
            if (guildModel.getTerritory().contains(ChunkUtil.getChunkString(chunk))) {
                return Optional.of(guildModel);
            }
        }
        return Optional.empty();
    }

    @Deprecated
    default ImmutableList<GuildModel> getGuilds() {
        if (getRegisteredData().size() > 0) {
            return ImmutableList.copyOf(getRegisteredData().values());
        }
        return ImmutableList.of();
    }

    default UUID createGuild(String name, UUID leader) {
        UUID id = UUID.randomUUID();
        GuildModel model = new GuildModel(id, name, leader);
        register(model);
        return id;
    }

    default void deleteGuild(UUID uuid) {
        for (GuildModel guild : getRegisteredData().values()) {
            if (guild.getRelations().keySet().size() != 0) {
                if (guild.getRelations().keySet().contains(uuid)) {
                    guild.removeRelation(uuid);
                }
            }
        }
        remove(uuid.toString());
        NECore.getLoggers().info(NEGuilds.getPlugin(), "Deleted guild: " + uuid.toString() + ".");
    }

    @Deprecated
    Map<String, GuildModel> getRegisteredData();

    default boolean guildExists(UUID uuid) {
        return fromKey(uuid.toString()).isPresent();
    }

    default boolean guildExists(String name) {
        return getGuild(name).isPresent();
    }

    default boolean containsBannedWord(String str) {
        /*for (String banned : BANNED_WORDS) {
            if (str.toLowerCase().contains(banned.toLowerCase())) {
                return true;
            }
        }*/

        // TODO NECore setting
        return false;
    }
}
