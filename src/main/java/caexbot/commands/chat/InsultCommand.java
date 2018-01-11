package caexbot.commands.chat;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import caexbot.commands.*;
import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Ranzer
 */
public class InsultCommand extends CaexCommand implements Describable, DraconicCommand {
	
	private static String[] insults = {//TODO put these into a text file later
			"What smells worse than a goblin? Oh yeah, you!",
			"Your mother takes up more tiles than a gelatinous cube!",
			"I'm glad you're tall...It means there's more of you I can despise!",
			"I don't know whether to use charm person or hold monster!",
			"You're the reason baby gnomes cry!",
			"Ugh. What the he'll is that all over your face? Oh...its just your face!",
			"You are the feces that is created when shame eats too much stupidly!",
			"You're like a dragon, only shit!",
			"I've seen more threatening geckos!",
			"I swear, if you were any worse at this, you'd be doing our job for us!",
			"Your mother was a kobold and your father smelled of elderberry!",
			"You would bore the legs off a village idiot!",
			"It gives me a headache just trying to think down to your level!",
			"You're not a complete idiot...Some parts are obviously missing!",
			"You're like a trained ape, only, without the training!",
			"Well, my time of not taking you seriously is coming to a middle!",
			"Your mother's so ugly, folk turn to stone just incase they might happen to catch a glimpse of her face!",
			"Some day you'll meet a doppelganger of yourself and be disappointed!",
			"Are you always stupid, or are you making a special effort today!",
			"Some day you'll go far and I hope you stay there!",
			"You're lucky to be born beautiful, unlike me, who was born to be a big liar!",
			"I'd like to leave you with one thought...but I'm not sure you have anywhere to put it!",
			"Your momma's so ugly, clerics try to turn her!",
			"Your magic is as bad as your breath!",
			"Oh look, both your weapons are tiny!",
			"A wet cat is tougher than you!",
			"If ignorance is bliss, you must be the happiest person alive!",
			"Are you a half orc crossed with a pig? Oh yeah, there are some things a pig wouldn't do!",
			"Wow, that's a fat ass. I guess those behind you are gaining cover for this fight!",
			"Your Breath would put an otyugh off it's breakfast!",
			"I could say you're as ugly as an ogre, but that would be an insult to ogres!",
			"I would contact your mother about your death, but I don't speak goblin!",
			"Your very existence is an insult to all!",
			"You look like the armpit of an unshaven bog hag!",
			"You are maggot pie served from a dwarf's codpiece!",
			"A goblin with one hand nailed to a tree would be more of a threat than you!",
			"You look like a scab on a troll's wart!",
			"No loot is worth having to look at you!",
			"You are the worst example of your class that I've ever come across!",
			"Why don't you give me your weapon so I can hit myself with it, because that'd be more effective than you trying it!",
			"I can tell your reservoir of courage is fed by the tributary running down your leg!",
			"I'd insult your parents, but you probably don't know who they are!",
			"Well...I have met sharper loaves of bread!",
			"Even evard's black tentacles wouldn't touch something as gross as you!",
			"Would you like me to remove that curse? Oh my mistake, you were just born that way!",
			"There is no beholder's eye in which you are beautiful!",
			"Animal friendship was the only way your parents could get puppies to play with you!",
			"Your ugly face makes a good argument against raising the dead!",
			"When your god put teeth in your mouth, he ruined a perfectly good asshole!",
			"Whomever is willing to have sex with you, is just too lazy to jerk off!",
			"If your brain exploded, it wouldn't even mess up your hair!",
			"Somewhere, Your depriving a village of it's idiot!",
			"I'd like to see things from your point of view, but I can't get my head that far up my arse!",
			"Could you go get your husband, I don't like fighting ugly women!",
			"I heard what happened to your mother, it's not everyday your reflection kills you!",
			"You look like your mother, and, your mother looks like your father!",
			"You're so stupid, if an illithid tried to eat your brain, it would starve to death!",
			"What's that smell? I thought breath weapons were suppose to come out of your mouth!",
			"I would try to insult your father, but you were probably mistaken for a half orc, and disowned!",
			"Did your mother cast a darkness spell to feed you!",
			"No wonder you're hiding behind cover, I'd hide too with a face like that!",
			"Do you have a pen? Well you'd better get back to it before the farmer knows you are missing!",
			"Quick grab some fire...no wait, it's ok, it's not an actual troll!",
			"If I were you, I'd go and get my money back for that remove curse spell!",
			"And I thought troglodytes smelt bad!",
			"We're you once hit by a melf's acid arrow or have you always looked like a half eaten marrow!",
			"Phew! Have you just cast stinking cloud or do you always smell like that!",
			"Hey, you pox ridden dung heap, I bet not even a starving vampire would go near you!",
			"By looking at you, now I know what you get when you scrape out the bottom of the barrel!",
			"I was going to cast detect thoughts, but I don't think I'm going to find anything up there!",
			"I wish I still had that blindness spell, then I wouldn't have to endure that face anymore!",
			"I was thinking of casting feeblemind, but I doubt it would work on you!",
			"Tell me, did you run away from your parents, or did they run away from you!",
			"What's the difference between a troll and your mother? One's a stinking ugly monster, and the other is a troll!",
			"I was wondering what you are, you're fat enough to be an ogre, but I've never seen an ogre THAT ugly before!",
			"Your mother was so stupid, zombies made her a dunce hat!",
			"You're like a gnome on stilts, real cute, but it's not working!",
			"They say every rose has its thorn, ain't that right, buttercup!",
			"I'd say you were a worthy opponent, but I once fought a flumph wielding a dandelion!",
			"I'd draw my rapier, but I wouldn't want to make you jealous!",
			"Do you know what happens to a person when they fail their save? Neither do I, but based on what happened to your comrade, my money's on 'dies horribly'!",
			"How does it feel that you're not worthy of anyone casting a decent spell on you!",
			"One day I'm going to make a ballad of this fight. Tell me your name, I hope it rhymes with horribly slaughtered!",
			"Your mother is so fat that making a joke here would detract from the seriousness of her condition!",
			"Stop me if you've heard this one. The sole purpose of your existence is to serve as a speedbump on others path to greatness - okay you definitely should of stopped me by now!",
			"What's the difference between a dragon and a mallard with a cold? One's a sick duck and I forget the punchline, but your mother's a whore!",
			"By the Master you're ugly, I bet your father regrets not pulling out when he had the chance!",
			"Do you know, that if you were at a party surrounded by female zombies, the only thing you'd pull is a hamstring!",
			"Wait, so you're the manifestation of a divine being of supreme power and malevolence, and you chose that face? Do they even have mirrors on your plane of existence!"
		};
	public static User lastOwnerInsult = null;
	
