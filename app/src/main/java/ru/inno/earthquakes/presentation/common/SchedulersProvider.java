package ru.inno.earthquakes.presentation.common;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Scheduler provider needed to facilitate testing.
 *
 * @author Artur Badretdinov (Gaket)
 *         09.08.17
 */
public class SchedulersProvider {

    public Scheduler io() {
        return Schedulers.io();
    }

    public Scheduler computation() {
        return Schedulers.computation();
    }

    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
