package com.ashiepleb.velocityNexus.menu;

import com.ashiepleb.velocityNexus.config.ConfigManager;
import com.ashiepleb.velocityNexus.config.ConfigManager.ServerConfig;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.*;

/**
 * Represents an inventory-style GUI menu for server selection.
 * This creates a protocol-level inventory that works across all Minecraft versions.
 */
public class InventoryMenu {
    private final String id;
    private final Component title;
    private final int rows;
    private final Map<Integer, MenuItem> items;
    
    public InventoryMenu(String id, Component title, int rows) {
        this.id = id;
        this.title = title;
        this.rows = rows;
        this.items = new HashMap<>();
    }
    
    public String getId() {
        return id;
    }
    
    public Component getTitle() {
        return title;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getSize() {
        return rows * 9;
    }
    
    public void setItem(int slot, MenuItem item) {
        if (slot >= 0 && slot < getSize()) {
            items.put(slot, item);
        }
    }
    
    public MenuItem getItem(int slot) {
        return items.get(slot);
    }
    
    public Map<Integer, MenuItem> getItems() {
        return Collections.unmodifiableMap(items);
    }
    
    /**
     * Represents a clickable item in the inventory menu.
     */
    public static class MenuItem {
        private final String material;
        private final Component displayName;
        private final List<Component> lore;
        private final MenuAction action;
        
        public MenuItem(String material, Component displayName, List<Component> lore, MenuAction action) {
            this.material = material;
            this.displayName = displayName;
            this.lore = lore;
            this.action = action;
        }
        
        public String getMaterial() {
            return material;
        }
        
        public Component getDisplayName() {
            return displayName;
        }
        
        public List<Component> getLore() {
            return lore;
        }
        
        public MenuAction getAction() {
            return action;
        }
    }
    
    /**
     * Represents an action to perform when an item is clicked.
     */
    @FunctionalInterface
    public interface MenuAction {
        void onClick(Player player);
    }
    
    /**
     * Builder for creating inventory menus.
     */
    public static class Builder {
        private final ProxyServer proxy;
        private final ConfigManager configManager;
        private final MiniMessage miniMessage;
        
        public Builder(ProxyServer proxy, ConfigManager configManager) {
            this.proxy = proxy;
            this.configManager = configManager;
            this.miniMessage = MiniMessage.miniMessage();
        }
        
        public InventoryMenu buildServerSelector() {
            String menuTitle = configManager.getMenuTitle();
            int rows = configManager.getMenuRows();
            
            InventoryMenu menu = new InventoryMenu(
                "server-selector",
                miniMessage.deserialize(menuTitle),
                rows
            );
            
            Map<String, ServerConfig> servers = configManager.getServers();
            
            for (ServerConfig serverConfig : servers.values()) {
                if (!serverConfig.isEnabled()) continue;
                
                // Get player count
                int playerCount = proxy.getServer(serverConfig.getInternalName())
                    .map(server -> server.getPlayersConnected().size())
                    .orElse(0);
                
                // Build lore
                List<Component> lore = new ArrayList<>();
                for (String line : serverConfig.getDescription()) {
                    String processed = line.replace("%players%", String.valueOf(playerCount));
                    lore.add(miniMessage.deserialize(processed));
                }
                
                // Create menu item
                MenuItem item = new MenuItem(
                    serverConfig.getIcon(),
                    miniMessage.deserialize(serverConfig.getDisplayName()),
                    lore,
                    player -> connectToServer(player, serverConfig)
                );
                
                menu.setItem(serverConfig.getSlot(), item);
            }
            
            return menu;
        }
        
        private void connectToServer(Player player, ServerConfig serverConfig) {
            // Check permission
            if (serverConfig.getPermission() != null && 
                !player.hasPermission(serverConfig.getPermission())) {
                player.sendMessage(miniMessage.deserialize(
                    configManager.getNoPermissionMessage()));
                return;
            }
            
            // Check if player is already on this server
            if (player.getCurrentServer().isPresent()) {
                var currentServer = player.getCurrentServer().get().getServer();
                if (currentServer.getServerInfo().getName().equals(serverConfig.getInternalName())) {
                    player.sendMessage(miniMessage.deserialize(
                        "<gray>You are already connected to this server!"));
                    return;
                }
            }
            
            // Get server
            var serverOptional = proxy.getServer(serverConfig.getInternalName());
            if (serverOptional.isEmpty()) {
                player.sendMessage(miniMessage.deserialize(
                    configManager.getOfflineServerMessage()));
                return;
            }
            
            var server = serverOptional.get();
            
            // Check if server has any players or is registered (means it's online)
            // If we got here and serverOptional was present, server exists in config
            
            // Try to connect
            player.createConnectionRequest(server)
                .connect()
                .thenAccept(result -> {
                    if (!result.isSuccessful()) {
                        player.sendMessage(miniMessage.deserialize(
                            configManager.getOfflineServerMessage()));
                    }
                });
        }
    }
}
