package com.ashiepleb.velocityNexus.commands;

import com.ashiepleb.velocityNexus.VelocityNexus;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class VelocityNexusCommand implements SimpleCommand {
    private final VelocityNexus plugin;
    private final MiniMessage miniMessage;
    
    public VelocityNexusCommand(VelocityNexus plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
    }
    
    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        
        if (args.length == 0) {
            // Always show help/info when running /velocitynexus with no args
            sendInfo(invocation);
            return;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "reload" -> {
                if (!invocation.source().hasPermission("velocitynexus.admin")) {
                    invocation.source().sendMessage(miniMessage.deserialize(
                        "<red>You don't have permission to use this command!"));
                    return;
                }
                
                plugin.reloadConfig();
                invocation.source().sendMessage(miniMessage.deserialize(
                    "<green>✓ Configuration reloaded successfully!"));
            }
            
            case "help", "info" -> sendInfo(invocation);
            
            case "version", "ver" -> {
                invocation.source().sendMessage(miniMessage.deserialize(""));
                invocation.source().sendMessage(miniMessage.deserialize(
                    "<gradient:#00D9FF:#7B2FBE><bold>Velocity Nexus</bold></gradient> <gray>v1.0.0"));
                invocation.source().sendMessage(miniMessage.deserialize(
                    "<gray>Made by <white>AshiePleb"));
                invocation.source().sendMessage(miniMessage.deserialize(""));
            }
            
            default -> invocation.source().sendMessage(miniMessage.deserialize(
                "<red>Unknown subcommand. Use <yellow>/velocitynexus help</yellow> for help."));
        }
    }
    
    private void sendInfo(Invocation invocation) {
        invocation.source().sendMessage(miniMessage.deserialize(""));
        invocation.source().sendMessage(miniMessage.deserialize(
            "<gradient:#00D9FF:#7B2FBE><bold>Velocity Nexus</bold></gradient> <gray>v1.0.0"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "<gray>Transform your Velocity proxy with inventory GUI server selection!"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "<gray><strikethrough>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</strikethrough>"));
        invocation.source().sendMessage(miniMessage.deserialize(""));
        invocation.source().sendMessage(miniMessage.deserialize(
            "<gold><bold>Commands:</bold>"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "  <yellow>/server</yellow> <dark_gray>→</dark_gray> <gray>Open inventory GUI server selector"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "  <yellow>/server <name></yellow> <dark_gray>→</dark_gray> <gray>Connect to a specific server"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "  <yellow>/velocitynexus</yellow> <dark_gray>→</dark_gray> <gray>Show plugin information"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "  <yellow>/velocitynexus reload</yellow> <dark_gray>→</dark_gray> <gray>Reload configuration <dark_gray>(admin)</dark_gray>"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "  <yellow>/velocitynexus version</yellow> <dark_gray>→</dark_gray> <gray>Show plugin version"));
        invocation.source().sendMessage(miniMessage.deserialize(""));
        invocation.source().sendMessage(miniMessage.deserialize(
            "<gold><bold>Features:</bold>"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "  <green>✓</green> <gray>Inventory GUI with clickable items"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "  <green>✓</green> <gray>Real-time player counts"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "  <green>✓</green> <gray>Beautiful gradient formatting"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "  <green>✓</green> <gray>Per-server permissions"));
        invocation.source().sendMessage(miniMessage.deserialize(""));
        invocation.source().sendMessage(miniMessage.deserialize(
            "<gray><strikethrough>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</strikethrough>"));
        invocation.source().sendMessage(miniMessage.deserialize(
            "<gray>Made with <red>❤</red> by <white>AshiePleb"));
        invocation.source().sendMessage(miniMessage.deserialize(""));
    }
    
    @Override
    public boolean hasPermission(Invocation invocation) {
        return true; // Everyone can see plugin info
    }
}
