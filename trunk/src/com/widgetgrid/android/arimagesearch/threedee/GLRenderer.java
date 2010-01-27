package com.widgetgrid.android.arimagesearch.threedee;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.graphics.Bitmap;
import android.content.Context;

//import java.io.IOException;

class GLRenderer implements GLSurfaceView.Renderer {
   private static final String TAG = "GLRenderer";
   //private final Context context;
   
   public Bitmap cameraBitmap;
   
   private int _width;
   private int _height;
   
   
   GLRenderer(Context context) {
      //this.context = context;
   }

   public void onSurfaceCreated(GL10 gl, EGLConfig config) {
      
      // Set up any OpenGL options we need
      gl.glEnable(GL10.GL_DEPTH_TEST); 
      gl.glDepthFunc(GL10.GL_LEQUAL);
      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

      // gl.glDisable(GL10.GL_DITHER);
      
      // Enable textures
      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
      gl.glEnable(GL10.GL_TEXTURE_2D);
      
   }
   
   public void onSurfaceChanged(GL10 gl, int width, int height) {
      
	   _width = width;
	   _height = height;
	   
      gl.glViewport(0, 0, width, height);
      gl.glMatrixMode(GL10.GL_PROJECTION);
      gl.glLoadIdentity();
      float ratio = (float) width / height;
      GLU.gluPerspective(gl, 45.0f, ratio, 1, 100f);
      
      
   }
   public void onDrawFrame(GL10 gl) {
      
      // Clear the screen to black
      gl.glClear(GL10.GL_COLOR_BUFFER_BIT
            | GL10.GL_DEPTH_BUFFER_BIT);
      
      if(cameraBitmap != null) {
    	  //gl.glBindTexture(GL10.GL_TEXTURE_2D, cameraBitmap);
    	  ((GL11Ext) gl).glDrawTexfOES(0, 0, 0, _width, _height);
      }
      
   }
   
   
}