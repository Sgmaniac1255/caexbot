package net.ranzer.caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.games.zdice.controlers.UserPlayerAdapter;
import net.ranzer.caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ZomScoreCommand extends AbstractZombieCommand implements Describable{

	
	@Override
	public void process(String[] args,  MessageReceivedEvent event) {
		int brains = ZomDiceDiscordControler.getInstance().getScore(new UserPlayerAdapter(event.getAuthor()));
		event.getChannel().sendMessage(String.format("%s: your score is **%d** Brains!", event.getAuthor().getAsMention(), brains)).queue();
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("score","s");
	}

	@Override
	public String getShortDescription() {
		return "See your current number of brains.";
	}

}
