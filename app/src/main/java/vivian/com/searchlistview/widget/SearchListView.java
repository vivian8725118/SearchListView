package vivian.com.searchlistview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import vivian.com.searchlistview.R;

/**
 *
 *                        ,%%%%%%%%,
 *                      ,%%/\%%%%/\%%
 *                     ,%%%\c "" J/%%%
 *            %.       %%%%/ o  o \%%%
 *            `%%.     %%%%    _  |%%%
 *             `%%     `%%%%(__Y__)%%'
 *             //       ;%%%%`\-/%%%'
 *            ((       /  `%%%%%%%'
 *             \\    .'          |
 *              \\  /       \  | |
 *               \\/         ) | |
 *                \         /_ | |__
 *                (___________))))))) 攻城湿
 *
 *
 *          _       _
 *   __   _(_)_   _(_) __ _ _ __
 *   \ \ / / \ \ / / |/ _` | '_ \
 *    \ V /| |\ V /| | (_| | | | |
 *     \_/ |_| \_/ |_|\__,_|_| |_|
 *
 *
 * @author vivian:the girl who is in love with 7heaven deeply
 * create at 15/11/23 17:10
 */

public class SearchListView extends ListView implements OnScrollListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "listview";

    private final static int SCROLL_DURATION = 200; // scroll back duration

    private final static float OFFSET_RADIO = 2.5f; // support iOS like pull  2.5f

    /**
     * 状态
     **/
    private int state = TOUCH_STATE_DONE;

    /**
     * 完成
     **/
    private final static int TOUCH_STATE_DONE = 0;
    /**
     * 松开更新
     **/
    private final static int TOUCH_STATE_RELEASE_TO_REFRESH = 1;
    /**
     * 下拉更新
     **/
    private final static int TOUCH_STATE_PULL_TO_REFRESH = 2;
    /**
     * 正在刷新
     **/
    private final static int TOUCH_STATE_REFRESHING = 3;
    /**
     * 已加载全部
     */
    private final static int LOAD_ALL=4;

    /**
     * 标识查看更多状态
     **/
    private boolean isMore = false;

    private Scroller scroller;

    /**
     * 头部刷新的布局
     **/
    private View viewHeader;
    /**
     * 尾部加载更多的布局
     **/
    private View viewFooter;

    private int headerContentHeight; // 记录头部的高度

    /**
     * 头部显示下拉刷新等的控件
     **/
    private TextView tipsTextview;
    private ImageView imgHeader;
    private ProgressBar progressBarHeader;

    /**
     * 内容显示
     **/
    private RelativeLayout layoutContent;

    /**
     * 头部高度
     **/
    private int headHeight;

    /**
     * 第一次记录的Y轴坐标
     **/
    private float firstY;

    /**
     * 最后一次记录的Y轴坐标
     **/
    private float lastY;

    /**
     * 总的item个数
     */
    private int totalItemCount;

    /**
     * 是否要使用下拉刷新功能
     **/
    public boolean enablePullRefresh = true;

    /**
     * 是否自动获取更多
     */
    private boolean isAutoFetchMore = false;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemSelectedListener onItemSelectedListener;

    private OnRefreshListener onRefreshListener;
    private OnLastItemVisibleListener onLastItemVisibleListener;

    public SearchListView(Context context) {
        super(context);
        init();
    }

    public SearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private TextView txtFooter;
    private ProgressBar progressBarFooter;

    private void init() {
        scroller = new Scroller(getContext(), new DecelerateInterpolator());

        measureView(getHeaderView());
        headHeight = getHeaderView().getMeasuredHeight();
        getHeaderView().setPadding(0, -1 * headHeight, 0, 0);
        getHeaderView().invalidate();

//        this.setBackgroundResource(R.color.stand_default_bg_color);
        //添加头部视图
        super.addHeaderView(getHeaderView(), null, false);

        setOnScrollListener(this);

        super.setOnItemClickListener(this);
        super.setOnItemLongClickListener(this);
        super.setOnItemSelectedListener(this);
    }

    /**
     * 获取头部视图
     *
     * @return View
     */
    private View getHeaderView() {
        if (viewHeader == null || tipsTextview == null) {
            viewHeader = LayoutInflater.from(getContext()).inflate(R.layout.model_pull_listview_head, null);

            tipsTextview = (TextView) viewHeader.findViewById(R.id.tvHead);
            imgHeader = (ImageView) viewHeader.findViewById(R.id.ivHead);
            progressBarHeader = (ProgressBar) viewHeader.findViewById(R.id.pulldown_footer_loading);

            layoutContent = (RelativeLayout) viewHeader.findViewById(R.id.layoutContent);

            final ViewConfiguration configuration = ViewConfiguration.get(getContext());
            mTouchSlop = configuration.getScaledTouchSlop();
        }

        return viewHeader;
    }

    /**
     * 获取尾部视图
     *
     * @return View
     */
    private View getFooterView() {
        if (viewFooter == null) {
            viewFooter = LayoutInflater.from(getContext()).inflate(R.layout.model_pull_listview_footer, null);

            txtFooter = (TextView) viewFooter.findViewById(R.id.pulldown_footer_text);
            progressBarFooter = (ProgressBar) viewFooter.findViewById(R.id.pulldown_footer_loading);

            viewFooter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isMore) {
                        isMore = true;

                        txtFooter.setText(getContext().getResources().getString(R.string.loading));
                        progressBarFooter.setVisibility(View.VISIBLE);

                        if (onLastItemVisibleListener != null) {
                            onLastItemVisibleListener.onLastItemVisible();
                        }
                    }
                }
            });
        }

        return viewFooter;
    }

    /**
     * 添加头部的view 与下拉刷新不冲突
     *
     * @param header 视图
     */
    public void addHeaderView(View header) {
        if (null == header) {
            return;
        }

        layoutContent.addView(header);

        measureView(header);
        headerContentHeight = header.getMeasuredHeight();

        headHeight += headerContentHeight;

        getHeaderView().setPadding(0, -1 * headHeight, 0, 0);
    }

    /**
     * 移除头部自定义添加内容
     */
    public void removeHeader() {
        layoutContent.removeAllViews();

        measureView(getHeaderView()); //防止多次添加导致高度计算失误
        headHeight = getHeaderView().getMeasuredHeight();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onItemClickListener == null) {
            return;
        }

        onItemClickListener.onItemClick(parent, view, position > 0 ? position - 1 : 0, id);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (onItemSelectedListener == null) {
            return;
        }

        onItemSelectedListener.onItemSelected(parent, view, position > 0 ? position - 1 : 0, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return onItemLongClickListener!=null && onItemLongClickListener.onItemLongClick(parent, view, position > 0 ? position - 1 : 0, id);
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    public Object getItemAtPosition(int position) {
        return super.getItemAtPosition(position + 1);
    }

    public long getItemIdAtPosition(int position) {
        return super.getItemIdAtPosition(position + 1);
    }

    /**
     * 设置是否可用下拉刷新
     *
     * @param enable boolean
     */
    public void pullRefreshEnable(boolean enable) {
        enablePullRefresh = enable;
    }

    private final int TOUCH_STATE_REST = 0;
    private final int TOUCH_STATE_HORIZONTAL_SCROLLING = 1;
    private final int TOUCH_STATE_VERTICAL_SCROLLING = -1;

    private int mTouchState = TOUCH_STATE_REST;

    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;


    /**
     * 触摸事件的处理
     */
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        cancelLongPress();
        switch (action) {
            case MotionEvent.ACTION_DOWN: // 按下的时候
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                firstY = ev.getRawY();
                lastY = firstY;
                Log.v(TAG, "在down时候记录当前位置: X: " + ev.getX() + " Y: " + ev.getRawY());
                break;
            case MotionEvent.ACTION_MOVE: // 手指正在移动的时候
                if (!enablePullRefresh) {
//                    return super.onTouchEvent(ev);
                    break;
                }

                if (firstY == -1) {
                    firstY = ev.getRawY();
                    lastY = firstY;
                }

                final float deltaY = ev.getRawY() - lastY;
                lastY = ev.getRawY();

                if (getFirstVisiblePosition() == 0 && (Math.abs(getHeaderView().getPaddingTop()) < headHeight || deltaY > 0)) {
                    final int paddingTop = getHeaderView().getPaddingTop();

                    // change paddingTop with the movement of the finger
                    getHeaderView().setPadding(0, (int) (paddingTop + deltaY / OFFSET_RADIO), 0, 0);

                    if (getHeaderView().getPaddingTop() > 0) {
                        if (state != TOUCH_STATE_RELEASE_TO_REFRESH && state != TOUCH_STATE_REFRESHING) {
                            state = TOUCH_STATE_RELEASE_TO_REFRESH;
                            changeHeaderViewByState();
                        }
                    } else {
                        if (state != TOUCH_STATE_PULL_TO_REFRESH && state != TOUCH_STATE_REFRESHING) {
                            state = TOUCH_STATE_PULL_TO_REFRESH;
                            changeHeaderViewByState();
                        }
                    }
                    return true;
                } else if (getLastVisiblePosition() == totalItemCount - 1 && deltaY < 0) {
                    Log.i(TAG, "Footer" + String.valueOf(-deltaY / OFFSET_RADIO));
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP: // 手指抬起来的时候
                if (getFirstVisiblePosition() == 0 || getFirstVisiblePosition() == 1) {
                    if (enablePullRefresh) {
                        final int paddingTop = getHeaderView().getPaddingTop();

                        switch (state) {
                            case TOUCH_STATE_REFRESHING:
                                scroller.startScroll(0, paddingTop, 0, -paddingTop, SCROLL_DURATION);
                                break;
                            case TOUCH_STATE_PULL_TO_REFRESH:
                                if (paddingTop + headHeight > headHeight * 0.75) { //高过搜索框0.75的 那么显示搜索框
                                    scroller.startScroll(0, paddingTop, 0, -(headHeight - headerContentHeight + paddingTop), SCROLL_DURATION);
                                } else if (paddingTop + headHeight > 0 && paddingTop + headHeight < headHeight * 0.25) {
                                    if (firstY - lastY < 0) {//向下
                                        scroller.startScroll(0, paddingTop, 0, -(headHeight - headerContentHeight + paddingTop), SCROLL_DURATION);
                                    } else {//向上
                                        scroller.startScroll(0, paddingTop, 0, (headHeight - headerContentHeight + paddingTop), SCROLL_DURATION);
                                    }
                                } else if (firstY - lastY < 0) {//向下
                                    scroller.startScroll(0, paddingTop, 0, -(headHeight - headerContentHeight + paddingTop));
                                } else {//向上
                                    scroller.startScroll(0, paddingTop, 0, -(headHeight + paddingTop));
                                }
                                break;
                            case TOUCH_STATE_RELEASE_TO_REFRESH:
                                state = TOUCH_STATE_REFRESHING;  //将进度切换到正在刷新
                                changeHeaderViewByState();
                                scroller.startScroll(0, paddingTop, 0, -paddingTop, SCROLL_DURATION);

                                if (onRefreshListener != null) {
                                    onRefreshListener.onRefresh();
                                }
                                break;

                            default:
                                break;
                        }
                    }
                } else if (getLastVisiblePosition() == totalItemCount - 1) {
                    // 数量充满屏幕才触发
                    if (isAutoFetchMore && !isMore && onLastItemVisibleListener != null /*&& isFillScreenItem()*/ && viewFooter != null) {
                        txtFooter.setText(getContext().getResources().getString(R.string.loading));
                        progressBarFooter.setVisibility(View.VISIBLE);

                        isMore = true;
                        onLastItemVisibleListener.onLastItemVisible();
                        return true;
                    }
                } else {
                    //这里只是为了方便计算
                    //不在第一个和最后一个的时候 隐藏头部
                    //不需要处理动画效果
                    getHeaderView().setPadding(0, -1 * headHeight, 0, 0);
                }
                Log.i(TAG, "up:" + getLastVisiblePosition() + "   " + (totalItemCount - 1));
                firstY = -1;
                lastY = -1; // reset
                break;
        }

        return super.onTouchEvent(ev);
    }


    /**
     * 条目是否填满整个屏幕
     */
    private boolean isFillScreenItem() {
        final int firstVisiblePosition = getFirstVisiblePosition();
        final int lastVisiblePostion = getLastVisiblePosition() - getFooterViewsCount();
        final int visibleItemCount = lastVisiblePostion - firstVisiblePosition + 1;
        final int totalItemCount = getCount() - getFooterViewsCount();

        return (visibleItemCount<totalItemCount);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            getHeaderView().setPadding(0, scroller.getCurrY(), 0, 0);
            postInvalidate();
        }
    }

    /**
     * 自动刷新，显示头部
     * @param show boolean
     */
    public void showHeader(boolean show) {
        final int paddingTop = getHeaderView().getPaddingTop();
        if (show) {
            imgHeader.setVisibility(View.GONE);
            scroller.startScroll(0, paddingTop, 0, -paddingTop, SCROLL_DURATION);
        }
    }

    /**
     * 是否开启自动获取更多 自动获取更多，将会隐藏footer，并在到达底部的时候自动刷新
     */
    public void setAutoFetchMore(boolean enable) {
        isAutoFetchMore = enable;

        if (viewFooter == null) {
            return;
        }

        txtFooter.setText(getContext().getResources().getString(R.string.pull_to_refresh_from_bottom_pull_label));
        progressBarFooter.setVisibility(View.GONE);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
        this.totalItemCount = totalItemCount;
        if (getLastVisiblePosition() == totalItemCount - 1) {
            // 数量充满屏幕才触发
            if (isAutoFetchMore && !isMore && onLastItemVisibleListener != null /*&& isFillScreenItem()*/ && viewFooter != null) {
                txtFooter.setText(getContext().getResources().getString(R.string.loading));
                progressBarFooter.setVisibility(View.VISIBLE);

                isMore = true;
                onLastItemVisibleListener.onLastItemVisible();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * 计算view的宽高
     *
     * @param child View
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault());

    public void onRefreshComplete() {
        state = TOUCH_STATE_DONE;
        changeHeaderViewByState();

        final int paddingTop = getHeaderView().getPaddingTop();
        scroller.startScroll(0, paddingTop, 0, -(headHeight + paddingTop), SCROLL_DURATION);
        invalidate();

        isMore = false;

        if (viewFooter == null) {
            return;
        }

        txtFooter.setText(getContext().getResources().getString(R.string.pull_to_refresh_from_bottom_pull_label));
        progressBarFooter.setVisibility(View.GONE);

//        getFooterView().setVisibility(View.GONE);
    }

    public void setLoadAll(){
        state=LOAD_ALL;
        changeHeaderViewByState();
        pullRefreshEnable(false);
    }
    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
            case TOUCH_STATE_RELEASE_TO_REFRESH:
                Log.v(TAG, "当前状态，松开刷新");

                tipsTextview.setVisibility(View.VISIBLE);
                tipsTextview.setText(getContext().getResources().getString(R.string.pull_to_refresh_from_bottom_release_label));
                imgHeader.setVisibility(View.VISIBLE);
                progressBarHeader.setVisibility(View.GONE);

                break;
            case TOUCH_STATE_PULL_TO_REFRESH:
                Log.v(TAG, "当前状态，下拉刷新");

                tipsTextview.setVisibility(View.VISIBLE);
                tipsTextview.setText(getContext().getResources().getString(R.string.pull_to_refresh_pull_label));

                imgHeader.setVisibility(View.VISIBLE);
                progressBarHeader.setVisibility(View.GONE);

                break;
            case TOUCH_STATE_REFRESHING:
                Log.v(TAG, "当前状态,正在刷新...");

                tipsTextview.setText(getContext().getResources().getString(R.string.pull_to_refresh_refreshing_label));
                imgHeader.setVisibility(View.GONE);
                progressBarHeader.setVisibility(View.VISIBLE);
                break;
            case TOUCH_STATE_DONE:
                Log.v(TAG, "当前状态，done");

                tipsTextview.setText(getContext().getResources().getString(R.string.pull_to_refresh_pull_label));
                imgHeader.setVisibility(View.VISIBLE);
                progressBarHeader.setVisibility(View.GONE);
                break;
            case LOAD_ALL:
                Log.v(TAG,"当前状态,load all");

                tipsTextview.setText(getContext().getResources().getString(R.string.load_all));
                imgHeader.setVisibility(View.VISIBLE);
                progressBarFooter.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 下拉刷新监听事件
     *
     * @param listener OnRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }

    /**
     * 获取更多监听事件
     *
     * @param listener OnLastItemVisibleListener
     */
    public void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
        this.onLastItemVisibleListener = listener;
        if (listener == null) {
            removeFooterView(getFooterView());
            viewFooter = null;
            txtFooter = null;
            progressBarFooter = null;
        } else {
            if (viewFooter == null) {
                addFooterView(getFooterView());
            }
        }

        if (viewFooter == null) {
            return;
        }

        txtFooter.setText(getContext().getResources().getString(R.string.pull_to_refresh_from_bottom_pull_label));
        progressBarFooter.setVisibility(View.GONE);
    }

    /**
     * 刷新事件接口
     */
    public interface OnRefreshListener {
        /**
         * 刷新事件接口 这里要注意的是获取更多完 要关闭 刷新的进度条onRefreshComplete()
         **/
        void onRefresh();
    }

    /**
     * 获取更多事件接口
     */
    public interface OnLastItemVisibleListener {
        /**
         * 刷新事件接口 这里要注意的是获取更多完 要关闭 更多的进度条 onRefreshComplete()
         **/
        void onLastItemVisible();
    }
}