	@Override
	public void process(String[] args,MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();
		
		for ( Member m : event.getMessage().getMentionedMembers()) {
			if(m.getUser().getId().equals(CaexConfiguration.getInstance().getOwner())){
				if(m.getOnlineStatus()==OnlineStatus.ONLINE){
					event.getChannel().sendMessage("You want me to insult him?!?!.... \n I'm sorry but I can't insult *him*.... he'll *__KILL__* me!!").queue();
				} else {
					event.getChannel().sendMessage("*looks around nervously* ... he's not here right now....\n"
							+ "alright, I'll do it..... **but** I wont tag him! *gulp*\n"
							+ "if he says \"sleep\", its on your head!").queue();
					lastOwnerInsult=event.getAuthor();
					sb.append(m.getEffectiveName()+", ");
				}
				continue;
			}
			
			if(event.getMember().equals(m)){
				event.getChannel().sendMessage("*looks confused* yourself? but.... ok...").queue();
			}
			sb.append(m.getAsMention()+", ");
		}
		if(sb.length()==0) return;//don't throw an insult if no one was tagged.
	
		String insult = bardicInsult();
		
		sb.append(insult);
		event.getChannel().sendMessage(sb.toString()).queue();
	}
	
	private String bardicInsult(){
		return insults[ThreadLocalRandom.current().nextInt(insults.length)];
	}

	private String shakespearInsult(){
		try {
			Document doc = Jsoup.parse(new URL("http://www.pangloss.com/seidel/Shaker/index.html?"), 3000);
			return doc.getElementsByTag("p").get(0).text();			
		} catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<String> getAlias() {
		return Arrays.asList("insult","offend","curse*");
	}

	@Override
	public List<String> getDraconicAlias() {
		return Arrays.asList("chikohk");
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}

	@Override
	public String getShortDescription() {
		return "Give them a slap in the face!";
	}

	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" <Taged users you want to insult>`";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n"
				+ "Tag the people you want to insult. can insult more than one at a time.\n\n"
				+ "Powered by [pangloss.com](http://www.pangloss.com/seidel/Shaker/index.html?)";
	}

	@Override
	public boolean isAplicableToPM() {
		return false;
	}

}
