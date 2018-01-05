package net.nighthawkempires.guilds.user;

import net.nighthawkempires.guilds.guild.Guild;
import net.nighthawkempires.guilds.guild.rank.RankType;

import java.util.UUID;

public class User {

    private UUID uuid;
    private String name;
    private Guild guild;
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

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
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
