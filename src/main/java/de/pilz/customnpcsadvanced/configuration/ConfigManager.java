package de.pilz.customnpcsadvanced.configuration;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

public class ConfigManager {

    public static void init() {
        try {
            ConfigurationManager.registerConfig(ConfigMail.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }
}
