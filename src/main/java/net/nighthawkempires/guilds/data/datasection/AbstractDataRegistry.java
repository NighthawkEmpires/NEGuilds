package net.nighthawkempires.guilds.data.datasection;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class AbstractDataRegistry<T extends AbstractPersistentModel<String>> {
    protected final ConcurrentMap<String, T> REGISTERED_DATA = new ConcurrentHashMap<>();
    protected final String SAVE_PATH;
    protected final boolean SAVE_PRETTY;

    public AbstractDataRegistry(String savePath, boolean savePretty) {
        SAVE_PATH = savePath;
        SAVE_PRETTY = savePretty;
    }

    public T fromId(String id) {
        if (REGISTERED_DATA.get(id) == null) {
            DataSection currentDb = getDatabase();
            if (currentDb != null) {
                synchronized (currentDb) {
                    if (currentDb.isSection(id)) {
                        registerFromDatabase();
                    }
                }
            }
        }
        return REGISTERED_DATA.get(id);
    }

    public void register(T data) {
        REGISTERED_DATA.put(data.getPersistentId(), data);
        synchronized (data) {
            addToDatabase(data.getPersistentId(), data);
        }
    }

    public final void register(T[] data) {
        register(Arrays.asList(data));
    }

    final void register(Collection<T> data) {
        data.forEach(this::register);
    }

    public final synchronized void registerFromDatabase() {
        DataSection currentDb = getDatabase();
        if (currentDb != null) {
            synchronized (currentDb) {
                for (String key : currentDb.getKeys()) {
                    REGISTERED_DATA.put(key, valueFromData(key, currentDb.getSectionNullable(key)));
                }
            }
        }
    }

    public void unregister(T data) {
        REGISTERED_DATA.remove(data.getPersistentId());
        synchronized (data) {
            deleteFromDatabase(data.getPersistentId());
        }
    }

    public Collection<T> getRegistered() {
        if (REGISTERED_DATA.isEmpty()) {
            registerFromDatabase();
        }
        return REGISTERED_DATA.values();
    }

    synchronized boolean deleteFromDatabase(String key) {
        // Grab the current db, and its data as a usable map.
        DataSection currentDb = getDatabase();
        if (currentDb != null) {
            // Remove data.
            currentDb.remove(key);
            return saveData(currentDb);
        }

        return false;
    }

    synchronized boolean addToDatabase(String key, T data) {
        // Grab the current file, and its data as a usable map.
        DataSection currentDb = getDatabase();

        if (currentDb != null) {
            // Create/overwrite a configuration section.
            currentDb.createSection(key, data.serialize());
            return saveData(currentDb);
        }

        return false;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    final DataSection getDatabase() {
        try {
            return DataSectionUtil.loadSectionFromFile(SAVE_PATH + getName() + getExtention()).get();
        } catch (Exception ignored) {
        }
        return new FJsonSection(new ConcurrentHashMap<>());
    }

    private synchronized boolean saveData(DataSection currentDb) {
        // Save the file!
        if (isPretty() || SAVE_PRETTY) {
            return DataSectionUtil.saveFilePretty(SAVE_PATH + getName() + getExtention(), currentDb);
        }
        return DataSectionUtil.saveFile(SAVE_PATH + getName() + getExtention(), currentDb);
    }

    public synchronized final void clearCache() {
        REGISTERED_DATA.clear();
    }

    /**
     * Convert to a get from a number of objects representing the data.
     *
     * @param stringKey The string key for the data.
     * @param data      The provided data object.
     * @return The converted get.
     */
    protected abstract T valueFromData(String stringKey, DataSection data);

    protected abstract String getName();

    @SuppressWarnings("SameReturnValue")
    protected String getExtention() {
        return ".extdat";
    }

    @SuppressWarnings("SameReturnValue")
    protected boolean isPretty() {
        return false;
    }
}
