package p5_GravitationalVoronoiGame.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import p5_GravitationalVoronoiGame.AbsPlayer;
import p5_GravitationalVoronoiGame.Board;
import p5_GravitationalVoronoiGame.Move;
import p5_GravitationalVoronoiGame.Strategy;

// 1. sample positions for every 200 interval
// 2. select min / max position
// 3. for this position, randomly sample +-50 positions, return the best score position
public class MinMaxScoreStrategy implements Strategy{

	int[][] directions = {{-1, 0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}, {1,1}, {0, 1}, {-1, 1}};
	Random r = new Random();
	int interval = 200;
	boolean firstPlayer;
	List<Move> sampleMoves = new ArrayList<Move>();
	
	@Override
	public Move makeAMove(Board board) {
		if(sampleMoves.size() == 0){ addSampleMoves(board); }
		
		if(board.getPrevMoves().size() == 0){
			firstPlayer = true;
			return Move.createMyMove(board.getBoardSize()/2, board.getBoardSize()/2);
		}

		if(firstPlayer){
			Move myMove = getMinMaxBestMove(board, sampleMoves);
			Move bestMove = getBestRandomMove(board, myMove.x-50, myMove.y-50, 100);
			return bestMove;
		}else{
			return getBestOnePixelOffMove(board);
		}
	}
	
	private void addSampleMoves(Board board){
		int size = board.getBoardSize();
		for(int i=interval; i<size; i+=interval){
			for(int j=interval; j<size; j+=interval){
				sampleMoves.add(Move.createMyMove(i, j));
			}
		}
	}
	
	private Move getMinMaxBestMove(Board board, List<Move> moves){
		int size = board.getBoardSize();
		int maxScore = 0;
		Move myBestMove = moves.get(0);
		for(int k=0; k<moves.size(); k++){
			Move myMove = moves.get(k);
			int minScore = Integer.MAX_VALUE;
			for(int i=interval; i<size; i+=interval){
				for(int j=interval; j<size; j+=interval){
					Move othersMove = Move.createOthersMove(1, i, j);
					int thisScore = testMyScoreWithThisMove(board, myMove, othersMove);
					if(thisScore < minScore){
						minScore = thisScore;
					}
				}
			}
			
			if(minScore > maxScore){
				maxScore = minScore;
				myBestMove = myMove;
			}
		}
		
		return myBestMove;
	}
	
	public int testMyScoreWithThisMove(Board board, Move myMove, Move othersMove){

		double[][][] currPull = board.getCurrPull();
		int size = board.getBoardSize();
		
		int myScore = 0;
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				double newMyPull = currPull[i][j][0] + 1/AbsPlayer.getDistanceSq(i, j, myMove);
				double newOthersPull = currPull[i][j][1] + 1/AbsPlayer.getDistanceSq(i, j, othersMove);
				if(newMyPull > newOthersPull){
					myScore++;
				}else{
					myScore--;
				}
			}
		}
		
		return myScore;
	}
	
	private Move getBestOnePixelOffMove(Board board){
		List<Move> otherPlayersMove = board.getPrevMovesByPlayerId(1);
		int maxScore = 0;
		Move bestMove = null;
		
		for(int j=0; j<otherPlayersMove.size(); j++){
			Move prevMove = otherPlayersMove.get(j);
			for(int i=0; i<directions.length; i++){
				int nextX = prevMove.x + directions[i][0];
				int nextY = prevMove.y + directions[i][1];
				if(isValidMove(board, nextX, nextY)){
					Move thisMove = Move.createMyMove(nextX, nextY);
					int thisScore = board.testMyScoreWithThisMove(thisMove);
					if(thisScore > maxScore){
						maxScore = thisScore;
						bestMove = thisMove;
					}
				}else{
					int nextX2 = prevMove.x + directions[i][0]*2;
					int nextY2 = prevMove.y + directions[i][1]*2;
					if(isValidMove(board, nextX2, nextY2)){
						Move thisMove = Move.createMyMove(nextX2, nextY2);
						int thisScore = board.testMyScoreWithThisMove(thisMove);
						if(thisScore > maxScore){
							maxScore = thisScore;
							bestMove = thisMove;
						}
					}
				}
			}
		}
		return bestMove;
	}
	
	private Move getBestRandomMove(Board board, int startX, int startY, int offset){
		int poolNo = 1000;
		Move bestMove = null;
		int maxScore = 0;
		
		for(int i=0; i<poolNo; i++){
			Move nextMove = null;
			while(nextMove == null){
				int x = startX + r.nextInt(offset);
				int y = startY + r.nextInt(offset);
				
				// if this position is empty and the color of this position is not mine, then accept this move
				if(isValidMove(board, x, y)){
					nextMove = Move.createMyMove(x, y);
					int thisScoreDiff = board.testMyScoreWithThisMove(nextMove);
					if(thisScoreDiff > maxScore){
						maxScore = thisScoreDiff;
						bestMove = nextMove;
					}
				}
			}
		}
		return bestMove;
	}

	
	private boolean isValidMove(Board board, int x, int y){
		if(x < 0 || y < 0 || x >= board.getBoardSize() || y >= board.getBoardSize() || !board.isEmptyAt(x, y)){
			return false;
		}
		return true;
	}

}
