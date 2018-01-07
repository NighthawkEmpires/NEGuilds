package net.nighthawkempires.guilds.guild;

import com.google.common.collect.ImmutableList;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.data.AbstractGuildsDataRegistry;
import net.nighthawkempires.guilds.data.datasection.DataSection;

import java.util.*;

public class GuildRegistry extends AbstractGuildsDataRegistry<GuildModel> {
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

    public GuildRegistry() {
        super(SAVE_PRETTY);
    }

    @Override
    protected GuildModel valueFromData(String stringKey, DataSection data) {
        return new GuildModel(stringKey, data);
    }

    @Override
    protected String getName() {
        return FILE_NAME;
    }

    public GuildModel getGuild(UUID uuid) {
        return fromId(uuid.toString());
    }

    public GuildModel getGuild(String name) {
        for (GuildModel guild : getRegistered()) {
            if (guild.getName().toLowerCase().equals(name.toLowerCase())) {
                return guild;
            }
        }
        return null;
    }

    public UUID createGuild(String name, UUID leader) {
        UUID id = UUID.randomUUID();
        GuildModel model = new GuildModel(id, name, leader);
        register(model);
        return id;
    }

    public void deleteGuild(UUID uuid) {
        for (GuildModel guild : getRegistered()) {
            if (guild.getRelations().keySet().size() != 0) {
                if (guild.getRelations().keySet().contains(uuid)) {
                    guild.removeRelation(uuid);
                }
            }
        }
        unregister(fromId(uuid.toString()));
        NECore.getLoggers().info(NEGuilds.getPlugin(), "Deleted guild: " + uuid.toString() + ".");
    }

    public boolean guildExists(UUID uuid) {
        return fromId(uuid.toString()) != null;
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
