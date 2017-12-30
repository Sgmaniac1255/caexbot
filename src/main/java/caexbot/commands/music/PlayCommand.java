package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayer;
import caexbot.functions.music.GuildPlayerManager;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PlayCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		GuildPlayer player = GuildPlayerManager.getPlayer(event.getGuild());
		if(args.length>0){
			player.queueSearch(StringUtil.arrayToString(Arrays.asList(args), " "));
		}
		
		if(!player.isConnected()){
			player.join(event.getGuild().getMember(event.getAuthor()).getVoiceState().getChannel());
		}
		
		player.start();
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("play","p");
	}

	@Override
	public String getShortDescription() {
		return "Start playing the first song in queue";
	}
	
	@Override
	public String getLongDescription() {
		return super.getLongDescription()+
				"this command does several things all in one.\n"
				+ "* if Caex isn't connected to a voice channel, he'll join you as per the `join` command\n\n"
				+ "* if supplied with a search string, he will search Youtube and add the song to the end of the queue as per the `queue` command\n\n"
				+ "* if caex is paused, he'll start playing again\n\n"
				+ "* last but not least, if he's stopped he'll start playing the first song in the queue";
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("`%smusic %s [<search string>]`", getPrefix(g), getName());
	}
}
