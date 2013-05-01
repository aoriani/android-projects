
package com.example.openglanimations.animations;

import com.example.openglanimations.QuadShape;

import java.util.Collection;

public abstract class AnimationSet extends Animation {

    protected Collection<Animation> mAnimations;

    public AnimationSet(Collection<Animation> animations){
        mAnimations = animations;
        this.startTime = now();
        this.duration = 0l;
    }


    @Override
    public void setTarget(QuadShape quad) {
        super.setTarget(quad);
        for(Animation animation : mAnimations){
            animation.setTarget(quad);
        }
    }

}
