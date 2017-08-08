package ru.inno.earthquakes.presentation.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.arellomobile.mvp.MvpDelegate;

/**
 * @author Artur Badretdinov (Gaket)
 *         08.08.17
 */
public abstract class MvpController extends ButterKnifeController {

    private boolean mIsStateSaved;
    private MvpDelegate<? extends MvpController> mMvpDelegate;

    protected MvpController() {
        super();
        this.getMvpDelegate().onCreate();
    }

    protected MvpController(Bundle args) {
        super(args);
        this.getMvpDelegate().onCreate(args);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        this.getMvpDelegate().onAttach();
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        this.getMvpDelegate().onDetach();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.getMvpDelegate().onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsStateSaved = true;
        getMvpDelegate().onSaveInstanceState(outState);
        getMvpDelegate().onDetach();
    }

    private MvpDelegate getMvpDelegate() {
        if (this.mMvpDelegate == null) {
            this.mMvpDelegate = new MvpDelegate<>(this);
        }

        return this.mMvpDelegate;
    }
}
