package com.widgetgrid.android.arimagesearch;

public class SimpleVector {
	public float i, j, k;
	
	SimpleVector(float i, float j, float k) {
		
		this.i = i;
		this.j = j;
		this.k = k;
		
	}
	
	SimpleVector(float bx, float by, float gx, float gy) {
		
		float c = -1 * (float) Math.pow(gx * bx + gy * by, 2);
		float b = gx * gx + gy * gy - (bx * bx) - (by * by);
		b *= -1;
		
		float d = (float) Math.sqrt(b * b - 4 * c);
		float p = ((b + d) / 2);
		float q = ((b - d) / 2);
		
		float gz, bz;
		
		if(p >= 0) {
			q *= -1;
			bz = (float) Math.sqrt(q);
			gz = (float) Math.sqrt(p);
		} else {
			p *= -1;
			bz = (float) Math.sqrt(p);
			gz = (float) Math.sqrt(q);
		}
		
		if(gy < 0) {
			gz *= -1;
		}
		if(by < 0) {
			bz *= -1;
		}
		
		float i, j, k;
		i = gy * bz - by * gz;
		j = gz * bx - bz * gx;
		k = gx * by - bx * gy;
		
		if(j < 0) {
			i *= -1;
			j *= -1;
			k *= -1;
		}

		this.i = i;
		this.j = j;
		this.k = k;
		
	}
	
	public void makeUnitVector() {
		
		float x = (float) Math.sqrt(i * i + j * j + k * k);
		i /= x;
		j /= x;
		k /= x;
		
	}
	
}
