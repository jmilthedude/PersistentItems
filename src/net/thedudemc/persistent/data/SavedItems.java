package net.thedudemc.persistent.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class SavedItems {

	private UUID player;
	private List<ItemStack> items;

	public SavedItems(UUID player, List<ItemStack> items) {
		this.player = player;
		this.items = items;
	}

	public UUID getPlayer() {
		return player;
	}

	public void setPlayer(UUID player) {
		this.player = player;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void setItems(List<ItemStack> items) {
		this.items = items;
	}

	public void addItem(ItemStack stack) {
		items.add(stack);
	}

}
