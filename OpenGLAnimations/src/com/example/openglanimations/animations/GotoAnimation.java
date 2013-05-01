package com.example.openglanimations.animations;

import android.view.animation.Interpolator;



public class GotoAnimation extends Animation {
    private float startX,startY,endX,endY,startZ, endZ;
    
    public GotoAnimation(float startX, float startY, float endX, float endY,
            float startZ, float endZ,Interpolator interpolator, long duration){
        super(duration,interpolator);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.startZ = startZ;
        this.endZ = endZ;
    }
    
    @Override
    public void computeAndApply(){
        if(target != null){
            
            float interp = interpolator.getInterpolation(currentProgress());
            float newX = calculateInterpolation(startX,endX,interp);
            float newY = calculateInterpolation(startY,endY,interp);
            float newZ = calculateInterpolation(startZ,endZ,interp);
            target.setX(newX);
            target.setY(newY);
            target.setZ(newZ);
        }
    }
}
