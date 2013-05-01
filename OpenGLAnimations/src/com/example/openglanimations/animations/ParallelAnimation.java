package com.example.openglanimations.animations;


import java.util.Collection;

public class ParallelAnimation extends AnimationSet {
    
    public ParallelAnimation(Collection<Animation> animations){
        super(animations);
        for(Animation animation : mAnimations){
            animation.startTime = this.startTime;
            //set Total duration to the biggest one
            if(animation.duration > this.duration){
                this.duration = animation.duration;
            }
        }
    }

    @Override
    public void computeAndApply() {
        for(Animation animation : mAnimations){
            animation.computeAndApply();
        }
    }

}
