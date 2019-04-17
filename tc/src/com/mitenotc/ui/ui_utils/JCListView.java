package com.mitenotc.ui.ui_utils;

import java.util.List;

import com.mitenotc.ui.adapter.JCListAdapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Scroller;
import android.widget.ExpandableListView.OnGroupClickListener;

public class JCListView extends ExpandableListView implements OnScrollListener, OnGroupClickListener {
	private static final int MAX_ALPHA = 255;
	private JCHeaderAdapter mAdapter;
	// private JCListAdapter mJCAdapter;//适配器
	/**
	 * 用于在列表头显示的 View,mHeaderViewVisible 为 true 才可见
	 */
	private View mHeaderView;// header2 最顶部 头部
	private View headerClick;// header1 每列组头部

	/**
	 * 列表头是否可见
	 */
	public boolean mHeaderViewVisible;
	private int mHeaderViewWidth;
	private int mHeaderViewHeight;
	private boolean mPullRefreshing = false; // is refreashing.

	public JCListView(Context context) {
		super(context);
		registerListener();
	}

	public JCListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		registerListener();
	}

	public JCListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		registerListener();
	}

	private void registerListener() {
		setOnScrollListener(this);
		setOnGroupClickListener(this);
	}

	public void setHeaderClick(View headerClick) {
		this.headerClick = headerClick;
		this.headerClick.setBackgroundColor(Color.argb(0, 255, 255, 255));
		this.headerClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				headerViewClick();
			}
		});
	}

	/**
	 * Adapter 接口 . 列表必须实现此接口 .
	 */
	public interface JCHeaderAdapter {
		public static final int PINNED_HEADER_GONE = 0;
		public static final int PINNED_HEADER_VISIBLE = 1;
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		/**
		 * 获取 Header 的状态
		 * 
		 * @param groupPosition
		 * @param childPosition
		 * @return 
		 *         PINNED_HEADER_GONE,PINNED_HEADER_VISIBLE,PINNED_HEADER_PUSHED_UP
		 *         其中之一
		 */
		int getJCHeaderState(int groupPosition, int childPosition);

		/**
		 * 配置 jcHeader, 让 jcHeader 知道显示的内容
		 * 
		 * @param header
		 * @param groupPosition
		 * @param childPosition
		 * @param alpha
		 */
		void configureJCHeader(View header, int groupPosition, int childPosition, int alpha);

		/**
		 * 设置组按下的状态
		 * 
		 * @param groupPosition
		 * @param status
		 */
		void setGroupClickStatus(int groupPosition, int status);

		/**
		 * 获取组按下的状态
		 * 
		 * @param groupPosition
		 * @return
		 */
		int getGroupClickStatus(int groupPosition);

	}

	public void setHeaderView(View view) {
		mHeaderView = view;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		mOldState = -1;
		requestLayout();
	}

	/**
	 * 点击 HeaderView 触发的事件
	 */
	public void headerViewClick() {
		long packedPosition = getExpandableListPosition(getFirstVisiblePosition());
		int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
		if (packedPosition >= 0 && groupPosition >= 0) {  //判断 赛事点击框 如果没有子内容，就不让adapter填充，否则会崩溃
			if (mAdapter != null) {
				if (mAdapter.getGroupClickStatus(groupPosition) == 1) {
					this.expandGroup(groupPosition);
					mAdapter.setGroupClickStatus(groupPosition, 0);
				} else {
					this.collapseGroup(groupPosition);
					mAdapter.setGroupClickStatus(groupPosition, 1);
				}
				this.setSelectedGroup(groupPosition);
			}
		}
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (JCHeaderAdapter) adapter;
	}

	/**
	 * 
	 * 点击了 Group 触发的事件 , 要根据根据当前点击 Group 的状态来
	 */
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		groupClick(parent, groupPosition);
		return true;// 返回 true 才可以弹回第一行

	}

	/**
	 * group组列表栏点击事件
	 * 
	 * @param parent
	 * @param groupPosition
	 */
	private void groupClick(ExpandableListView parent, int groupPosition) {
		if (mAdapter.getGroupClickStatus(groupPosition) == 0) {
			mAdapter.setGroupClickStatus(groupPosition, 1);
			parent.collapseGroup(groupPosition);// 关闭

		} else if (mAdapter.getGroupClickStatus(groupPosition) == 1) {
			mAdapter.setGroupClickStatus(groupPosition, 0);
			parent.expandGroup(groupPosition);// 打开
			parent.setSelectedGroup(groupPosition);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	private int mOldState = -1;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		try {
			final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
			final int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
			final int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
			int state = mAdapter.getJCHeaderState(groupPos, childPos);
			if (mHeaderView != null && mAdapter != null && state != mOldState) {
				mOldState = state;
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}

			configureHeaderView(groupPos, childPos);
		} catch (Exception e) {

		}
	}

	public void configureHeaderView(int groupPosition, int childPosition) {
		if (mHeaderView == null || mAdapter == null || ((ExpandableListAdapter) mAdapter).getGroupCount() == 0) {
			return;
		}
		int state = mAdapter.getJCHeaderState(groupPosition, childPosition);
		switch (state) {
		case JCHeaderAdapter.PINNED_HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case JCHeaderAdapter.PINNED_HEADER_VISIBLE: {
			int s = headerClick.getTop();
			mAdapter.configureJCHeader(mHeaderView, groupPosition, childPosition, MAX_ALPHA);
			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}
			mHeaderViewVisible = true;
			break;
		}

		case JCHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();

			// intitemHeight = firstView.getHeight();
			int headerHeight = mHeaderView.getHeight();
			int y;
			int alpha;
			if (bottom < headerHeight) {
				y = (bottom - headerHeight);
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}

			mAdapter.configureJCHeader(mHeaderView, groupPosition, childPosition, alpha);

			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
			}

			mHeaderViewVisible = true;
			break;
		}
		}
		if (headerClick != null) {
			if (mHeaderViewVisible) {
				headerClick.setVisibility(View.VISIBLE);
			} else {
				headerClick.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 列表界面更新时调用该方法(如滚动时)
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible) {
			// 分组栏是直接绘制到界面中，而不是加入到ViewGroup中
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		try {
			final long flatPos = getExpandableListPosition(firstVisibleItem);
			int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
			int childPosition = ExpandableListView.getPackedPositionChild(flatPos);
			configureHeaderView(groupPosition, childPosition);
		} catch (Exception e) {
			System.out.println("jclistView onScroll e:" + e.getMessage());
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}
