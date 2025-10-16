package com.ashiepleb.velocityNexus.menu;

import com.ashiepleb.velocityNexus.config.ConfigManager;
import com.ashiepleb.velocityNexus.menu.InventoryMenu.MenuItem;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages inventory GUI menus using plugin messaging.
 * This communicates with a backend server plugin to display actual inventory GUIs.
 */
public class MenuManager {
    private static final MinecraftChannelIdentifier MENU_CHANNEL = 
        MinecraftChannelIdentifier.from("velocitynexus:menu");
    
    private final ProxyServer proxy;
    private final ConfigManager configManager;
    private final Logger logger;
    private final InventoryMenu.Builder menuBuilder;
    private final Map<UUID, InventoryMenu> openMenus;
    
    public MenuManager(ProxyServer proxy, ConfigManager configManager, Logger logger) {
        this.proxy = proxy;
        this.configManager = configManager;
        this.logger = logger;
        this.menuBuilder = new InventoryMenu.Builder(proxy, configManager);
        this.openMenus = new HashMap<>();
        
        // Register plugin channel
        proxy.getChannelRegistrar().register(MENU_CHANNEL);
    }
    
    /**
     * Opens the server selector GUI for a player.
     */
    public void openServerSelector(Player player) {
        InventoryMenu menu = menuBuilder.buildServerSelector();
        openMenus.put(player.getUniqueId(), menu);
        
        // Check if player is connected to a server
        if (player.getCurrentServer().isEmpty()) {
            player.sendMessage(Component.text("§cYou must be connected to a server to use the GUI!"));
            player.sendMessage(Component.text("§eUse /nexus to see the server list in chat."));
            return;
        }
        
        // Send menu data to backend plugin via plugin messaging
        sendMenuData(player, menu);
    }
    
    /**
     * Handles a menu click from a player.
     */
    public void handleClick(Player player, int slot) {
        InventoryMenu menu = openMenus.get(player.getUniqueId());
        if (menu == null) return;
        
        MenuItem item = menu.getItem(slot);
        if (item != null && item.getAction() != null) {
            item.getAction().onClick(player);
        }
    }
    
    /**
     * Closes a menu for a player.
     */
    public void closeMenu(Player player) {
        openMenus.remove(player.getUniqueId());
    }
    
    /**
     * Sends menu data to the backend server plugin.
     */
    private void sendMenuData(Player player, InventoryMenu menu) {
        try {
            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(msgBytes);
            
            // Write action type
            out.writeUTF("OPEN_MENU");
            
            // Write menu ID
            out.writeUTF(menu.getId());
            
            // Write menu data as JSON
            JsonObject menuJson = new JsonObject();
            menuJson.addProperty("title", GsonComponentSerializer.gson().serialize(menu.getTitle()));
            menuJson.addProperty("rows", menu.getRows());
            
            JsonArray itemsArray = new JsonArray();
            for (Map.Entry<Integer, MenuItem> entry : menu.getItems().entrySet()) {
                JsonObject itemJson = new JsonObject();
                itemJson.addProperty("slot", entry.getKey());
                itemJson.addProperty("material", entry.getValue().getMaterial());
                itemJson.addProperty("displayName", 
                    GsonComponentSerializer.gson().serialize(entry.getValue().getDisplayName()));
                
                JsonArray loreArray = new JsonArray();
                for (Component loreLine : entry.getValue().getLore()) {
                    loreArray.add(GsonComponentSerializer.gson().serialize(loreLine));
                }
                itemJson.add("lore", loreArray);
                
                itemsArray.add(itemJson);
            }
            menuJson.add("items", itemsArray);
            
            out.writeUTF(menuJson.toString());
            
            // Send to player's current server
            player.getCurrentServer().ifPresent(serverConnection -> {
                serverConnection.sendPluginMessage(MENU_CHANNEL, msgBytes.toByteArray());
            });
            
            logger.debug("Sent menu data to backend server for player {}", player.getUsername());
            
        } catch (IOException e) {
            logger.error("Failed to send menu data", e);
            player.sendMessage(Component.text("§cFailed to open menu. Please try again."));
        }
    }
    
    public void shutdown() {
        openMenus.clear();
    }
}
