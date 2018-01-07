package net.nighthawkempires.guilds.guild;

import com.demigodsrpg.util.datasection.DataSection;
import com.google.common.collect.ImmutableList;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.data.AbstractRegistry;
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

    public GuildRegistry(NEGuilds backend) {
        super(backend, FILE_NAME, SAVE_PRETTY);
    }

    @Override
    protected GuildModel fromDataSection(String stringKey, DataSection data) {
        return new GuildModel(stringKey, data);
    }

    public Optional<GuildModel> getGuild(UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("Cannot find GuildModel with a null UUID.");
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
        for (GuildModel guild : REGISTERED_DATA.asMap().values()) {
            if (guild.getTerritory().contains(chunk)) {
                return Optional.of(guild);
            }
        }
        return Optional.empty();
    }

    @Deprecated
    public ImmutableList<GuildModel> getGuilds() {
        return ImmutableList.copyOf(REGISTERED_DATA.asMap().values());
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
        return fromKey(uuid.toString()) != null;
    }

    public boolean guildExists(String name) {
        return getGuild(name) != null;
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
