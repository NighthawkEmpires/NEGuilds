package net.nighthawkempires.guilds.guild;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.nighthawkempires.core.utils.ChunkUtil;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.data.datasection.AbstractPersistentModel;
import net.nighthawkempires.guilds.data.datasection.DataSection;
import net.nighthawkempires.guilds.guild.relation.RelationType;
import org.bukkit.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class GuildModel extends AbstractPersistentModel<String> {

    private UUID uuid;
    private String name;
    private String description;
    private ChatColor color;
    private Location home;
    private List<UUID> members;
    private List<UUID> invites;
    private List<Chunk> territory;
    private ConcurrentMap<UUID, RelationType> relations;

    public GuildModel(UUID uuid, String name, UUID leader) {
        this.uuid = uuid;
        this.name = name;
        this.description = "";
        this.color = ChatColor.GRAY;
        this.members = Lists.newArrayList(leader);
        this.invites = new ArrayList<>();
        this.territory = new ArrayList<>();
        this.relations = new ConcurrentHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        NEGuilds.getGuildRegistry().register(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        NEGuilds.getGuildRegistry().register(this);
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
        NEGuilds.getGuildRegistry().register(this);
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
        NEGuilds.getGuildRegistry().register(this);
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
        NEGuilds.getGuildRegistry().register(this);
    }

    public List<UUID> getInvites() {
        return invites;
    }

    public void setInvites(List<UUID> invites) {
        this.invites = invites;
        NEGuilds.getGuildRegistry().register(this);
    }

    public List<Chunk> getTerritory() {
        return territory;
    }

    public void setTerritory(List<Chunk> territory) {
        this.territory = territory;
        NEGuilds.getGuildRegistry().register(this);
    }

    public ImmutableMap<UUID, RelationType> getRelations() {
        return ImmutableMap.copyOf(relations);
    }

    public void addRelation(UUID uuid, RelationType type) {
        relations.put(uuid, type);
        NEGuilds.getGuildRegistry().register(this);
    }

    public void removeRelation(UUID uuid) {
        relations.remove(uuid);
        NEGuilds.getGuildRegistry().register(this);
    }

    public void setRelations(Map<UUID, RelationType> relations) {
        this.relations = new ConcurrentHashMap<>();
        for (Map.Entry<UUID, RelationType>  entry : relations.entrySet()) {
            this.relations.put(entry.getKey(), entry.getValue());
        }
        NEGuilds.getGuildRegistry().register(this);
    }

    public boolean isAlly(GuildModel guild) {
        return getRelations().containsKey(guild.getUUID()) && getRelations().get(guild.getUUID()) ==
                RelationType.ALLY && guild.getRelations().containsKey(uuid) && guild.getRelations().get(uuid) ==
                RelationType.ALLY;
    }

    public boolean isTruce(GuildModel guild) {
        return getRelations().containsKey(guild.getUUID()) && getRelations().get(guild.getUUID()) ==
                RelationType.TRUCE && guild.getRelations().containsKey(uuid) && guild.getRelations().get(uuid) ==
                RelationType.TRUCE;
    }

    public boolean isNeutral(GuildModel guild) {
        if (getRelations().containsKey(guild.getUUID()) && getRelations().get(guild.getUUID()) ==
                RelationType.NEUTRAL && guild.getRelations().containsKey(uuid) && guild.getRelations().get(uuid) ==
                RelationType.NEUTRAL) {
            return true;
        } else if (!getRelations().containsKey(guild.getUUID())) {
            return true;
        } else return !isEnemy(guild) && !isTruce(guild) && !isAlly(guild);
    }

    public boolean isEnemy(GuildModel guild) {
        return getRelations().containsKey(guild.getUUID()) && getRelations().get(guild.getUUID()) ==
                RelationType.ENEMY || guild.getRelations().containsKey(uuid) && guild.getRelations().get(uuid) ==
                RelationType.ENEMY;
    }

    public UUID getUUID() {
        return uuid;
    }

    public GuildModel(String id, DataSection data) {
        name = data.getString("name");
        String desc = data.getStringNullable("description");
        if (desc != null) {
            description = data.getStringNullable("description");
        }
        color = ChatColor.valueOf(data.getString("color", "DARK_GRAY"));
        members = data.getStringList("members").stream().map(UUID::fromString).collect(Collectors.toList());
        invites = data.getStringList("invites").stream().map(UUID::fromString).collect(Collectors.toList());

        List<String> chunks = (List) data.getListNullable("territory");
        territory = new ArrayList<>();
        if (chunks != null) {
            for (String chunk : chunks) {
                territory.add(ChunkUtil.getChunk(chunk));
            }
        }

        relations = new ConcurrentHashMap<>();
        Map<String, String> relateMap = (Map) data.getNullable("relations");
        if (relateMap != null) {
            for (Map.Entry<String, String> entry : relateMap.entrySet()) {
                RelationType type = RelationType.valueOf(entry.getValue().toUpperCase());
                relations.put(UUID.fromString(entry.getKey()), type);
            }
        }

        DataSection homeData = data.getSectionNullable("home");
        if (homeData != null) {
            World world = Bukkit.getWorld(homeData.getString("world"));
            double x = homeData.getDouble("cord-x");
            double y = homeData.getDouble("cord-y");
            double z = homeData.getDouble("cord-z");
            float yaw = Float.valueOf(homeData.getString("yaw"));
            float pitch = Float.valueOf(homeData.getString("pitch"));
            home = new Location(world, x, y ,z, yaw, pitch);
        }
    }

    @Override
    public String getPersistentId() {
        return uuid.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", getName());
        if (description != null) {
            map.put("description", getDescription());
        }
        if (color != null) {
            map.put("color", getColor().name());
        }
        map.put("members", getMembers().stream().map(UUID::toString).collect(Collectors.toList()));
        map.put("invites", getInvites().stream().map(UUID::toString).collect(Collectors.toList()));

        List<String> chunks = new ArrayList<>();
        for (Chunk chunk : getTerritory()) {
            chunks.add(ChunkUtil.getChunkString(chunk));
        }
        map.put("territory", chunks);

        Map<String, Object> relateMap = new HashMap<>();
        for (UUID guildId : getRelations().keySet()) {
            relateMap.put(guildId.toString(), getRelations().get(guildId).name());
        }
        map.put("relations", relateMap);

        if (getHome() != null) {
            Location home = getHome();
            Map<String, Object> homeMap = new HashMap<>();
            homeMap.put("world", home.getWorld().getName());
            homeMap.put("cord-x", home.getBlockX());
            homeMap.put("cord-y", home.getBlockY());
            homeMap.put("cord-z", home.getBlockZ());
            homeMap.put("yaw", String.valueOf(home.getYaw()));
            homeMap.put("pitch", String.valueOf(home.getPitch()));
            map.put("home", homeMap);
        }

        return map;
    }
}
