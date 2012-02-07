package de.xghostkillerx.colorme;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SuffixCommands implements CommandExecutor {

	public ColorMe plugin;
	public SuffixCommands(ColorMe instance) {
		plugin = instance;
	}
	private String pluginPart = "suffix", message, target, suffix, senderName, world = "default", globalSuffix;
	private Double cost;

	// Commands for suffixing
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		// Reloads the configs
		if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
			if (sender.hasPermission("suffixer.reload")) {
				Actions.reload(sender);
				return true;
			}
			else {
				message = ColorMe.localization.getString("permission_denied");
				ColorMe.message(sender, null, message, null, null, null, null);
				return true;
			}
		}
		// Stop here if suffixer is unwanted
		if (ColorMe.config.getBoolean("Suffixer") == false) {
			message = ColorMe.localization.getString("part_disabled");
			ColorMe.message(sender, null, message, null, null, null, null);
			return true;
		}
		// Displays the help
		if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
			Actions.help(sender, "suffix");
			return true;
		}
		// Sets the global suffix
		if (args.length > 1 && args[0].equalsIgnoreCase("global")) {
			globalSuffix = args[1];
			if (sender.hasPermission("suffixer.global")) {
				ColorMe.config.set("global_default.suffix", globalSuffix);
				plugin.saveConfig();
				message = ColorMe.localization.getString("global_change_suffix");
				ColorMe.message(sender, null, message, globalSuffix, null, null, null);
				return true;
			}
			else {
				message = ColorMe.localization.getString("permission_denied");
				ColorMe.message(sender, null, message, null, null, null, null);
				return true;
			}
		}
		// Removes a suffix
		if (args.length > 1 && args[0].equalsIgnoreCase("remove")) {
			world = "default";
			target = args[1].toLowerCase();
			// Support for "me" -> this is the senderName!
			if (target.equalsIgnoreCase("me")) {
				target = sender.getName().toLowerCase();
				// Tell console only ingame command
				if (sender instanceof ConsoleCommandSender) {
					message = ColorMe.localization.getString("only_ingame");
					ColorMe.message(sender, null, message, null, null, null, null);
					return true;
				}
			}
			senderName = sender.getName().toLowerCase();
			if (args.length > 2) {
				world = args[2].toLowerCase();
			}
			// Check for permission or self
			if (sender.hasPermission("suffixer.remove") || Actions.self(sender, target)) {
				// Trying to remove a suffix from a suffix-less player
				if (((!Actions.has(target, world, pluginPart) && ColorMe.players.contains(target + "." + pluginPart + "." + world)))
						|| !ColorMe.players.contains(target)) {
					// Self
					if (target.equalsIgnoreCase(senderName)) {
						message = ColorMe.localization.getString("no_suffix_self");
						ColorMe.message(sender, null, message, null, world, null, null);
						return true;
					}
					// Other
					message = ColorMe.localization.getString("no_suffix_other");
					ColorMe.message(sender, null, message, null, world, target, null);
					return true;
				}
				// Remove suffix
				Actions.remove(target, world, pluginPart);
				if (plugin.getServer().getPlayerExact(target) != null) {
					// Notify player is online
					Player player = plugin.getServer().getPlayerExact(target);
					message = ColorMe.localization.getString("removed_suffix_self");
					ColorMe.message(null, player, message, null, world, null, null);
				}
				// If player is offline just notify the sender
				if (!target.equalsIgnoreCase(senderName)) {
					message = ColorMe.localization.getString("removed_suffix_other");
					ColorMe.message(sender, null, message, null, world, target, null);
					return true;
				}
				return true;
			}
			// Deny access
			else {
				message = ColorMe.localization.getString("permission_denied");
				ColorMe.message(sender, null, message, null, null, null, null);
				return true;
			}
		}
		// Gets a suffix
		if (args.length > 1 && args[0].equalsIgnoreCase("get")) {
			world = "default";
			// If a player name is there, too
			target = args[1].toLowerCase();
			if (target.equalsIgnoreCase("me")) {
				target = sender.getName().toLowerCase();
				// Tell console only ingame command
				if (sender instanceof ConsoleCommandSender) {
					message = ColorMe.localization.getString("only_ingame");
					ColorMe.message(sender, null, message, null, null, null, null);
					return true;
				}
			}
			senderName = sender.getName().toLowerCase();
			if (args.length > 2) {
				world = args[2].toLowerCase();
			}
			Actions.get(target, world, pluginPart);
			// Check for permission or self
			if (sender.hasPermission("suffix.get") || Actions.self(sender, target)) {
				// Trying to get a suffix from a suffix-less player
				if (((!Actions.has(target, world, pluginPart) && ColorMe.players.contains(target)))
						|| !ColorMe.players.contains(target)) {
					// Self
					if (target.equalsIgnoreCase(senderName)) {
						message = ColorMe.localization.getString("no_suffix_self");
						ColorMe.message(sender, null, message, null, world, null, null);
						return true;
					}
					// Other
					message = ColorMe.localization.getString("no_suffix_other");
					ColorMe.message(sender, null, message, null, world, target, null);
					return true;
				}
				// Gets suffix
				if (target.equalsIgnoreCase(senderName)) {
					message = ColorMe.localization.getString("get_suffix_self");
					ColorMe.message(sender, null, message, suffix, world, null, null);
					return true;
				}
				message = ColorMe.localization.getString("get_suffix_other");
				ColorMe.message(sender, null, message, suffix, world, target, null);
				return true;
			}
			// Deny access
			else {
				message = ColorMe.localization.getString("permission_denied");
				ColorMe.message(sender, null, message, null, null, null, null);
				return true;
			}
		}
		// Suffixing
		if (args.length > 1) {
			world = "default";
			target = args[0].toLowerCase();
			if (target.equalsIgnoreCase("me")) {
				target = sender.getName().toLowerCase();
				// Tell console only ingame command
				if (sender instanceof ConsoleCommandSender) {
					message = ColorMe.localization.getString("only_ingame");
					ColorMe.message(sender, null, message, null, null, null, null);
					return true;
				}
			}
			suffix = args[1];
			senderName = sender.getName().toLowerCase();
			if (args.length > 2) {
				world = args[2].toLowerCase();
			}

			// If the suffixes are the same
			if (suffix.equalsIgnoreCase(Actions.get(target, world, pluginPart))) {
				if (senderName.equalsIgnoreCase(target)) {
					message = ColorMe.localization.getString("same_suffix_self");
					ColorMe.message(sender, null, message, null, world, null, null);
					return true;
				}	
				message = ColorMe.localization.getString("same_suffix_other");
				ColorMe.message(sender, null, message, null, world, target, null);
				return true;
			}

			// Self suffixing
			if (sender.hasPermission("suffix.self") && Actions.self(sender, target)) {
				// Without economy or costs are null
				cost = ColorMe.config.getDouble("costs.suffix");
				if (ColorMe.economy == null || cost == 0) {
					Actions.set(senderName, suffix, world, pluginPart);
					message = ColorMe.localization.getString("changed_suffix_self");
					ColorMe.message(sender, null, message, suffix, world, null, null);
					return true;
				}
				// With economy
				else if (ColorMe.economy != null){
					// Charge costs :)
					if (cost > 0 && ColorMe.economy.has(senderName, cost)) {
						ColorMe.economy.withdrawPlayer(senderName, cost);
						// Set suffix an notify sender
						Actions.set(senderName, suffix, world, pluginPart);
						message = ColorMe.localization.getString("charged");
						ColorMe.message(sender, null, message, null, null, null, cost);
						message = ColorMe.localization.getString("changed_suffix_self");
						ColorMe.message(sender, null, message, suffix, world, null, null);
						return true;
					}
					// If player hasn't got enough money
					else if (cost > 0 && ColorMe.economy.getBalance(senderName) < cost) {						
						message = ColorMe.localization.getString("not_enough_money_1");
						ColorMe.message(sender, null, message, null, null, null, cost);
						message = ColorMe.localization.getString("not_enough_money_2");
						ColorMe.message(sender, null, message, null, null, null, cost);
						return true;
					}
				}
			}
			// Suffixing other
			else if (sender.hasPermission("suffix.other") && !Actions.self(sender, target)) {
				// Set the new suffix
				Actions.set(target, suffix, world, pluginPart);
				if (plugin.getServer().getPlayerExact(target) != null) {
					// Tell the affected player
					Player player = plugin.getServer().getPlayerExact(target);
					message = ColorMe.localization.getString("changed_suffix_self");
					ColorMe.message(null, player, message, suffix, world, null, null);
				}
				message = ColorMe.localization.getString("changed_suffix_other");
				ColorMe.message(sender, null, message, suffix, world, target, null);
				return true;
			}
			// Permission check failed
			else {
				message = ColorMe.localization.getString("permission_denied");
				ColorMe.message(sender, null, message, null, null, null, null);
				return true;
			}
		}
		return false;
	}
}