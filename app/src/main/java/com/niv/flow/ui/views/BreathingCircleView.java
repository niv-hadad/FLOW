package com.niv.flow.ui.views;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

import static com.niv.flow.utils.BreathingState.HOLDING;
import static com.niv.flow.utils.BreathingState.IDLE;
import static com.niv.flow.utils.BreathingState.INHALING;
import static com.niv.flow.utils.BreathingState.RETURNING;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.niv.flow.utils.BreathingState;

public class BreathingCircleView extends View {

    private static final int MAX_RIPPLE_ALPHA = 90;
    private static final int IDLE_FILL = Color.parseColor("#294A56");
    private static final int EXPANDED_FILL = Color.parseColor("#477F8D");

    private static final int IDLE_STROKE = Color.parseColor("#7FAEB4");
    private static final int EXPANDED_STROKE = Color.parseColor("#C0EBEF");

    private static final int RIPPLE_COLOR = Color.parseColor("#9ED3DA");

    private BreathingState breathingState;
    private ValueAnimator animator;
    private ArgbEvaluator colorEvaluator;

    private BreathingListener listener;

    private float density;
    private float minRadius;
    private float maxRadius;
    private float currentRadius;

    private float minStrokeWidth;
    private float maxStrokeWidth;
    private float rippleMaxRadius;

    private float rippleRadius1;
    private float rippleRadius2;

    private int rippleAlpha1;
    private int rippleAlpha2;

    private Paint mCircle;
    private Paint mStroke;
    private Paint mRipple;

    public BreathingCircleView(Context context) {
        super(context);
        this.listener = null;
        init();
    }

    public BreathingCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.listener = null;
        init();
    }

    public BreathingCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.listener = null;
        init();
    }

    public interface BreathingListener {
        public void onStateChanged(BreathingState previousState, BreathingState newState);
        public void onBreathingCycleCompleted();
    }

    public void setBreathingListener(BreathingListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        mRipple.setAlpha(rippleAlpha1);
        canvas.drawCircle(centerX, centerY, rippleRadius1, mRipple);

        mRipple.setAlpha(rippleAlpha2);
        canvas.drawCircle(centerX, centerY, rippleRadius2, mRipple);

        canvas.drawCircle(centerX, centerY, currentRadius, mCircle);
        canvas.drawCircle(centerX, centerY, currentRadius, mStroke);
    }

    private void updateCircleAppearance() {
        float progress = (currentRadius - minRadius) / (maxRadius - minRadius);

        int fillColor = (Integer) colorEvaluator.evaluate(progress, IDLE_FILL, EXPANDED_FILL);
        int strokeColor = (Integer) colorEvaluator.evaluate(progress, IDLE_STROKE, EXPANDED_STROKE);

        float currentStrokeWidth = minStrokeWidth + (maxStrokeWidth - minStrokeWidth) * progress;

        mCircle.setColor(fillColor);
        mStroke.setColor(strokeColor);
        mStroke.setStrokeWidth(currentStrokeWidth);
    }

    private void startInhaleAnimation() {
        listener.onStateChanged(IDLE, INHALING);
        breathingState = INHALING;
        animator = ValueAnimator.ofFloat(minRadius, maxRadius);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(4000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                currentRadius = (float) valueAnimator.getAnimatedValue();
                updateCircleAppearance();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            boolean cancelled = false;
            @Override
            public void onAnimationCancel(Animator animation) {
                cancelled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!cancelled) {
                    breathingState = HOLDING;
                    listener.onStateChanged(INHALING, HOLDING);
                }
            }
        });
        animator.start();
    }

    private void startReturnAnimation() {
        boolean showRipple;
        long returnDuration = (long) (4000 * ((currentRadius - minRadius) / (maxRadius - minRadius)));
        if (breathingState == HOLDING) {
            showRipple = true;
            returnDuration = 6000;
            listener.onStateChanged(HOLDING, RETURNING);
        } else {
            showRipple = false;
            if (breathingState == INHALING)
                listener.onStateChanged(INHALING, RETURNING);
        }
        breathingState = RETURNING;

        rippleAlpha1 = 0;
        rippleAlpha2 = 0;

        animator = ValueAnimator.ofFloat(currentRadius, minRadius);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(returnDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                currentRadius = (float) valueAnimator.getAnimatedValue();
                updateCircleAppearance();
                float p = valueAnimator.getAnimatedFraction();
                if (showRipple) {
                    if (p >= 0f && p <= 0.70f) {
                        float p1 = p / 0.70f;
                        rippleRadius1 = maxRadius + (rippleMaxRadius - maxRadius) * p1;
                        rippleAlpha1 = (int) (MAX_RIPPLE_ALPHA * (1f - p1));
                    } else {
                        rippleAlpha1 = 0;
                    }

                    if (p >= 0.22f && p <= 0.92f) {
                        float p2 = (p - 0.22f) / 0.70f;
                        rippleRadius2 = maxRadius + (rippleMaxRadius - maxRadius) * p2;

                        if (p2 <= 0.15f) {
                            rippleAlpha2 = (int) (MAX_RIPPLE_ALPHA * p2 / 0.15f);
                        } else {
                            rippleAlpha2 = (int) (MAX_RIPPLE_ALPHA * (1f - p2) / 0.85f);
                        }
                    } else {
                        rippleAlpha2 = 0;
                    }
                }
                invalidate();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                breathingState = IDLE;
                rippleAlpha1 = 0;
                rippleAlpha2 = 0;
                invalidate();
                listener.onStateChanged(RETURNING, IDLE);
                if (showRipple)
                    listener.onBreathingCycleCompleted();
            }
        });
        animator.start();
    }

    private void init() {
        breathingState = IDLE;
        density = getResources().getDisplayMetrics().density;

        minRadius = 95 * density;
        maxRadius = 150 * density;
        currentRadius = minRadius;

        minStrokeWidth = 3 * density;
        maxStrokeWidth = 5 * density;

        rippleRadius1 = maxRadius;
        rippleRadius2 = maxRadius;
        rippleMaxRadius = 180 * density;

        rippleAlpha1 = 0;
        rippleAlpha2 = 0;

        colorEvaluator = new ArgbEvaluator();

        mCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRipple = new Paint(Paint.ANTI_ALIAS_FLAG);

        mCircle.setStyle(Paint.Style.FILL);

        mStroke.setStyle(Paint.Style.STROKE);
        mStroke.setStrokeWidth(minStrokeWidth);

        mRipple.setStyle(Paint.Style.STROKE);
        mRipple.setStrokeWidth(1.2f * density);
        mRipple.setColor(RIPPLE_COLOR);

        updateCircleAppearance();

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                if (action == ACTION_DOWN) {
                    if (breathingState == IDLE) {
                        startInhaleAnimation();
                    }
                    return true;
                } else if (action == ACTION_UP || action == ACTION_CANCEL) {
                    if (breathingState == INHALING || breathingState == HOLDING) {
                        animator.cancel();
                        startReturnAnimation();
                    }
                    return true;
                } else if (action == ACTION_MOVE) {
                    return true;
                }
                return false;
            }
        });
    }
}