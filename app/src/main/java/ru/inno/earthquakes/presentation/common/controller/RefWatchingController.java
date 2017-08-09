package ru.inno.earthquakes.presentation.common.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.ControllerChangeType;

import ru.inno.earthquakes.EartquakeApp;
/**
 * Extension of  {@link Controller} that works with the LeakCanary library
 * to check if something has leaked.
 */
 public abstract class RefWatchingController extends ButterKnifeController {

    protected RefWatchingController() { }
    protected RefWatchingController(Bundle args) {
        super(args);
    }

    private boolean hasExited;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (hasExited) {
            EartquakeApp.getRefWatcher(getActivity()).watch(this);
        }
    }

    @Override
    protected void onChangeEnded(@NonNull ControllerChangeHandler changeHandler, @NonNull ControllerChangeType changeType) {
        super.onChangeEnded(changeHandler, changeType);

        hasExited = !changeType.isEnter;
        if (isDestroyed()) {
            EartquakeApp.getRefWatcher(getActivity()).watch(this);
        }
    }
}
