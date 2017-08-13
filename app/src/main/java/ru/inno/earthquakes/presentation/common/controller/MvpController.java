package ru.inno.earthquakes.presentation.common.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.arellomobile.mvp.MvpDelegate;
import com.bluelinelabs.conductor.Controller;

/**
 * Extension of  {@link Controller} that works with the Moxy library
 * to save view state in configuration changes.
 *
 * @author Artur Badretdinov (Gaket)
 *         08.08.17
 */
public abstract class MvpController extends RefWatchingController {

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
            if (getActivity() != null && getActivity().isFinishing()) {
                this.getMvpDelegate().onDestroy();
            }
        }

        @Override
        protected void onDestroyView(@NonNull View view) {
            super.onDestroyView(view);
            this.getMvpDelegate().onDetach();
            this.getMvpDelegate().onDestroyView();
        }

        @Override
        protected void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            this.getMvpDelegate().onSaveInstanceState(outState);
            this.getMvpDelegate().onDetach();
        }

        private MvpDelegate getMvpDelegate() {
            if (this.mMvpDelegate == null) {
                this.mMvpDelegate = new MvpDelegate<>(this);
            }
            return this.mMvpDelegate;
        }
}
