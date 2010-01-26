package com.widgetgrid.android.arimagesearch.zxing;

public class PointPattern {
	
	private final float estimatedModuleSize;
	private int count;
	
	public int x;
	public int y;

	PointPattern(int posX, int posY, float estimatedModuleSize) {
		this.x = posX;
		this.y = posY;
		this.estimatedModuleSize = estimatedModuleSize;
		this.count = 1;
	}

	public int getX() {return x;}
	public int getY() {return y;}
	
	public boolean equals(Object other) {
		if (other instanceof PointPattern) {
			PointPattern otherPoint = (PointPattern) other;
			return x == otherPoint.x && y == otherPoint.y;
		}
		return false;
	}
	
	public int hashCode() {return 31 * Float.floatToIntBits(x) + Float.floatToIntBits(y);}
	
	// toString has been eliminated. http://zxing.googlecode.com/svn/trunk/core/src/com/google/zxing/ResultPoint.java

	public float getEstimatedModuleSize() { return estimatedModuleSize; }
	int getCount() { return count; }
	void incrementCount() { this.count++; }
	
	
	public static void orderBestPatterns(PointPattern[] patterns) {

		if(patterns == null)
			return;
		
		// Find distances between pattern centers
		float zeroOneDistance = distance(patterns[0], patterns[1]);
		float oneTwoDistance = distance(patterns[1], patterns[2]);
		float zeroTwoDistance = distance(patterns[0], patterns[2]);

		PointPattern pointA, pointB, pointC;
		// Assume one closest to other two is B; A and C will just be guesses at first
		if (oneTwoDistance >= zeroOneDistance && oneTwoDistance >= zeroTwoDistance) {
			pointB = patterns[0];
			pointA = patterns[1];
			pointC = patterns[2];
		} else if (zeroTwoDistance >= oneTwoDistance && zeroTwoDistance >= zeroOneDistance) {
			pointB = patterns[1];
			pointA = patterns[0];
			pointC = patterns[2];
		} else {
			pointB = patterns[2];
			pointA = patterns[0];
			pointC = patterns[1];
		}

		// Use cross product to figure out whether A and C are correct or flipped.
		// This asks whether BC x BA has a positive z component, which is the arrangement
		// we want for A, B, C. If it's negative, then we've got it flipped around and
		// should swap A and C.
		if (crossProductZ(pointA, pointB, pointC) < 0.0f) {
			PointPattern temp = pointA;
			pointA = pointC;
			pointC = temp;
		}

		patterns[0] = pointA;
		patterns[1] = pointB;
		patterns[2] = pointC;
	}
	
	public static float distance(PointPattern pattern1, PointPattern pattern2) {
		// This is my hack replacement formula. Not as accurate, but hella faster.
		int leg1 = Math.abs(pattern1.x - pattern2.x),
			leg2 = Math.abs(pattern1.y - pattern2.y);
		int rough = leg1 + leg2;
		
		rough += Math.min(leg1, leg2) * -0.585786438; // That last term is a rough approximation of sqrt(2) - 2
		
		return rough;
		
		//return (float) Math.sqrt((double) (xDiff * xDiff + yDiff * yDiff));
	}
	
	private static float crossProductZ(PointPattern pointA, PointPattern pointB, PointPattern pointC) {
		float bX = pointB.x;
		float bY = pointB.y;
		return ((pointC.x - bX) * (pointA.y - bY)) - ((pointC.y - bY) * (pointA.x - bX));
	}
	

	/**
	* <p>Determines if this finder pattern "about equals" a finder pattern at the stated
	* position and size -- meaning, it is at nearly the same center with nearly the same size.</p>
	*/
	boolean aboutEquals(float moduleSize, float i, float j) {
		if (Math.abs(i - y) <= moduleSize && Math.abs(j - x) <= moduleSize) {
			float moduleSizeDiff = Math.abs(moduleSize - estimatedModuleSize);
			return moduleSizeDiff <= 1.0f || moduleSizeDiff / estimatedModuleSize <= 1.0f;
		}
		return false;
	}
}
