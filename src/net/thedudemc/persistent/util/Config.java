package net.thedudemc.persistent.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import net.thedudemc.persistent.Persistent;

public class Config {

	private Persistent plugin;

	private List<String> tags = new ArrayList<String>();

	public Config(Persistent plugin) {
		this.plugin = plugin;
		tags.add("gift");
		tags.add("special");
	}

	public void initConfig() {

		FileConfiguration config = plugin.getConfig();

		plugin.saveDefaultConfig();
		config.addDefault("saved.saveByTag", tags);

		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
}
