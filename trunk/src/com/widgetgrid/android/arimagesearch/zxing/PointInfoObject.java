package com.widgetgrid.android.arimagesearch.zxing;

public class PointInfoObject {

	private PointPattern bottomLeft;
	private PointPattern topLeft;
	private PointPattern topRight;
	
	public PointInfoObject(PointPattern patternCenters[]) {
		if(patternCenters == null)
			return;
		this.bottomLeft = patternCenters[0];
		this.topLeft = patternCenters[1];
		this.topRight = patternCenters[2];
		
	}

	public PointPattern getBottomLeft() { return bottomLeft; }
	public PointPattern getTopLeft() { return topLeft; }
	public PointPattern getTopRight() { return topRight; }
	
	public PointPattern[] getPoints() {
		int count = (bottomLeft == null ? 0 : 1) + (topLeft == null ? 0 : 1) + (topRight == null ? 0 : 1);
		PointPattern points[] = new PointPattern[count];
		
		short on = 0;
		if(bottomLeft != null)
			points[on++] = bottomLeft;
		if(topLeft != null)
			points[on++] = topLeft;
		if(topRight != null)
			points[on++] = topRight;
		
		return points;
	}
	
}
