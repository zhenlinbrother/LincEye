package com.linc.linceye.mvvm.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.base.utils.UIUtils;
import com.linc.linceye.MyApplication;

import java.util.Objects;

public class GridListItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private float space;
    private Context context;

    public GridListItemDecoration(Context context, int spanCount, float space) {
        this.spanCount = spanCount;
        this.space = space;
        this.context = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int count = Objects.requireNonNull(parent.getAdapter()).getItemCount();
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = layoutParams.getSpanIndex();
        int lastRowFirstItemPosition = count - this.spanCount;
        int space = UIUtils.dip2px(context, this.space);

        if (position < spanCount){
            outRect.bottom = space;
        } else if (position < lastRowFirstItemPosition){
            outRect.top = space;
            outRect.bottom = space;
        } else {
            outRect.top = space;
        }

        if (spanIndex == spanCount - 1){
            outRect.left = space;
        } else if (spanIndex == 0){
            outRect.right = space;
        } else  {
            outRect.left = space;
            outRect.right = space;
        }
    }
}
