package ua.com.it_cluster.blockdoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableRow;
import android.widget.TextView;

public class Helper {

    public static int getViewHeight(View view) {
        WindowManager wm =
                (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return view.getMeasuredWidth();
    }

    public static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable object.
        private static Drawable shadow;
        private Point mScaleFactor;

        // Constructor
        public MyDragShadowBuilder(View v) {

            // Stores the View parameter.
            super(v);

            // Creates a draggable image that fills the Canvas provided by the system.
            //shadow = new ColorDrawable(Color.LTGRAY);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point
        // back to the system.
        @Override
        public void onProvideShadowMetrics (Point size, Point touch) {

            // Defines local variables
            int width, height;

            // Set the width of the shadow to half the width of the original View.
            width = getView().getWidth();

            // Set the height of the shadow to half the height of the original View.
            height = getView().getHeight();

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the
            // same as the Canvas that the system provides. As a result, the drag shadow
            // fills the Canvas.
            //shadow.setBounds(0, 0, width, height);

            // Set the size parameter's width and height values. These get back to the
            // system through the size parameter.
            size.set(width, height);
            mScaleFactor = size;

            // Set the touch point's position to be in the middle of the drag shadow.
            //touch.set(width / 2, height / 2);
            // TODO fix 60 = 120 / 2 =  half of the
            touch.set(0, 0) ;
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system
        // constructs from the dimensions passed to onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {
            // Draw the ColorDrawable on the Canvas passed in from the system.
            canvas.scale(mScaleFactor.x/(float)getView().getWidth(), mScaleFactor.y/(float)getView().getHeight());
            getView().draw(canvas);
            //shadow.draw(canvas);
        }
    }

    public static  void debugShapeCount(TableRow view, TextView statusView) {
        String statusText = statusView.getText().toString();
        statusView.setText("shapeCount = " +
                Integer.toString(view.getChildCount()));
    }


}
