package net.thedudemc.persistent.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemStackSerializer {
	// Conversion to Base64 code courtesy of github.com/JustRayz
	public static String toBase64(Inventory inventory) {
		return toBase64(inventory.getContents());
	}

	public static String toBase64(ItemStack[] contents) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			dataOutput.writeInt(contents.length);

			for (ItemStack stack : contents) {
				dataOutput.writeObject(stack);
			}
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static ItemStack[] fromBase64(String data) {
		if (data == null || Base64Coder.decodeLines(data) == null)
			return new ItemStack[] {};

		ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
		BukkitObjectInputStream dataInput = null;
		ItemStack[] stacks = null;

		try {
			dataInput = new BukkitObjectInputStream(inputStream);
			stacks = new ItemStack[dataInput.readInt()];
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < stacks.length; i++) {
			try {
				stacks[i] = (ItemStack) dataInput.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		try {
			dataInput.close();
		} catch (IOException e1) {
		}

		return stacks;
	}
}
