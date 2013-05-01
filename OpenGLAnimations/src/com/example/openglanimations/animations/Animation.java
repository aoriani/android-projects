
package com.example.openglanimations.animations;

import android.view.animation.Interpolator;

import com.example.openglanimations.QuadShape;

public abstract class Animation {

    protected long startTime;
    protected long duration;
    protected Interpolator interpolator;
    protected QuadShape target;


    protected Animation(long duration, Interpolator interpolator){
        this.startTime = now();
        this.duration = duration;
        this.interpolator = interpolator;
    }
    
    
    public abstract void computeAndApply();
    
    protected static float calculateInterpolation(float si, float sf, float interp) {
        return si + (sf-si)*interp;
    }

    public void setStartTime(long time) {
        startTime = time;
    }

    public void setTarget(QuadShape quad) {
        target = quad;
    }

    public boolean hasStarted(){
        return now()>= startTime;
    }
    
    public boolean hasEnded() {
        return now()> (startTime + duration);
    }

    protected float currentProgress() {
        long now = now();
        if(now < startTime){
            return 0f;
        }else if( now> (startTime + duration)){
            return 1f;
        }else {
            float timeSinceStart = (now - startTime);
            float progress = timeSinceStart/duration;
            return progress;
        }
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    public Animation() {
        super();
    }

}
