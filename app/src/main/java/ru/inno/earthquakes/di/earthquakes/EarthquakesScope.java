package ru.inno.earthquakes.di.earthquakes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

/**
 * @author Artur Badretdinov (Gaket) 22.07.17.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
@interface EarthquakesScope {

}
