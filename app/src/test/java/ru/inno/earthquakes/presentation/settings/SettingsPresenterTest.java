package ru.inno.earthquakes.presentation.settings;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.inno.earthquakes.business.settings.SettingsInteractor;
import ru.inno.earthquakes.models.entities.Location;
import ru.inno.earthquakes.models.entities.Location.Coordinates;

/**
 * Created by Artur (gaket) on 2019-11-15.
 */
@RunWith(MockitoJUnitRunner.class)
public class SettingsPresenterTest {

  private SettingsPresenter sut;

  @Mock
  SettingsInteractor settingsInteractor;
  @Mock
  SettingsView settingsView;

  @Before
  public void setUp() throws Exception {
    Mockito.when(settingsInteractor.getDefaultLocation())
        .thenReturn(new Location("Moscow", new Coordinates(0,0)));
    sut = new SettingsPresenter(settingsInteractor);
    sut.attachView(settingsView);
  }

  @Test
  public void testSettingHardcodedString() {
    // Arrange
    String emptyInput = "";
    double someValue = 1.0;

    // Act
    sut.onSave(emptyInput, someValue);

    // Assert
    Mockito.verify(settingsView).showError("Enter integer number");
  }

}