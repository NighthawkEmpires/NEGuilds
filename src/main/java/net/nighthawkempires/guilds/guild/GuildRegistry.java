package net.nighthawkempires.guilds.guild;

import com.demigodsrpg.util.datasection.AbstractRegistry;
import com.demigodsrpg.util.datasection.DataSection;
import com.google.common.collect.ImmutableList;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.utils.ChunkUtil;
import net.nighthawkempires.guilds.NEGuilds;
import org.bukkit.Chunk;

import java.util.*;

public class GuildRegistry extends AbstractRegistry<GuildModel> {
    private static final boolean SAVE_PRETTY = true;
    private static final String FILE_NAME = "guilds";
    private static final ImmutableList<String> BANNED_WORDS;

    static {
        // Manual Blacklist
        List<String> banned = new ArrayList<>();
        banned.add("Pussy");
        banned.add("Cock");
        banned.add("Fuck");
        banned.add("Shit");
        banned.add("Ass");
        banned.add("Dick");
        banned.add("Penis");
        banned.add("Vagina");
        banned.add("Cunt");
        banned.add("Bitch");
        banned.add("Nigger");
        banned.add("Phil");
        banned.add("Staff");
        banned.add("Server");
        banned.add("Console");
        banned.add("Disowned");
        BANNED_WORDS = ImmutableList.copyOf(banned);
    }

    public GuildRegistry(String path) {
        super(path, FILE_NAME, SAVE_PRETTY, 0);
    }

    @Override
    protected GuildModel fromDataSection(String stringKey, DataSection data) {
        return new GuildModel(stringKey, data);
    }

    public Optional<GuildModel> getGuild(UUID uuid) {
        if (uuid == null) {
            return Optional.empty();
        }
        return fromKey(uuid.toString());
    }

    public Optional<GuildModel> getGuild(String name) {
        for (GuildModel guild : REGISTERED_DATA.asMap().values()) {
            if (guild.getName().toLowerCase().equals(name.toLowerCase())) {
                return Optional.of(guild);
            }
        }
        return Optional.empty();
    }

    public Optional<GuildModel> getGuild(Chunk chunk) {
        if (!chunk.isLoaded()) {
            chunk.load();
        }
        for (GuildModel guildModel : REGISTERED_DATA.asMap().values()) {
            if (guildModel.getTerritory().contains(ChunkUtil.getChunkString(chunk))) {
                return Optional.of(guildModel);
            }
        }
        return Optional.empty();
    }

    @Deprecated
    public ImmutableList<GuildModel> getGuilds() {
        if (REGISTERED_DATA.size() > 0) {
            return ImmutableList.copyOf(REGISTERED_DATA.asMap().values());
        }
        return ImmutableList.of();
    }

    public UUID createGuild(String name, UUID leader) {
        UUID id = UUID.randomUUID();
        GuildModel model = new GuildModel(id, name, leader);
        register(model);
        return id;
    }

    public void deleteGuild(UUID uuid) {
        for (GuildModel guild : REGISTERED_DATA.asMap().values()) {
            if (guild.getRelations().keySet().size() != 0) {
                if (guild.getRelations().keySet().contains(uuid)) {
                    guild.removeRelation(uuid);
                }
            }
        }
        remove(uuid.toString());
        NECore.getLoggers().info(NEGuilds.getPlugin(), "Deleted guild: " + uuid.toString() + ".");
    }

    public boolean guildExists(UUID uuid) {
        return fromKey(uuid.toString()).isPresent();
    }

    public boolean guildExists(String name) {
        return getGuild(name).isPresent();
    }

    public ImmutableList<String> getBannedWords() {
        return BANNED_WORDS;
    }

    public boolean containsBannedWord(String str) {
        for (String banned : BANNED_WORDS) {
            if (str.toLowerCase().contains(banned.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
