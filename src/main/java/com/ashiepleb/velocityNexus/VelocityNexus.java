package com.ashiepleb.velocityNexus;

import com.ashiepleb.velocityNexus.commands.VelocityNexusCommand;
import com.ashiepleb.velocityNexus.commands.ServerCommand;
import com.ashiepleb.velocityNexus.config.ConfigManager;
import com.ashiepleb.velocityNexus.listener.PluginMessageListener;
import com.ashiepleb.velocityNexus.menu.MenuManager;
import com.ashiepleb.velocityNexus.menu.ServerSelectorMenu;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "velocitynexus", name = "Velocity Nexus", version = "1.0.0", 
        description = "Velocity Nexus transforms your Velocity proxy into an intuitive server hub by replacing the default /server command with a customizable GUI interface. Players can seamlessly navigate between servers with beautiful, user-friendly menus while administrators maintain full control over server access and presentation.", 
        authors = {"AshiePleb"})
public class VelocityNexus {

    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;
    
    private ConfigManager configManager;
    private MenuManager menuManager;
    private ServerSelectorMenu textMenu;

    @Inject
    public VelocityNexus(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Initializing Velocity Nexus...");
        
        // Load configuration
        configManager = new ConfigManager(dataDirectory, logger);
        configManager.loadConfig();
        
        // Initialize menu systems
        menuManager = new MenuManager(proxy, configManager, logger);
        textMenu = new ServerSelectorMenu(proxy, configManager);
        
        // Register listeners
        registerListeners();
        
        // Register commands
        registerCommands();
        
        logger.info("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        logger.info("┃  Velocity Nexus v1.0.0 - Enabled!     ┃");
        logger.info("┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫");
        logger.info("┃  Commands:                             ┃");
        logger.info("┃    /server - Open GUI selector         ┃");
        logger.info("┃    /velocitynexus - Plugin info        ┃");
        logger.info("┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫");
        logger.info("┃  Note: Backend plugin required on      ┃");
        logger.info("┃  Spigot/Paper servers for GUI!         ┃");
        logger.info("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }
    
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (menuManager != null) {
            menuManager.shutdown();
        }
        logger.info("Velocity Nexus has been disabled");
    }
    
    private void registerListeners() {
        EventManager eventManager = proxy.getEventManager();
        eventManager.register(this, new PluginMessageListener(menuManager, logger));
    }
    
    private void registerCommands() {
        CommandManager commandManager = proxy.getCommandManager();
        
        // Register /velocitynexus command (for info and admin functions)
        CommandMeta velocityNexusMeta = commandManager.metaBuilder("velocitynexus")
            .plugin(this)
            .build();
        commandManager.register(velocityNexusMeta, new VelocityNexusCommand(this));
        
        // Register /server command override if enabled
        if (configManager.isOverrideServerCommand()) {
            // Unregister default /server command
            commandManager.unregister("server");
            
            // Register our custom /server command (opens GUI)
            CommandMeta serverMeta = commandManager.metaBuilder("server")
                .plugin(this)
                .build();
            commandManager.register(serverMeta, new ServerCommand(menuManager, textMenu));
            
            logger.info("Overridden default /server command with GUI menu interface");
        }
        
        logger.info("Commands registered: /velocitynexus (info) and /server (GUI)");
    }
    
    public void reloadConfig() {
        configManager.loadConfig();
        logger.info("Configuration reloaded");
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public MenuManager getMenuManager() {
        return menuManager;
    }
    
    public ServerSelectorMenu getTextMenu() {
        return textMenu;
    }
}
