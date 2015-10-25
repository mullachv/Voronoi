package p5_GravitationalVoronoiGame.strategy;

import java.util.*;

import p5_GravitationalVoronoiGame.*;

public class RandomMaxPullLocationStrategy implements Strategy {
	Random r = new Random();
	
	@Override
	public Move makeAMove(Board board){
		int poolNo = 1000;
		double maxPullDiff = 0;
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
					double thisPullDiff = getMyPullIncreseWithThisMove(board, nextMove, currPull, currColor);
					if(thisPullDiff > maxPullDiff){
						maxPullDiff = thisPullDiff;
						bestMove = nextMove;
					}
				}
			}
		}
		
		return bestMove;
	}
	
	// calculate how many score I can increase if I make this move
	private double getMyPullIncreseWithThisMove(Board board, Move newMove, double[][][] currPull, int[][] currColor){
		
		double pullDiff = 0;
		for(int i=0; i<board.getBoardSize(); i++){
			for(int j=0; j<board.getBoardSize(); j++){
				if(currColor[i][j] == 0){ continue; }
				pullDiff =+ 1/AbsPlayer.getDistanceSq(i, j, newMove);
			}
		}
		
		return pullDiff;
	}
}
