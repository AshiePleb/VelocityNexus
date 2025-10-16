package com.ashiepleb.velocitynexus.backend.listener;

import com.ashiepleb.velocitynexus.backend.VelocityNexusBackend;
import com.ashiepleb.velocitynexus.backend.menu.MenuManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/**
 * Listens for plugin messages from the Velocity proxy.
 */
public class VelocityMessageListener implements PluginMessageListener {
    private final VelocityNexusBackend plugin;
    private final MenuManager menuManager;
    
    public VelocityMessageListener(VelocityNexusBackend plugin, MenuManager menuManager) {
        this.plugin = plugin;
        this.menuManager = menuManager;
    }
    
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("velocitynexus:menu")) {
            return;
        }
        
        try {
            ByteArrayInputStream msgBytes = new ByteArrayInputStream(message);
            DataInputStream in = new DataInputStream(msgBytes);
            
            String action = in.readUTF();
            
            if (action.equals("OPEN_MENU")) {
                String menuId = in.readUTF();
                String menuJson = in.readUTF();
                
                JsonObject menuData = JsonParser.parseString(menuJson).getAsJsonObject();
                menuManager.openMenu(player, menuData);
                
                plugin.getLogger().info("Received OPEN_MENU request for player " + player.getName());
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to process plugin message");
            e.printStackTrace();
        }
    }
}
