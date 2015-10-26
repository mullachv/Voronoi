package p5_GravitationalVoronoiGame.strategy;

import java.util.List;
import p5_GravitationalVoronoiGame.AbsPlayer;
import p5_GravitationalVoronoiGame.Board;
import p5_GravitationalVoronoiGame.Move;
import p5_GravitationalVoronoiGame.Strategy;

// V2: use all previous moves from other player, and get position having the best score
public class OnePixelToMaxScoreStrategyV2 implements Strategy{

	int[][] directions = {{-1, 0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}, {1,1}, {0, 1}, {-1, 1}};
	
	@Override
	public Move makeAMove(Board board) {
		if(board.getPrevMoves().size() == 0){
			return Move.createMyMove(board.getBoardSize()/2, board.getBoardSize()/2);
		}
		
		List<Move> otherPlayersMove = board.getPrevMovesByPlayerId(1);
		Move bestMove = null;
		double maxScore = 0;
		
		for(int j=0; j<otherPlayersMove.size(); j++){
			Move prevMove = otherPlayersMove.get(j);
			for(int i=0; i<directions.length; i++){
				int nextX = prevMove.x + directions[i][0];
				int nextY = prevMove.y + directions[i][1];
				if(isValidMove(board, nextX, nextY)){
					Move thisMove = Move.createMyMove(nextX, nextY);
					double thisScore = board.testMyScoreWithThisMove(thisMove);
					if(thisScore > maxScore){
						maxScore = thisScore;
						bestMove = thisMove;
					}
				}else{
					int nextX2 = prevMove.x + directions[i][0]*2;
					int nextY2 = prevMove.y + directions[i][1]*2;
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
