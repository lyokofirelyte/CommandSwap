package com.github.lyokofirelyte.CommandSwap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.lyokofirelyte.CommandSwap.CSMain;

public class CSMain extends JavaPlugin {

	File configFile;
	FileConfiguration config;
	File datacoreFile;
	FileConfiguration datacore;
	File datacoreBACKUPFile;
	File configBACKUPFile;
	
	
	CSMain plugin;
	static String header = "§9§oCS §f// §9§o";
	
	@Override
	public void onEnable(){
	
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CSSwap(this), this);
		configFile = new File(getDataFolder(), "config.yml");
		datacoreFile = new File(getDataFolder(), "datacore.yml");
		datacoreBACKUPFile = new File(getDataFolder() + File.separator + "Backups", "datacore.yml");
		configBACKUPFile = new File(getDataFolder() + File.separator + "Backups", "config.yml");
	
			try {
		
				firstRun();
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		config = new YamlConfiguration();
		datacore = new YamlConfiguration();
		
		loadYamls();
		plugin = this;
		plugin.getCommand("cswap").setExecutor(new CSCommand(this));
		getLogger().info("Thanks for using my plugin! You're awesome! :D");
		
	}
		
	public void onDisable() {
		
		saveYamls();
		
	}
	
	  public void backupYamls(){
		    
		  try
		    {
		      config.save(configBACKUPFile);
		      datacore.save(datacoreBACKUPFile);
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	  }
	
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	}
	
	public void saveYamls() {

	    try {
	        config.save(configFile);
	        datacore.save(datacoreFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadYamls() {
	    try {
	        config.load(configFile);
	        datacore.load(datacoreFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void firstRun() throws Exception {
	    if(!configFile.exists()){
	        configFile.getParentFile().mkdirs();
	        copy(getResource("config.yml"), configFile);
	    }
	    if(!datacoreFile.exists()){
	    	datacoreFile.getParentFile().mkdirs();
	        copy(getResource("datacore.yml"), datacoreFile);
	    }
	}
	
	public static String AS(String DecorativeToasterCozy){
		
		String FlutterShysShed = ChatColor.translateAlternateColorCodes('&', DecorativeToasterCozy);
		return FlutterShysShed;
		
	}
}
