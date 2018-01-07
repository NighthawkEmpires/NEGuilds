package net.nighthawkempires.guilds.user;

import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.guild.rank.RankType;

import java.util.Optional;
import java.util.UUID;

public class User {

    private UUID uuid;
    private String name;
    UUID guild;
    private RankType type;
    private int power;

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<GuildModel> getGuild() {
        return NEGuilds.getGuildRegistry().getGuild(guild);
    }

    public void setGuild(GuildModel guild) {
        if (guild == null) {
            throw new NullPointerException("Guild cannot be null.");
        }
        this.guild = guild.getUUID();
    }

    public void setGuild(UUID guildId) {
        if (guildId == null) {
            throw new NullPointerException("Guild UUID cannot be null.");
        }
        this.guild = guildId;
    }

    public void removeGuild() {
        guild = null;
    }

    public RankType getType() {
        return type;
    }

    public void setType(RankType type) {
        this.type = type;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public UUID getUUID() {
        return uuid;
    }
}
