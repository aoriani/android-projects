package com.example.openglanimations.animations;

import java.util.Collection;

public class SequentialAnimation extends AnimationSet {

    public SequentialAnimation(Collection<Animation> animations) {
        super(animations);
        long nextStart = this.startTime;
        long totalDuration = 0l;
        for(Animation animation:mAnimations){
            animation.setStartTime(nextStart);
            nextStart += animation.duration;
            totalDuration += animation.duration;
        }
    }

    @Override
    public void computeAndApply() {
        for(Animation animation:mAnimations){
            if(animation.hasStarted() && !animation.hasEnded()){
                animation.computeAndApply();
                break;
            }
        }
    }

}
