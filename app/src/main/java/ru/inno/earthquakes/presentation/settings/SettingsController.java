package ru.inno.earthquakes.presentation.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.model.settings.SettingsInteractor;
import ru.inno.earthquakes.presentation.common.controller.BaseController;
import ru.inno.earthquakes.presentation.info.InfoController;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

/**
 * @author Artur Badretdinov (Gaket)
 *         08.08.17
 */
public class SettingsController extends BaseController implements SettingsView {

    @InjectPresenter
    SettingsPresenter presenter;
    @Inject
    SettingsInteractor interactor;

    @BindView(R.id.settings_distance)
    EditText distanceView;
    @BindView(R.id.settings_magnitude)
    SeekBar magnitudeView;
    @BindView(R.id.settings_magnitude_value)
    TextView magnitudeValueView;

    @ProvidePresenter
    SettingsPresenter providePresenter() {
        EartquakeApp.getComponentsManager().getEarthquakesComponent().inject(this);
        return new SettingsPresenter(interactor);
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        // According to YAGNI, we have only some hardcoded views here.
        // If application becomes more complicated, here should be a RecyclerView with options.
        setDistanceView();
        setMagnitudeView();
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    protected String getTitle() {
        return getResources().getString(R.string.title_settings);
    }

    @Override
    public void setMaxDistance(Double dist) {
        distanceView.setText(String.format(Locale.getDefault(), "%.0f", dist));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                presenter.onInfoAction();
                return true;
            case android.R.id.home:
                getRouter().popCurrentController();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setMinMagnitude(Double mag) {
        magnitudeView.setProgress((int) (mag * 10));
    }

    @Override
    public void navigateToInfo() {
        getRouter().pushController(RouterTransaction.with(new InfoController()));
    }

    @Override
    public void close() {
        getRouter().popCurrentController();
    }

    @Override
    public void showDistanceFormatError() {
        distanceView.setError(getResources().getString(R.string.error_settings_distance));
    }

    private void setMagnitudeView() {
        magnitudeView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                magnitudeValueView.setText(String.format(Locale.getDefault(), "%.1f", progress / 10.0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });
    }

    private void setDistanceView() {
        distanceView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == IME_ACTION_DONE) {
                saveSettings();
                return true;
            } else {
                return false;
            }
        });
    }

    @OnClick(R.id.settings_bt_save)
    void onSaveSettings() {
        saveSettings();
    }

    private void saveSettings() {
        String dist = distanceView.getText().toString();
        double magnitude = magnitudeView.getProgress() / 10.0;
        presenter.onSave(dist, magnitude);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void onDetach(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) distanceView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(distanceView.getWindowToken(), 0);
    }
}
