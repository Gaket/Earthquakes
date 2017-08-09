package ru.inno.earthquakes.presentation.common.controller;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

import ru.inno.earthquakes.presentation.common.ActionBarProvider;
import timber.log.Timber;

/**
 * @author Artur Badretdinov (Gaket)
 *         09.08.17
 */
public abstract class BaseController extends MvpController {

    @Override
    protected void onAttach(@NonNull View view) {
        setTitle();
        super.onAttach(view);
    }

    protected ActionBar getActionBar() {
        ActionBarProvider actionBarProvider = ((ActionBarProvider) getActivity());
        return actionBarProvider != null ? actionBarProvider.getSupportActionBar() : null;
    }

    protected void setTitle() {
        Controller parentController = getParentController();
        while (parentController != null) {
            if (parentController instanceof BaseController && ((BaseController) parentController).getTitle() != null) {
                return;
            }
            parentController = parentController.getParentController();
        }

        String title = getTitle();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        } else {
            Timber.e("Trying to set title on null action bar");
        }
    }

    @NonNull
    protected String getTitle() {
        return "";
    }
}
