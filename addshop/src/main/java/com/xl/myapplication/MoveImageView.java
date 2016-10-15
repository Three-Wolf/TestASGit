package com.xl.myapplication;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.widget.ImageView;

/**
 * Created by xl on 2016/9/22.
 */
public class MoveImageView extends ImageView {
    public MoveImageView(Context context) {
        super(context);
    }

    public void setMPointF(PointF pointF) {
        setX(pointF.x);
        setY(pointF.y);
    }
}
