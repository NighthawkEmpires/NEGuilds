package net.nighthawkempires.guilds.guild;

import net.nighthawkempires.core.NECore;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.data.AbstractGuildsDataRegistry;
import net.nighthawkempires.guilds.data.datasection.DataSection;

import java.util.UUID;

public class GuildRegistry extends AbstractGuildsDataRegistry<GuildModel> {
    private static final boolean SAVE_PRETTY = true;
    private static final String FILE_NAME = "guilds";

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
                    guild.getRelations().remove(uuid);
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
}
