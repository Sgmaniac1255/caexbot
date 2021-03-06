package net.ranzer.caexbot.commands.admin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.data.IGuildData;

import java.util.Arrays;
import java.util.List;

public class JoinLeaveSettingCommand extends BotCommand implements Describable {

	private static final String TRUE_MESSAGE = "Alright, I'll start telling you when someone Joins or Leaves the guild.";
	private static final String FALSE_MESSAGE = "Sounds good, I'll stop telling you when someone leaves or joins.";
	private static final String INFO_MESSAGE = "I am currently %sset to say when someone leaves or joins the guild.";

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		IGuildData gd = GuildManager.getGuildData(event.getGuild());
		if(args.length>0){
			//set alert toggle
			boolean setting = Boolean.parseBoolean(args[0]);
			gd.setJLAnnouncement(setting);
			if(setting){
				event.getChannel().sendMessage(TRUE_MESSAGE).queue();
			} else {
				event.getChannel().sendMessage(FALSE_MESSAGE).queue();
			}
			
		} else {
			//inform toggle
			event.getChannel().sendMessage(String.format(INFO_MESSAGE,
					gd.getJLAnnouncement()?"":"**__not__** ")).queue();
		}
	
	}

	@Override
	public String getShortDescription() {
		return "Toggle the setting for member leave and join alerts.";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n"
				+ "Caex must have an announcement channel set for this option to work.";
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("`%s%s [{true | false}]`",
				getPrefix(g),
				getName());
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("join-leave-alert","join-alert", "leave-alert");
	}
	
	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}

	@Override
	public boolean isApplicableToPM() {
		return false;
	}

	@Override
	public Permission getPermissionRequirements() {
		return Permission.ADMINISTRATOR;
	}
}
