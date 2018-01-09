package net.nighthawkempires.guilds.guild;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

public class GuildTempData {

    public List<UUID> adminBypass;

    public GuildTempData() {
        adminBypass = Lists.newArrayList();
    }
}
