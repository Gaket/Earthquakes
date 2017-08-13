package ru.inno.earthquakes.presentation.common;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Artur Badretdinov (Gaket) 09.08.17
 */
public abstract class BasePresenter<T extends MvpView> extends MvpPresenter<T> {

  private CompositeDisposable compositeDisposable;

  protected BasePresenter() {
    compositeDisposable = new CompositeDisposable();
  }

  protected void unsubscribeOnDestroy(Disposable disposable) {
    compositeDisposable.add(disposable);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    compositeDisposable.clear();
  }
}
