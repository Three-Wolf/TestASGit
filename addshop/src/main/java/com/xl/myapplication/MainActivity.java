package com.xl.myapplication;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 添加到购物车
 */
public class MainActivity extends AppCompatActivity {

    private ListView listview;

    /**容器*/
    private RelativeLayout container;
    /**购物车*/
    private ImageView iv_car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(new GoodAdapter());
        container = (RelativeLayout) findViewById(R.id.rl_container);
        iv_car = (ImageView) findViewById(R.id.iv_car);
    }


    /**点击添加*/
    public void addGoods(View v) {
        int[] addLocation = new int[2];//添加按钮的位置
        int[] shopLocation = new int[2];//购物车的位置
        int[] parentLocation = new int[2];//父容器的位置

        v.getLocationInWindow(addLocation);
        container.getLocationInWindow(parentLocation);
        iv_car.getLocationInWindow(shopLocation);

        TextView textView = new TextView(this);
        new TextView(this );
        //设置初始位置
        MoveImageView moveImageView = new MoveImageView(this);
        moveImageView.setImageResource(R.mipmap.ic_launcher);
        moveImageView.setX(addLocation[0] - parentLocation[0]);
        moveImageView.setY(addLocation[1] - parentLocation[1]);
        container.addView(moveImageView);
        //利用 二次贝塞尔曲线 需首先计算出 MoveImageView的初始点、结束点和一个控制点
        PointF startP = new PointF();
        PointF endP = new PointF();
        final PointF controlP = new PointF();
        //初始点的坐标
        startP.x = addLocation[0] - parentLocation[0];
        startP.y = addLocation[1] - parentLocation[1];
        //结束点位置
        endP.x = shopLocation[0] - parentLocation[0];
        endP.y = shopLocation[1] - parentLocation[1];
        //控制点坐标 x=购物车的x;y=add的y
        controlP.x = endP.x;
        controlP.y = startP.y;
        //属性动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(moveImageView, "mPointF", new PointTypeEvaluator(controlP), startP, endP);
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Object target = ((ObjectAnimator) animation).getTarget();
                container.removeView((View) target);
                Animation anim_add = AnimationUtils.loadAnimation(MainActivity.this
                        , R.anim.add_shop);
                iv_car.startAnimation(anim_add);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    public class PointTypeEvaluator implements TypeEvaluator<PointF> {
        /**每个估值器对应一个属性动画，每个属性动画仅对应唯一一个控制点 */
        PointF control;

        /**估值器返回的点*/
        private PointF mPointF = new PointF();

        public PointTypeEvaluator(PointF control) {
            this.control = control;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            return getBezierPoint(startValue, endValue, control, fraction);
        }

        /**
         * 二次贝塞尔曲线公式
         *
         * @param start   开始的数据点
         * @param end     结束的数据点
         * @param control 控制点
         * @param t       float 0-1
         * @return 不同t对应的PointF
         */
        private PointF getBezierPoint(PointF start, PointF end, PointF control, float t) {
            mPointF.x = (1 - t) * (1 - t) * start.x +2 * t * (1 - t) * control.x + t * t * end.x;
            mPointF.y = (1 - t) * (1 - t) * start.y + 2 * t * (1 - t) * control.y + t * t * end.y;
            return mPointF;
        }
    }

    public class GoodAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.listview_item_goods, parent, false);
            }
            convertView.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addGoods(v);
                }
            });
            ((TextView) convertView.findViewById(R.id.tv_name)).setText("我是菊花:" + position + "号");
            return convertView;
        }
    }
}