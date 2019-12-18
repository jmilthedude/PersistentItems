package net.thedudemc.persistent.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.thedudemc.persistent.Persistent;

public class Commands implements CommandExecutor {

	private Persistent plugin;

	public Commands(Persistent plugin) {
		this.plugin = plugin;
	}

	public void initCommands() {
		plugin.getCommand("tag").setExecutor(this);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command may only be run by a player in game.");
			return true;
		}

		Player player = (Player) sender;
		
		if (args.length != 1) return false;

		String tag = args[0];

		org.bukkit.inventory.ItemStack inHand = player.getInventory().getItemInMainHand();

		if (inHand == null || inHand.getType() == Material.AIR)
			return true;

		ItemStack stack = CraftItemStack.asNMSCopy(inHand);
		NBTTagCompound nbt = stack.getOrCreateTag();
		nbt.setBoolean(tag, true);
		stack.setTag(nbt);
		player.getInventory().setItemInMainHand(CraftItemStack.asBukkitCopy(stack));
		player.updateInventory();
		player.sendMessage(ChatColor.LIGHT_PURPLE + inHand.getType().name() + 
						   ChatColor.RESET + " has been given the " + 
						   ChatColor.YELLOW + tag + 
						   ChatColor.RESET + " tag!");
		return true;
	}

}
