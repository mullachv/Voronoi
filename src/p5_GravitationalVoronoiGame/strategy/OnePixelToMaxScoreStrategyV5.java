package p5_GravitationalVoronoiGame.strategy;

import java.util.List;
import java.util.Random;

import p5_GravitationalVoronoiGame.AbsPlayer;
import p5_GravitationalVoronoiGame.Board;
import p5_GravitationalVoronoiGame.Move;
import p5_GravitationalVoronoiGame.MoveTimer;
import p5_GravitationalVoronoiGame.Strategy;

// V2: use all previous moves from other player, and get position having the best score
public class OnePixelToMaxScoreStrategyV5 implements Strategy{

	int[][] oneOfAllDirections = {{-1, 0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}, {1,1}, {0, 1}, {-1, 1}};
	int[][] diagonalMoves = {{-1,-1}, {1,-1}, {1,1}, {-1, 1}};
	Random r = new Random();
	Move bestMove = null;
	double maxScore = 0;
	int invocationCounter = 0;
	
	@Override
	public Move makeAMove(Board board) {
		if(board.getPrevMoves().size() == 0){
			return Move.createMyMove(board.getBoardSize()/2, board.getBoardSize()/2);
		}

		MoveTimer.getTimer().beginTimer();
		bestMove = null;
		maxScore = 0;
		if (invocationCounter % 3 == 0) {
			System.out.println("Now trying random");
			getBestRandomMove(board);
			if (MoveTimer.getTimer().isTimeExceeded()) {
				invocationCounter++;
				return bestMove;
			}
			System.out.println("Now trying centered");
			getBestCenteredMove(board);
			if (MoveTimer.getTimer().isTimeExceeded()) {
				invocationCounter++;
				return bestMove;
			}
//			System.out.println("Now trying diagonal");
//			checkDiagonalMoves(board);
//			System.out.println("Now trying one off");
//			getBestOnePixelOffMove(board);
		} else {
			System.out.println("Now trying diagonal");
			checkDiagonalMoves(board);			
			if (MoveTimer.getTimer().isTimeExceeded()) {
				invocationCounter++;
				return bestMove;
			}
			System.out.println("Now trying one off");
			getBestOnePixelOffMove(board);
			if (MoveTimer.getTimer().isTimeExceeded()) {
				invocationCounter++;
				return bestMove;
			}
			System.out.println("Now trying centered");
			getBestCenteredMove(board);
			if (MoveTimer.getTimer().isTimeExceeded()) {
				invocationCounter++;
				return bestMove;
			}
			System.out.println("Now trying random");
			getBestRandomMove(board);
			if (MoveTimer.getTimer().isTimeExceeded()) {
				invocationCounter++;
				return bestMove;
			}
			System.out.println("Now trying corners");
			getBestCornerMove(board);
		}
		invocationCounter++;
		return bestMove;		
	}
	
