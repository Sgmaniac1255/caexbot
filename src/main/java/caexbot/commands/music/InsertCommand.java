package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InsertCommand extends AbstractMusicCommand implements Describable {

	@Override
	public List<String> getAlias() {
		return Arrays.asList("insert");
	}

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		if (args[0].startsWith(getPrefix(event.getGuild()))) {
			GuildPlayerManager.getPlayer(event.getGuild()).insertID(args[0].substring(getPrefix(event.getGuild()).length(), args[0].length()));
		} else {
			GuildPlayerManager.getPlayer(event.getGuild()).insertSearch(StringUtil.arrayToString(Arrays.asList(args), " "));
		}

	}
	@Override
	public String getShortDescription() {
		return "Add song to the Head of the queue";
	}
}
