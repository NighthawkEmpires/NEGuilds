package net.nighthawkempires.guilds.guild;

import net.nighthawkempires.guilds.guild.relation.RelationType;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class Guild {

    private UUID uuid;
    private String name;
    private String description;
    private ChatColor color;
    private Location home;
    private List<UUID> members;
    private List<UUID> invites;
    private List<Chunk> territory;
    private ConcurrentMap<Guild, RelationType> relations;

    public Guild(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public List<UUID> getInvites() {
        return invites;
    }

    public void setInvites(List<UUID> invites) {
        this.invites = invites;
    }

    public List<Chunk> getTerritory() {
        return territory;
    }

    public void setTerritory(List<Chunk> territory) {
        this.territory = territory;
    }

    public ConcurrentMap<Guild, RelationType> getRelations() {
        return relations;
    }

    public void setRelations(ConcurrentMap<Guild, RelationType> relations) {
        this.relations = relations;
    }

    public boolean isAlly(Guild guild) {
        return getRelations().containsKey(guild) && getRelations().get(guild) == RelationType.ALLY && guild.getRelations().containsKey(this) && guild.getRelations().get(this) == RelationType.ALLY;
    }

    public boolean isTruce(Guild guild) {
        return getRelations().containsKey(guild) && getRelations().get(guild) == RelationType.TRUCE && guild.getRelations().containsKey(this) && guild.getRelations().get(this) == RelationType.TRUCE;
    }

    public boolean isNeutral(Guild guild) {
        if (getRelations().containsKey(guild) && getRelations().get(guild) == RelationType.NEUTRAL && guild.getRelations().containsKey(this) && guild.getRelations().get(this) == RelationType.NEUTRAL) {
            return true;
        } else if (!getRelations().containsKey(guild)) {
            return true;
        } else return !isEnemy(guild) && !isTruce(guild) && !isAlly(guild);
    }

    public boolean isEnemy(Guild guild) {
        return getRelations().containsKey(guild) && getRelations().get(guild) == RelationType.ENEMY || guild.getRelations().containsKey(this) && guild.getRelations().get(this) == RelationType.ENEMY;
    }

    public UUID getUUID() {
        return uuid;
    }
}
