package net.nighthawkempires.guilds.user;

import com.google.common.collect.Maps;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileFolder;
import net.nighthawkempires.guilds.NEGuilds;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import static net.nighthawkempires.core.NECore.getFileManager;

public class UserManager {

    private ConcurrentMap<UUID, User> userMap;

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
        UserCreator creator = new UserCreator(user);
        creator.create();
        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(user.getUUID()))) {
            loadUser(user);
        }
    }

    public void loadUser(User user) {
        if (!userExists(user.getUUID())) return;
        UserLoader loader = new UserLoader(user);
        loader.load();
        getUserMap().put(user.getUUID(), user);
    }

    public void saveUser(User user) {
        if (!userExists(user.getUUID())) return;
        UserSaver saver = new UserSaver(user);
        saver.save();
        getUserMap().remove(user.getUUID());
    }

    public boolean userExists(UUID uuid) {
        if (NECore.getSettings().mysqlEnabled) {
            try {
                String table = "guilds_data";
                PreparedStatement statement =
                        NECore.getMySQL().getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                statement.setString(1, uuid.toString());
                ResultSet set = statement.executeQuery();
                return set.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return getFileManager().fileExists(uuid.toString(), FileFolder.PLAYER_PATH);
        }
        return false;
    }

    public boolean userLoaded(UUID uuid) {
        return getUserMap().containsKey(uuid);
    }

    public FileConfiguration getPlayerFile(UUID uuid) {
        if (!getFileManager().isFileLoaded(uuid.toString())) {
            getFileManager().loadFile(uuid.toString(), FileFolder.PLAYER_PATH);
        }
        return getFileManager().getFile(uuid.toString());
    }

    public void savePlayerFile(UUID uuid) {
        getFileManager().saveFile(uuid.toString(), false);
    }
}
