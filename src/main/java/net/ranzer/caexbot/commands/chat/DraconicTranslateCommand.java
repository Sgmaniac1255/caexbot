package net.ranzer.caexbot.commands.chat;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.util.DraconicTranslator;
import net.ranzer.caexbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DraconicTranslateCommand extends BotCommand implements Describable{

		
	@Override
	public void process(String[] args,  MessageReceivedEvent event) {
		
		if (args[0].equals("com")){
			fromDraconic(StringUtil.arrayToString(Arrays.asList(Arrays.copyOfRange(args, 1,args.length))," "), event);
			return;
		}
		
		toDraconic(StringUtil.arrayToString(Arrays.asList(args)," "), event);
	}
	
	private void fromDraconic(String phrase, MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();
		
		eb.setAuthor("Draconic Translation", "http://draconic.twilightrealm.com/", null);
		eb.setFooter("Powered by Draconic Translator from Twilight Realm", null);
		eb.setColor(new Color(0xa0760a));
		eb.addField("Draconic:", phrase, false);
		eb.addField("Common:", DraconicTranslator.translate(phrase, false), false);
		
		event.getChannel().sendMessage(mb.setEmbeds(eb.build()).build()).queue();
	}

	private void toDraconic(String phrase, MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();
		
		eb.setAuthor("Draconic Translation", "http://draconic.twilightrealm.com/", null);
		eb.setFooter("Powered by Draconic Translator from Twilight Realm", null);
		eb.setColor(new Color(0xa0760a));
		eb.addField("Common:", phrase, false);
		eb.addField("Draconic", DraconicTranslator.translate(phrase, true), false);
		
		event.getChannel().sendMessage(mb.setEmbeds(eb.build()).build()).queue();
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("draconic","drc");
	}

	@Override
	public String getShortDescription() {
		return "I speak Draconic! What do you want to know how to say?";
	}
	
	@Override
	public String getLongDescription() {
		return "Translates a word or phrase from Common (english) to Draconic.\n\n"
				+ "`com`: will translate a word or phrase in Draconic back into Common (english)\n\n"
				+ "This Translator is powered by [Twilight Realm](http://draconic.twilightrealm.com/)";
	}

	@Override
	public String getUsage(Guild g) {
		
		return "`"+getPrefix(g)+getName()+" [com] <translation phrase>`";
		
	}
	
	@Override
	public Category getCategory() {
		return Category.CHAT;
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}
}