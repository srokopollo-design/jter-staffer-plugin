JterStaffers is a complete staff-management plugin built for Paper 1.20.1. It bundles three core systems — staff chat, staff mode, and player notes — into a single lightweight, fully configurable plugin.

Staff Chat

/sc <message> — sends a private message visible only to staff members (permission jterstaffers.staffchat or op), formatted as [SC] name: message.

Staff Mode

/staffmode — toggles staff mode (permission jterstaffers.staffmode or op). Your inventory, armor, off-hand, XP and gamemode are automatically saved and restored when you leave.
While in staff mode you receive a set of protected tools (can't be dropped, moved, or placed):
Freeze Stick — right-click a player to freeze/unfreeze them; frozen players can't move or use commands, but can still chat.
Vanish — become completely invisible to other players, including in the tab list.
Random Teleport — instantly teleports you to a random online player.
Inspect Tool — right-click a player to view detailed info (UUID, gamemode, health, food, XP, world, coordinates, IP, ping, first join date, vanish/frozen status).
Invsee — right-click a player to open and live-edit their inventory (including armor and off-hand).
Player List — opens a paginated GUI of all online players; clicking one opens an actions menu (Info, Teleport, Invsee, Freeze/Unfreeze, Spectator).
Spectator — toggles spectator mode.
Standalone equivalents are also available as commands: /vanish, /invsee <player>, /freeze <player>.
While in staff mode and/or vanish, you automatically get flight and infinite Night Vision, restored to your original state when you leave both.

Player Notes

/note <player> <text> — adds a note to a player (auto-assigned lowest free ID).
/note <player> — lists all notes for that player.
/note remove <player> <id> — removes a specific note.
/note — lists every player who has at least one note.
Permission: jterstaffer.notes (or op).

Admin / Utility

/jterstaffer or /js (permission jterstaffers.admin) — shows a command help list, or with reload reloads config.yml and messages.yml on the fly.

Configuration

config.yml — enable/disable individual commands, set command aliases, customize every staff-mode item (material, name, lore, slot), toggle flight/night vision, and more.
messages.yml — fully customizable prefix and message text for every feature (English by default)
