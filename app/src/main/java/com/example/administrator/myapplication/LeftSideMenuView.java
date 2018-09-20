package com.example.administrator.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 *
 * 平板端app主页左侧导航栏
 *
 * 由于导航栏点击时，页面会有刷新，导航栏用view的objectAnimation做动画会被页面的刷新功能影响，
 * 所以导航栏的动画要用canvas的刷新做出动画效果
 *
 */

public class LeftSideMenuView extends View {

    private float width;
    private float height;
    private float curItemHeight; // height * 340/1030 + (menuItemCount - 5)*200
    private float norItemHeigth;// height * 200/1030
    private float backItemHeight;// height * 90/1030

    private float firstborderHeight;
    private float secondborderHeight;
    private float thirdborderHeight;

    private int colorEbook;
    private int colorVideo;
    private int colorExercise;
    private int colorExperimentation;
    private int colorExit;

    private int menuItemCount = 5;
    private int curIndex = 0;
    private int clickIndex;
    private String subject;
    private int animationDuction;//动画持续时间
    private int frequent = 10;//10ms刷新一次

    private float clickX;
    private float clickY;
    private long clickTime;

    private onItemClickListener onItemClickListener;
    public interface onItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public LeftSideMenuView(Context context) {
        super(context);
        setSubject("语文");
        setAnimationDuction(300);
    }

