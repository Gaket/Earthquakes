package ru.inno.earthquakes.presentation.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Divider item decoration that is able to:
 *  1) use different margins for different view types (look at {@link MarginProvider})
 *  2) hide dividers after given types of views (look at {@link MarginProvider})
 * Also, it can add margins to the divider
 *
 * @author Artur Badretdinov (Gaket)
 *         20.01.17.
 */
public class SmartDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Context mContext;

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private boolean mShowLastDivider;

    private Drawable divider;
    private int leftMargin;
    private int rightMargin;

    private final MarginProvider marginProvider;
    private final VisibilityProvider visibilityProvider;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int orientation;

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *
     * @param context            Current context, it will be used to access resources.
     * @param orientation        Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     * @param marginProvider
     * @param visibilityProvider
     */
    SmartDividerItemDecoration(Context context, int orientation, MarginProvider marginProvider, VisibilityProvider visibilityProvider) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        divider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
        mContext = context;
        this.marginProvider = marginProvider;
        this.visibilityProvider = visibilityProvider;
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        this.orientation = orientation;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        divider = drawable;
    }

    /**
     * Set the left margin for all dividers
     *
     * @param margin in dp
     */
    public void setLeftMargin(int margin) {
        leftMargin = Utils.dpToPx(mContext, margin);
    }

    /**
     * Sets if we should draw divider for the last element in list
     *
     * @param show
     */
    public void setShowLastDivider(boolean show) {
        this.mShowLastDivider = show;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            int childPosition = parent.getChildAdapterPosition(child);

            if (childPosition != NO_POSITION && visibilityProvider.getVisibility(childPosition, parent)) {
                int top, bottom, left, right;
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                if (orientation == HORIZONTAL) {
                    top = parent.getPaddingTop();
                    bottom = parent.getHeight() - parent.getPaddingBottom();

                    left = leftMargin + child.getRight() + params.rightMargin;
                    right = rightMargin + left + divider.getIntrinsicWidth();
                } else {
                    left = Utils.dpToPx(mContext, marginProvider.getMargin(childPosition, parent)) + parent.getPaddingLeft();
                    right = parent.getWidth() - parent.getPaddingRight();

                    top = child.getBottom() + params.bottomMargin;
                    bottom = top + divider.getIntrinsicHeight();
                }

                divider.setBounds(left, top, right, bottom);
                divider.draw(canvas);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (orientation == VERTICAL) {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
        }
    }

    public interface MarginProvider {

        /**
         * Returns left margin for a divider in dp
         *
         * @param position of item to decorate
         * @param parent   RecyclerView
         * @return left margin in dp
         */
        int getMargin(int position, RecyclerView parent);
    }

    public interface VisibilityProvider {
        /**
         * Returns if a divider should be shown
         *
         * @param position of item to decorate
         * @param parent   RecyclerView
         * @return true if it should be visible, false otherwise
         */
        boolean getVisibility(int position, RecyclerView parent);
    }

    static class DefaultMarginProvider implements MarginProvider {
        @Override
        public int getMargin(int position, RecyclerView parent) {
            return 0;
        }
    }

    static class DefaultVisibilityProvider implements VisibilityProvider {
        @Override
        public boolean getVisibility(int position, RecyclerView parent) {
            return true;
        }
    }

    public static class Builder {
        private final Context context;
        private MarginProvider marginProvider;
        private VisibilityProvider visibilityProvider;
        private int orientationMode;

        /**
         * Start building a new {@link SmartDividerItemDecoration} instance.
         */
        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context;
        }

        public Builder setMarginProvider(MarginProvider marginProvider) {
            this.marginProvider = marginProvider;
            return this;
        }

        public Builder setVisibilityProvider(VisibilityProvider visibilityProvider) {
            this.visibilityProvider = visibilityProvider;
            return this;
        }

        public Builder setOrientation(@OrientationMode int orientationMode) {
            this.orientationMode = orientationMode;
            return this;
        }

        public SmartDividerItemDecoration build() {
            if (marginProvider == null) {
                marginProvider = new DefaultMarginProvider();
            }
            if (visibilityProvider == null) {
                visibilityProvider = new DefaultVisibilityProvider();
            }
            return new SmartDividerItemDecoration(context, orientationMode, marginProvider, visibilityProvider);
        }


    }
}

