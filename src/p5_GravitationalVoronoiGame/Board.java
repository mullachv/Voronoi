package p5_GravitationalVoronoiGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
	
	List<Move> previousMoves;
	boolean[][] hasStone;
	final int size = 1000;
	int[][] currColor;
	double[][][] currPull;
	//double[][] cache;
	Map<Integer, List<Move>> prevMovesByPlayer;
	int playerNo =2;
	double currScore[];
	
	public Board(){
		previousMoves = new ArrayList<Move>();
		hasStone = new boolean[size][size];
		prevMovesByPlayer = new HashMap<Integer, List<Move>>();
		
		// store the current board color, default = -1 = belongs to no one
		// color = playerId
		currColor = new int[size][size];
		for(int[] row: currColor){
			Arrays.fill(row, -1);
		}
		
		// store the currPull for each position for each player
		// when a new stone is added, update all positions by adding pulls for this new stone
		currPull = new double[size][size][playerNo];
		
		currScore = new double[playerNo];
	}
	
	public void addPrevMove(Move m){
		previousMoves.add(m);
		hasStone[m.x][m.y] = true;
		
		// add previous move to this player's move list
		if(prevMovesByPlayer.get(m.plId) == null){
			prevMovesByPlayer.put(m.plId, new ArrayList<Move>());
		}
		prevMovesByPlayer.get(m.plId).add(m);
		
		updateCurrScore(m);
	}
	
	public List<Move> getPrevMoves(){
		return previousMoves;
	}
	
	public List<Move> getPrevMovesByPlayerId(int playerId){
		return prevMovesByPlayer.get(playerId);
	}
	
	public void reset(){
		previousMoves.clear();
		hasStone = new boolean[size][size];
	}
	
	public int getBoardSize(){
		return size;
	}
	
	public boolean isEmptyAt(int x, int y){
		return !hasStone[x][y];
	}
	
	public void printAllPrevMovs(){
		for(Move m: previousMoves){
			System.out.println(m.toFullString());
		}
	}
	
	private void updateCurrScore(Move newMove){
		
		// reset the score to 0
		Arrays.fill(currScore, 0);
		
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				
				// check the calculation rules here with architect team
				currPull[i][j][newMove.plId] += 1/AbsPlayer.getDistanceSq(i, j, newMove);
				
				// update currColor based on currPull
				double maxPull = -1;
				int maxPullPlayer = -1;
				List<Integer> maxScoreIdx = new ArrayList<Integer>();
				for(int k=0; k<playerNo; k++){
					if(currPull[i][j][k] > maxPull){
						maxPull = currPull[i][j][k];
						maxPullPlayer = k;
						maxScoreIdx.clear();
						maxScoreIdx.add(k);
					}else if(currPull[i][j][k] == maxPull){
						maxScoreIdx.add(k);
					}
				}
				
				// found a tie
				if(maxScoreIdx.size() > 1){
					
					// use playerNo representing a tie position
					currColor[i][j] = playerNo;
					
					// each tie player get 1/n score, where n is the number of tie players in this position
					for(int k=0; k<maxScoreIdx.size(); k++){
						currScore[maxScoreIdx.get(k)] += (double)1/maxScoreIdx.size();						
					}

				}else{
					// update this position color with the playerId with maxPull
					currColor[i][j] = maxPullPlayer;
					
					// add score to the player
					currScore[maxPullPlayer]++;	
				}
			}
		}
	}
	
	/*
	private void initialCache(){
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				double d = i*i + j*j;
				if(i==0 && j==0){
					cache[i][j] = 1000;
				}else{
					// check the calculation rules here with architect team
					cache[i][j] = 1/(d*d);
				}
			}
		}
	}*/
	
	public void printCurrScore(){
		System.out.println("CurrScore==");
		for(int i=0; i<playerNo; i++){
			System.out.println("player " + i + ":" + currScore[i]);
		}
		System.out.println();
	}
	
	public double getCurrScoreByPlayer(int playerId){
		return currScore[playerId];
	}
	
	public double[][][] getCurrPull(){
		return currPull;
	}
	
	public int[][] getCurrColor(){
		return currColor;
	}
	
	// calculate how many score I can increase if I make this move
	// this method won't change board status
	public double testMyScoreWithThisMove(Move newMove){
		
		double scoreDiff = 0;
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				if(currColor[i][j] == 0){ continue; }
				double newPull = currPull[i][j][0] + (double)1/AbsPlayer.getDistanceSq(i, j, newMove);
				
				double maxPull = newPull;
				int tieNo = 0;
				for(int k=1; k<playerNo; k++){
					if(currPull[i][j][k] > maxPull){
						maxPull = currPull[i][j][k];
						tieNo = 0;
					}else if(currPull[i][j][k] == maxPull){
						tieNo++;
					}
				}
				
				// I am one of the max pull player
				// tieNo+1= maxPull player in this position
				if(maxPull == newPull){
					scoreDiff += 1/(tieNo+1);
				}
			}
		}
		
		return scoreDiff;
	}

	//approximates the left (x) of an inscribed circle inside a square of 1000*1000
	int getJMin(int i) {
		int tl = Math.abs(i - size/2);
		int tl015 = (int) ((double) tl * 0.15);
		if (tl - tl015 >=0 ) {
			return tl - tl015;
		}
		return 0;
	}
	
	//approximates the right (x) of an inscribed circle inside a square of 1000*1000
	int getJMax(int i) {
		int tr = (i + size/2);
		int tl015 = (int) ((double) Math.abs(i - size/2) * 0.15);
		if (i >= size/2) {
			tr = size/2 - tr;
		}
		int tr015 = tr + tl015;
		if (tr015 >= size) {
			return size;
		}
		return tr015;		
	}
	
	//do not scan 1000 x 1000 but reduce it to the inscribed circle
	public double testMyScoreWithThisMoveV5(Move newMove){
		
		double scoreDiff = 0;
		for(int i=0; i<size; i++){
			int jmin = getJMin(i);
			int jmax = getJMax(i);			
			for(int j=jmin; j<=jmax; j++){
				if(currColor[i][j] == 0){ continue; }
				double newPull = currPull[i][j][0] + (double)1/AbsPlayer.getDistanceSq(i, j, newMove);
				
				double maxPull = newPull;
				int tieNo = 0;
				for(int k=1; k<playerNo; k++){
					if(currPull[i][j][k] > maxPull){
						maxPull = currPull[i][j][k];
						tieNo = 0;
					}else if(currPull[i][j][k] == maxPull){
						tieNo++;
					}
				}
				
				// I am one of the max pull player
				// tieNo+1= maxPull player in this position
				if(maxPull == newPull){
					scoreDiff += 1/(tieNo+1);
				}
			}
		}
		
		return scoreDiff;
	}
}
