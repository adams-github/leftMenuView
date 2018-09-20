package com.example.administrator.myapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/10.
 */

public class LeftSideMenuLayout extends RelativeLayout {

    private View menu;
    private View ebookRlLayout;
    private View videoRlLayout;
    private View exerciseRlLayout;
    private View experimentationRlLayout;
    private View exitRlLayout;
    private ImageView ebookIv;
    private ImageView videoIv;
    private ImageView exerciseIv;
    private ImageView experimentationIv;
    private List<View> viewList = new ArrayList<>();
    private List<ImageView> iconList = new ArrayList<>();

    private float width;
    private float height;
    private float curItemHeight; // height * 340/1030 + (menuItemCount - 5)*200
    private float norItemHeigth;// height * 200/1030
    private float backItemHeight;// height * 90/1030

    private int colorEbook;
    private int colorVideo;
    private int colorExercise;
    private int colorExperimentation;

    private int menuItemCount = 5;
    private int curIndex = 0;
    private int clickIndex;
    private String subject = "语文";

    private long clickTime;

    private onItemClickListener onItemClickListener;
    public interface onItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    //设置科目，改变menu的颜色，和menu的数量和高度
    public void setSubject(String subject) {
        this.subject = subject;
        setMenuItemColor();
        initMenuItemHeight();
    }

    public LeftSideMenuLayout(Context context) {
        super(context);
        initView();
    }

    public LeftSideMenuLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        menu = LayoutInflater.from(getContext()).inflate(R.layout.leftsidemenu_layout, this);
        ebookRlLayout = menu.findViewById(R.id.ebook_RlLayout);
        videoRlLayout = menu.findViewById(R.id.video_RlLayout);
        exerciseRlLayout = menu.findViewById(R.id.exercise_RlLayout);
        experimentationRlLayout = menu.findViewById(R.id.experimentation_RlLayout);
        exitRlLayout = menu.findViewById(R.id.exit_RlLayout);
        ebookIv = (ImageView) menu.findViewById(R.id.ebook_Iv);
        videoIv = (ImageView) menu.findViewById(R.id.video_Iv);
        exerciseIv = (ImageView) menu.findViewById(R.id.exercise_Iv);
        experimentationIv = (ImageView) menu.findViewById(R.id.experimentation_Iv);

        ebookRlLayout.setOnClickListener(onClickListener);
        videoRlLayout.setOnClickListener(onClickListener);
        exerciseRlLayout.setOnClickListener(onClickListener);
        experimentationRlLayout.setOnClickListener(onClickListener);
        exitRlLayout.setOnClickListener(onClickListener);

        viewList.add(ebookRlLayout);
        viewList.add(videoRlLayout);
        viewList.add(exerciseRlLayout);
        viewList.add(experimentationRlLayout);
        iconList.add(videoIv);
        iconList.add(exerciseIv);
        iconList.add(experimentationIv);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = getWidth();
        height = getHeight();

        initMenuItemHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        ebookIv.layout((int)(width / 7), (int)(width / 7), (int)(width / 7) * 6, (int)(width / 7) * 6);
        videoIv.layout((int)(width / 7), curIndex == 0? (int)((width / 7) + curItemHeight):(int)((width / 7) + norItemHeigth), (int)(width / 7) * 6,
                curIndex == 0? (int)((width / 7) * 6 + curItemHeight):(int)((width / 7) * 6 + norItemHeigth));
        exerciseIv.layout((int)(width / 7), curIndex <= 1? (int)((width / 7) + curItemHeight + norItemHeigth):(int)((width / 7) + 2 * norItemHeigth),
                (int)(width / 7) * 6, curIndex == 0? (int)((width / 7) * 6 + curItemHeight + norItemHeigth):(int)((width / 7) * 6 + 2 * norItemHeigth));
        experimentationIv.layout((int)(width / 7), curIndex <= 2? (int)((width / 7) + curItemHeight + 2*norItemHeigth):(int)((width / 7) + 3 * norItemHeigth),
                (int)(width / 7) * 6, curIndex == 0? (int)((width / 7) * 6 + curItemHeight + 2*norItemHeigth):(int)((width / 7) * 6 + 3 * norItemHeigth));
    }

    //设置menu的高度
    private void initMenuItemHeight(){
        curItemHeight = height * 340/1030 + (5 - menuItemCount) * 200;
        norItemHeigth = height * 200/1030;
        backItemHeight = height * 90/1030;

        final LinearLayout.LayoutParams curParams = new LinearLayout.LayoutParams((int)width, (int)curItemHeight);
        final LinearLayout.LayoutParams norParams = new LinearLayout.LayoutParams((int)width, (int)norItemHeigth);
        final LinearLayout.LayoutParams exitParams = new LinearLayout.LayoutParams((int)width, (int)backItemHeight);

        post(new Runnable() {
            @Override
            public void run() {
                ebookRlLayout.setLayoutParams(curIndex == 0? curParams : norParams);
                videoRlLayout.setLayoutParams(curIndex == 1? curParams : norParams);
                exerciseRlLayout.setLayoutParams(curIndex == 2? curParams : norParams);
                experimentationRlLayout.setLayoutParams(curIndex == 3? curParams : norParams);
                exitRlLayout.setLayoutParams(exitParams);
            }
        });


        if (menuItemCount == 5){
            experimentationRlLayout.setVisibility(View.VISIBLE);
            experimentationIv.setVisibility(View.VISIBLE);
        }else{
            experimentationIv.setVisibility(View.GONE);
            experimentationRlLayout.setVisibility(View.GONE);
        }
    }

    //设置menu的数量和颜色
    private void setMenuItemColor(){
        if (subject.equals("语文")){
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_chinese_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_chinese_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_chinese_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_chinese_experimentation);
        }else if (subject.equals("数学")){
            menuItemCount = 4;
            colorEbook = getResources().getColor(R.color.colorMenuItem_math_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_math_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_math_exercise);
        }else if (subject.equals("英语")){
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_english_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_english_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_english_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_english_experimentation);
        }else if (subject.equals("物理")){
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_physics_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_physics_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_physics_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_physics_experimentation);
        } else if (subject.equals("化学")){
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_chemistry_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_chemistry_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_chemistry_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_chemistry_experimentation);
        } else if (subject.equals("生物")){
            menuItemCount = 5;
            colorEbook = getResources().getColor(R.color.colorMenuItem_biology_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_biology_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_biology_exercise);
            colorExperimentation = getResources().getColor(R.color.colorMenuItem_biology_experimentation);
        }else if (subject.equals("地理")){
            menuItemCount = 4;
            colorEbook = getResources().getColor(R.color.colorMenuItem_geography_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_geography_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_geography_exercise);
        } else if (subject.equals("历史")){
            menuItemCount = 4;
            colorEbook = getResources().getColor(R.color.colorMenuItem_history_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_history_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_history_exercise);
        } else if (subject.equals("政治")){
            menuItemCount = 4;
            colorEbook = getResources().getColor(R.color.colorMenuItem_politics_ebook);
            colorVideo = getResources().getColor(R.color.colorMenuItem_politics_video);
            colorExercise = getResources().getColor(R.color.colorMenuItem_politics_exercise);
        }

        ebookRlLayout.setBackgroundColor(colorEbook);
        videoRlLayout.setBackgroundColor(colorVideo);
        exerciseRlLayout.setBackgroundColor(colorExercise);
        experimentationRlLayout.setBackgroundColor(colorExperimentation);
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (System.currentTimeMillis() - clickTime < 500){
                return;
            }else{
                clickTime = System.currentTimeMillis();
            }

            switch (v.getId()){
                case R.id.ebook_RlLayout:
                    clickIndex = 0;
                    break;
                case R.id.video_RlLayout:
                    clickIndex = 1;
                    break;
                case R.id.exercise_RlLayout:
                    clickIndex = 2;
                    break;
                case R.id.experimentation_RlLayout:
                    clickIndex = 3;
                    break;
                case R.id.exit_RlLayout:
                    clickIndex = 4;
                    break;
            }
            if (clickIndex != 4){
                menuItemclick();
            }
            if (onItemClickListener != null){
                 onItemClickListener.onItemClick(clickIndex);
            }
        }
    };

    //点击menu的响应
    private void menuItemclick(){
        View curView = viewList.get(curIndex);
        View clickView = viewList.get(clickIndex);
        if (clickIndex > curIndex){
            doUpEnlargeScale(clickView);
            doUpNarrowScale(curView);
            int size = (clickIndex - curIndex) / 1;
            for (int i =  curIndex + 1; i < curIndex + size; i++) {
                View upTranslateView = viewList.get(i);
                doUpTranslate(upTranslateView);
            }
            for (int i =  curIndex; i < clickIndex; i++) {
                ImageView imageView = iconList.get(i);
                doUpTranslate(imageView);
            }
        }else if (clickIndex < curIndex){
            doDownNarrowScale(curView);
            doDownEnlargeScale(clickView);
            int size = (curIndex - clickIndex) / 1;
            for (int i =  clickIndex + 1; i < clickIndex + size; i++) {
                View downTranslateView = viewList.get(i);
                doDownTranslate(downTranslateView);
            }

            for (int i =  clickIndex; i < curIndex; i++) {
                ImageView imageView = iconList.get(i);
                doDownTranslate(imageView);
            }
        }
        curIndex = clickIndex;
    }


    //让空间停留在动画的最后一帧，必须要在onAnimationEnd方法中调整控件的位置，不然会有闪屏现象
    private void doUpEnlargeScale(final View view){
        ScaleAnimation upEnlargeScaleAnimation = new ScaleAnimation(1f, 1f, 1f, curItemHeight / norItemHeigth,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        upEnlargeScaleAnimation.setDuration(300);
        upEnlargeScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                float top = view.getY();
                float bottom = view.getBottom();
                view.clearAnimation();
                view.layout(0, (int)(top - (curItemHeight - norItemHeigth)), (int)width, (int)bottom);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(upEnlargeScaleAnimation);
    }

    private void doUpNarrowScale(final View view ){
        ScaleAnimation upNarrowScaleAnimation = new ScaleAnimation(1f, 1f, 1f, norItemHeigth / curItemHeight);
        upNarrowScaleAnimation.setDuration(300);
        upNarrowScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                float top = view.getY();
                float bottom = view.getBottom();
                view.clearAnimation();
                view.layout(0, (int)top, (int) width, (int)(bottom - (curItemHeight - norItemHeigth)));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(upNarrowScaleAnimation);
    }

    private void doDownEnlargeScale(final View view){
        ScaleAnimation downEnlargeScaleAnimation = new ScaleAnimation(1f, 1f, 1f, curItemHeight + 5 / norItemHeigth,// +5 是为了去掉动画连接之间的缝隙
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        downEnlargeScaleAnimation.setDuration(300);
        downEnlargeScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                float top = view.getY();
                float bottom = view.getBottom();
                view.clearAnimation();
                view.layout(0, (int)top, (int) width, (int)(bottom + (curItemHeight - norItemHeigth)));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(downEnlargeScaleAnimation);
    }

    private void doDownNarrowScale(final View view){
        ScaleAnimation downNarrowScaleAnimation = new ScaleAnimation(1f, 1f, 1f, norItemHeigth / curItemHeight,
                Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, view.getHeight());
        downNarrowScaleAnimation.setDuration(300);
        downNarrowScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                float top = view.getY();
                float bottom = view.getBottom();
                view.clearAnimation();
                view.layout(0, (int)(top + (curItemHeight - norItemHeigth)), (int) width, (int)bottom);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(downNarrowScaleAnimation);
    }

    private void doUpTranslate(final View view){
        TranslateAnimation upTranslateAnimation = new TranslateAnimation(0, 0, 0, - (curItemHeight - norItemHeigth));
        upTranslateAnimation.setDuration(300);
        upTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                float top = view.getY();
                float bottom = view.getBottom();
                view.clearAnimation();
                view.layout(0, (int)(top - (curItemHeight - norItemHeigth)), (int) width, (int)(bottom - (curItemHeight - norItemHeigth)));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(upTranslateAnimation);
    }

    private void doDownTranslate(final View view){
        TranslateAnimation downTranslateAnimation = new TranslateAnimation(0, 0, 0, curItemHeight - norItemHeigth);
        downTranslateAnimation.setDuration(300);
        downTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                float top = view.getY();
                float bottom = view.getBottom();
                view.clearAnimation();
                view.layout(0, (int)(top + (curItemHeight - norItemHeigth)), (int) width, (int)(bottom + (curItemHeight - norItemHeigth)));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(downTranslateAnimation);
    }

}
