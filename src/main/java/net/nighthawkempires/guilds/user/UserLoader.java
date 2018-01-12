package net.nighthawkempires.guilds.user;

import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileFolder;
import net.nighthawkempires.core.file.FileManager;
import net.nighthawkempires.guilds.guild.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.UUID;

public class UserLoader {

    private User user;
    private FileManager fileManager;

    public UserLoader(User user) {
        this.user = user;
        this.fileManager = NECore.getFileManager();
    }

    public User getUser() {
        return user;
    }

    public void load() {
        if (NECore.getSettings().mysqlEnabled) {
            try {
                PreparedStatement statement = NECore.getMySQL().getConnection().prepareStatement(
                        "SELECT * FROM guilds_data WHERE uuid='" + getUser().getUUID().toString() + "'");
                ResultSet results = statement.executeQuery();
                results.next();
                getUser().setName(Bukkit.getOfflinePlayer(getUser().getUUID()).getName());
                try {
                    getUser().setGuild(
                            UUID.fromString(results.getString("guild_uuid")));
                    getUser().setType(RankType.valueOf(results.getString("guild_rank")));
                } catch (Exception e) {
                    getUser().removeGuild();
                    getUser().setType(null);
                }
                getUser().setPower(results.getInt("power"));
                NECore.getLoggers().info("Loaded Guilds User " + getUser().getUUID().toString() + ": " +
                        Bukkit.getOfflinePlayer(getUser().getUUID()).getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            getUser().setName(Bukkit.getOfflinePlayer(getUser().getUUID()).getName());
            getUser().setPower(getPlayerFile().getInt("power"));
        }
    }

    public FileConfiguration getPlayerFile() {
        if (!fileManager.isFileLoaded(user.getUUID().toString())) {
            fileManager.loadFile(user.getUUID().toString(), FileFolder.PLAYER_PATH);
        }
        return fileManager.getFile(user.getUUID().toString());
    }

    public void savePlayerFile() {
        fileManager.saveFile(user.getUUID().toString(), false);
    }
}
