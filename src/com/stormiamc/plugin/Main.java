// Main.java

package com.stormiamc.plugin;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.Connection;

public class Main extends JavaPlugin{
	
	static Connection connection;
	
	public void consoleLog(String text){
		getLogger().info(text);
	}
	
	public boolean connect(){
		
		final String username = "paienmcc_test";
		final String password = "";
		final String url = "jdbc:mysql://www.paienmc.com/paienmcc_test";
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			return false;
		}
		try{
			connection = (Connection) DriverManager.getConnection(url,username,password);
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
		//Connected to database
		
		consoleLog("Connected to MySQL");
		return true;
		
	}
	@Override
	public void onEnable(){
		if(connect() == false){
			getServer().getPluginManager().disablePlugin(this);
			consoleLog("Didn't Connected To MySQL");
			consoleLog("Disabling the Plugin");
		}
		
		
	}
	@Override
	public void onDisable(){
		
	}
}
