package com.ashiepleb.velocityNexus.listener;

import com.ashiepleb.velocityNexus.menu.MenuManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Listens for plugin messages from backend servers.
 * Handles menu click events from the backend.
 */
public class PluginMessageListener {
    private static final MinecraftChannelIdentifier MENU_CHANNEL = 
        MinecraftChannelIdentifier.from("velocitynexus:menu");
    
    private final MenuManager menuManager;
    private final Logger logger;
    
    public PluginMessageListener(MenuManager menuManager, Logger logger) {
        this.menuManager = menuManager;
        this.logger = logger;
    }
    
    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(MENU_CHANNEL)) {
            return;
        }
        
        // Only handle messages from servers
        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }
        
        ServerConnection serverConnection = (ServerConnection) event.getSource();
        Player player = serverConnection.getPlayer();
        
        try {
            ByteArrayInputStream msgBytes = new ByteArrayInputStream(event.getData());
            DataInputStream in = new DataInputStream(msgBytes);
            
            String action = in.readUTF();
            
            switch (action) {
                case "CLICK" -> {
                    int slot = in.readInt();
                    menuManager.handleClick(player, slot);
                    logger.debug("Player {} clicked slot {} in menu", player.getUsername(), slot);
                }
                case "CLOSE" -> {
                    menuManager.closeMenu(player);
                    logger.debug("Player {} closed menu", player.getUsername());
                }
                default -> logger.warn("Unknown menu action: {}", action);
            }
            
        } catch (IOException e) {
            logger.error("Failed to read plugin message", e);
        }
        
        event.setResult(PluginMessageEvent.ForwardResult.handled());
    }
}
