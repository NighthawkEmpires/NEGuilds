package net.nighthawkempires.guilds.user;

import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileManager;
import net.nighthawkempires.core.file.FileType;
import net.nighthawkempires.guilds.NEGuilds;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;

public class UserCreator {

    private User user;
    private FileManager fileManager;

    public UserCreator(User user) {
        this.user = user;
        this.fileManager = NECore.getFileManager();
    }

    public User getUser() {
        return user;
    }

    public void create() {
        if (NECore.getSettings().useSQL) {
            try {
                PreparedStatement statement =
                        NECore.getMySQL().getConnection().prepareStatement("SELECT * FROM guilds_data WHERE uuid=?");
                statement.setString(1, getUser().getUUID().toString());
                ResultSet results = statement.executeQuery();
                results.next();
                if (!NEGuilds.getUserManager().userExists(getUser().getUUID())) {
                    PreparedStatement insert =
                            NECore.getMySQL().getConnection().prepareStatement("INSERT INTO guilds_data(" +
                                    "uuid,name,guild_uuid,guild_rank,power) VALUE (?,?,?,?,?)");
                    insert.setString(1, getUser().getUUID().toString());
                    insert.setString(2, Bukkit.getOfflinePlayer(getUser().getUUID()).getName());
                    insert.setString(3, "");
                    insert.setString(4, "");
                    insert.setInt(5, 10);
                    insert.executeUpdate();
                    NECore.getLoggers().info("Created Guilds User " + getUser().getUUID().toString() + ": " +
                            Bukkit.getOfflinePlayer(getUser().getUUID()).getName() + ".");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            getPlayerFile().set("name", Bukkit.getOfflinePlayer(getUser().getUUID()).getName());
            getPlayerFile().set("power", 10);
            savePlayerFile(true);
        }
    }

    public FileConfiguration getPlayerFile() {
        savePlayerFile(true);
        if (!fileManager.isFileLoaded(user.getUUID().toString())) {
            fileManager.loadFile(user.getUUID().toString(), FileType.PLAYER_FILE);
        }
        return fileManager.getFile(user.getUUID().toString());
    }

    public void savePlayerFile(boolean clear) {
        fileManager.saveFile(user.getUUID().toString(), clear);
    }
}
