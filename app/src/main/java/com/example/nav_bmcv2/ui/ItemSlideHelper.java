package com.example.nav_bmcv2.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ItemSlideHelper implements RecyclerView.OnItemTouchListener, GestureDetector.OnGestureListener{

    private final int DEFAULT_DURATION = 200;
    private View mTargetView;

    private int mActivePointerId;
    private int mTouchSlop;
    private int mMaxVelocity;
    private int mMinVelocity;
    private int mLastX;
    private int mLastY;

    private boolean mIsDragging;
    private Animator mExpandAndCollapseAnim;
    private GestureDetectorCompat mGestureDetector;

    private Callback mCallback;

    public ItemSlideHelper(Context context, Callback callback) {
        this.mCallback = callback;

        //手勢用於處理fling
        mGestureDetector = new GestureDetectorCompat(context, this);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaxVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinVelocity = configuration.getScaledMinimumFlingVelocity();
    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        int action = MotionEventCompat.getActionMasked(e);
        int x = (int) e.getX();
        int y = (int) e.getY();


        //如果RecyclerView滾動狀態不是空閒targetView不是空
        if (rv.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
            if (mTargetView != null) {
                //隱藏已經開啟
                smoothHorizontalExpandOrCollapse(DEFAULT_DURATION / 2);
                mTargetView = null;
            }

            return false;
        }

        //如果正在執行動畫 ,直接攔截什麼都不做
        if (mExpandAndCollapseAnim != null && mExpandAndCollapseAnim.isRunning()) {
            return true;
        }

        boolean needIntercept = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mActivePointerId = MotionEventCompat.getPointerId(e, 0);
                mLastX = (int) e.getX();
                mLastY = (int) e.getY();

                /*
                 * 如果之前有一個已經開啟的專案,當此次點選事件沒有發生在右側的選單中則返回TRUE,
                 * 如果點選的是右側選單那麼返回FALSE這樣做的原因是因為選單需要響應Onclick
                 * */
                if (mTargetView != null) {
                    return !inView(x, y);
                }

                //查詢需要顯示選單的view;
                mTargetView = mCallback.findTargetView(x, y);

                break;
            case MotionEvent.ACTION_MOVE:

                int deltaX = (x - mLastX);
                int deltaY = (y - mLastY);

                if (Math.abs(deltaY) > Math.abs(deltaX))
                    return false;

                //如果移動距離達到要求,則攔截
                needIntercept = mIsDragging = mTargetView != null && Math.abs(deltaX) >= mTouchSlop;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /*
                 * 走這是因為沒有發生過攔截事件
                 * */
                if (isExpanded()) {

                    if (inView(x, y)) {
                        // 如果走這那行這個ACTION_UP的事件會發生在右側的選單中
                    } else {
                        //攔截事件,防止targetView執行onClick事件
                        needIntercept = true;
                    }

                    //摺疊選單
                    smoothHorizontalExpandOrCollapse(DEFAULT_DURATION / 2);
                }

                mTargetView = null;
                break;
        }

        return needIntercept;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        if (mExpandAndCollapseAnim != null && mExpandAndCollapseAnim.isRunning() || mTargetView == null)
            return;

        //如果要響應fling事件設定將mIsDragging設為false
        if (mGestureDetector.onTouchEvent(e)) {
            mIsDragging = false;
            return;
        }

        int x = (int) e.getX();
        int y = (int) e.getY();
        int action = MotionEventCompat.getActionMasked(e);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //RecyclerView 不會轉發這個Down事件

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastX - e.getX());
                if (mIsDragging) {
                    horizontalDrag(deltaX);
                }
                mLastX = x;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (mIsDragging) {
                    if (!smoothHorizontalExpandOrCollapse(0) && isCollapsed())
                        mTargetView = null;

                    mIsDragging = false;
                }

                break;
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > mMinVelocity && Math.abs(velocityX) < mMaxVelocity) {
            if (!smoothHorizontalExpandOrCollapse(velocityX)) {
                if (isCollapsed())
                    mTargetView = null;
                return true;
            }
        }
        return false;
    }
    /**
     * 根據當前scrollX的位置判斷是展開還是摺疊
     *
     * @param velocityX 如果不等於0那麼這是一次fling事件,否則是一次ACTION_UP或者ACTION_CANCEL
     */
    private boolean smoothHorizontalExpandOrCollapse(float velocityX) {

        int scrollX = mTargetView.getScrollX();
        int scrollRange = getHorizontalRange();

        if (mExpandAndCollapseAnim != null)
            return false;

        int to = 0;
        int duration = DEFAULT_DURATION;

        if (velocityX == 0) {
            //如果已經展一半,平滑展開
            if (scrollX > scrollRange / 2) {
                to = scrollRange;
            }
        } else {

            if (velocityX > 0)
                to = 0;
            else
                to = scrollRange;

            duration = (int) ((1.f - Math.abs(velocityX) / mMaxVelocity) * DEFAULT_DURATION);
        }

        if (to == scrollX)
            return false;

        mExpandAndCollapseAnim = ObjectAnimator.ofInt(mTargetView, "scrollX", to);
        mExpandAndCollapseAnim.setDuration(duration);
        mExpandAndCollapseAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mExpandAndCollapseAnim = null;
                if (isCollapsed())
                    mTargetView = null;

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //onAnimationEnd(animation);
                mExpandAndCollapseAnim = null;

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mExpandAndCollapseAnim.start();

        return true;
    }
    public int getHorizontalRange() {
        RecyclerView.ViewHolder viewHolder = mCallback.getChildViewHolder(mTargetView);
        return mCallback.getHorizontalRange(viewHolder);
    }
    private boolean isCollapsed() {

        return mTargetView != null && mTargetView.getScrollX() == 0;
    }
    private boolean isExpanded() {
        return mTargetView != null && mTargetView.getScrollX() == getHorizontalRange();
    }
    /*
     * 根據targetView的scrollX計算出targetView的偏移,這樣能夠知道這個point
     * 是在右側的選單中
     * */
    private boolean inView(int x, int y) {

        if (mTargetView == null)
            return false;

        int scrollX = mTargetView.getScrollX();
        int left = mTargetView.getWidth() - scrollX;
        int top = mTargetView.getTop();
        int right = left + getHorizontalRange();
        int bottom = mTargetView.getBottom();
        Rect rect = new Rect(left, top, right, bottom);
        return rect.contains(x, y);
    }
    /**
     * 根據touch事件來滾動View的scrollX
     *
     * @param delta
     */
    private void horizontalDrag(int delta) {
        int scrollX = mTargetView.getScrollX();
        int scrollY = mTargetView.getScrollY();
        if ((scrollX + delta) <= 0) {
            mTargetView.scrollTo(0, scrollY);
            return;
        }

        int horRange = getHorizontalRange();
        scrollX += delta;
        if (Math.abs(scrollX) < horRange) {
            mTargetView.scrollTo(scrollX, scrollY);
        } else {
            mTargetView.scrollTo(horRange, scrollY);
        }

    }

    public interface Callback {

        int getHorizontalRange(RecyclerView.ViewHolder holder);

        RecyclerView.ViewHolder getChildViewHolder(View childView);

        View findTargetView(float x, float y);

    }


}
