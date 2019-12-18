package net.thedudemc.persistent.util;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.thedudemc.persistent.data.SaveData;
import net.thedudemc.persistent.data.SavedItems;

public class Utils {

	public static HashSet<OfflinePlayer> getPlayersWithItems() {
		HashSet<OfflinePlayer> players = new HashSet<OfflinePlayer>();
		for (SavedItems saved : SaveData.saved) {
			OfflinePlayer player = Bukkit.getOfflinePlayer(saved.getPlayer());
			players.add(player);
		}
		return players;

	}

	public static boolean hasSavedItems(Player player) {
		for (SavedItems saved : SaveData.saved) {
			if (saved.getPlayer().equals(player.getUniqueId()))
				return true;
		}
		return false;
	}

	public static SavedItems getSavedItems(Player player) {
		for (SavedItems saved : SaveData.saved) {
			if (saved.getPlayer().equals(player.getUniqueId()))
				return saved;
		}
		return null;
	}

	public static void removeSavedItems(SavedItems saved) {
		Bukkit.getLogger().info("Removing saved items from memory after being returned: " + SaveData.saved.remove(saved));
		SaveData.instance.removeFile(saved.getPlayer());
	}

	public static boolean hasSavedItems(OfflinePlayer player) {
		for (SavedItems saved : SaveData.saved) {
			if (saved.getPlayer().equals(player.getUniqueId()))
				return true;
		}
		return false;
	}

	public static SavedItems getSavedItems(OfflinePlayer player) {
		for (SavedItems saved : SaveData.saved) {
			if (saved.getPlayer().equals(player.getUniqueId()))
				return saved;
		}
		return null;
	}

}
