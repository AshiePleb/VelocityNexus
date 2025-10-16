# 🚀 Velocity Nexus

**Transform your Velocity proxy with a true inventory GUI server selector!**

Velocity Nexus replaces the default `/server` command with a beautiful, **clickable inventory GUI** interface. Players navigate between servers using an interactive inventory menu with custom items, real-time player counts, and stunning gradient text formatting.

---

## ✨ Features

🎮 **True Inventory GUI** - Real clickable inventory interface (like popular server selector plugins!)  
🎯 **Clickable Items** - Each server represented by customizable Minecraft materials  
🎨 **Beautiful Gradients** - MiniMessage formatting with eye-catching colors  
📊 **Real-time Player Counts** - See live player counts for each server  
🔐 **Permission System** - Control server access per-player or per-group  
⚙️ **Hot Reload** - Update configuration without restarting  
🌐 **Cross-version** - Works on Minecraft 1.16.5 to 1.21+  
💎 **Custom Materials** - Use any Minecraft item (compass, diamond, emerald, etc.)

---

## 📦 Quick Start

### Requirements
- **Velocity Proxy** (3.0.0+)
- **Spigot/Paper Backend Servers** (1.16.5+)
- **Java 17** or higher

### Installation

1. **Build or download both plugins** (see [BUILD.md](BUILD.md))
   - `velocitynexus-1.0.0-velocity.jar` (proxy plugin)
   - `velocitynexus-1.0.0-spigot.jar` (backend plugin)

2. **Install proxy plugin**
   ```
   Copy velocitynexus-1.0.0-velocity.jar to /velocity/plugins/
   ```

3. **Install backend plugin on ALL servers** ⚠️ REQUIRED
   ```
   Copy velocitynexus-1.0.0-spigot.jar to /server1/plugins/
   Copy velocitynexus-1.0.0-spigot.jar to /server2/plugins/
   Copy velocitynexus-1.0.0-spigot.jar to /server3/plugins/
   ```

4. **Restart everything**
   - Restart Velocity proxy
   - Restart all backend servers

5. **Test it!**
   - Join your network
   - Type `/server`
   - Enjoy your new GUI! 🎉

---

## 🎮 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/server` | Open inventory GUI server selector | `velocitynexus.server` |
| `/server <name>` | Connect directly to a specific server | `velocitynexus.server` |
| `/velocitynexus` | Show plugin information and commands | None |
| `/velocitynexus reload` | Reload configuration | `velocitynexus.admin` |
| `/velocitynexus version` | Show plugin version | None |

---

## ⚙️ Configuration

Configuration file: `plugins/velocitynexus/config.toml` (on Velocity proxy)

### Basic Example (3 Servers)

```toml
[menu]
title = "<gradient:#00D9FF:#7B2FBE>✨ Server Selector ✨</gradient>"
rows = 3

[servers.lobby]
display-name = "<yellow>🏠 Lobby</yellow>"
description = [
    "<gray>Main hub server",
    "",
    "<green>Click to join!"
]
icon = "COMPASS"
slot = 11
enabled = true

[servers.survival]
display-name = "<green>⚔️ Survival</green>"
description = [
    "<gray>Vanilla survival experience",
    "<yellow>Players online: %players%",
    "",
    "<green>Click to join!"
]
icon = "GRASS_BLOCK"
slot = 13
enabled = true

[servers.creative]
display-name = "<aqua>🎨 Creative</aqua>"
description = [
    "<gray>Build your dreams",
    "<yellow>Players online: %players%",
    "",
    "<green>Click to join!"
]
icon = "DIAMOND_BLOCK"
slot = 15
enabled = true

[settings]
update-player-count = true
sounds-enabled = true
override-server-command = true
offline-server-message = "<red>This server is currently offline!"
no-permission-message = "<red>You don't have permission to access this server!"
```

### Configuration Options

#### `[menu]` Section
- **`title`** - Menu title (supports MiniMessage formatting)
- **`rows`** - Number of rows (1-6)

