# Velocity Nexus - Backend Server Plugin

This is the companion plugin that must be installed on your backend Spigot/Paper servers to enable GUI functionality.

## Installation

1. Build this plugin: `mvn clean package`
2. Copy `target/velocitynexus-backend-1.0.0.jar` to your backend server's `plugins` folder
3. Restart the server

## What it does

This plugin:
- Receives menu data from Velocity Nexus proxy
- Displays inventory GUIs to players
- Sends click events back to the proxy
- Handles all client-side GUI rendering

## Requirements

- Spigot/Paper 1.16.5 or higher
- Velocity Nexus proxy plugin installed on your Velocity proxy

## Compatibility

✅ Spigot 1.16.5+  
✅ Paper 1.16.5+  
✅ Purpur 1.16.5+  
✅ All Minecraft versions from 1.16.5 to 1.21+

---

**Note**: This plugin does nothing on its own. It must be used with the Velocity Nexus proxy plugin.
