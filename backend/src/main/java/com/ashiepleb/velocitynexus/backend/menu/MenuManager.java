package com.ashiepleb.velocitynexus.backend.menu;

import com.ashiepleb.velocitynexus.backend.VelocityNexusBackend;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Manages inventory menus on the backend server.
 */
public class MenuManager {
    private final VelocityNexusBackend plugin;
    private final Map<UUID, Inventory> openMenus;
    
    public MenuManager(VelocityNexusBackend plugin) {
        this.plugin = plugin;
        this.openMenus = new HashMap<>();
    }
    
    /**
     * Opens a menu for a player from JSON data.
     */
    public void openMenu(Player player, JsonObject menuData) {
        try {
            // Parse menu data
            Component title = GsonComponentSerializer.gson()
                .deserialize(menuData.get("title").getAsString());
            int rows = menuData.get("rows").getAsInt();
            
            // Create inventory
            Inventory inventory = Bukkit.createInventory(null, rows * 9, title);
            
            // Add items
            JsonArray items = menuData.getAsJsonArray("items");
            for (JsonElement itemElement : items) {
                JsonObject itemData = itemElement.getAsJsonObject();
                
                int slot = itemData.get("slot").getAsInt();
                String materialName = itemData.get("material").getAsString();
                Component displayName = GsonComponentSerializer.gson()
                    .deserialize(itemData.get("displayName").getAsString());
                
                // Create item
                Material material = getMaterial(materialName);
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                
                if (meta != null) {
                    meta.displayName(displayName);
                    
                    // Add lore
                    JsonArray loreArray = itemData.getAsJsonArray("lore");
                    List<Component> lore = new ArrayList<>();
                    for (JsonElement loreElement : loreArray) {
                        Component loreLine = GsonComponentSerializer.gson()
                            .deserialize(loreElement.getAsString());
                        lore.add(loreLine);
                    }
                    meta.lore(lore);
                    
                    item.setItemMeta(meta);
                }
                
                inventory.setItem(slot, item);
            }
            
            // Open inventory
            player.openInventory(inventory);
            openMenus.put(player.getUniqueId(), inventory);
            
            plugin.getLogger().info("Opened menu for player " + player.getName());
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to open menu for player " + player.getName());
            e.printStackTrace();
        }
    }
    
    /**
     * Checks if a player has a menu open.
     */
    public boolean hasMenuOpen(Player player) {
        return openMenus.containsKey(player.getUniqueId());
    }
    
    /**
     * Gets the open menu for a player.
     */
    public Inventory getOpenMenu(Player player) {
        return openMenus.get(player.getUniqueId());
    }
    
    /**
     * Closes a menu for a player.
     */
    public void closeMenu(Player player) {
        openMenus.remove(player.getUniqueId());
    }
    
    /**
     * Closes all open menus.
     */
    public void closeAllMenus() {
        for (UUID uuid : new HashSet<>(openMenus.keySet())) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.closeInventory();
            }
        }
        openMenus.clear();
    }
    
    /**
     * Gets a material by name, with fallback for unknown materials.
     */
    private Material getMaterial(String name) {
        try {
            // Try to get material directly
            Material material = Material.valueOf(name.toUpperCase());
            return material;
        } catch (IllegalArgumentException e) {
            // Fallback materials
            return switch (name.toLowerCase()) {
                case "server", "compass" -> Material.COMPASS;
                case "emerald" -> Material.EMERALD;
                case "diamond" -> Material.DIAMOND;
                case "gold_ingot", "gold" -> Material.GOLD_INGOT;
                case "iron_ingot", "iron" -> Material.IRON_INGOT;
                case "grass_block", "grass" -> Material.GRASS_BLOCK;
                case "stone" -> Material.STONE;
                case "diamond_block" -> Material.DIAMOND_BLOCK;
                case "gold_block" -> Material.GOLD_BLOCK;
                default -> Material.PAPER; // Ultimate fallback
            };
        }
    }
}
