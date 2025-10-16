package com.ashiepleb.velocityNexus.config;

import com.moandjiezana.toml.Toml;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private final Path dataDirectory;
    private final Logger logger;
    private Toml config;
    
    public ConfigManager(Path dataDirectory, Logger logger) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }
    
    public void loadConfig() {
        try {
            Path configPath = dataDirectory.resolve("config.toml");
            
            // Create data directory if it doesn't exist
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
            
            // Copy default config if it doesn't exist
            if (!Files.exists(configPath)) {
                try (InputStream in = getClass().getResourceAsStream("/config.toml")) {
                    if (in != null) {
                        Files.copy(in, configPath);
                        logger.info("Created default configuration file");
                    }
                }
            }
            
            config = new Toml().read(configPath.toFile());
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration", e);
        }
    }
    
    public String getMenuTitle() {
        return config.getString("menu.title", "<gradient:#00D9FF:#7B2FBE>Server Selector</gradient>");
    }
    
    public int getMenuRows() {
        return config.getLong("menu.rows", 3L).intValue();
    }
    
    public Map<String, ServerConfig> getServers() {
        Map<String, ServerConfig> servers = new HashMap<>();
        
        Toml serversTable = config.getTable("servers");
        if (serversTable != null) {
            for (Map.Entry<String, Object> entry : serversTable.entrySet()) {
                String serverName = entry.getKey();
                if (entry.getValue() instanceof Toml) {
                    Toml serverData = (Toml) entry.getValue();
                    
                    ServerConfig serverConfig = new ServerConfig(
                        serverName,
                        serverData.getString("display-name", serverName),
                        serverData.getList("description", List.of()),
                        serverData.getString("icon", "SERVER"),
                        serverData.getLong("slot", 0L).intValue(),
                        serverData.getBoolean("enabled", true),
                        serverData.getString("permission", null)
                    );
                    
                    servers.put(serverName, serverConfig);
                }
            }
        }
        
        return servers;
    }
    
    public boolean isUpdatePlayerCount() {
        return config.getBoolean("settings.update-player-count", true);
    }
    
    public boolean isSoundsEnabled() {
        return config.getBoolean("settings.sounds-enabled", true);
    }
    
    public boolean isOverrideServerCommand() {
        return config.getBoolean("settings.override-server-command", true);
    }
    
    public String getOfflineServerMessage() {
        return config.getString("settings.offline-server-message", "<red>This server is currently offline!");
    }
    
    public String getNoPermissionMessage() {
        return config.getString("settings.no-permission-message", "<red>You don't have permission to access this server!");
    }
    
    public static class ServerConfig {
        private final String internalName;
        private final String displayName;
        private final List<String> description;
        private final String icon;
        private final int slot;
        private final boolean enabled;
        private final String permission;
        
        public ServerConfig(String internalName, String displayName, List<String> description, 
                          String icon, int slot, boolean enabled, String permission) {
            this.internalName = internalName;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.slot = slot;
            this.enabled = enabled;
            this.permission = permission;
        }
        
        public String getInternalName() { return internalName; }
        public String getDisplayName() { return displayName; }
        public List<String> getDescription() { return description; }
        public String getIcon() { return icon; }
        public int getSlot() { return slot; }
        public boolean isEnabled() { return enabled; }
        public String getPermission() { return permission; }
    }
}
