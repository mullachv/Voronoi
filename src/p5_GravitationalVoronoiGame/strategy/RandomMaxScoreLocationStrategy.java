package p5_GravitationalVoronoiGame.strategy;

import java.util.*;

import p5_GravitationalVoronoiGame.*;

public class RandomMaxScoreLocationStrategy implements Strategy {
	Random r = new Random();
	
	@Override
	public Move makeAMove(Board board){
		int poolNo = 1000;
		int maxScoreDiff = 0;
		Move bestMove = null;
		
		double[][][] currPull = board.getCurrPull();
		int[][] currColor = board.getCurrColor();
		
		for(int i=0; i<poolNo; i++){
			Move nextMove = null;
			while(nextMove == null){
				int x = r.nextInt(board.getBoardSize());
				int y = r.nextInt(board.getBoardSize());
				
				// if this position is empty and the color of this position is not mine, then accept this move
				if(board.isEmptyAt(x, y) && currColor[x][y] != 0){
					nextMove = Move.createMyMove(x, y);
					int thisScoreDiff = getMyScoreWithThisMove(board, nextMove, currPull, currColor);
					if(thisScoreDiff > maxScoreDiff){
						maxScoreDiff = thisScoreDiff;
						bestMove = nextMove;
					}
				}
			}
		}
		
		return bestMove;
	}
	
	// calculate how many score I can increase if I make this move
	private int getMyScoreWithThisMove(Board board, Move newMove, double[][][] currPull, int[][] currColor){
		
		int scoreDiff = 0;
		for(int i=0; i<board.getBoardSize(); i++){
			for(int j=0; j<board.getBoardSize(); j++){
				if(currColor[i][j] == 0){ continue; }
				double newPull = currPull[i][j][0] + 1/AbsPlayer.getDistanceSq(i, j, newMove);
				if(newPull > currPull[i][j][1]){
					scoreDiff++;
				}
			}
		}
		
		return scoreDiff;
	}
}
