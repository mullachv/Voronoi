package p5_GravitationalVoronoiGame.strategy;

import java.util.*;

import p5_GravitationalVoronoiGame.*;

public class RandomMaxScoreLocationStrategy implements Strategy {
	Random r = new Random();
	
	@Override
	public Move makeAMove(Board board){
		int poolNo = 3000;
		int maxScoreDiff = 0;
		Move bestMove = null;
		
		for(int i=0; i<poolNo; i++){
			Move nextMove = null;
			while(nextMove == null){
				int x = r.nextInt(board.getBoardSize());
				int y = r.nextInt(board.getBoardSize());
				
				// if this position is empty and the color of this position is not mine (don't use this rule), then accept this move
				if(board.isEmptyAt(x, y)){ //&& currColor[x][y] != 0){
					nextMove = Move.createMyMove(x, y);
					int thisScoreDiff = board.testMyScoreWithThisMove(nextMove);
					if(thisScoreDiff > maxScoreDiff){
						maxScoreDiff = thisScoreDiff;
						bestMove = nextMove;
					}
				}
			}
		}
		
		return bestMove;
	}
}
