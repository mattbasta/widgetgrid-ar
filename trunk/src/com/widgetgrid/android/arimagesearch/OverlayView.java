package com.widgetgrid.android.arimagesearch;

import com.widgetgrid.android.arimagesearch.zxing.PointInfoObject;
import com.widgetgrid.android.arimagesearch.zxing.PointPattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class OverlayView extends View {
	
	public Bitmap testData;
	public PointInfoObject points;
	
	public int _width;
	public int _height;
	
	public OverlayView(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		
		_width = canvas.getWidth();
		_height = canvas.getHeight();
		
		if(testData != null)
			canvas.drawBitmap(testData, 0, 0, paint);
		

		int colors[] = {
			Color.rgb(225, 40, 225), // Magenta
			Color.rgb(190, 190, 0), // Yellow
			Color.rgb(0, 255, 255) // Cyan
		};

		paint.setStrokeWidth(3);
		paint.setColor(Color.GREEN);
		
		if(points != null) {
			PointPattern patterns[] = points.getPoints();
			for(int x = 0; x < patterns.length; x++) {
				if(patterns[x] == null)
					continue;
				paint.setColor(colors[x]);
				PointPattern dot = patterns[x];
				
				canvas.drawCircle(dot.x, dot.y, 4 + (3 - x) * 3, paint);

			}
			
			float bx, by, gx, gy;

			bx = patterns[0].x - patterns[1].x;
			by = patterns[0].y - patterns[1].y;
			gx = patterns[2].x - patterns[1].x;
			gy = patterns[2].y - patterns[1].y;
			
			SimpleVector s = new SimpleVector(bx, by, gx, gy);
			s.makeUnitVector();
			
			paint.setColor(Color.GREEN);
			canvas.drawLine(patterns[1].x, patterns[1].y, patterns[1].x + (int)(s.i * 40), patterns[1].y + (int)(s.j * -40), paint);
			
			
		}
		
		
		super.onDraw(canvas);
	}
	
}
