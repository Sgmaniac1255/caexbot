package caexbot.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import caexbot.CaexBot;
import caexbot.config.CaexConfiguration;
import caexbot.database.CaexDB;
import caexbot.functions.levels.UserLevel;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class GuildData {

	private Guild guild;
	
	public GuildData(Guild guild) {
		this.guild=guild;
	}


	//xp methods
	public void addXP(User author, int XP, TextChannel channel) {

		Logging.debug("Adding "+ XP + "XP to "+ author.getName()+":"+guild.getName());
		
		try{
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"insert into member (guild_id, user_id, xp) values (?,?,?)"
					+ "on duplicate key update xp=xp+?;");
			stmt.setString(1, guild.getId());
			stmt.setString(2, author.getId());
			stmt.setInt(3, XP);
			stmt.setInt(4, XP);
			stmt.executeUpdate();
			stmt.close();
			
		} catch (Exception e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}

	public List<UserLevel> getGuildRankings() {
		
		List<UserLevel> ranking = new ArrayList<>(); 
		
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"select guild_id, user_id, xp from member where guild_id=? order by xp desc;"
			);
			
			stmt.setString(1, guild.getId());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				Member member = CaexBot.getJDA().getGuildById(rs.getString(1)).getMemberById(rs.getString(2));
				if(member==null){
					deleteMember(rs.getString(1),rs.getString(2));
					continue;
				}
				ranking.add(new UserLevel(member, rs.getInt(3)));
			}
			
			stmt.close();
		} catch (SQLException e) {
			Logging.error("issue getting GuildRankings");
			Logging.log(e);
		}
		
		return ranking;
	}

	public int getLevel(User author) {
		
		return UserLevel.getLevel(getXP(author));
	}
	
	public int getXP(User u){
		
		try {
			ResultSet rs = CaexDB.getConnection().prepareStatement(
					String.format("select xp from member where guild_id = %s and user_id=%s;",guild.getId(), u.getId())
					).executeQuery();
			rs.next();
			int rtn = rs.getInt(1);
			rs.close();
			
			return rtn;
			
		} catch (SQLException e) {

			Logging.error("issue getting user's XP");
			Logging.log(e);
			return -1;
		}
		
	}

	
	//prefix methods
	public String getPrefix() {
		String prefix=null;
		
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"select prefix from guild where guild_id = ?"
			);
			stmt.setString(1, guild.getId());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				prefix=rs.getString(1);
			}
			stmt.close();
		} catch (SQLException e) {

			Logging.error("issue getting Prefix");
			Logging.log(e);
		}
		
		if (prefix==null){
			return CaexConfiguration.getInstance().getPrefix();
		}
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		if (prefix!=null) {
			prefix = prefix.toLowerCase();
			try {
				PreparedStatement stmt = CaexDB.getConnection()
						.prepareStatement(
						"insert into guild (guild_id, prefix) values (?,?) "
						+ "on duplicate key update prefix=?;"
				);
				stmt.setString(1, guild.getId());
				stmt.setString(2, prefix);
				stmt.setString(3, prefix);
				stmt.executeUpdate();
				stmt.close();
			} catch (Exception e) {
				Logging.error(e.getMessage());
				Logging.log(e);
			} 
		} else {
			removePrefix();
		}
	}
	
	public void removePrefix() {
		
		try {
			PreparedStatement stmt = CaexDB.getConnection()
					.prepareStatement("update guild set prefix = null where guild_id = ?;");
			stmt.setString(1, guild.getId());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {

			Logging.error("issue removeing prefix");
			Logging.log(e);
		}
	}
	/**
	 * 
	 * @return admin defined channel for music
	 */
	public TextChannel getMusicChannel() {
		
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
				"select def_chan_music from guild where guild_id = ?;"	
			);
			
			stmt.setString(1, guild.getId());
			
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			String rtn = rs.getString(1);
			stmt.close();
			return CaexBot.getJDA().getTextChannelById(rtn);
			
			
		} catch (SQLException e) {
			Logging.error("prob getting def_chan_music");
			Logging.log(e);
			return null;
		}
	}
	
	

	/**
	 * used to set the default text channel used for music
	 * @param musicChannel
	 */
	public void setMusicChannel(TextChannel musicChannel) {
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"update guild set def_chan_music=? where guild_id=?"
			);
			
			stmt.setString(1, musicChannel.getId());
			stmt.setString(2, musicChannel.getGuild().getId());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void deleteMember(String guild, String user) {
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"delete from member where guild_id = ? and user_id = ?"
			);
			
			stmt.setString(1, guild);
			stmt.setString(2, user);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public boolean xpExempt(TextChannel channel) {
		
		boolean rtn = true;
		
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"select perm_xp from text_channel where channel_id = ?"
			);
			stmt.setString(1, channel.getId());
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()){
				rtn = rs.getBoolean(1);
			}
			stmt.close();
			
		} catch (SQLException e){
			Logging.error("problem getting xp permmisions");
			Logging.log(e);
		}
		
		return rtn;
	}


	public ChannelData getChannel(TextChannel channel) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
