package net.nighthawkempires.guilds.guild;

import com.demigodsrpg.util.datasection.AbstractMongoRegistry;
import com.mongodb.client.MongoDatabase;

import java.util.Map;

public class MGuildRegistry extends AbstractMongoRegistry<GuildModel> implements GuildRegistry {

    public MGuildRegistry(MongoDatabase database, int expireInMins) {
        super(database.getCollection(NAME), expireInMins);
    }

    @Override
    public Map<String, GuildModel> getRegisteredData() {
        return REGISTERED_DATA.asMap();
    }
}