#### `[servers.<name>]` Section
- **`display-name`** - Server name shown in GUI (supports MiniMessage)
- **`description`** - Array of lore lines (use `%players%` for player count)
- **`icon`** - Minecraft material name (e.g., `COMPASS`, `DIAMOND`, `GRASS_BLOCK`)
- **`slot`** - Inventory slot (0-53, based on rows × 9)
- **`enabled`** - Enable/disable this server
- **`permission`** - Optional permission required to see this server

#### `[settings]` Section
- **`update-player-count`** - Show real-time player counts
- **`sounds-enabled`** - Play sounds (reserved for future use)
- **`override-server-command`** - Replace default `/server` command
- **`offline-server-message`** - Message when server is offline
- **`no-permission-message`** - Message when player lacks permission

### Inventory Slot Reference

```
Rows = 3 (27 slots):
┌───┬───┬───┬───┬───┬───┬───┬───┬───┐
│ 0 │ 1 │ 2 │ 3 │ 4 │ 5 │ 6 │ 7 │ 8 │
├───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 9 │10 │11 │12 │13 │14 │15 │16 │17 │  ← Place servers here
├───┼───┼───┼───┼───┼───┼───┼───┼───┤
│18 │19 │20 │21 │22 │23 │24 │25 │26 │
└───┴───┴───┴───┴───┴───┴───┴───┴───┘

Popular layouts:
• 3 servers: slots 11, 13, 15 (centered)
• 5 servers: slots 10, 11, 12, 13, 14
• 7 servers: slots 10, 11, 12, 13, 14, 15, 16
```

### Color Formatting (MiniMessage)

```toml
# Gradients
display-name = "<gradient:#FF0000:#0000FF>Rainbow Server</gradient>"

# Hex colors
display-name = "<#FF5733>Custom Color Server</#FF5733>"

# Preset colors
display-name = "<red>Red</red> <gold>Gold</gold> <yellow>Yellow</yellow>"

# Multiple effects
display-name = "<bold><gradient:#FFD700:#FFA500>VIP Server</gradient></bold>"

# With emojis
display-name = "🏠 <yellow>Lobby</yellow>"
```

### Popular Materials

| Material | Best For |
|----------|----------|
| `COMPASS` | Main lobby, hub |
| `DIAMOND` | Premium servers |
| `EMERALD` | Economy, survival |
| `GRASS_BLOCK` | Survival servers |
| `DIAMOND_BLOCK` | Creative, building |
| `NETHER_STAR` | VIP/Special servers |
| `DIAMOND_SWORD` | PvP servers |
| `FEATHER` | SkyWars, flying |
| `RED_BED` | BedWars |
| `GOLD_INGOT` | Economy servers |

[Full Material List](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)

---

## 🔐 Permissions

### Default Permissions
- **`velocitynexus.server`** - Access to `/server` command (default: everyone)
- **`velocitynexus.admin`** - Admin commands like reload (default: ops only)

### Per-Server Permissions (Optional)
Add `permission` to any server to restrict access:

```toml
[servers.vip]
display-name = "⭐ VIP Lounge"
icon = "NETHER_STAR"
permission = "velocitynexus.server.vip"  # Only players with this permission
```

### LuckPerms Examples

```bash
# Give everyone basic access
lp group default permission set velocitynexus.server true

# Give admins reload permission
lp group admin permission set velocitynexus.admin true

# Give VIP access to VIP server
lp group vip permission set velocitynexus.server.vip true

# Give specific player access
lp user Steve permission set velocitynexus.server.vip true
```

---

## 📚 Example Configurations

### Minigames Network

```toml
[menu]
title = "<gradient:#FF0000:#00FF00>🎮 Minigames Menu</gradient>"
rows = 3

[servers.lobby]
display-name = "🏠 <yellow>Lobby</yellow>"
icon = "COMPASS"
slot = 4
description = ["<green>Return to lobby"]
enabled = true

[servers.skywars]
display-name = "<aqua>☁️ SkyWars</aqua>"
icon = "FEATHER"
slot = 10
description = ["<gray>Sky battles!", "<yellow>%players% playing"]
enabled = true

[servers.bedwars]
display-name = "<red>🛏️ BedWars</red>"
icon = "RED_BED"
slot = 12
description = ["<gray>Protect your bed!", "<yellow>%players% playing"]
enabled = true

[servers.duels]
display-name = "<gold>⚔️ Duels</gold>"
icon = "IRON_SWORD"
slot = 14
description = ["<gray>1v1 battles", "<yellow>%players% playing"]
enabled = true
```

