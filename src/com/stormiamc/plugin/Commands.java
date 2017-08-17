package com.stormiamc.plugin;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Objects;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor{
	
	Connection connection = Main.connection;
	
	public void sendErrorMessage(Player player,String text){
		player.sendMessage(ChatColor.RED + text);
	}
	
	public void sendInformationMessage(Player player,String text){
		player.sendMessage(ChatColor.YELLOW + text);
	}
	
	public boolean teleportPlayer(Player player,int location){
		switch(location){
		case 1: //Watch arena 1
			player.teleport(new Location(Bukkit.getWorld("world"),0,0,0));
			return true;
		case 2: //Watch arena 2
			player.teleport(new Location(Bukkit.getWorld("world"),0,0,0));
			return true;
		case 3: //Watch arena 3
			player.teleport(new Location(Bukkit.getWorld("world"),0,0,0));
			return true;
		case 4: //Enter arena 1
			player.teleport(new Location(Bukkit.getWorld("world"),0,0,0));
			return true;
		case 5: //Enter arena 2
			player.teleport(new Location(Bukkit.getWorld("world"),0,0,0));
			return true;
		case 6: //Enter arena 3
			player.teleport(new Location(Bukkit.getWorld("world"),0,0,0));
			return true;
		default:
			return false;
		}
	}
	
	public int lookPlayersArena(String playername){
		
		//Get players arena from database
		
		String sql = "select point from players where playername = ?";
		try{
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);
			stmt.setString(1, playername);
			ResultSet playerinfo = stmt.executeQuery();
			if(!playerinfo.next()){
				//There is no user data, create new one
				String sqlnew = "insert into players (playername,point,faction) values (?,0,'')";
				try{
					PreparedStatement stmtnew = (PreparedStatement) connection.prepareStatement(sqlnew);
					stmtnew.setString(1, playername);
					stmtnew.executeUpdate();
					return 1; //There is no point, enter to arena 1
				}catch(SQLException e){
					e.printStackTrace();
					return 0;
				}
			}
			else{
				int point = playerinfo.getInt(0);
				if(point < 100){
					return 1;
				}
				else if((point >= 100 ) && (point < 500)){
					return 2;
				}
				else if((point >= 500)){
					return 3;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	public boolean enterArena(int arena,String playername){
		String sql = "select playercount from arenalist where arena = " + arena;
		try{
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet query = stmt.executeQuery();
			if(!query.next()){
				return false;
			}
			int count = query.getInt("playercount");
			if(count < 16){
				//Enter to arena
				String sqlnew = "update arenalist set playercount = ? where arena = " + arena;
				try{
					PreparedStatement stmtnew = (PreparedStatement) connection.prepareStatement(sqlnew);
					stmtnew.setInt(1, count++);
					stmtnew.executeUpdate();
					//Player entered to arena teleport the player.
					return true;
				}catch(SQLException e){
					e.printStackTrace();
					return false;
				}
			}
			else{
				//Arena is full!
				return false;
			}
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if(!(sender instanceof Player)){
			//Console sending that command
			
		}
		
		Player player = (Player) sender;
		String playername = player.getName();
		
		if(Objects.equal(label, "arena")){
			
			if(Objects.equal(args[0], "enter")){
				
				//Look players arena
				switch(lookPlayersArena(playername)){
				case 1:
					//Arena 1
					if(enterArena(1,playername) == false){
						sendErrorMessage(player,"Arena Þuan Dolu! Ama arenaya izleyici olarak katýlabilirsin /arena watch 1");
						return false;
					}
					//Teleport
					teleportPlayer(player,4);
					sendInformationMessage(player,"Arenaya hoþ geldiniz!");
					Bukkit.broadcastMessage(ChatColor.GOLD + playername + "1. Arenaya Giriþ Yaptý!");
					break;
				case 2:
					//Arena 2
					break;
				case 3:
					//Arena 3
					break;
				default:
					sendErrorMessage(player,"Kullaným /arena enter");
					return false;
				}
				
			}
			else if(Objects.equal(args[0], "watch")){
				int arenaint = Integer.parseInt(args[1]);
				
				switch(arenaint){
				case 1:
					//Enter to arena 1
					if(teleportPlayer(player,1) == true){
						return true;
					}
					else{
						return false;
					}
				case 2:
					//Enter to arena 2
					if(teleportPlayer(player,2) == true){
						return true;
					}
					else{
						return false;
					}
				case 3:
					//Enter to arena 3
					if(teleportPlayer(player,3) == true){
						return true;
					}
					else{
						return false;
					}
				default:
					sendErrorMessage(player,"Kullaným /arena watch <arena_id>");
					return false;
				}
			}
			else{
				sendErrorMessage(player,"Kullaným : /arena <enter,watch>");
				return false;
			}
			
		}
		
		return true;
	}
}
