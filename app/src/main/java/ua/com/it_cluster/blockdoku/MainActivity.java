package ua.com.it_cluster.blockdoku;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ua.com.it_cluster.blockdoku.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity /*implements View.OnDragListener */{

    static TextView statusView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusView = (TextView)findViewById(R.id.statusView);
        TableLayout boardLayout =  (TableLayout)findViewById(R.id.boardLayoutId);

        // Down here is little hack because width is zero (no elements occupies TableLayout)
        //boardLayout.removeView(headerText);

        Board board = new Board(getApplicationContext(), boardLayout);
        shapeFactoryProducer(boardLayout);

        //TableLayout  shapeLayout1  = (TableLayout) shapeLayouts.findViewWithTag(Shape.shape_tag_prefix + "1");
        boardLayout.setOnDragListener(this::onDragListener);
        boardLayout.setPadding(Board.device_width/20, Board.device_width/20, Board.device_width/20, Board.device_width/20);
    }

    private void shapeFactoryProducer(TableLayout boardLayout) {
        int width = Board.board_width;
        TableLayout shapeLayouts = (TableLayout) findViewById(R.id.shapeLayoutsId);
        Shape shape = new Shape(getApplicationContext(), shapeLayouts);
        shape.generateRandomShapes(width, 3);
    }

    private TextView getCellInBoardByDragPosition(View boardView, double x, double y)
    {
        double epsilon = 0.00001;
        // TODO fix 120.0
        if (x < 0)
        {
            return null;
        }
        if (y < 0)
        {
            return null;
        }

        int x_cell = (int) (x / Cell.width);
        int y_cell = (int) (y / Cell.width);

        if (x_cell > 8 + epsilon)
        {
            return null;
        }
        if (y_cell > 8 + epsilon)
        {
            return null;
        }


        if (x_cell > 8)
            x_cell = 8;

        if (y_cell > 8)
            y_cell = 8;

        Log.i("Position", "x_cell = " + Double.toString(y_cell)  +
                "; y_cell = " + Double.toString(x_cell));

        TableRow current_row =  (TableRow) ((TableLayout) boardView).getChildAt(y_cell);
        TextView current_cell = (TextView) current_row.getChildAt(x_cell);



        return current_cell;
    }

    boolean validMove = true;
    int score = 0;

    private boolean onDragListener(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    ((View) view).setBackgroundColor(Color.BLUE);
                    view.invalidate();
                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                ((View) view).setBackgroundColor(Color.GREEN);
                view.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                validMove = true;
                View v = (View) dragEvent.getLocalState();
                //TextView textView = (TextView)view.findViewWithTag("BlockDoku");

                //v.setPadding(0,0,0,0);

                float original_x = dragEvent.getX();
                float original_y = dragEvent.getY();

                original_x =  original_x - Shape.padding;
                original_y =  original_y - Shape.padding;

                String textq =
                        "width = " + Float.toString( v.getWidth()) +
                                "height = " + Float.toString(v.getHeight());
                Log.i("Position", textq);

                String text =
                        "X = " + Float.toString(original_x) +
                        "Y = " + Float.toString(original_y);
                Log.i("Position", text);




                clearDragShadow((TableLayout) view, true);
                view.invalidate();

                ArrayList<TextView> cellsInBoardToRedraw = new ArrayList<TextView>();
                cellsInBoardToRedraw.clear();

                TableLayout shapeLayout = (TableLayout) dragEvent.getLocalState();
                for (int i = 0; i < shapeLayout.getChildCount();i++)
                {
                    TableRow currentRow = (TableRow)shapeLayout.getChildAt(i);

                    for (int j = 0; j < currentRow.getChildCount(); j++)
                    {
                        TextView brick = (TextView) currentRow.getChildAt(j);
                        if (brick.getTag().toString().equals(Cell.BUSY))
                        {
                            TextView cellInBoard =  getCellInBoardByDragPosition(view,
                                            original_x + j*Cell.width, original_y+i*Cell.width);
                            if (cellInBoard == null)
                            {
                                // out of bounds
                                validMove = false;
                            }
                            else
                            {
                                Log.i("Position", cellInBoard.getTag().toString());
                                if (cellInBoard.getTag().toString().equals(Cell.BUSY))
                                {
                                    // cell is busy
                                    validMove = false;
                                }
                                else if (cellInBoard.getTag().toString().equals(Cell.EMPTY))
                                {
                                    cellsInBoardToRedraw.add(cellInBoard);
                                }
                            }
                        }
                    }
                }

                if (validMove)
                {
                    Log.i("Position", "validMove if, size = " + Integer.toString(cellsInBoardToRedraw.size()) );


                    for (int i = 0; i < cellsInBoardToRedraw.size(); i++)
                    {
                        cellsInBoardToRedraw.get(i).setBackgroundColor(Color.GRAY);
                        cellsInBoardToRedraw.get(i).setTag(Cell.READY);
                    }
                    view.invalidate();
                }
                //textView.setText(text);
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.i("ACTIONS", "ACTION_DRAG_EXITED");
                ((View) view).setBackgroundColor(Color.BLUE);
                clearDragShadow((TableLayout) view, true);
                view.invalidate();
                return true;
            case DragEvent.ACTION_DROP:
                Log.i("ACTIONS", "ACTION_DROP");
                ClipData.Item item = dragEvent.getClipData().getItemAt(0);
                CharSequence dragData = item.getText();
                //Toast.makeText(getApplicationContext(), "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();
                ((View) view).getBackground().clearColorFilter();
                view.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                ((View) view).getBackground().clearColorFilter();
                if (dragEvent.getResult()) {
                    Log.i("ACTIONS", "ACTION_DRAG_ENDED: dragEvent.getResult(): The drop was handled. ");
                    v = (View) dragEvent.getLocalState();

                    try {
                        // TODO drop only is a valid move
                        if (validMove)
                        {

                            Board.makeReadyCellsBusy((TableLayout) view, getDrawable(R.drawable.myblue_cell_shape));
                            ViewGroup owner = (ViewGroup) v.getParent();
                            owner.removeView(v);

                            Helper.debugShapeCount((TableRow) owner, statusView);


                            // TODO disable those shapes that are not fit.
                            // TODO check for winning situation
                            // TODO if there is only disables shapes - then - game over

                            TableLayout boardLayout =  (TableLayout)findViewById(R.id.boardLayoutId);
                            int [][]boardCounter = Board.searchForStrikes(boardLayout);

                            for (int rowIndex = 0; rowIndex < 9; rowIndex++)
                            {
                                for (int colIndex = 0; colIndex < 9; colIndex++)
                                {
                                    if (boardCounter[rowIndex][colIndex] > 0)
                                    {
                                        score += boardCounter[rowIndex][colIndex] * 10;
                                        TextView cell = Board.getXY(boardLayout, rowIndex, colIndex);
                                        cell.setTag(Cell.EMPTY);

                                        boolean flag = Board.getChessColor(rowIndex, colIndex);
                                        if (flag)
                                            cell.setBackground(getApplicationContext().getDrawable(R.drawable.white_outer_cell_shape));
                                        else
                                            cell.setBackground(getApplicationContext().getDrawable(R.drawable.dark_outer_cell_shape));

                                        ActionBar actionBar = getSupportActionBar();

                                        if(actionBar != null)
                                        {
                                            String dynamicTitle = getResources().
                                                    getString(R.string.app_name) + " " +
                                                    Integer.toString(score);

                                            actionBar.setTitle(dynamicTitle);
                                        }
                                    }
                                }
                            }

                            if (owner.getChildCount() == 0)
                            {
                                TableLayout bLayout =  (TableLayout)findViewById(R.id.boardLayoutId);
                                shapeFactoryProducer(bLayout);
                                statusView.invalidate();

                                //Helper.debugShapeCount((TableRow) owner, statusView);

                            }
                        }
                    }
                    catch (Exception e)
                    {
                        Log.i("MainActivity", e.getMessage().toString());
                    }

                    v.setVisibility(View.VISIBLE);

                } else {
                    //Toast.makeText(getApplicationContext(), "The drop didn't work.",
                    //        Toast.LENGTH_LONG).show();
                    v = (View) dragEvent.getLocalState();
                    v.setVisibility(View.VISIBLE);

                    clearDragShadow((TableLayout) view, true);
                }

                validMove = false;
                view.invalidate();
                return true;
            default:
                //Log.e("DragDrop Example", "Unknown action type received by View.OnDragListener.");
                break;
        }

        return false;
    }


    private void clearDragShadow(TableLayout view, boolean deep_clean) {
        for (int i = 0; i < 9; i++) {
            TableRow row = (TableRow) view.getChildAt(i);
            for (int j = 0; j < 9; j++) {
                TextView textView = (TextView) row.getChildAt(j);
                if (textView.getTag().toString().equals(Cell.EMPTY))
                {
                    boolean flag = Board.getChessColor(i, j);
                    if (flag)
                        textView.setBackground(getDrawable(R.drawable.white_outer_cell_shape));
                    else
                        textView.setBackground(getDrawable(R.drawable.dark_outer_cell_shape));

                    //textView.setBackground(getApplicationContext().getDrawable(R.drawable.white_outer_cell_shape));
                }
                if (deep_clean && textView.getTag().toString().equals(Cell.READY))
                {
                    textView.setTag(Cell.EMPTY);

                    boolean flag = Board.getChessColor(i, j);
                    if (flag)
                        textView.setBackground(getDrawable(R.drawable.white_outer_cell_shape));
                    else
                        textView.setBackground(getDrawable(R.drawable.dark_outer_cell_shape));

                    //textView.setBackground(getApplicationContext().getDrawable(R.drawable.white_outer_cell_shape));
                }
            }
        }
    }
}

