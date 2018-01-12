package net.nighthawkempires.guilds.user.registry;

import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.Registry;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.user.UserModel;

import java.util.*;

public interface UserRegistry extends Registry<UserModel> {
    String NAME = "users";

    default UserModel fromDataSection(String stringKey, DataSection data) {
        return new UserModel(stringKey, data);
    }

    default UserModel getUser(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return fromKey(uuid.toString()).orElseGet(() -> register(new UserModel(uuid)));
    }

    default void deleteUser(UUID uuid) {
        Optional<UserModel> user = fromKey(uuid.toString());
        if (user.isPresent()) {
            user.get().getGuild().ifPresent(guildModel -> guildModel.removeMember(uuid));
            remove(uuid.toString());
            NECore.getLoggers().info(NEGuilds.getPlugin(), "Deleted user: " + user.get().getName() + ".");
        }
    }

    @Deprecated
    Map<String, UserModel> getRegisteredData();

    default boolean userExists(UUID uuid) {
        return fromKey(uuid.toString()).isPresent();
    }
}
