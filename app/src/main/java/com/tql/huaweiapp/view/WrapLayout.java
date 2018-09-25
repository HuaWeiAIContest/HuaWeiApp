package com.tql.huaweiapp.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tql.huaweiapp.R;
import com.tql.huaweiapp.constant.Hobby;

import java.util.ArrayList;
import java.util.List;

public class WrapLayout extends ViewGroup {
    private static final String TAG = "WrapLayout";
    public static int[] selectedTag = new int[Hobby.HOBBIES.length];
    private MarkClickListener markClickListener;
    /**
     * 存储所有的View，按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();

    public WrapLayout(Context context) {
        super(context);
    }

    public WrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setData(List<String> data, Context context, int textSize) {
        String[] mydata = null;
        if (data != null) {
            int length = data.size();
            mydata = new String[length];
            for (int i = 0; i < length; i++) {
                mydata[i] = data.get(i);
            }
        }
        createChild(mydata, context, textSize, 8, 5, 5,
                8, 5, 5, 5, 5);
    }

    /**
     * 初始化被选中的标签
     */
    public void initTag(String selectedTags) {
        if (selectedTags.isEmpty()) return;
        String[] hobbies = Hobby.HOBBIES;
        for (int i = 0, hobbiesLength = hobbies.length; i < hobbiesLength; i++) {
            String tag = hobbies[i];
            if (selectedTags.contains(tag)) selectedTag[i] = 1;
        }
    }

    private void createChild(String[] data, final Context context, int textSize, int pl, int pt, int pr, int pb, int ml, int mt, int mr, int mb) {
        int size = data.length;
        for (int i = 0; i < size; i++) {
            String text = data[i];
            //通过判断style是TextView还是Button进行不同的操作，还可以继续添加不同的view
            final View btn = LayoutInflater.from(context).inflate(R.layout.wrap_layout_textview, null);
            ((TextView) btn).setGravity(Gravity.CENTER);
            ((TextView) btn).setText(text);
            ((TextView) btn).setTextSize(textSize);
            if (selectedTag[i] == 1) {
                btn.setBackground(getResources().getDrawable(R.drawable.selected_wrap_layout_textview_bg));
                ((TextView) btn).setTextColor(Color.WHITE);
            }
            btn.setClickable(true);
            btn.setPadding(dip2px(context, pl), dip2px(context, pt), dip2px(context, pr), dip2px(context, pb));
            MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
            params.setMargins(ml, mt, mr, mb);
            btn.setLayoutParams(params);
            final int finalI = i;
            //给每个view添加点击事件
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    markClickListener.clickMark(finalI);
                    if (selectedTag[finalI] == 1) {
                        btn.setBackground(getResources().getDrawable(R.drawable.unselected_wrap_layout_textview_bg));
                        ((TextView) btn).setTextColor(R.attr.colorAccent);
                        selectedTag[finalI] = 0;
                    } else {
                        btn.setBackground(getResources().getDrawable(R.drawable.selected_wrap_layout_textview_bg));
                        ((TextView) btn).setTextColor(Color.WHITE);
                        selectedTag[finalI] = 1;
                    }
                }
            });
            this.addView(btn);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setData(String[] data, Context context, int textSize,
                        int paddingLeft, int paddingTop, int paddingRight, int paddingBottom,
                        int marginLeft, int marginTop, int marginRight, int marginButton) {
        createChild(data, context, textSize,
                paddingLeft, paddingTop, paddingRight, paddingBottom,
                marginLeft, marginTop, marginRight, marginButton);
    }

    public void setData(String[] data, Context context, int textSize) {
        createChild(data, context, textSize, 8, 5, 5,
                8, 5, 5, 5, 5);
    }

    public void setData(List<String> data, Context context, int textSize,
                        int paddingLeft, int paddingTop, int paddingRight, int paddingBottom,
                        int marginLeft, int marginTop, int marginRight, int marginButton) {
        String[] mydata = null;
        if (data != null) {
            int length = data.size();
            mydata = new String[length];
            for (int i = 0; i < length; i++) {
                mydata[i] = data.get(i);
            }
        }
        createChild(mydata, context, textSize,
                paddingLeft, paddingTop, paddingRight, paddingBottom,
                marginLeft, marginTop, marginRight, marginButton);
    }

    public void setMarkClickListener(MarkClickListener markClickListener) {
        this.markClickListener = markClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        int lineWidth = 0;
        int lineHeight = 0;
        int width = 0;//warpcontet是需要记录的宽度
        int height = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            Log.e(TAG, "onMeasure: lineHeight = " + lineHeight + " childHeight = " + childHeight);
            if (lineWidth + childWidth > widthSize) {
                width = Math.max(lineWidth, childWidth);//这种情况就是排除单个标签很长的情况
                lineWidth = childWidth;//开启新行
                height += lineHeight;//记录总行高
                lineHeight = childHeight;//因为开了新行，所以这行的高度要记录一下
            } else {
                lineWidth += childWidth;
//                lineHeight = Math.max(lineHeight, childHeight); //记录行高
                lineHeight = Math.max(height, childHeight); //记录行高
            }
            // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);  //宽度
                height += lineHeight;  //
            }
        }

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize
                : width, (heightMode == MeasureSpec.EXACTLY) ? heightSize
                : height);
    }

    //onLayout中完成对所有childView的位置以及大小的指定
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();  //清空子控件列表
        mLineHeight.clear();  //清空高度记录列表
        int width = getWidth();//得到当前控件的宽度（在onmeasure方法中已经测量出来了）
        int childCount = getChildCount();
        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<View>();
        int lineWidth = 0;  //行宽
        int lineHeight = 0; //总行高
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();//得到属性参数
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            // 如果已经需要换行
            if (i == 3) {
                i = 3;
            }
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width)  //大于父布局的宽度
            {
                // 记录这一行所有的View以及最大高度
                mLineHeight.add(lineHeight);
                // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add(lineViews);
                lineWidth = 0;// 重置行宽
                lineViews = new ArrayList<View>();
            }
            /**
             * 如果不需要换行，则累加
             */
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);
        }
        // 记录最后一行  (因为最后一行肯定大于父布局的宽度，所以添加最后一行是必要的)
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
        int left = 0;
        int top = 0;
        int lineNums = mAllViews.size();
        for (int i = 0; i < lineNums; i++) {
            // 每一行的所有的views
            lineViews = mAllViews.get(i);
            // 当前行的最大高度  每一行的高度都相同  所以使用（i+1）进行设置高度
            lineHeight = (i + 1) * mLineHeight.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View lineChild = lineViews.get(j);
                if (lineChild.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) lineChild.getLayoutParams();
                //开始画标签了。左边和上边的距离是要根据累计的数确定的。
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + lineChild.getMeasuredWidth();
                int bc = tc + lineChild.getMeasuredHeight();
                lineChild.layout(lc, tc, rc, bc);
                left += lineChild.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            }
            left = 0;//将left归零
            top = lineHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public interface MarkClickListener {
        void clickMark(int position);
    }
}

