package net.ranzer.caexbot.data;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.database.HibernateManager;
import net.ranzer.caexbot.database.interfaces.GuildData;
import net.ranzer.caexbot.database.model.ChannelDataModel;
import net.ranzer.caexbot.database.model.GuildDataModel;
import net.ranzer.caexbot.database.model.MemberDataModel;
import net.ranzer.caexbot.util.Logging;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * container for all the GuildData objects
 * 
 * @author Ranzer
 *
 */
public class GuildManager extends ListenerAdapter {

	/*
	 * update the DB to match things that happened while bot was offline
	 */
	static{
		Logging.info("updating DB to things that happened while offline");

		Logging.info("add new guilds");
		addNewGuilds();

		Logging.info("removing old guilds");
		removeOldGuilds();

		Logging.info("updating text channels");
		removeOldTextChannels();

		Logging.info("updating members");
		updateMembers();


		
	}

	//Black Magic code copied from the internet
	public static <T> List<T> loadAllData(Class<T> type, Session s){
		CriteriaBuilder builder = s.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		criteria.from(type);
		return s.createQuery(criteria).getResultList();
	}

	public static IGuildData getGuildData(Guild key){
		return new GuildData(key);
	}


	//convenience pass through methods
	public static String getPrefix(Guild key){
		return new GuildData(key).getPrefix();
	}
	public static void setPrefix(Guild key, String prefix){
		new GuildData(key).setPrefix(prefix);
	}
	public static void removePrefix(Guild key){
		new GuildData(key).removePrefix();
	}

	//DB update methods
	private static void addNewGuilds(){

	}

	//todo test this
	private static void removeOldGuilds() {//todo test this

		try (Session s = HibernateManager.getSessionFactory().openSession()){
			s.beginTransaction();
			List<GuildDataModel> guilds = loadAllData(GuildDataModel.class,s);
			for (GuildDataModel guild:guilds){
				if(CaexBot.getJDA().getGuildById(guild.getId())==null){
					s.remove(guild);
				}
			}
			s.getTransaction().commit();
		}
	}

	//todo test this
	private static void removeOldTextChannels() {
		try (Session s = HibernateManager.getSessionFactory().openSession()){
			s.beginTransaction();
			List<ChannelDataModel> channels = loadAllData(ChannelDataModel.class,s);
			for (ChannelDataModel channel:channels){
				if(CaexBot.getJDA().getTextChannelById(channel.getID())==null){
					s.remove(channel);
				}
			}
			s.getTransaction().commit();
		}
	}

	//todo test this
	private static void updateMembers() {

		//delete old members' XP
		try (Session s = HibernateManager.getSessionFactory().openSession()) {
			JDA jda = CaexBot.getJDA();
			s.beginTransaction();
			List<MemberDataModel> members = loadAllData(MemberDataModel.class, s);
			for (MemberDataModel member : members) {
				try{
					Guild g = jda.getGuildById(member.getGuildId());
					if (g == null) continue; //this should never happen
					g.retrieveMemberById(Long.parseLong(member.getUserId())).complete();
				} catch (ErrorResponseException e){
					switch (e.getErrorResponse()){
						case UNKNOWN_MEMBER:
						case UNKNOWN_USER:
							s.remove(member);
					}
				}
			}
			s.getTransaction().commit();
		}
	}


	//data modification listeners
	//todo test this
	@Override
	public void onGuildLeave(@NotNull GuildLeaveEvent event) {

		try (Session s = HibernateManager.getSessionFactory().openSession()){
			GuildData g = new GuildData(event.getGuild());
			s.delete(g.getModel());
		}
	}

	//todo test this
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		getGuildData(event.getGuild()).addMember(event.getMember());
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {

		getGuildData(event.getGuild()).deleteMember(event.getMember());

	}

	//todo see if this is still needed
//	@Override
//	public void onGuildJoin(GuildJoinEvent event) {
//
//		try(PreparedStatement stmt = BotDB.getConnection().prepareStatement("insert into grimcodb.guild(guild_id) values (?)")){
//			stmt.setString(1, event.getGuild().getId());
//			stmt.execute();
//		} catch (SQLException e) {
//			Logging.error("issue joining guild to DB");
//			Logging.log(e);
//		}
//
//	}

	@Override
	public void onTextChannelDelete(TextChannelDeleteEvent event) {
		getGuildData(event.getGuild()).deleteChannel(event.getChannel());
	}

	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		getGuildData(event.getGuild()).addChannel(event.getChannel());
	}
}