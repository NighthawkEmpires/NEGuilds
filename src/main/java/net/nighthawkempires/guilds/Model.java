package net.nighthawkempires.guilds;

import java.util.Map;

public interface Model {
    default NEGuilds getNEGuilds() {
        return NEGuilds.instance;
    }

    String getKey();

    Map<String, Object> serialize();
}
