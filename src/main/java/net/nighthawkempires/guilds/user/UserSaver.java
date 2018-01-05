package net.nighthawkempires.guilds.user;

import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileManager;
import net.nighthawkempires.core.file.FileType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserSaver {

    private User user;
    private FileManager fileManager;

    public UserSaver(User user) {
        this.user = user;
        this.fileManager = NECore.getFileManager();
    }

    public User getUser() {
        return user;
    }

    public void save() {
        savePlayerFile(true);
        if (NECore.getSettings().useSQL) {
            try {
                PreparedStatement statement = NECore.getMySQL().getConnection().prepareStatement("UPDATE guilds_data SET name=?,guild_uuid=?,guild_rank=?,power=? WHERE uuid='" + getUser().getUUID().toString() + "'");
                statement.setString(1, Bukkit.getOfflinePlayer(getUser().getUUID()).getName());
                try {
                    statement.setString(2, getUser().getGuild().getUUID().toString());
                } catch (Exception e) {
                    statement.setString(2, "");
                }
                try {
                    statement.setString(3, getUser().getType().name());
                } catch (Exception e) {
                    statement.setString(3, "");
                }
                statement.setInt(4, getUser().getPower());
                statement.executeUpdate();
                NECore.getLoggers().info("Saved Guilds User " + getUser().getUUID().toString() + ": " + Bukkit.getOfflinePlayer(getUser().getUUID()).getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            getPlayerFile().set("name", Bukkit.getOfflinePlayer(getUser().getUUID()).getName());
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
        fileManager.saveFile(user.getUUID().toString(), false);
    }
}
