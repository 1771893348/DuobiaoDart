package com.duobiao.mainframedart.game.base;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Author:Admin
 * Time:2019/8/2 10:32
 * 描述：
 */
public interface IBasePresenter extends LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate();
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume();
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause();
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy();
}
