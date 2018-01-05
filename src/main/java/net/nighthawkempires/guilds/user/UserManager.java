package net.nighthawkempires.guilds.user;

import com.google.common.collect.Maps;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileType;
import net.nighthawkempires.guilds.NEGuilds;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import static net.nighthawkempires.core.NECore.getFileManager;

public class UserManager {

    private ConcurrentMap<UUID, User> userMap;
    private UserCreator creator;
    private UserLoader loader;
    private UserSaver saver;

    public UserManager() {
        userMap = Maps.newConcurrentMap();
    }

    public ConcurrentMap<UUID, User> getUserMap() {
        return userMap;
    }

    public User getUser(UUID uuid) {
        if (getUserMap().containsKey(uuid)) {
            return getUserMap().get(uuid);
        }
        return getTempUser(uuid);
    }

    public User getTempUser(UUID uuid) {
        if (userLoaded(uuid)) {
            return getUser(uuid);
        } else if (userExists(uuid)) {
            loadUser(new User(uuid));
            Bukkit.getScheduler().scheduleSyncDelayedTask(NEGuilds.getPlugin(), () -> saveUser(getUser(uuid)), 100L);
            return getUser(uuid);
        }
        return null;
    }

    public void createUser(User user) {
        if (userExists(user.getUUID())) return;
        creator = new UserCreator(user);
        creator.create();
        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(user.getUUID()))) {
            loadUser(user);
        }
    }

    public void loadUser(User user) {
        if (!userExists(user.getUUID())) return;
        loader = new UserLoader(user);
        loader.load();
        getUserMap().put(user.getUUID(), user);
    }

    public void saveUser(User user) {
        if (!userExists(user.getUUID())) return;
        saver = new UserSaver(user);
        saver.save();
        getUserMap().remove(user.getUUID());
    }

    public boolean userExists(UUID uuid) {
        if (NECore.getSettings().useSQL) {
            try {
                String table = "guilds_data";
                PreparedStatement statement = NECore.getMySQL().getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                statement.setString(1, uuid.toString());
                ResultSet set = statement.executeQuery();
                return set.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return getFileManager().fileExists(uuid.toString(), FileType.PLAYER_FILE);
        }
        return false;
    }

    public boolean userLoaded(UUID uuid) {
        return getUserMap().containsKey(uuid);
    }

    public FileConfiguration getPlayerFile(UUID uuid) {
        if (!getFileManager().isFileLoaded(uuid.toString())) {
            getFileManager().loadFile(uuid.toString(), FileType.PLAYER_FILE);
        }
        return getFileManager().getFile(uuid.toString());
    }

    public void savePlayerFile(UUID uuid) {
        getFileManager().saveFile(uuid.toString(), false);
    }
}
