package net.nighthawkempires.guilds.guild.registry;

import com.demigodsrpg.util.datasection.AbstractMongoRegistry;
import com.mongodb.client.MongoDatabase;
import net.nighthawkempires.guilds.guild.GuildModel;

import java.util.Map;

public class MGuildRegistry extends AbstractMongoRegistry<GuildModel> implements GuildRegistry {

    public MGuildRegistry(MongoDatabase database) {
        super(database.getCollection(NAME), 0);
    }

    @Override
    public Map<String, GuildModel> getRegisteredData() {
        return REGISTERED_DATA.asMap();
    }
}
