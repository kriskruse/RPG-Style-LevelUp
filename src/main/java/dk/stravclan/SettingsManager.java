package dk.stravclan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class SettingsManager {

    public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

    public static void saveSettings() {
        Gson gson = new Gson();
        String json = gson.toJson(Constants.skillDataMap);

        try (FileWriter writer = new FileWriter(Constants.SETTINGS_FILE)) {
            writer.write(json);
        } catch (IOException e) {
            LOGGER.error("Failed to save settings file: {}", e.getMessage());
        }
    }

    public static Map<String, Map<String, Float>> loadSettings() {
        Gson gson = new Gson();
        Map<String, Map<String, Float>> data = null;

        Type type = new TypeToken<Map<String, Map<String, Float>>>() {}.getType();

        try (FileReader reader = new FileReader(Constants.SETTINGS_FILE)) {
            data = gson.fromJson(reader, type);
        } catch (IOException e) {
            LOGGER.error("Failed to load settings file: {}", e.getMessage());

            if (Files.exists(Path.of(Constants.SETTINGS_FILE))) {
                LOGGER.error("Settings file exists but could not be loaded");
            } else {
                LOGGER.error("Settings file does not exist, creating new file");
                saveSettings();
            }
        }
        return data != null ? data : Constants.skillDataMap;
    }
}
