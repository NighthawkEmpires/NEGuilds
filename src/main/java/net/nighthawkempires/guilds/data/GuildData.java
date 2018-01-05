package net.nighthawkempires.guilds.data;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

public class GuildData {

    public List<UUID> adminBypass;

    public GuildData() {
        adminBypass = Lists.newArrayList();
    }
}