    public LeftSideMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSubject("语文");
        setAnimationDuction(300);
    }

    public void setSubject(String subject) {
        this.subject = subject;
        setMenuItemColor();
    }

    public void setAnimationDuction(int animationDuction) {
        this.animationDuction = animationDuction;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(140, getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = 140;
        height = getHeight();
        initMenuItemHeight();
        setMenuItemHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);//设置填满

        paint.setColor(colorEbook);
        Rect ebookRect = new Rect(0, 0, (int) width, (int) firstborderHeight);
        canvas.drawRect(ebookRect, paint);
        paint.setColor(colorVideo);
        Rect videoRect = new Rect(0, (int) firstborderHeight, (int) width, (int) secondborderHeight);
        canvas.drawRect(videoRect, paint);
        paint.setColor(colorExercise);
        Rect exerciseRect = new Rect(0, (int) secondborderHeight, (int) width, (int) thirdborderHeight);
        canvas.drawRect(exerciseRect, paint);
        paint.setColor(colorExperimentation);
        Rect experimentationRect = new Rect(0, (int) thirdborderHeight, (int) width, (int) (height - backItemHeight));
        canvas.drawRect(experimentationRect, paint);
        paint.setColor(colorExit);
        Rect exitRect = new Rect(0, (int) (height - backItemHeight), (int) width, (int) height);
        canvas.drawRect(exitRect, paint);

        Bitmap ebookIc = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_ebook);
        RectF ebookRectF = new RectF(width / 7, width / 7, width * 6 / 7, width * 6 / 7);
        canvas.drawBitmap(ebookIc, null, ebookRectF, paint);
        Bitmap videoIc = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_video);
        RectF videoRectF = new RectF(width / 7, firstborderHeight + width / 7, width * 6 / 7, firstborderHeight + width * 6 / 7);
        canvas.drawBitmap(videoIc, null, videoRectF, paint);
        Bitmap exerciseIc = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_exercise);
        RectF exerciseRectF = new RectF(width / 7, secondborderHeight + width / 7, width * 6 / 7, secondborderHeight + width * 6 / 7);
        canvas.drawBitmap(exerciseIc, null, exerciseRectF, paint);
        Bitmap experimentationIc = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_experiment);
        RectF experimentationRectF = new RectF(width / 7, thirdborderHeight + width / 7,
                width * 6 / 7, thirdborderHeight + width * 6 / 7);
        canvas.drawBitmap(experimentationIc, null, experimentationRectF, paint);
        Bitmap backIc = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_exit);
        if (width < backItemHeight){
            float marginTop = (backItemHeight - width * 5 / 7) / 2;
            RectF backRectF = new RectF(width / 7, (int) (height - backItemHeight + marginTop),
                    width * 6 / 7, (int) (height - marginTop));
            canvas.drawBitmap(backIc, null, backRectF, paint);
        }else{
            float marginLeft = (width - backItemHeight * 5/ 7) / 2;
            RectF backRectF = new RectF(marginLeft, (int) (height - backItemHeight) + backItemHeight / 7,
                    width - marginLeft, (int) (height - backItemHeight) + backItemHeight * 6 / 7);
            canvas.drawBitmap(backIc, null, backRectF, paint);
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                clickX = event.getX();
                clickY = event.getY();
                calculateClickPositon();
                return true;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - clickTime < 500){
                    return false;
                }else{
                    clickTime = System.currentTimeMillis();
                }

                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(clickIndex);
                }
                if (clickIndex != 4 && curIndex != clickIndex){
                    startMoveAnimation();
                }

                clickX = 0;
                clickY = 0;
                return false;
        }
        return super.onTouchEvent(event);
    }

    //设置menu的高度
    private void initMenuItemHeight(){
        curItemHeight = height * 340/1030 + (5 - menuItemCount) * 200;
        norItemHeigth = height * 200/1030;
        backItemHeight = height * 90/1030;
    }

    //设置menu的数量和学科对应的颜色
    private void setMenuItemColor() {
        if (subject.equals("语文")) {
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_chinese_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_chinese_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_chinese_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_chinese_experimentation);
        } else if (subject.equals("数学")) {
            menuItemCount = 4;
            colorEbook = getResources().getColor(R.color.colorMenuItem_math_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_math_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_math_exercise);
        } else if (subject.equals("英语")) {
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_english_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_english_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_english_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_english_experimentation);
        } else if (subject.equals("物理")) {
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_physics_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_physics_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_physics_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_physics_experimentation);
        } else if (subject.equals("化学")) {
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_chemistry_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_chemistry_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_chemistry_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_chemistry_experimentation);
        } else if (subject.equals("生物")) {
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_biology_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_biology_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_biology_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_biology_experimentation);
        } else if (subject.equals("地理")) {
            menuItemCount = 4;
            colorEbook = getResources().getColor(R.color.colorMenuItem_geography_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_geography_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_geography_exercise);
        } else if (subject.equals("历史")) {
            menuItemCount = 4;
            colorEbook = getResources().getColor(R.color.colorMenuItem_history_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_history_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_history_exercise);
        } else if (subject.equals("政治")) {
            menuItemCount = 4;
            colorEbook = getResources().getColor(R.color.colorMenuItem_politics_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_politics_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_politics_exercise);
        }
        colorExit = getResources().getColor(R.color.colorItemBack);
    }

    //计算点击了哪个menuItem
    private void calculateClickPositon(){
        if (clickX == 0 && clickY == 0){
            return;
        }
        if (clickY < norItemHeigth){
            clickIndex = 0;
        }else if (clickY < curItemHeight) {
            clickIndex = curIndex == 0 ? 0 : 1;
        }else if (clickY < norItemHeigth * 2){
            clickIndex = 1;
        }else if (clickY < curItemHeight + norItemHeigth) {
            clickIndex = curIndex <= 1 ? 1 : 2;
        }else if (clickY < norItemHeigth * 3){
            clickIndex = 2;
        }else if (clickY < curItemHeight + norItemHeigth * 2){
            clickIndex = curIndex <= 2 ? 2 : 3;
        }else if (clickY < norItemHeigth * 4 || clickY < curItemHeight + norItemHeigth * 3){
            clickIndex = 3;
        }else{
            clickIndex = 4;
        }
    }

    private void setMenuItemHeight(){
        float[] menuItemHeightArr = getMenuItemHeight(curIndex);
        firstborderHeight = menuItemHeightArr[0];
        secondborderHeight = menuItemHeightArr[1];
        thirdborderHeight = menuItemHeightArr[2];
    }

    private float[] getMenuItemHeight(int index){
        float[] menuItemHeightArr = new float[3];
        float firstborderHeight;
        float secondborderHeight;
        float thirdborderHeight;
        if (index == 0){
            firstborderHeight = curItemHeight;
            secondborderHeight = curItemHeight + norItemHeigth;
            thirdborderHeight = curItemHeight + norItemHeigth * 2;
        }else if (index == 1){
            firstborderHeight = norItemHeigth;
            secondborderHeight = curItemHeight + norItemHeigth;
            thirdborderHeight = curItemHeight + norItemHeigth * 2;
        }else if(index == 2){
            firstborderHeight = norItemHeigth;
            secondborderHeight = norItemHeigth * 2;
            thirdborderHeight = curItemHeight + norItemHeigth * 2;
        }else{
            firstborderHeight = norItemHeigth;
            secondborderHeight = norItemHeigth * 2;
            thirdborderHeight = norItemHeigth * 3;
        }
        menuItemHeightArr[0] = firstborderHeight;
        menuItemHeightArr[1] = secondborderHeight;
        menuItemHeightArr[2] = thirdborderHeight;
        return menuItemHeightArr;
    }

    //设置动画
    private void startMoveAnimation(){
        final float[] curMenuItemHeightArr = getMenuItemHeight(curIndex);
        float[] clickMenuItemHeightArr = getMenuItemHeight(clickIndex);
        int invalidateTime = animationDuction / frequent;//动画期间刷新的次数
        if (invalidateTime == 0){
            invalidateTime = 1;
        }

        //四个menuitem的三个边界，每次动画移动的距离
        final float firstAdd = (clickMenuItemHeightArr[0] - curMenuItemHeightArr[0]) / (float)invalidateTime;
        final float secondAdd = (clickMenuItemHeightArr[1] - curMenuItemHeightArr[1]) / (float)invalidateTime;
        final float thirdAdd = (clickMenuItemHeightArr[2] - curMenuItemHeightArr[2]) / (float)invalidateTime;

        //动画数值生成器，
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, invalidateTime);//一次产生0 ~ invalidateTime的int值
        valueAnimator.setDuration(animationDuction);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                firstborderHeight = curMenuItemHeightArr[0] + firstAdd * curValue;
                secondborderHeight = curMenuItemHeightArr[1] + secondAdd * curValue;
                thirdborderHeight = curMenuItemHeightArr[2] + thirdAdd * curValue;
                invalidate();

            }
        });
        valueAnimator.start();
        curIndex = clickIndex;
    }

}
