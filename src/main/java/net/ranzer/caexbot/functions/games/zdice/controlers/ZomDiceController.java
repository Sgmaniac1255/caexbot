package net.ranzer.caexbot.functions.games.zdice.controlers;

import java.util.Deque;

import net.ranzer.caexbot.functions.games.zdice.Player;
import net.ranzer.caexbot.functions.games.zdice.RollResult;
import net.ranzer.caexbot.functions.games.zdice.ZombieDiceGame;

/* 
 * this class will be the unit that takes in commands from Discord
 * and passes them to the Game. takes the responses from the game and interprets
 * it into output via Discord
 * 
 * i may make discord integration into a subclass and make this abstract
 * 
 * (in MVC terms this is the Controller, while the discord Command structure is the View)
 */
public abstract class ZomDiceController {

	ZombieDiceGame game;
	
	public ZomDiceController(){
		game = new ZombieDiceGame();
		game.setController(this);
	}

	public boolean addPlayer(Player p){
		return game.addPlayer(p);
	}

	public boolean removePlayer(Player p){
		return game.removePlayer(p);
	}

	/**
	 * starts the game if players contains more than one participant. 
	 */
	public boolean startGame(){
		return game.start();
	}
	
	public int getScore(Player p){
		return game.getPlayer(p).getBrains();
	}
	abstract public void needMorePlayers();

	abstract public void announceGameStart(Deque<Player> deque);

	abstract public void promptPlayer(Player nextPlayer);
	
	abstract public void notActivePlayer();

	public void roll(Player p){
		game.roll(p);
	}

	abstract public void rollResult(RollResult result);
	
	public void endTurn(Player p) {
		game.endTurn(p);
		
	}

	abstract public void announceEndTurn(Player currentPlayer);

	abstract public void announceGameEnd(Player[] players);

	abstract public void announceFinalRound();	
}
