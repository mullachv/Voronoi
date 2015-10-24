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
	int currScore[];
	
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
		
		//cache = new double[size][size];
		//initialCache();
		
		currScore = new int[playerNo];
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
				double maxPull = 0;
				int maxPullPlayer = -1;
				for(int k=0; k<playerNo; k++){
					if(currPull[i][j][k] > maxPull){
						maxPull = currPull[i][j][k];
						maxPullPlayer = k;
					}
				}
				
				// update this position color with the playerId with maxPull
				currColor[i][j] = maxPullPlayer;
				
				// add score to the player
				currScore[maxPullPlayer]++;
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
	
	public int getCurrScoreByPlayer(int playerId){
		return currScore[playerId];
	}
	
	public double[][][] getCurrPull(){
		return currPull;
	}
	
	public int[][] getCurrColor(){
		return currColor;
	}
	
	/*
	public double[][][] getCopyOfCurrPull(){
		double[][][] copyCurrPull = new double[size][size][playerNo];
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				for(int k=0; k<playerNo; k++){
					copyCurrPull[i][j][k] = currPull[i][j][k];
				}
			}
		}
		return copyCurrPull;
	}*/
}
