package net.nighthawkempires.guilds.data;

import net.nighthawkempires.core.NECore;
import net.nighthawkempires.guilds.data.datasection.AbstractDataRegistry;
import net.nighthawkempires.guilds.data.datasection.AbstractPersistentModel;

public abstract class AbstractGuildsDataRegistry<T extends AbstractPersistentModel<String>> extends AbstractDataRegistry<T> {
    public AbstractGuildsDataRegistry(boolean savePretty) {
        super (NECore.getFileManager().getGuildDirectory().getPath(), savePretty, false, null);
    }
}
