package com.demigodsrpg.util.datasection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Map;
import java.util.Optional;

/**
 * Json file related utility methods.
 */
public class DataSectionUtil {
    // -- CONSTRUCTOR -- //

    /**
     * Private constructor to prevent instance calls.
     */
    private DataSectionUtil() {
    }

    // -- UTILITY METHODS -- //

    /**
     * Load a JsonSection from a json file.
     *
     * @param file The json file path.
     * @return The JsonSection for the entire file.
     */
    @SuppressWarnings("unchecked")
    public static Optional<DataSection> loadSectionFromFile(String file) {
        File dataFile = new File(file);
        if (!(dataFile.exists())) createFile(dataFile);
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        try {
            FileInputStream inputStream = new FileInputStream(dataFile);
            InputStreamReader reader = new InputStreamReader(inputStream);
            Map<String, Object> sectionData = gson.fromJson(reader, Map.class);
            reader.close();
            return Optional.of(new FJsonSection(sectionData));
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Create a file and its directory path.
     *
     * @param dataFile The file object.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createFile(File dataFile) {
        try {
            // Create the directories.
            (dataFile.getParentFile()).mkdirs();

            // Create the new file.
            dataFile.createNewFile();
        } catch (Exception errored) {
            throw new RuntimeException("Demigods RPG couldn't create a data file!", errored);
        }
    }

    /**
     * Save a JsonSection as a json file.
     *
     * @param file    The path to the json file.
     * @param section The JsonSection to be saved.
     * @return Save success or failure.
     */
    public static boolean saveFile(String file, DataSection section) {
        File dataFile = new File(file);
        if (!(dataFile.exists())) createFile(dataFile);
        return section.toFJsonSection().save(dataFile);
    }

    /**
     * Save a JsonSection as a json file in a pretty format.
     *
     * @param file    The path to the json file.
     * @param section The JsonSection to be saved.
     * @return Save success or failure.
     */
    public static boolean saveFilePretty(String file, DataSection section) {
        File dataFile = new File(file);
        if (!(dataFile.exists())) createFile(dataFile);
        return section.toFJsonSection().savePretty(dataFile);
    }
}
