package p5_GravitationalVoronoiGame;

public class MoveTimer {
	long thinkingTime = 0;
	long previousTime;
	long currentTime;
	long allowedTimeBurst;//per move
	long MaxMoves;
	long MaxTime = 2 * 60 * 1000; //2minutes
	boolean firstCall = true;
	static MoveTimer mt = null;
	int printModulo = 10000;
	long callCount = 0;
	
	public static MoveTimer initTimer(int max) {
		if (mt == null) {
			mt = new MoveTimer(max);
		}
		return mt;
	}

	public static MoveTimer getTimer() {
		return mt;
	}
	
	private MoveTimer(int max) {
		MaxMoves = max;
		previousTime = System.currentTimeMillis();
		allowedTimeBurst = MaxTime / MaxMoves;
	}
	
	private void beginTimer() {
		previousTime = System.currentTimeMillis();		
	}
	
	public boolean isTimeExceeded() {
		if (firstCall) {
			firstCall = false;
			beginTimer();
			return false;
		}
		callCount++;
		currentTime = System.currentTimeMillis();
		updateThinkTime();
		if (currentTime >= previousTime + allowedTimeBurst) {
			previousTime = currentTime;
			System.out.println("isTimeExceeded allowed time: " + allowedTimeBurst/1000);
			return true;
		}
		previousTime = currentTime;
		if (callCount % printModulo == 0) {
			System.out.println("not Exceeded allowed time: " + allowedTimeBurst/1000);
		}
		return false;
	}
	
	public void updateThinkTime() {
		thinkingTime += currentTime - previousTime;
	}
}
