package com.example.slidingpuzzle;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.Toast;

public class GestureDetectGridView extends GridView {
    private GestureDetector gDetector;
    private boolean mFlingConfirmed = false;
    private float mTouchX;
    private float mTouchY;

    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_MAX_OFF_PATH = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 50;

    public static int blankPosition = MainActivity.DIMENSIONS;

    public GestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) // API 21
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr,
                                 int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                final int position1 = GestureDetectGridView.this.pointToPosition
                        (Math.round(e1.getX()), Math.round(e1.getY()));
                final int position2 = GestureDetectGridView.this.pointToPosition
                        (Math.round(e2.getX()), Math.round(e2.getY()));
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH && position2==blankPosition) {
                    if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH
                            || Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                        MainActivity.moveTiles(context, MainActivity.up, position1);
                    } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                        MainActivity.moveTiles(context, MainActivity.down, position1);
                    }
                    blankPosition = position1;
                } else if(position2==blankPosition){
                    if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                        MainActivity.moveTiles(context, MainActivity.left, position1);
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                        MainActivity.moveTiles(context, MainActivity.right, position1);
                    }
                    blankPosition = position1;
                }else{
//                    Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Invalid move" +position1, Toast.LENGTH_SHORT).show();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (mFlingConfirmed) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { return gDetector.onTouchEvent(event);}
}
