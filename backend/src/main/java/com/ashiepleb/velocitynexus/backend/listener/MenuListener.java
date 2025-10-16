package com.ashiepleb.velocitynexus.backend.listener;

import com.ashiepleb.velocitynexus.backend.menu.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Listens for inventory events and sends them to the proxy.
 */
public class MenuListener implements Listener {
    private final MenuManager menuManager;
    
    public MenuListener(MenuManager menuManager) {
        this.menuManager = menuManager;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }
        
        // Check if this is a menu inventory
        Inventory openMenu = menuManager.getOpenMenu(player);
        if (openMenu == null || !clickedInventory.equals(openMenu)) {
            return;
        }
        
        // Cancel the event (prevent item taking)
        event.setCancelled(true);
        
        // Send click event to proxy
        int slot = event.getSlot();
        sendClickToProxy(player, slot);
        
        // Close the inventory
        player.closeInventory();
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        
        // Check if this is a menu inventory
        if (!menuManager.hasMenuOpen(player)) {
            return;
        }
        
        // Send close event to proxy
        sendCloseToProxy(player);
        
        // Remove from tracking
        menuManager.closeMenu(player);
    }
    
    private void sendClickToProxy(Player player, int slot) {
        try {
            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(msgBytes);
            
            out.writeUTF("CLICK");
            out.writeInt(slot);
            
            player.sendPluginMessage(
                player.getServer().getPluginManager().getPlugin("VelocityNexus-Backend"),
                "velocitynexus:menu",
                msgBytes.toByteArray()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void sendCloseToProxy(Player player) {
        try {
            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(msgBytes);
            
            out.writeUTF("CLOSE");
            
            player.sendPluginMessage(
                player.getServer().getPluginManager().getPlugin("VelocityNexus-Backend"),
                "velocitynexus:menu",
                msgBytes.toByteArray()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
