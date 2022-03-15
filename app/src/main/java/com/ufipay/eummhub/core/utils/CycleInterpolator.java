package com.ufipay.eummhub.core.utils;

public class CycleInterpolator implements android.view.animation.Interpolator{

    @Override
    public float getInterpolation(float input) {
        float mCycles = 0.5f;
        return (float) Math.sin( 2.0f * mCycles * Math.PI * input );
    }
}
