package net.thedudemc.persistent;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import net.thedudemc.persistent.command.Commands;
import net.thedudemc.persistent.config.Config;
import net.thedudemc.persistent.data.SaveData;
import net.thedudemc.persistent.event.EventPersistItems;

public class Persistent extends JavaPlugin {

	public Commands commands = new Commands(this);
	public Config config = new Config(this);

	public SaveData saveData;

	@Override
	public void onEnable() {
		commands.initCommands();
		config.initConfig();

		saveData = new SaveData(new File(this.getDataFolder(), "saved" + File.separator));
		saveData.loadPlayerSaves();

		getServer().getPluginManager().registerEvents(new EventPersistItems(this), this);

	}

	@Override
	public void onDisable() {
		saveData.save();
	}

}
