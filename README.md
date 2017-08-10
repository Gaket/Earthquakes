# Earthquakes Alert

## About

Demo app, that helps you to be aware of earthquakes nearby. Based on data from http://earthquake.usgs.gov/

The app demonstrates principles of Clean Architecture in Android. You can read [the original post](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) from Uncle Bob or one of the first [thoughts about it in Android](https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/). MVP pattern is used for the presentation layer.

## Features

1. The main screen notifies users if there are any earthquakes with given from settings magnitude and distance from them. If there is more than one earthquake satisfying settings, only the closest one is shown.
2. The settings screen gives a possibility to set maximal distance, to which we should show the alert and minimal magnitude needed to show the alert. Both of these parameters are used as filters for Earthquakes alert.
3. The list of all earthquakes in the last day sorted by the distance from the user without filtering. It supports offline mode and will show cached data even in a case of lack of internet connection. If users click on an earthquake, a webpage with more details is opened through the default browser.
4. Simple info screen, containing basic application info.

## Technology

To show some of the nowadays Android developer toolkits and facilitate the development process, the project uses some libraries:

1. [Retrofit 2](https://github.com/square/retrofit) is the #1 library for network calls at the moment.
2. [RxJava 2](https://github.com/ReactiveX/RxJava) brings the possibility to work with streams of data and helps to connect different Clean Architecture layers with each other.
3. [Dagger 2](https://github.com/google/dagger) helps to achieve Dependency Inversion principle through Dependency Injection mechanisms.
3. [Moxy](https://github.com/Arello-Mobile/Moxy) helps to handle Android Activities and Fragments lifecycle so that you don't have to think about them in your views and presenters. 
4. [RxPermissions](https://github.com/tbruyelle/RxPermissions) helps to work with permissions directly from the business logic using usual Context and not a concrete Activity.
5. [Timber](https://github.com/JakeWharton/timber) helps to create a different logic of logging depending on build types. In the example, we turn on the logging only in `debug`.
6. [ObjectBox](https://github.com/greenrobot/ObjectBox) (beta) helps to store objects in a (more or less :) convenient way.
7. [MapStruct](http://mapstruct.org/) reduces the boilerplate needed to map one class to other.
8. [LeakCanary](https://github.com/square/leakcanary) watches for memory leaks.
9. (Branch `conductor`) [Conductor](https://github.com/bluelinelabs/Conductor) provides a more convenient way to create Android View-based applications.
10. (Branch `conductor`) [ButterKnife]() tries to reduce the boilerplate needed to inflate views.

## Testing

There are some example tests of Interactors and Presenters that show how we can create local tests without instrumentation. These tests cover the most complex logic in the app.

## Notes

In the example, we have two Dagger components regardless the fact that actually, their lifecycle in the application is the same. This was made for a demo purpose.

## Next steps

There are next features that may be made to do these example even more complete as a Clean Architecture example.

1. Make logic of GPS permissions more complex and suggest user open Settings screen if there are problems with them.
2. Add possibility to manually change the default location.
3. Add Android Service or BroadcastListener that will check if there are new earthquakes alerts.
4. In addition to 3, push a system notification about it.
5. Show earthquakes on Google Map.
6. Add filtering to the "All earthquakes" screen.
