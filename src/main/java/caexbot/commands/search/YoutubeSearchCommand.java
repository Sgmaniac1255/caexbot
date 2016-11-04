package caexbot.commands.search;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.util.Logging;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class YoutubeSearchCommand extends CaexCommand {

	private static final String YOUTUBE_BASE_STRING = "https://youtu.be/";
	private YouTubeSearcher yts;
	
	public YoutubeSearchCommand() {
		yts = new YouTubeSearcher();
	}
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		StringBuilder queryBuilder = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			queryBuilder.append(args[i]).append(" ");
		}
		String videoID = yts.searchForVideo(queryBuilder.toString());
		
		Logging.debug(queryBuilder.toString()+" : " +videoID);
		
		if(videoID!=null){
			StringBuilder youtubeURL = new StringBuilder();
			youtubeURL.append(YOUTUBE_BASE_STRING).append(videoID);
			channel.sendMessage(author.getAsMention() + " "+ youtubeURL.toString());
		} else {
			channel.sendMessage("I'm sorry, i didn't find anything");			
		}

		

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("youtube", "yt");
	}

	@Override
	public String getDescription() {
		return "Search YouTube for your Videos!";
	}

	@Override
	public String getUsage() {
		return getPrefix()+getAlias().get(0) + " <your search terms>";
	}

}