package com.example.openglanimations.animations;

import android.view.animation.Interpolator;

public class RotateAnimation extends Animation {
    
    float startRx,endRx,startRy,endRy,startRz,endRz;

    public RotateAnimation( float startRx, float endRx, float startRy, float endRy,
            float startRz, float endRz,  Interpolator interpolator, long duration) {
        super(duration, interpolator);
        this.startRx = startRx;
        this.endRx = endRx;
        this.startRy = startRy;
        this.endRy = endRy;
        this.startRz = startRz;
        this.endRz = endRz;
    }



    @Override
    public void computeAndApply() {
        if(target != null){
            
            float interp = interpolator.getInterpolation(currentProgress());
            float newX = calculateInterpolation(startRx,endRx,interp);
            float newY = calculateInterpolation(startRy,endRy,interp);
            float newZ = calculateInterpolation(startRz,endRz,interp);
            target.setRx(newX);
            target.setRy(newY);
            target.setRz(newZ);
        }

    }

}
