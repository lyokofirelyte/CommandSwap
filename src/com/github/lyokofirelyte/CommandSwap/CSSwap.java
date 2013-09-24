package com.github.lyokofirelyte.CommandSwap;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


public class CSSwap implements Listener {

	  CSMain plugin;
	  public CSSwap(CSMain instance){
      this.plugin = instance;
	  }
	  
	  @EventHandler (priority = EventPriority.HIGH)
	  public void onEx(PlayerCommandPreprocessEvent e){
		  
		  if (e.isCancelled() && plugin.config.getBoolean("NoOverride")){
			  if (plugin.config.getBoolean("DebugMode")){
				  Bukkit.getServer().getConsoleSender().sendMessage(CSMain.AS(CSMain.header + e.getPlayer().getName() + " attempted to override command - failed @ already cancelled!"));
			  }
			  return;
		  }
		  
		  List <String> aliases = plugin.datacore.getStringList("Users." + e.getPlayer().getName() + ".Aliases");
		  
		  	for (String bleh : aliases){
		  		
		  		if (e.getMessage().startsWith("/" + bleh)){
		  			
		  			List <String> blockedCommands = plugin.datacore.getStringList("Blocked");
		  			Boolean replace = false;
		  			
		  				if (blockedCommands.contains(bleh) == false){
		  					e.setCancelled(true);
		  					
		  					
		  					List <String> allowedArgs = new ArrayList<String>();
		  					int x = 0;
		  					
		  						while (x <= 20){
		  							allowedArgs.add("%args" + x);
		  							x++;
		  						}
		  						
		  					x = 0;
		  					
		  					String command = plugin.datacore.getString("Users." + e.getPlayer().getName() + "." + bleh);
	  						plugin.datacore.set("Users." + e.getPlayer().getName() + ".TempStorage", command);
	  						
		  					for (String blargh : allowedArgs){
		  						
		  						command = plugin.datacore.getString("Users." + e.getPlayer().getName() + "." + bleh);

		  						
		  						if (command.contains(blargh)){
		  							
		  							String arr[] = e.getMessage().split(" ");
		  							
		  							try {
		  								plugin.datacore.set("Users." + e.getPlayer().getName() + "." + bleh, (command.replaceAll(blargh, arr[x+1])));
									} catch (ArrayIndexOutOfBoundsException a) {
										e.getPlayer().sendMessage(CSMain.header + "You didn't define all of your args!");
										return;
			  						}
		  							
		  							replace = true;
		  						}
		  						
		  						x++;
		  					}
		  					
		  					command = plugin.datacore.getString("Users." + e.getPlayer().getName() + "." + bleh);
		  					
		  					if (e.getMessage().split(" ").length > 1 && replace == false){
		  					String arr[] = e.getMessage().split(" ", 2);
		  					String theRest = arr[1];	

		  					if (e.getMessage().endsWith("%c")){
		  						
		  						if (e.getPlayer().hasPermission("cs.console") == false){
		  							e.getPlayer().sendMessage(CSMain.header + "You don't have permissions (cs.console) to use %c.");
		  							break;
		  						}
		  						
		  						try {
		  							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command + " " + theRest);
								} catch (ArrayIndexOutOfBoundsException a) {
									break;
		  						}
		  					
		  						
		  						if (plugin.config.getBoolean("LogCommands")){
	  						  		Bukkit.getLogger().log(Level.INFO, e.getPlayer().getName() + " issued server command: /" + command);
	  						  	}
		  						
			  					return;
		  					}
		  					
		  					try {
		  						Bukkit.getServer().dispatchCommand(e.getPlayer(), command + " " + theRest);
							} catch (ArrayIndexOutOfBoundsException a) {
								break;
	  						}
		  					
		  	
		  					
		  					if (plugin.config.getBoolean("LogCommands")){
  						  		Bukkit.getLogger().log(Level.INFO, e.getPlayer().getName() + " issued server command: /" + command);
  						  	}
		  					
		  					return;
		  					
		  						} else {
		  							
		  							if (e.getMessage().endsWith("%c")){
				  						
				  						if (e.getPlayer().hasPermission("cs.console") == false){
				  							e.getPlayer().sendMessage(CSMain.header + "You don't have permissions (cs.console) to use %c.");
				  							break;
				  						}
				  						
				  						try {
					  						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
										} catch (ArrayIndexOutOfBoundsException a) {
											break;
				  						}
				  						
				  						plugin.datacore.set("Users." + e.getPlayer().getName() + "." + bleh, plugin.datacore.getString("Users." + e.getPlayer().getName() + ".TempStorage"));
				  						
				  						if (plugin.config.getBoolean("LogCommands")){
			  						  		Bukkit.getLogger().log(Level.INFO, e.getPlayer().getName() + " issued server command: /" + command);
			  						  	}
				  						break;
		  							}
		  							
		  							
		  							try {
		  							  Bukkit.getServer().dispatchCommand(e.getPlayer(), command);
									} catch (ArrayIndexOutOfBoundsException a) {
										break;
			  						}
		  						
		  						  plugin.datacore.set("Users." + e.getPlayer().getName() + "." + bleh, plugin.datacore.getString("Users." + e.getPlayer().getName() + ".TempStorage"));
		  						  	
		  						  	if (plugin.config.getBoolean("LogCommands")){
		  						  		Bukkit.getLogger().log(Level.INFO, e.getPlayer().getName() + " issued server command: /" + command);
		  						  	}
		  						}
		  				
		  				} else {
		  					
		  					 if (plugin.config.getBoolean("DebugMode")){
		  						  Bukkit.getServer().getConsoleSender().sendMessage(CSMain.AS(CSMain.header + e.getPlayer().getName() + " attempted to override command - failed @ on block list!"));
		  					  }
		  					 
		  					 return;
		  				}
		  		}
		  	}
	  }
	  
}
