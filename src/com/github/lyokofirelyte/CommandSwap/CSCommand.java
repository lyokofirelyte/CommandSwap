package com.github.lyokofirelyte.CommandSwap;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CSCommand implements CommandExecutor {

	 CSMain plugin;
	 public CSCommand(CSMain instance){
     this.plugin = instance;
	 }
	 
	 public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		 
		 if (cmd.getName().equalsIgnoreCase("cswap")){
			 
			 if (args.length <= 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))){
				 
				 sender.sendMessage(new String[]{
						 
					CSMain.AS("&a| &9/cswap add <alias> <command> &f// &9Changes <command> to <alias> for yourself."),	 
					CSMain.AS("&a| &9/cswap rem <alias> &f// &9Remove one of your aliases."),
					CSMain.AS("&a| &9/cswap list &f// &9View your set aliases."),
					CSMain.AS("&a| &9/cswap placeholders &f// &9View placeholders you can use."),
					CSMain.AS("&a| &9/cswap block <alias> &f// &9Prevent an alias from working."),	
					CSMain.AS("&a| &9/cswap unblock <alias> &f// &9Allow an alias to work again."),	
					CSMain.AS("&a| &9/cswap save &f// &9Save config and data files."),	
					CSMain.AS("&a| &9/cswap reload &f// &9Reload config from file if you made any changes."),
					CSMain.AS("&a| &9/cswap backup &f// &9Save a copy of the config and data to a backup folder."),
					CSMain.AS("&a| &9/cswap disable &f// &9Kill the plugin. Kill it naow."),
					CSMain.AS("&7&oYou are running CommandSwap version 1.0 for 1.6.2 & 1.6.4.")
				 });
				 
				 return true;
			 }
			 
			 switch (args[0]){
			 
			 default: 
				 
				 sender.sendMessage(CSMain.header + "Try /cswap help for information!");
				 break;
				 
			 case "placeholders":
				 
				 sender.sendMessage(new String[]{	 
				 CSMain.AS("&a| &9%args0 up to %args20 &f// &9Replaced with the argument you typed."),
				 CSMain.AS("&7&oExample: /cswap add b pex user %args0 group add %args1 &f// &7&o/b Hugh_Jasses Member"),
				 CSMain.AS("&a| &9%c &f// &9Add this TO THE END of your final command to execute it as console (needs cs.console perms)."),
				 CSMain.AS("&7&oExample: /b Hugh_Jasses Member %c") 		
				 });
				 
			 break;
			 
			 case "list":

				 if (sender instanceof Player == false){
					 sender.sendMessage(CSMain.header + "Silly console, alias lists are for players!");
					 break;
				 }
				 
				 List <String> aliases = plugin.datacore.getStringList("Users." + sender.getName() + ".Aliases");
				 
				 if (aliases.size() == 0){
					 sender.sendMessage(CSMain.header + "You have no aliases defined.");
					 break;
				 }
				 
				 sender.sendMessage(CSMain.header + "Defined Aliases:");
				 List <String> blockedCommands = plugin.datacore.getStringList("Blocked");
				 
				 	for (String bleh : aliases){
				 		
				 		String swapped = plugin.datacore.getString("Users." + sender.getName() + "." + bleh);
				 		sender.sendMessage(CSMain.AS("&a| " + bleh + " &f-> &a" + swapped + "&a."));
				 	}
				 	
				 if (blockedCommands.size() > 0){
					 sender.sendMessage(CSMain.AS("&f-----"));
					 sender.sendMessage(CSMain.header + "Globally Blocked:");
					 
					 for (String bleh : blockedCommands){
					 		sender.sendMessage(CSMain.AS("&4| &c" + bleh + "&4."));
					 	}
				 }
				 	
				 	break;
			 
			 case "add":
				 
				 if (sender instanceof Player == false){
					 sender.sendMessage(CSMain.header + "Sorry, console can't set an alias for itself. That would be pretty unwise.");
					 break;
				 }
				 
				 if (args.length < 3){
					 sender.sendMessage(CSMain.header + "/cswap add <alias> <command> (You can use a full command, like /home bed or something.)");
					 break;
				 }
				 
				 String message = createString(args, 2);
			 
				 aliases = plugin.datacore.getStringList("Users." + sender.getName() + ".Aliases");
				 
				 	if (aliases.contains(args[1])){
				 		sender.sendMessage(CSMain.header + "You've already defined that alias. Try something else or remove it! :)");
				 		break;
				 	}
				 	
				 if (message.startsWith("/") || args[1].startsWith("/")){
					 sender.sendMessage(CSMain.header + "Don't put a slash in your command - CS will do that for you.");
					 break;
				 }
				 	
				 aliases.add(args[1]);
				 plugin.datacore.set("Users." + sender.getName() + ".Aliases", aliases);
				 plugin.datacore.set("Users." + sender.getName() + "." + args[1], message);
				 sender.sendMessage(CSMain.header + "The command /" + args[1] + "§9§o will now execute /" + message + "§9.");
				 break;
				 
			 case "rem":
				 
				 if (sender instanceof Player == false){
					 sender.sendMessage(CSMain.header + "Sorry, console can't set an alias for itself. That would be pretty unwise.");
					 break;
				 }
				 
				 if (args.length < 2){
					 sender.sendMessage(CSMain.header + "/cswap rem <alias>");
					 break;
				 }

			 
				 aliases = plugin.datacore.getStringList("Users." + sender.getName() + ".Aliases");
				 
				 	if (aliases.contains(args[1]) == false){
				 		sender.sendMessage(CSMain.header + "You've not defined that alias. Try something else! :)");
				 		break;
				 	}
				 	
				 aliases.remove(args[1]);
				 plugin.datacore.set("Users." + sender.getName() + ".Aliases", aliases);
				 plugin.datacore.set("Users." + sender.getName() + "." + args[1], null);
				 sender.sendMessage(CSMain.header + "Alias removed.");
				 break;
				 
				 	
			 case "block":
				 
				 if (sender.hasPermission("cs.admin") == false){
					 sender.sendMessage(CSMain.header + "You require the permission node cs.admin to use this command.");
					 break;
				 }
				 
				 if (args.length != 2){
					 sender.sendMessage(CSMain.header + "/cswap block <alias>.");
					 break;
				 }
				 
				 blockedCommands = plugin.datacore.getStringList("Blocked");
				 
				 	if (blockedCommands.contains(args[1])){
				 		sender.sendMessage(CSMain.header + "That command is already blocked from having an alias.");
				 		break;
				 	}
				 	
				 blockedCommands.add(args[1]);
				 plugin.datacore.set("Blocked", blockedCommands);
				 sender.sendMessage(CSMain.header + "Blocked " + args[1] + ".");
				 break;
				 
			 case "unblock":
				 
				 if (sender.hasPermission("cs.admin") == false){
					 sender.sendMessage(CSMain.header + "You require the permission node cs.admin to use this command.");
					 break;
				 }
				 
				 if (args.length != 2){
					 sender.sendMessage(CSMain.header + "/cswap unblock <command>.");
					 break;
				 }
				 
				 blockedCommands = plugin.datacore.getStringList("Blocked");
				 
				 	if (blockedCommands.contains(args[1]) == false){
				 		sender.sendMessage(CSMain.header + "That command isn't blocked from having an alias.");
				 		break;
				 	}
				 	
				 blockedCommands.remove(args[1]);
				 plugin.datacore.set("Blocked", blockedCommands);
				 sender.sendMessage(CSMain.header + "Unblocked " + args[1] + ".");
				 break;
			 	
			 case "disable":
				 
				 if (sender.hasPermission("cs.manage") == false){
					 sender.sendMessage(CSMain.header + "You require the permission node cs.manage to use this command.");
					 break;
				 }
				 
				 sender.sendMessage(CSMain.header + "Thanks for using CS! Goodbye.");
				 Bukkit.getServer().getPluginManager().disablePlugin(plugin);
				 break;
				 
			 case "save":
				 
				 if (sender.hasPermission("cs.manage") == false){
					 sender.sendMessage(CSMain.header + "You require the permission node cs.manage to use this command.");
					 break;
				 }
				 
				 plugin.saveYamls();
				 sender.sendMessage(CSMain.header + "Saved everything!");
				 break;
				 
			 case "reload":
				 
				 if (sender.hasPermission("cs.manage") == false){
					 sender.sendMessage(CSMain.header + "You require the permission node cs.manage to use this command.");
					 break;
				 }
				 
				 plugin.loadYamls();
				 plugin.saveYamls();
				 sender.sendMessage(CSMain.header + "Reloaded everything!");
				 break;
				 
			 case "backup":
				 
				 if (sender.hasPermission("cs.manage") == false){
					 sender.sendMessage(CSMain.header + "You require the permission node cs.manage to use this command.");
					 break;
				 }
				 
				 plugin.backupYamls();
				 sender.sendMessage(CSMain.header + "Backed up everything!");
				 break;
		 }
		 
		 }
		 
		 return true;
	 }
	 
	  public static String createString(String[] args, int x)
	  {
	    StringBuilder sb = new StringBuilder();
	    for (int i = x; i < args.length; i++)
	    {
	      if ((i != x) && (i != args.length))
	      {
	        sb.append(" ");
	      }
	      sb.append(args[i]);
	    }
	    return sb.toString();
	  }
}