### Network with VIP Server

```toml
[menu]
title = "<gradient:#FFD700:#FFA500>✨ Premium Network ✨</gradient>"
rows = 4

[servers.lobby]
display-name = "🏠 <yellow>Lobby</yellow>"
icon = "EMERALD"
slot = 13
description = ["<gray>Welcome!", "", "<green>Everyone welcome!"]
enabled = true

[servers.survival]
display-name = "⚔️ <green>Survival</green>"
icon = "DIAMOND_SWORD"
slot = 20
description = ["<gray>Vanilla mode", "<yellow>%players% online"]
enabled = true

[servers.vip]
display-name = "⭐ <gradient:#FFD700:#FF8C00>VIP Lounge</gradient>"
icon = "NETHER_STAR"
slot = 24
description = ["<gold>Exclusive area", "<gray>Members only"]
enabled = true
permission = "velocitynexus.server.vip"
```

---

## 🐛 Troubleshooting

### GUI Not Opening?
✅ Install backend plugin on **ALL** Spigot/Paper servers  
✅ Verify player is connected to a backend server (not in limbo)  
✅ Check console for errors on both proxy and backend  
✅ Confirm both plugins are version 1.0.0

### Backend Plugin Not Loading?
✅ Ensure Spigot/Paper 1.16.5 or higher  
✅ Verify Java 17+ is installed  
✅ Look for errors in server console  
✅ Check plugin is named `velocitynexus-backend-1.0.0.jar`

### Items Not Appearing in GUI?
✅ Use valid material names (uppercase: `COMPASS`, not `compass`)  
✅ Check [Bukkit Materials](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)  
✅ Verify slot numbers are within range (0 to rows×9-1)

### Server Not in Menu?
✅ Server name in `config.toml` must match `velocity.toml` **EXACTLY** (case-sensitive!)  
   - If your server is `Hub` in velocity.toml, use `[servers.Hub]` in config.toml  
   - If your server is `lobby` in velocity.toml, use `[servers.lobby]` in config.toml  
✅ Set `enabled = true` in server config  
✅ Check player has required permission (if set)  
✅ Run `/velocitynexus reload` after config changes

### Colors Not Working?
✅ Use MiniMessage format: `<red>text</red>`  
✅ Check for typos in gradient syntax  
✅ Ensure strings are quoted in TOML: `"<red>text</red>"`

---

## 🎨 Premium GUI Design

The default configuration features a **premium, modern design** with elegant purple-pink gradients and decorative borders.

### Visual Layout (5-Row Design)

```
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃ [🟪][🟪][🟣][🟣][BEACON][🟣][🟣][🟪][🟪] ┃  Row 1
┃                 ✦ PREMIUM ✦             ┃
┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
┃ [🟪]    [ ][ ][ ][ ][ ][ ][ ]    [🟪] ┃  Row 2
┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
┃ [🟪]    [ ][ ][ ][⭐ HUB][ ][ ]   [🟪] ┃  Row 3
┃              (Nether Star)              ┃  CENTER
┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
┃ [🟪]    [ ][ ][ ][ ][ ][ ][ ]    [🟪] ┃  Row 4
┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
┃ [🟪][🟪][🟣][🟣][BOOK][🟣][🟣][🟪][🟪] ┃  Row 5
┃                ℹ INFO                   ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

🟪 = Purple Glass Pane (decorative border)
🟣 = Magenta Glass Pane (decorative accent)
⭐ = HUB Server (Nether Star - slot 22, center)
[ ] = Empty slots ready for more servers
```

### Default Hub Server

**Display Name**: `<gradient:#ffecd2:#fcb69f>🏠 HUB</gradient>` (Peach to Coral)  
**Icon**: Nether Star ⭐ (premium glowing effect)  
**Position**: Slot 22 (exact center of 5-row GUI)  
**Description**: 11 lines with gradients, emojis, live player count

**Hub Features:**
- ▸ Beautiful gradient title
- ⚡ "Always Online" indicator
- 👥 Live player count
- ❖ Elegant call-to-action

