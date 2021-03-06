package com.ohdroid.zbmaster.application.view.recycleview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ohdroid on 2016/4/9.
 * <p/>
 * 能够添加hear和foot的recycleViewAdapter
 */
public class RecycleViewHeaderFooterAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public static final int VIEW_TYPE_HEAD = Integer.MIN_VALUE;
    public static final int VIEW_TYPE_FOOT = Integer.MIN_VALUE + 1000;


    ArrayList<View> mHeaderViewHolders;
    ArrayList<View> mFooterViewHolders;

    private ArrayList<View> EMPTY_HEAD_LIST =
            new ArrayList<>();
    private ArrayList<View> EMPTY_FOOT_LIST =
            new ArrayList<>();

    private RecyclerView.Adapter<VH> mAdapter;

    private int mCurrentPosition = 0;


    public RecycleViewHeaderFooterAdapter(RecyclerView.Adapter<VH> adapter) {
        this(null, null, adapter);
    }

    public RecycleViewHeaderFooterAdapter(ArrayList<View> mHeaderViewHolders,
                                          ArrayList<View> mFooterViewHolders,
                                          RecyclerView.Adapter<VH> adapter) {
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mDataObserver);

        if (mHeaderViewHolders == null) {
            this.mHeaderViewHolders = EMPTY_HEAD_LIST;
        } else {
            this.mHeaderViewHolders = mHeaderViewHolders;
        }

        if (mFooterViewHolders == null) {
            this.mFooterViewHolders = EMPTY_FOOT_LIST;
        } else {
            this.mFooterViewHolders = mFooterViewHolders;
        }
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        if (null == adapter) {
            return;
        }

        if (mAdapter != null) {
            notifyItemRangeRemoved(getHeadersCount(), mAdapter.getItemCount());
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
        }

        this.mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mDataObserver);
        notifyItemRangeInserted(getHeadersCount(), mAdapter.getItemCount());
    }

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int headerViewsCountCount = getHeadersCount();
            notifyItemRangeChanged(fromPosition + headerViewsCountCount, toPosition + headerViewsCountCount + itemCount);
        }
    };

    /**
     * 总是返回footerview的最后添加的view
     *
     * @return
     */
    public List<View> getFooterViews() {
        if (mFooterViewHolders.size() == 0) {
            return null;
        }
        return mFooterViewHolders;
    }

    public List<View> getHeaderViews() {
        if (mHeaderViewHolders.size() == 0) {
            return null;
        }
        return mHeaderViewHolders;
    }

    public int getHeadersCount() {
        return mHeaderViewHolders.size();
    }

    public int getFootersCount() {
        return mFooterViewHolders.size();
    }

    public void addHeaderView(View view) {
        mHeaderViewHolders.add(view);
        this.notifyDataSetChanged();
    }

    public void addFootView(View view) {
        System.out.println("add foot view inside radapter");
        mFooterViewHolders.add(view);
        this.notifyDataSetChanged();
    }

    public void removeFootView(View view) {
        mFooterViewHolders.remove(view);
        this.notifyDataSetChanged();
    }

    public void removeAllFootView() {
        mFooterViewHolders.clear();
        this.notifyDataSetChanged();
    }

    public void removeAllHeadView() {
        mHeaderViewHolders.clear();
        this.notifyDataSetChanged();
    }

    public void removeHeadView(View view) {
        mHeaderViewHolders.remove(view);
        this.notifyDataSetChanged();
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        System.out.println("~~~~~~~~create view holder~~~~~~~" + viewType);

        if (viewType < VIEW_TYPE_FOOT) {//header
            return (VH) new RecyclerView.ViewHolder(mHeaderViewHolders.get(viewType - VIEW_TYPE_HEAD)) {
            };
        }

        if (viewType < VIEW_TYPE_FOOT + 10000) {//footer
            int p = viewType - VIEW_TYPE_FOOT - getHeadersCount() - getInsideAdapterCount();
            return (VH) new RecyclerView.ViewHolder(mFooterViewHolders.get(p)) {
            };
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    private int getInsideAdapterCount() {
        if (mAdapter == null) {
            return 0;
        }
        return mAdapter.getItemCount();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {


        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {

        if (mAdapter != null) {
            return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
        } else {
            return getHeadersCount() + getFootersCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
//        System.out.println("get item view type");
        mCurrentPosition = position;
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return VIEW_TYPE_HEAD + position;//头部类型
        }
        //中间类型
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }

        return VIEW_TYPE_FOOT + position;//尾部类型
    }


    public RecyclerView.Adapter getWrapedAdapter() {
        return mAdapter;
    }


    //=========================内置部分状态的footview=========
    //内置无网提示(含重置按钮)、空数据提示、无更多数据
//    public static final int STATE_NO_NET = 10000;
//    public static final int STATE_NO_DATA = 10001;
//    public static final int STATE_NO_MORE_DATA = 10002;
//    public static final int STATE_LOADING_MORE_DATA = 10003;
//    private int mCurrentState = -1;//默认是无
//
//    public void setDataState(int state, Context context) {
//        System.out.println("set state:" + state);
//        switch (state) {
//            case STATE_NO_DATA:
//                addNoDataFootView("暂无数据", context);
//                break;
//            case STATE_NO_MORE_DATA:
//                addNoMoreDataFootView("无更多数据" + System.currentTimeMillis(), context);
//                break;
//            case STATE_LOADING_MORE_DATA:
//                addLoadingMoreDataFootView("加载更多数据中", context);
//                break;
//            case STATE_NO_NET:
//                break;
//        }
//        mCurrentState = state;
//    }
//
//    public void addNoDataFootView(String str, Context context) {
//        TextView textView = new TextView(context);
//        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        textView.setTextColor(Color.GRAY);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F);
//        textView.setGravity(Gravity.CENTER);
//        textView.setText(str);
//        if (mCurrentPosition != STATE_NO_DATA) {
//            addFootView(textView);
//        }
//    }
//
//    public void addLoadingMoreDataFootView(String str, Context context) {
//        TextView textView = new TextView(context);
//        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        textView.setText(str);
//        textView.setTextColor(Color.GRAY);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F);
//        if (mCurrentState != STATE_LOADING_MORE_DATA) {
//            addFootView(textView);
//        } else {
////            ((TextView) getFooterView()).setText(str);
//        }
//
//    }
//
//    public void addNoMoreDataFootView(String str, Context context) {
//        System.out.println("-------------show foot view  木有更多数据" + str);
//        TextView textView = new TextView(context);
//        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        textView.setText(str);
//        textView.setTextColor(Color.GRAY);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F);
////        if (mCurrentState != STATE_NO_MORE_DATA) {
//        addFootView(textView);
////        } else {
////            ((TextView) getFooterView()).setText(str);
////        }
//    }
//
//    public void addNoNetFootView() {
//
//    }
}
