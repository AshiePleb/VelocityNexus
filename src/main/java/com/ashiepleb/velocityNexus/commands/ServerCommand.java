package com.ashiepleb.velocityNexus.commands;

import com.ashiepleb.velocityNexus.menu.MenuManager;
import com.ashiepleb.velocityNexus.menu.ServerSelectorMenu;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ServerCommand implements SimpleCommand {
    private final MenuManager menuManager;
    private final ServerSelectorMenu textMenu;
    private final MiniMessage miniMessage;
    
    public ServerCommand(MenuManager menuManager, ServerSelectorMenu textMenu) {
        this.menuManager = menuManager;
        this.textMenu = textMenu;
        this.miniMessage = MiniMessage.miniMessage();
    }
    
    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(miniMessage.deserialize(
                "<red>Only players can use this command!"));
            return;
        }
        
        String[] args = invocation.arguments();
        
        // If no arguments, show GUI menu
        if (args.length == 0) {
            menuManager.openServerSelector(player);
            return;
        }
        
        // If arguments provided, connect to specific server
        String serverName = args[0];
        textMenu.connectToServer(player, serverName);
    }
    
    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("velocitynexus.server");
    }
}
