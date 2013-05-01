package com.example.openglanimations;


import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.example.openglanimations.animations.Animation;
import com.example.openglanimations.animations.GotoAnimation;
import com.example.openglanimations.animations.ParallelAnimation;
import com.example.openglanimations.animations.RotateAnimation;
import com.example.openglanimations.animations.SequentialAnimation;

import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render implements Renderer {
    
    
    private GLSurfaceView mSurface;
    private QuadShape mQuadShape;
    private Animation mAnim;

    public Render(GLSurfaceView surface){
        mSurface = surface;
        mQuadShape = new QuadShape();
        Interpolator interpolator = new BounceInterpolator();
        Animation animTranslate = new GotoAnimation(0f,0f,2f,2f,-6f,-50f, interpolator,5000);
        Animation animRotate =  new RotateAnimation(45,135,180,0,0,359,interpolator,5000);
        mAnim = new SequentialAnimation(Arrays.asList(new Animation[]{animTranslate,animRotate}));
        mAnim.setTarget(mQuadShape);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if(height == 0) {                       //Prevent A Divide By Zero By
            height = 1;                         //Making Height Equal One
        }

        gl.glViewport(0, 0, width, height);     //Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION);    //Select The Projection Matrix
        gl.glLoadIdentity();                    //Reset The Projection Matrix

        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        

        gl.glMatrixMode(GL10.GL_MODELVIEW);     //Select The Modelview Matrix
        gl.glLoadIdentity();                    //Reset The Modelview Matrix

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glShadeModel(GL10.GL_SMOOTH);            //Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    //Black Background
        gl.glClearDepthf(1.0f);                     //Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL);             //The Type Of Depth Testing To Do
        
        //Really Nice Perspective Calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }
    
    @Override
    public void onDrawFrame(GL10 gl) {
        //Clear Screen And Depth Buffer
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);    
        gl.glLoadIdentity();                    //Reset The Current Modelview Matrix
        
        mAnim.computeAndApply();
        mQuadShape.draw(gl);

    }

}
