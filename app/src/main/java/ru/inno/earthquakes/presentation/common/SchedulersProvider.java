package ru.inno.earthquakes.presentation.common;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Artur Badretdinov (Gaket)
 *         09.08.17
 */
public class SchedulersProvider {

    Scheduler io() {
        return Schedulers.io();
    }

    Scheduler computation() {
        return Schedulers.computation();
    }

    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
