package net.nighthawkempires.guilds.guild;

import com.demigodsrpg.util.datasection.AbstractFileRegistry;

import java.util.Map;

public class FGuildRegistry extends AbstractFileRegistry<GuildModel> implements GuildRegistry {
    private static final boolean SAVE_PRETTY = true;

    public FGuildRegistry(String path) {
        super(path, NAME, SAVE_PRETTY, 0);
    }

    @Override
    public Map<String, GuildModel> getRegisteredData() {
        return REGISTERED_DATA.asMap();
    }
}
