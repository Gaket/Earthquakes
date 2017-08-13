package ru.inno.earthquakes.presentation.common;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Extension of {@link RecyclerView} that automatically shows a view,
 * given through {@link EmptyRecyclerView#setEmptyViewLayout(int)}
 *
 * @author Artur Badretdinov (Gaket) 15.01.2017.
 */
public class EmptyRecyclerView extends FrameLayout {

  @Nullable
  private View emptyView;
  private RecyclerView recyclerView;

  private Context context;

  final private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
    @Override
    public void onChanged() {
      checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
      checkIfEmpty();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
      checkIfEmpty();
    }
  };

  public EmptyRecyclerView(Context context) {
    super(context);
    initializeViews(context);
  }

  public EmptyRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeViews(context);
  }

  public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initializeViews(context);
  }

  /**
   * Add view layout that will be shown when there are no elements in {@link EmptyRecyclerView}
   */
  public void setEmptyViewLayout(@LayoutRes int emptyLayout) {
    LayoutInflater inflater = LayoutInflater.from(context);
    emptyView = inflater.inflate(emptyLayout, this, false);
    this.addView(emptyView);
    checkIfEmpty();
  }

  public void setAdapter(RecyclerView.Adapter adapter) {
    final RecyclerView.Adapter oldAdapter = recyclerView.getAdapter();
    if (oldAdapter != null) {
      oldAdapter.unregisterAdapterDataObserver(mDataObserver);
    }
    recyclerView.setAdapter(adapter);
    if (adapter != null) {
      adapter.registerAdapterDataObserver(mDataObserver);
    }
    checkIfEmpty();
  }

  public void setLayoutManager(LinearLayoutManager lm) {
    recyclerView.setLayoutManager(lm);
  }

  public void addOnScrollListener(RecyclerView.OnScrollListener scrollListener) {
    recyclerView.addOnScrollListener(scrollListener);
  }

  public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
    recyclerView.setItemAnimator(itemAnimator);
  }

  public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
    recyclerView.addItemDecoration(itemDecoration);
  }

  public RecyclerView.LayoutManager getLayoutManager() {
    return recyclerView.getLayoutManager();
  }

  public void scrollToPosition(int i) {
    recyclerView.scrollToPosition(i);
  }

  private void initializeViews(Context context) {
    recyclerView = new RecyclerView(context);
    recyclerView.setPadding(0, 0, 0, Utils.dpToPx(getContext(), 16));
    recyclerView.setClipToPadding(false);
    this.addView(recyclerView);
    this.context = context;
  }

  private void checkIfEmpty() {
    if (emptyView != null && recyclerView.getAdapter() != null) {
      final boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
      emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
      recyclerView.setVisibility(emptyViewVisible ? GONE : VISIBLE);
    }
  }
}
