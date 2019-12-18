package net.thedudemc.persistent.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.thedudemc.persistent.Persistent;
import net.thedudemc.persistent.data.SaveData;
import net.thedudemc.persistent.data.SavedItems;
import net.thedudemc.persistent.util.Utils;

public class EventPersistItems implements Listener {

	private Persistent plugin;
	List<String> tags = new ArrayList<String>();

	public EventPersistItems(Persistent plugin) {
		this.plugin = plugin;
		tags = plugin.getConfig().getStringList("saved.saveByTag");
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();

		List<ItemStack> items = new ArrayList<ItemStack>();
		
		for (ItemStack stack : event.getDrops()) {
			
			net.minecraft.server.v1_14_R1.ItemStack item = CraftItemStack.asNMSCopy(stack);
			
			if (!item.hasTag()) break; 
			
			NBTTagCompound nbt = item.getTag();
			
			for (String key : tags) {
				if (!nbt.hasKey(key)) break;
				
				items.add(stack);
				Bukkit.getLogger().info(ChatColor.YELLOW + player.getName() + 
										ChatColor.RESET + " died with a persistent item: " + 
										ChatColor.LIGHT_PURPLE + stack.getType().name() + 
										ChatColor.RESET + "! Saving..");
			}
			
		}	
		
		if (items.isEmpty())
			return;
		
		for (ItemStack stack : items) {
			event.getDrops().remove(stack);
		}
		
		SaveData.saved.add(new SavedItems(player.getUniqueId(), items));
		plugin.saveData.save();

	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		
		if (!Utils.hasSavedItems(player)) return;
		
		SavedItems saved = Utils.getSavedItems(player);
		
		for (ItemStack stack : saved.getItems()) {
			player.getInventory().addItem(stack);
			Bukkit.getLogger().info(ChatColor.YELLOW + player.getName() + 
									ChatColor.RESET + " is respawning with a persistent item: " + 
									ChatColor.LIGHT_PURPLE + stack.getType().name() + 
									ChatColor.RESET + "! Returning to inventory!");
		}
		
		player.sendMessage(ChatColor.GOLD + "[" + 
						   ChatColor.DARK_AQUA + "Persistent Items" + 
						   ChatColor.GOLD + "] " + 
						   ChatColor.RESET + "You had special items that have been returned!");
		
		Utils.removeSavedItems(saved);
		
		plugin.saveData.save();

	}
}
