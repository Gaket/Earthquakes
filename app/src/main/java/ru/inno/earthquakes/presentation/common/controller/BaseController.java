package ru.inno.earthquakes.presentation.common.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

import ru.inno.earthquakes.presentation.common.ActionBarProvider;
import timber.log.Timber;

/**
 *  Extensin of {@link Controller} that helps to work with {@link ActionBar}
 *
 * @author Artur Badretdinov (Gaket)
 *         09.08.17
 */
public abstract class BaseController extends MvpController {

    @Override
    protected void onAttach(@NonNull View view) {
        setTitle();
        super.onAttach(view);
    }

    @Nullable
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

    protected void showActionBarBackButton(boolean show) {
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getRouter().popCurrentController();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    protected String getTitle() {
        return "";
    }
}
