package com.ashiepleb.velocityNexus.menu;

import com.ashiepleb.velocityNexus.config.ConfigManager;
import com.ashiepleb.velocityNexus.config.ConfigManager.ServerConfig;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.*;

public class ServerSelectorMenu {
    private final ProxyServer proxy;
    private final ConfigManager configManager;
    private final MiniMessage miniMessage;
    
    public ServerSelectorMenu(ProxyServer proxy, ConfigManager configManager) {
        this.proxy = proxy;
        this.configManager = configManager;
        this.miniMessage = MiniMessage.miniMessage();
    }
    
    public void openMenu(Player player) {
        Map<String, ServerConfig> servers = configManager.getServers();
        
        // Build menu text representation for Velocity (since Velocity doesn't have native GUI)
        List<Component> menuLines = new ArrayList<>();
        menuLines.add(miniMessage.deserialize(""));
        menuLines.add(miniMessage.deserialize(configManager.getMenuTitle()));
        menuLines.add(miniMessage.deserialize("<gray><strikethrough>                                        </strikethrough>"));
        menuLines.add(miniMessage.deserialize(""));
        
        // Sort servers by slot number
        List<ServerConfig> sortedServers = new ArrayList<>(servers.values());
        sortedServers.sort(Comparator.comparingInt(ServerConfig::getSlot));
        
        for (ServerConfig serverConfig : sortedServers) {
            if (!serverConfig.isEnabled()) continue;
            
            // Check permission
            if (serverConfig.getPermission() != null && 
                !player.hasPermission(serverConfig.getPermission())) {
                continue;
            }
            
            Optional<RegisteredServer> server = proxy.getServer(serverConfig.getInternalName());
            if (server.isEmpty()) continue;
            
            RegisteredServer registeredServer = server.get();
            int playerCount = registeredServer.getPlayersConnected().size();
            
            // Build server entry
            Component displayName = miniMessage.deserialize(serverConfig.getDisplayName());
            
            menuLines.add(Component.empty()
                .append(miniMessage.deserialize("<gray>• </gray>"))
                .append(displayName)
                .append(miniMessage.deserialize(" <dark_gray>[<yellow>" + playerCount + "</yellow>]</dark_gray>"))
            );
            
            for (String descLine : serverConfig.getDescription()) {
                String processed = descLine.replace("%players%", String.valueOf(playerCount));
                menuLines.add(miniMessage.deserialize("  " + processed));
            }
            
            menuLines.add(miniMessage.deserialize("  <click:run_command:/server " + 
                serverConfig.getInternalName() + "><green><bold>[CLICK TO JOIN]</bold></green></click>"));
            menuLines.add(miniMessage.deserialize(""));
        }
        
        menuLines.add(miniMessage.deserialize("<gray><strikethrough>                                        </strikethrough>"));
        
        // Send all lines to player
        for (Component line : menuLines) {
            player.sendMessage(line);
        }
    }
    
    public boolean connectToServer(Player player, String serverName) {
        Map<String, ServerConfig> servers = configManager.getServers();
        ServerConfig serverConfig = servers.get(serverName);
        
        if (serverConfig == null || !serverConfig.isEnabled()) {
            player.sendMessage(miniMessage.deserialize(
                "<red>This server is not available!"));
            return false;
        }
        
        // Check permission
        if (serverConfig.getPermission() != null && 
            !player.hasPermission(serverConfig.getPermission())) {
            player.sendMessage(miniMessage.deserialize(
                configManager.getNoPermissionMessage()));
            return false;
        }
        
        Optional<RegisteredServer> server = proxy.getServer(serverName);
        if (server.isEmpty()) {
            player.sendMessage(miniMessage.deserialize(
                configManager.getOfflineServerMessage()));
            return false;
        }
        
        // Check if server is online
        server.get().ping().thenAccept(ping -> {
            // Server is online, connect
            player.createConnectionRequest(server.get()).fireAndForget();
        }).exceptionally(throwable -> {
            // Server is offline
            player.sendMessage(miniMessage.deserialize(
                configManager.getOfflineServerMessage()));
            return null;
        });
        
        return true;
    }
}
