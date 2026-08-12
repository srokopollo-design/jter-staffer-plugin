package com.jter.jterstaffers;

import com.jter.jterstaffers.commands.*;
import com.jter.jterstaffers.listeners.*;
import com.jter.jterstaffers.managers.*;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//MAIN CLASS
public final class JterStaffers extends JavaPlugin {

    private static JterStaffers instance;

    private MessagesManager messagesManager;
    private StaffItemsManager staffItemsManager;
    private StaffModeManager staffModeManager;
    private VanishManager vanishManager;
    private FreezeManager freezeManager;
    private NoteManager noteManager;
    private FlightVisionManager flightVisionManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.messagesManager = new MessagesManager(this);
        this.staffItemsManager = new StaffItemsManager(this);
        this.staffModeManager = new StaffModeManager(this);
        this.vanishManager = new VanishManager(this);
        this.freezeManager = new FreezeManager(this);
        this.noteManager = new NoteManager(this);
        this.noteManager.load();
        this.flightVisionManager = new FlightVisionManager(this);

        registerCommands();
        registerListeners();

        getLogger().info("JterStaffers has been enabled.");
    }

    @Override
    public void onDisable() {
        if (staffModeManager != null) {
            staffModeManager.disableAllOnShutdown();
        }
        if (noteManager != null) {
            noteManager.save();
        }
        getLogger().info("JterStaffers has been disabled.");
    }

    private void registerCommands() {
        PluginCommand sc = getCommand("sc");
        if (getConfig().getBoolean("commands.staffchat.enabled", true) && sc != null) {
            sc.setExecutor(new StaffChatCommand(this));
        }

        PluginCommand staffmode = getCommand("staffmode");
        if (getConfig().getBoolean("commands.staffmode.enabled", true) && staffmode != null) {
            staffmode.setExecutor(new StaffModeCommand(this));
        }

        PluginCommand vanish = getCommand("vanish");
        if (getConfig().getBoolean("commands.vanish.enabled", true) && vanish != null) {
            vanish.setExecutor(new VanishCommand(this));
        }

        PluginCommand invsee = getCommand("invsee");
        if (getConfig().getBoolean("commands.invsee.enabled", true) && invsee != null) {
            invsee.setExecutor(new InvseeCommand(this));
        }

        PluginCommand freeze = getCommand("freeze");
        if (getConfig().getBoolean("commands.freeze.enabled", true) && freeze != null) {
            freeze.setExecutor(new FreezeCommand(this));
        }

        PluginCommand note = getCommand("note");
        if (getConfig().getBoolean("commands.note.enabled", true) && note != null) {
            NoteCommand noteCommand = new NoteCommand(this);
            note.setExecutor(noteCommand);
            note.setTabCompleter(noteCommand);
        }

        PluginCommand jterstaffer = getCommand("jterstaffer");
        if (jterstaffer != null) {
            JterStaffersCommand jterStaffersCommand = new JterStaffersCommand(this);
            jterstaffer.setExecutor(jterStaffersCommand);
            jterstaffer.setTabCompleter(jterStaffersCommand);
        }

        applyConfiguredAliases(sc, "commands.staffchat.aliases");
        applyConfiguredAliases(staffmode, "commands.staffmode.aliases");
        applyConfiguredAliases(vanish, "commands.vanish.aliases");
        applyConfiguredAliases(invsee, "commands.invsee.aliases");
        applyConfiguredAliases(freeze, "commands.freeze.aliases");
        applyConfiguredAliases(note, "commands.note.aliases");
    }

    //EXTRA COMMANDS
    private void applyConfiguredAliases(PluginCommand command, String configPath) {
        if (command == null) return;

        List<String> configured = getConfig().getStringList(configPath);
        if (configured.isEmpty()) return;

        try {
            PluginManager pluginManager = getServer().getPluginManager();
            Field field = pluginManager.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(pluginManager);

            List<String> aliases = new ArrayList<>(command.getAliases());
            for (String alias : configured) {
                if (!aliases.contains(alias)) aliases.add(alias);
            }
            command.setAliases(aliases);

            for (String alias : aliases) {
                if (commandMap.getCommand(alias) == null) {
                    commandMap.register(alias, getDescription().getName().toLowerCase(), command);
                }
            }
        } catch (Exception e) {
            getLogger().warning("Could not register aliases for /" + command.getName() + ": " + e.getMessage());
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new StaffItemListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryProtectListener(this), this);
        getServer().getPluginManager().registerEvents(new FrozenPlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);
    }

    public static JterStaffers getInstance() {
        return instance;
    }

    public MessagesManager messages() {
        return messagesManager;
    }

    public StaffItemsManager staffItems() {
        return staffItemsManager;
    }

    public StaffModeManager staffMode() {
        return staffModeManager;
    }

    public VanishManager vanish() {
        return vanishManager;
    }

    public FreezeManager freeze() {
        return freezeManager;
    }

    public NoteManager notes() {
        return noteManager;
    }

    public FlightVisionManager flightVision() {
        return flightVisionManager;
    }
}
