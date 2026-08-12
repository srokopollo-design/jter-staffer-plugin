# JterStaffers

**JterStaffers** is a lightweight, fully configurable staff-management plugin for **Paper 1.20.1**.

It combines three essential moderation systems into one simple plugin:

* 💬 **Staff Chat** — communicate privately with your staff team.
* 🛡️ **Staff Mode** — access a complete set of moderation tools.
* 📝 **Player Notes** — keep track of important information about players.

Everything is highly configurable through `config.yml` and `messages.yml`.

---

## 💬 Staff Chat

Use `/sc <message>` to send a private message visible only to staff members.

**Permission:** `jterstaffers.staffchat` or OP

Messages are displayed in the following format:

```text
[SC] PlayerName: message
```

---

## 🛡️ Staff Mode

Use `/staffmode` to toggle Staff Mode.

**Permission:** `jterstaffers.staffmode` or OP

When entering Staff Mode, your current:

* Inventory
* Armor
* Off-hand item
* XP
* Gamemode

are automatically saved and restored when you leave Staff Mode.

### Staff Tools

While in Staff Mode, you receive a set of protected moderation tools. These items cannot be dropped, moved, or placed.

| Tool                   | Description                                                                                                      |
| ---------------------- | ---------------------------------------------------------------------------------------------------------------- |
| 🧊 **Freeze Stick**    | Right-click a player to freeze or unfreeze them. Frozen players cannot move or use commands, but can still chat. |
| 👻 **Vanish**          | Become completely invisible to other players, including the tab list.                                            |
| 🎲 **Random Teleport** | Instantly teleport to a random online player.                                                                    |
| 🔍 **Inspect Tool**    | View detailed information about a player.                                                                        |
| 🎒 **Invsee**          | Open and live-edit another player's inventory, including armor and off-hand.                                     |
| 👥 **Player List**     | Browse all online players through a paginated GUI and access moderation actions.                                 |
| 🕶️ **Spectator**      | Toggle Spectator Mode.                                                                                           |

### Player Information

The **Inspect Tool** provides detailed information such as:

* UUID
* Gamemode
* Health
* Food level
* XP
* World
* Coordinates
* IP address
* Ping
* First join date
* Vanish status
* Frozen status

### Player List

The Player List opens a paginated GUI containing all online players.

Clicking a player opens an action menu with:

* **Info**
* **Teleport**
* **Invsee**
* **Freeze / Unfreeze**
* **Spectator**

### Standalone Commands

Most moderation tools are also available independently through commands:

```text
/vanish
/invsee
/freeze
```

### Flight & Night Vision

While in **Staff Mode and/or Vanish**, you automatically receive:

* ✈️ Flight
* 🌙 Infinite Night Vision

Your original flight and night-vision state is automatically restored once you leave both Staff Mode and Vanish.

---

## 📝 Player Notes

JterStaffers includes a simple player-note system for keeping track of important information.

**Permission:** `jterstaffers.notes` or OP

### Commands

```text
/note <player> <message>
```

Adds a note to a player. The note is automatically assigned the lowest available ID.

```text
/note <player>
```

Lists all notes belonging to a player.

```text
/note <player> remove <id>
```

Removes a specific note.

```text
/note list
```

Lists every player who currently has at least one note.

---

## 🔧 Admin & Utilities

### `/jterstaffer`

Alias:

```text
/js
```

**Permission:** `jterstaffers.admin`

Displays the plugin's command help.

You can also reload the plugin configuration without restarting the server:

```text
/jterstaffer reload
```

This reloads:

* `config.yml`
* `messages.yml`

---

## ⚙️ Configuration

JterStaffers is designed to be highly customizable.

### `config.yml`

Configure things such as:

* Enable or disable individual commands
* Command aliases
* Staff Mode items
* Item materials
* Item names
* Item lore
* Item slots
* Flight
* Night Vision
* And more

### `messages.yml`

Customize the plugin's messages and formatting.

You can change the **prefix and message text for every feature**, allowing you to completely adapt JterStaffers to your server.

English is provided by default.

---

## 📋 Requirements

* **Minecraft:** 1.20.1
* **Server:** Paper
* **Java:** Java 17+

---

## 📜 License

JterStaffers is **open source**. Feel free to use, modify, and contribute to the project.

If you find a bug or have an idea for a new feature, feel free to open an **Issue** or submit a **Pull Request**.
