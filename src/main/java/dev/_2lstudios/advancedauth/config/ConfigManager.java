package dev._2lstudios.advancedauth.config;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    private Map<String, Configuration> configs;
    private Plugin plugin;

    public ConfigManager(Plugin plugin) {
        this.configs = new HashMap<>();
        this.plugin = plugin;
    }

    public Configuration loadFailover(String name) {
        try {
            InputStream failoverStream = this.plugin.getResource(name);

            if (failoverStream != null) {
                InputStreamReader isReader = new InputStreamReader(failoverStream);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer buffer = new StringBuffer();
                String line;
                while((line = reader.readLine())!= null){
                    buffer.append(line + "\n");
                }

                String failoverRaw = buffer.toString();
                Configuration failover = new Configuration(failoverRaw);
                failover.load();

                isReader.close();
                failoverStream.close();

                return failover;
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }


        return null;
    }

    public Configuration getConfig(String name) {
        if (this.configs.containsKey(name)) {
            return configs.get(name);
        }

        File configFile = new File(this.plugin.getDataFolder(), name);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            this.plugin.saveResource(name, false);
        }

        Configuration failover = this.loadFailover(name);
        Configuration config = new Configuration(configFile, failover);

        try {
            config.load();
            this.configs.put(name, config);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return config;
    }
}