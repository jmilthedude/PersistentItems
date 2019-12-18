package net.thedudemc.persistent.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.thedudemc.persistent.util.ItemStackSerializer;
import net.thedudemc.persistent.util.Utils;

public class SaveData {

	public static SaveData instance;

	private File file;

	private JSONParser parser = new JSONParser();

	public static List<SavedItems> saved = new ArrayList<SavedItems>();

	public SaveData(File file) {
		instance = this;

		this.file = file;
		createDirectory();
	}

	public void createDirectory() {
		try {
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void createFile(File playerFile, String json) {
		try {

			FileWriter fw = new FileWriter(playerFile);
			fw.write(json);
			fw.flush();
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void removeFile(UUID player) {
		File toRemove = new File(file, player.toString() + ".json");
		try {

			PrintWriter fw = new PrintWriter(toRemove, "UTF-8");

			fw.print("{");
			fw.print("}");
			fw.flush();
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<SavedItems> deserialize(File playerFile) {
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(playerFile), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (json == null)
			return null;

		String player = playerFile.getName();
		UUID playerUuid = UUID.fromString(player.substring(0, player.length() - 5));
		List<SavedItems> savedItems = new ArrayList<SavedItems>();
		for (Object s : json.keySet()) {
			JSONObject obj = (JSONObject) json.get(s);
			ItemStack[] items = ItemStackSerializer.fromBase64((String) obj.get("items"));
			savedItems.add(new SavedItems(playerUuid, Arrays.asList(items)));
		}
		return savedItems;

	}

	@SuppressWarnings("unchecked")
	public JSONObject serialize(SavedItems savedItems) {

		try {
			JSONObject playerJson = new JSONObject();
			List<ItemStack> toSave = savedItems.getItems();
			ItemStack[] items = new ItemStack[toSave.size()];
			for (int i = 0; i < items.length; i++) {
				items[i] = toSave.get(i);
			}

			playerJson.put("items", ItemStackSerializer.toBase64(items));

			return playerJson;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public void save() {
		for (OfflinePlayer player : Utils.getPlayersWithItems()) {
			savePlayerItems(player);
		}
	}

	@SuppressWarnings("unchecked")
	public void savePlayerItems(OfflinePlayer player) {
		JSONObject toSave = new JSONObject();
		SavedItems savedItems = Utils.getSavedItems(player);
		if (savedItems != null) {
			JSONObject savedJson = serialize(savedItems);
			toSave.put(player.getName(), savedJson);
		}
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		treeMap.putAll(toSave);

		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String prettyJsonString = g.toJson(treeMap);

		createFile(new File(file, player.getUniqueId().toString() + ".json"), prettyJsonString);

	}

	public void loadPlayerSaves() {
		if (file.listFiles().length == 0)
			return;
		for (File playerFile : file.listFiles()) {
			try {
				List<SavedItems> toSave = deserialize(playerFile);
				for (SavedItems savedItems : toSave) {
					saved.add(savedItems);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private String getPlayerName(UUID uuid) {
		String playerName = "";
		Player player = Bukkit.getPlayer(uuid);
		if (player == null) {
			OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
			playerName = offline.getName();
		} else {
			playerName = player.getName();
		}
		return playerName;
	}
}
