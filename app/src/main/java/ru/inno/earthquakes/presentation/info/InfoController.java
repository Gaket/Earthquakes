package ru.inno.earthquakes.presentation.info;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.presentation.common.controller.BaseController;

/**
 * Controller that shows that we do not always have to work with presenters and interactors :)
 *
 * @author Artur Badretdinov (Gaket)
 *         08.08.17
 */
public class InfoController extends BaseController {

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        showActionBarBackButton(true);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_info, container, false);
    }

    @OnClick(R.id.info_got_it)
    void onReadyAction(){
        getRouter().popCurrentController();
    }

    @NonNull
    @Override
    protected String getTitle() {
        return getResources().getString(R.string.title_info);
    }
}
