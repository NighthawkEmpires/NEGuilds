package net.nighthawkempires.guilds.user;

import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.Model;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.guild.GuildRank;
import org.bukkit.Bukkit;

import java.util.*;

public class UserModel implements Model {

    private UUID uuid;
    private String name;
    private UUID guild;
    private GuildRank rank;
    private int power;

    public UserModel(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
        this.power = 10;
    }

    public UserModel(String id, DataSection data) {
        uuid = UUID.fromString(id);
        name = data.getString("name");
        if (data.isString("guild")) {
            guild = UUID.fromString(data.getString("guild"));
        }
        if (data.isString("rank")) {
            rank = GuildRank.valueOf(data.getString("rank"));
        }
        power = data.getInt("power");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        NEGuilds.getUserRegistry().register(this);
    }

    public Optional<GuildModel> getGuild() {
        return NEGuilds.getGuildRegistry().getGuild(guild);
    }

    public void setGuild(GuildModel guild) {
        if (guild == null) {
            throw new NullPointerException("Guild cannot be null.");
        }
        this.guild = guild.getUUID();
        NEGuilds.getUserRegistry().register(this);
    }

    public void setGuild(UUID guildId) {
        if (guildId == null) {
            throw new NullPointerException("Guild UUID cannot be null.");
        }
        this.guild = guildId;
        NEGuilds.getUserRegistry().register(this);
    }

    public void removeGuild() {
        guild = null;
        NEGuilds.getUserRegistry().register(this);
    }

    public GuildRank getRank() {
        return rank;
    }

    public void setRank(GuildRank rank) {
        this.rank = rank;
        NEGuilds.getUserRegistry().register(this);
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
        NEGuilds.getUserRegistry().register(this);
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        if (guild != null) {
            map.put("guild", guild.toString());
        }
        if (rank != null) {
            map.put("rank", rank.name());
        }
        map.put("power", power);
        return map;
    }
}
