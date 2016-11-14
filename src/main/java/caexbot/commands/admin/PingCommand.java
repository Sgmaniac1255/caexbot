package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PingCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		
		channel.sendMessage("pong!").queue();	
	}

	@Override
	public String getUsage() {
		return getPrefix()+"ping";
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("ping");
	}

	@Override
	public String getDescription() {
		return "pong!";
	}


}