### Color Scheme

**Title Gradient**: `#667eea → #764ba2 → #f093fb` (Purple → Violet → Pink)  
**Hub Name**: `#ffecd2 → #fcb69f` (Peach → Coral, warm welcome)  
**Accents**: `#a8edea → #fed6e3` (Aqua → Pink, fresh and modern)  
**Borders**: Purple & Magenta glass panes for luxury feel

### Design Philosophy

✨ **Premium Aesthetic** - Glass pane borders, nether star icon, luxury colors  
🎯 **Center-Focused** - Hub at exact center draws attention  
⚖️ **Symmetrical** - Perfect left/right balance  
📐 **Spacious** - 5 rows provides breathing room  
🎨 **Modern** - Contemporary gradients and clean design  
🔮 **Expandable** - Empty slots ready for 10+ more servers

### Adding More Servers

Prime locations near hub (slots closest to center):

**Recommended positions:**
- Slot 13 (left of hub) - Perfect for main gamemode
- Slot 31 (below hub) - Good for secondary server  
- Slots 12, 14, 21, 23 - Surrounding positions
- Slots 11, 15, 30, 32 - Outer ring

**Example - Add Survival:**
```toml
[servers.Survival]
display-name = "<gradient:#00ff87:#60efff>⚔️ SURVIVAL</gradient>"
description = [
    "",
    "<gradient:#667eea:#764ba2>  ▸ Vanilla Experience</gradient>",
    "",
    "<gray>  Challenge yourself",
    "<yellow>  👥 Players: <white>%players%",
    "",
    "<gradient:#a8edea:#fed6e3>  ❖ Click to join ❖</gradient>",
    ""
]
icon = "DIAMOND_SWORD"
slot = 13  # Left of hub
enabled = true
```

### Premium Materials

Best items for the luxury aesthetic:

| Material | Effect | Use For |
|----------|--------|---------|
| `NETHER_STAR` | Glowing | Main hub, VIP |
| `BEACON` | Animated | Info, special |
| `ENCHANTED_GOLDEN_APPLE` | Shiny | Premium content |
| `DRAGON_HEAD` | Unique | Boss servers |
| `TOTEM_OF_UNDYING` | Gold | Hardcore |
| `NETHERITE_INGOT` | Dark luxury | Elite servers |

### Alternative Color Schemes

**Ocean Blue** (Professional):
```toml
title = "<gradient:#4facfe:#00f2fe>✦ SERVER SELECTOR ✦</gradient>"
```

**Sunset Orange** (Warm):
```toml
title = "<gradient:#fa709a:#fee140>✦ SERVER SELECTOR ✦</gradient>"
```

**Royal Gold** (Luxury):
```toml
title = "<gradient:#f7971e:#ffd200>✦ SERVER SELECTOR ✦</gradient>"
```

---

## 🔧 How It Works

### Architecture
- **Two-plugin system**: Velocity proxy plugin + Spigot/Paper backend plugin
- **Plugin messaging**: Uses `velocitynexus:menu` channel for communication
- **Data format**: JSON-serialized menu data sent via plugin messages
- **GUI rendering**: Backend plugin creates Bukkit inventories from received data
- **Click handling**: Backend sends click events back to proxy

### Flow
1. Player executes `/server` on proxy
2. Proxy sends menu data to player's current backend server
3. Backend plugin opens inventory GUI
4. Player clicks item in inventory
5. Backend sends click event to proxy
6. Proxy connects player to selected server

### Fallback System
If backend plugin is missing or player is not on a backend server, a text-based menu appears in chat.

---

## 🤝 Contributing

Contributions welcome! Please submit a Pull Request.

---

## 📝 License

This project is licensed under the MIT License.

---

## 🙏 Acknowledgments

- Built for [Velocity Proxy](https://papermc.io/software/velocity)
- Uses [Adventure](https://docs.advntr.dev/) for text components
- Configuration powered by [TOML4J](https://github.com/mwanji/toml4j)
- Backend compatibility with [Spigot](https://www.spigotmc.org/) and [Paper](https://papermc.io/)

---

**Made with ❤️ by AshiePleb**  
**Version**: 1.0.0  
**Release Date**: October 15, 2025
