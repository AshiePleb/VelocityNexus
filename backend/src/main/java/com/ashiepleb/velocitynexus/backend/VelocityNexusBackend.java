package com.ashiepleb.velocitynexus.backend;

import com.ashiepleb.velocitynexus.backend.listener.MenuListener;
import com.ashiepleb.velocitynexus.backend.listener.VelocityMessageListener;
import com.ashiepleb.velocitynexus.backend.menu.MenuManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VelocityNexusBackend extends JavaPlugin {
    
    private MenuManager menuManager;
    
    @Override
    public void onEnable() {
        // Initialize menu manager
        menuManager = new MenuManager(this);
        
        // Register plugin messaging channel
        getServer().getMessenger().registerIncomingPluginChannel(
            this, "velocitynexus:menu", new VelocityMessageListener(this, menuManager)
        );
        getServer().getMessenger().registerOutgoingPluginChannel(this, "velocitynexus:menu");
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(
            new MenuListener(menuManager), this
        );
        
        getLogger().info("Velocity Nexus Backend has been enabled!");
        getLogger().info("Ready to receive GUI menu data from proxy.");
    }
    
    @Override
    public void onDisable() {
        // Close all open menus
        if (menuManager != null) {
            menuManager.closeAllMenus();
        }
        
        getLogger().info("Velocity Nexus Backend has been disabled!");
    }
    
    public MenuManager getMenuManager() {
        return menuManager;
    }
}