	private Move checkDiagonalMoves(Board board){
		List<Move> otherPlayersMove = board.getPrevMovesByPlayerId(1);
		
		for(int j=otherPlayersMove.size()-1; j >=0; j--){
			if (MoveTimer.getTimer().isTimeExceeded()) {
				return bestMove;
			}
			Move prevMove = otherPlayersMove.get(j);
			for(int i=0; i<diagonalMoves.length; i++){
				int nextX = prevMove.x + diagonalMoves[i][0];
				int nextY = prevMove.y + diagonalMoves[i][1];
				if(isValidMove(board, nextX, nextY)){
					Move thisMove = Move.createMyMove(nextX, nextY);
					double thisScore = board.testMyScoreWithThisMove(thisMove);
					if(thisScore > maxScore) {
						maxScore = thisScore;
						bestMove = thisMove;
					}
				}else{
					int nextX2 = prevMove.x + diagonalMoves[i][0]*2;
					int nextY2 = prevMove.y + diagonalMoves[i][1]*2;
					if(isValidMove(board, nextX2, nextY2)){
						Move thisMove = Move.createMyMove(nextX2, nextY2);
						double thisScore = board.testMyScoreWithThisMove(thisMove);
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

	private Move getBestRandomMove(Board board){
		int poolNo = 1000;
		int[][] currColor = board.getCurrColor();
		
		for(int i=0; i<poolNo; i++){
			if (MoveTimer.getTimer().isTimeExceeded()) {
				return bestMove;
			}
			Move nextMove = null;
			while(nextMove == null){
				int x = r.nextInt(board.getBoardSize());
				int y = r.nextInt(board.getBoardSize());
				
				// if this position is empty and the color of this position is not mine, then accept this move
				if(board.isEmptyAt(x, y) && currColor[x][y] != 0){
					nextMove = Move.createMyMove(x, y);
					double thisScoreDiff = board.testMyScoreWithThisMove(nextMove);
					if(thisScoreDiff > maxScore){
						maxScore = thisScoreDiff;
						bestMove = nextMove;
					}
				}
			}
		}
		return bestMove;
	}

	private Move getBestCenteredMove(Board board){
		int poolNo = 100;
		int[][] currColor = board.getCurrColor();
		
		if (MoveTimer.getTimer().isTimeExceeded()) {
			return bestMove;
		}
		Move nextMove = null;
		if (poolNo >= board.getBoardSize()/2) {
			poolNo = board.getBoardSize()/2 - 1;
		}
		for (int x = board.getBoardSize()/2 - poolNo/2; x < board.getBoardSize()/2 + poolNo/2; x++) {
			for (int y = board.getBoardSize()/2 - poolNo/2; y < board.getBoardSize()/2 + poolNo/2; y++) {
				if (MoveTimer.getTimer().isTimeExceeded()) {
					return bestMove;
				}
				if(board.isEmptyAt(x, y) && currColor[x][y] != 0){
					nextMove = Move.createMyMove(x, y);
					double thisScoreDiff = board.testMyScoreWithThisMove(nextMove);
					if(thisScoreDiff > maxScore){
						maxScore = thisScoreDiff;
						bestMove = nextMove;
					}
				}
				
			}
			
		}
		return bestMove;
	}

	private Move getBestCornerMove(Board board){
		int poolNo = 100;
		int[][] currColor = board.getCurrColor();
		
		if (MoveTimer.getTimer().isTimeExceeded()) {
			return bestMove;
		}
		Move nextMove = null;
		if (poolNo >= board.getBoardSize()/2) {
			poolNo = board.getBoardSize()/2 - 1;
		}
		//NE and SE corners
		for (int x = board.getBoardSize() - poolNo; x < board.getBoardSize(); x++) {
			//NE corner
			for (int y = 0; y < poolNo; y++) {
				if (MoveTimer.getTimer().isTimeExceeded()) {
					return bestMove;
				}
				if(board.isEmptyAt(x, y) && currColor[x][y] != 0){
					nextMove = Move.createMyMove(x, y);
					double thisScoreDiff = board.testMyScoreWithThisMove(nextMove);
					if(thisScoreDiff > maxScore){
						maxScore = thisScoreDiff;
						bestMove = nextMove;
					}
				}
				
			}
			//SE corner
			for (int y = board.getBoardSize() - poolNo; y < board.getBoardSize(); y++) {
				if (MoveTimer.getTimer().isTimeExceeded()) {
					return bestMove;
				}
				if(board.isEmptyAt(x, y) && currColor[x][y] != 0){
					nextMove = Move.createMyMove(x, y);
					double thisScoreDiff = board.testMyScoreWithThisMove(nextMove);
					if(thisScoreDiff > maxScore){
						maxScore = thisScoreDiff;
						bestMove = nextMove;
					}
				}
				
			}
			
		}
		
		//NW and SW
		for (int x = 0; x < poolNo; x++) {
			for (int y = 0; y < poolNo; y++) {
				if (MoveTimer.getTimer().isTimeExceeded()) {
					return bestMove;
				}
				if(board.isEmptyAt(x, y) && currColor[x][y] != 0){
					nextMove = Move.createMyMove(x, y);
					double thisScoreDiff = board.testMyScoreWithThisMove(nextMove);
					if(thisScoreDiff > maxScore){
						maxScore = thisScoreDiff;
						bestMove = nextMove;
					}
				}
				
			}
			for (int y = board.getBoardSize() - poolNo; y < board.getBoardSize(); y++) {
				if (MoveTimer.getTimer().isTimeExceeded()) {
					return bestMove;
				}
				if(board.isEmptyAt(x, y) && currColor[x][y] != 0){
					nextMove = Move.createMyMove(x, y);
					double thisScoreDiff = board.testMyScoreWithThisMove(nextMove);
					if(thisScoreDiff > maxScore){
						maxScore = thisScoreDiff;
						bestMove = nextMove;
					}
				}
				
			}
			
		}
		return bestMove;
	}

	private Move getBestOnePixelOffMove(Board board){
		List<Move> otherPlayersMove = board.getPrevMovesByPlayerId(1);
		
		for(int j=0; j <otherPlayersMove.size(); j++){
			if (MoveTimer.getTimer().isTimeExceeded()) {
				return bestMove;
			}
			Move prevMove = otherPlayersMove.get(j);
			for(int i=0; i<oneOfAllDirections.length; i++){
				int nextX = prevMove.x + oneOfAllDirections[i][0];
				int nextY = prevMove.y + oneOfAllDirections[i][1];
				if(isValidMove(board, nextX, nextY)){
					Move thisMove = Move.createMyMove(nextX, nextY);
					double thisScore = board.testMyScoreWithThisMove(thisMove);
					if(thisScore > maxScore){
						maxScore = thisScore;
						bestMove = thisMove;
					}
				}else{
					int nextX2 = prevMove.x + oneOfAllDirections[i][0]*2;
					int nextY2 = prevMove.y + oneOfAllDirections[i][1]*2;
					if(isValidMove(board, nextX2, nextY2)){
						Move thisMove = Move.createMyMove(nextX2, nextY2);
						double thisScore = board.testMyScoreWithThisMove(thisMove);
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

	private boolean isValidMove(Board board, int x, int y){
		if(x < 0 || y < 0 || x >= board.getBoardSize() || y >= board.getBoardSize() || !board.isEmptyAt(x, y)){
			return false;
		}
		
		return true;
	}

}
