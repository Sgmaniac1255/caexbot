package caexbot.functions.games.zdice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Turn {

	public final static int HAND_SIZE = 3;
	private List<ZomDie> hand;
	private Player activePlayer;
	private ZomDicePool dicePool;
	
	private int pointsThisTurn;
	private int shots;
	
	public Turn(){

		dicePool = new ZomDicePool();
		hand=new ArrayList<>();
	}
	
	public void startTurn(Player p){
		activePlayer=p;
	}
	
	private void drawHand() {
		
		hand.addAll(dicePool.getDiceFromPool(getNeeded()));
	}
	
	private int getNeeded() {
		return HAND_SIZE-hand.size();
	}

	public RollResult roll() {
		
		RollResult rtn = new RollResult();
		
		drawHand();
		rtn.diceRolled(hand);
		Iterator<ZomDie> i = hand.iterator();
		
		while (i.hasNext()){
			ZomDie die = i.next();
			ZomDie.Side result = die.roll();
			rtn.addDieResult(result);
			switch (result){
			case BRAIN:
				pointsThisTurn++;
				i.remove();
				break;
			case RUN:
				break;
			case SHOT:
				shots++;
				i.remove();
				if(shots>=3){
					pointsThisTurn=0;
					endTurn();
				}
				break;
			}
			rtn.addTotals(pointsThisTurn,shots);
		}
		return rtn;
	}
	
	public void endTurn(){
		activePlayer.addPoints(pointsThisTurn);
	}
}
