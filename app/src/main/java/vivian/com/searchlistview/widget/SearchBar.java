package vivian.com.searchlistview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import vivian.com.searchlistview.R;


public class SearchBar extends LinearLayout {

    private static final int DURATION = 300;

    private int state = STATE_VIEW;

    private static final int STATE_VIEW = 1;
    private static final int STATE_EDIT = 2;

    private int distance;  //计算动画位移
    private int screenWidth; //屏幕的宽度

    private Scroller mScroller;

    private TextView etSearch;
    private LinearLayout layout;

    public SearchBar(Context context) {
        super(context);
        init();
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /***
     * 界面初始化
     **/
    private void init() {
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());

        //计算屏幕的宽度
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;

        View view=inflate(getContext(), R.layout.bt_header_recommend_search, this);
        layout=(LinearLayout)view.findViewById(R.id.recommend_search_layout);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

//        measureView(layout);
        distance = layout.getMeasuredWidth();

        try {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
            params.setMargins(20, 20, 20, 20);
            layout.setLayoutParams(params);
        }catch (Exception e){
            e.printStackTrace();
        }

        etSearch = (TextView) findViewById(R.id.drawer_search);
        etSearch.setOnTouchListener(editTextOnTouchListener);
    }

    /**
     * 计算view的宽高
     *
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, lp.width);
        int lpHeight = lp.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 取得搜索文本框
     */
    public TextView getSearchText() {
        return etSearch;
    }


    /**
     * 取消键点击事件处理
     **/
    private OnClickListener cancelClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (state == STATE_EDIT) {
                state = STATE_VIEW;
                changeState();
            }
        }
    };

    /**
     * EditText Touch事件处理
     **/
    private OnTouchListener editTextOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (state == STATE_VIEW) {
                state = STATE_EDIT;
                changeState();
            }
            return false;
        }
    };

    /***
     * 设置搜索框是否可以编辑
     *
     * @param isEditable
     */
    private void setTextEditable(boolean isEditable) {
        if (isEditable) {
            etSearch.setFocusableInTouchMode(true);
            etSearch.setFocusable(true);
            etSearch.requestFocus();
        } else {
            etSearch.clearFocus();
            etSearch.setFocusable(false);
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, DURATION);
    }

    /**
     * 设置搜索条的状态<br>
     * 浏览状态 STATE_VIEW 只显示搜索条 同时失去焦点<br>
     * 编辑状态 STATE_EDIT 显示搜索条和取消按钮 获取焦点
     *
     */
    private void changeState() {
        switch (state) {
            case STATE_VIEW:
                etSearch.setText("");
                setTextEditable(false);

                mScroller.startScroll(layout.getLeft(), 0, distance, 0, DURATION);
                invalidate();
                break;
            case STATE_EDIT:
                setTextEditable(true);

                mScroller.startScroll(layout.getLeft(), 0, -distance, 0, DURATION);
                invalidate();
                break;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.i("", String.valueOf(mScroller.getCurrX()));

            LayoutParams params = (LayoutParams) layout.getLayoutParams();
            params.setMargins(0, 0, screenWidth - mScroller.getCurrX() - distance, 0);
            layout.setLayoutParams(params);

            postInvalidate();
        }
        super.computeScroll();
    }
}
