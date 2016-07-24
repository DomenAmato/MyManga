package com.example.domen.mymanga.Utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class AutoSpanRecyclerView extends RecyclerView {

    private int     m_gridMinSpans;
    private int     m_gridItemLayoutId;
    private LayoutRequester m_layoutRequester = new LayoutRequester();

    public AutoSpanRecyclerView(Context context) {
        super(context);
    }

    public AutoSpanRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSpanRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
    *   Setto un LayoutManager a griglia con uno span automatico
    */
    public void setGridLayoutManager(int orientation, int itemLayoutId, int minSpans ) {
        GridLayoutManager layoutManager = new GridLayoutManager( getContext(), 2, orientation, false );
        m_gridItemLayoutId = itemLayoutId;
        m_gridMinSpans = minSpans;

        setLayoutManager( layoutManager );
    }

    /*
    *   Effettuo i calcoli per il numero di colonne in una riga
    *
    *   numeroColonne = viewWidth/itemWidth
    */
    @Override
    protected void onLayout( boolean changed, int left, int top, int right, int bottom ) {
        super.onLayout( changed, left, top, right, bottom );

        if( changed ) {
            LayoutManager layoutManager = getLayoutManager();
            if( layoutManager instanceof GridLayoutManager ) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                LayoutInflater inflater = LayoutInflater.from( getContext() );
                View item = inflater.inflate( m_gridItemLayoutId, this, false );
                int measureSpec = View.MeasureSpec.makeMeasureSpec( 0, View.MeasureSpec.UNSPECIFIED );
                item.measure( measureSpec, measureSpec );
                int itemWidth = item.getMeasuredWidth();
                int recyclerViewWidth = getMeasuredWidth();
                int spanCount = Math.max( m_gridMinSpans, recyclerViewWidth / itemWidth );

                gridLayoutManager.setSpanCount( spanCount );

                // if you call requestLayout() right here, you'll get ArrayIndexOutOfBoundsException when scrolling
                post( m_layoutRequester );
            }
        }
    }

    private class LayoutRequester implements Runnable {
        @Override
        public void run() {
            requestLayout();
        }
    }
}