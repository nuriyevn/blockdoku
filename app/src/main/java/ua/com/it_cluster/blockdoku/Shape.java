package ua.com.it_cluster.blockdoku;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import ua.com.it_cluster.blockdoku.R;

public class Shape {
    // pixels for the whole shape ( all left, right, top, bottom)
    public static int padding = 10;
    public static String shape_tag_prefix = "RandomShape";
    private Context context;
    private TableLayout shapesLayouts;
    private int squareSideSize = 0;
    private int nineSquaresSideSize = 0;
    Shape(Context context, TableLayout shapesLayouts)
    {
        this.context = context;
        this.shapesLayouts = shapesLayouts;
    }

    private static boolean onLongClick(View view) {

        String label = view.getTag().toString();
        ClipData.Item clipDataItem = new ClipData.Item(label);

        ClipData clipData = new ClipData(label,
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                clipDataItem);

        //view.setPadding(0,0,0,0);

        View.DragShadowBuilder myShadow = new Helper.MyDragShadowBuilder(view);
        //View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            view.startDrag(clipData, myShadow, view, 0);

        } else {
            view.startDragAndDrop(clipData,  // The data to be dragged
                    myShadow,  // The drag shadow builder
                    view,
                    0          // Flags (not currently used, set to 0)
            );
        }

        view.setVisibility(View.INVISIBLE);
        return true;
    }

    protected ArrayList<int[][]> getShapeMatrix()
    {
        ArrayList<int[][]> arrayList = new ArrayList<int[][]>();

        arrayList.add(new int[][]{
                {1,1,1},
                {0,1,0},
                {0,1,0},
        });

        arrayList.add(new int[][]{
                {1,0,0},
                {1,1,1},
                {1,0,0},
        });

        arrayList.add(new int[][]{
                {0,1,0},
                {0,1,0},
                {1,1,1},
        });

        arrayList.add(new int[][]{
                {0,0,1},
                {1,1,1},
                {0,0,1},
        });

        arrayList.add(new int[][]{
                {1},
        });

        arrayList.add(new int[][]{
                {1, 1},
        });

        arrayList.add(new int[][]{
                {1},
                {1},
        });

        arrayList.add(new int[][]{
                {1},
                {1},
        });

        arrayList.add(new int[][]{
                {1, 1, 1}
        });

        arrayList.add(new int[][]{
                {1, 1, 1, 1}
        });

        arrayList.add(new int[][]{
                {1, 1, 1, 1, 1}
        });


        arrayList.add(new int[][]{
                {1},
                {1},
                {1}
        });

        arrayList.add(new int[][]{
                {1},
                {1},
                {1},
                {1}
        });

        arrayList.add(new int[][]{
                {1},
                {1},
                {1},
                {1},
                {1}
        });

        arrayList.add(new int[][]{
                {1,1,1},
                {0,1,0}
        });

        arrayList.add(new int[][]{
                {0,1,0},
                {1,1,1}
        });

        arrayList.add(new int[][]{
                {1,0},
                {1,1},
                {1,0}
        });

        arrayList.add(new int[][]{
                {0,1},
                {1,1},
                {0,1}
        });
        arrayList.add(new int[][]{
                {1,0},
                {1,1},
                {0,1}
        });

        arrayList.add(new int[][]{
                {0,1},
                {1,1},
                {1,0}
        });

        arrayList.add(new int[][]{
                {1,1,0},
                {0,1,1}
        });

        arrayList.add(new int[][]{
                {0,1,1},
                {1,1,0}
        });

        arrayList.add(new int[][]{
                {0,1},
                {1,1}
        });

        arrayList.add(new int[][]{
                {1,0},
                {1,1}
        });
        arrayList.add(new int[][]{
                {1,1},
                {0,1}
        });
        arrayList.add(new int[][]{
                {1,1},
                {1,0}
        });
        arrayList.add(new int[][]{
                {1,1},
                {1,1}
        });

        arrayList.add(new int[][]{
                {1,1},
                {1,0},
                {1,0}
        });

        arrayList.add(new int[][]{
                {1,1},
                {0,1},
                {0,1}
        });
        arrayList.add(new int[][]{
                {1,0},
                {1,0},
                {1,1}
        });
        arrayList.add(new int[][]{
                {0,1},
                {0,1},
                {1,1}
        });

        arrayList.add(new int[][]{
                {1,1,1},
                {1,0,0}
        });

        arrayList.add(new int[][]{
                {1,1,1},
                {0,0,1}
        });

        arrayList.add(new int[][]{
                {1,0,0},
                {1,1,1}
        });
        arrayList.add(new int[][]{
                {0,0,1},
                {1,1,1}
        });

        return arrayList;
    }

    public void generateRandomShapes(int width, int shapeCount) {

        //TableRow shapesTextRow = new TableRow(context);
        //TextView shapesTextView = new TextView(context);
        ///shapesTextView.setText("Available Shapes");
        //shapesTextView.setGravity(Gravity.CENTER);
        //shapesTextRow.addView(shapesTextView);
        //shapesLayouts.addView(shapesTextRow);


        TableRow shapeTableRow = null;
        if (shapesLayouts.getChildCount() == 0)   // Creating table row for the first time
            shapeTableRow = new TableRow(context);
        else
            shapeTableRow = (TableRow) shapesLayouts.getChildAt(0);

        assert (shapeTableRow.getChildCount() <= 1);

        nineSquaresSideSize = width /3;
        shapeTableRow.setMinimumHeight(nineSquaresSideSize);
        shapeTableRow.setMinimumWidth(nineSquaresSideSize);

        ArrayList<int [][]> allShapes = getShapeMatrix();

        int totalShapeNumber = allShapes.size();

        for (int i = 0; i < shapeCount; i++)
        {
            Random random = new Random();
            int randomShape = random.nextInt(totalShapeNumber);
            int shape[][] = allShapes.get(randomShape); // getting certain shape matrix

            /*int background_color = i % 2 == 0 ? Color.rgb(0xAA,0xAA,0xAA)
                    : Color.rgb(0xDD,   0xDD,0xDD);
            int color = i % 2 == 0 ? Color.rgb(0xFF, 0x00, 0x00)
                    : Color.rgb(0,0, 0xFF);*/

            squareSideSize = nineSquaresSideSize / 3;
            TableLayout shapeLayout = drawShape(squareSideSize, shape, i);
            shapeLayout.setTag(shape_tag_prefix + Integer.toString(i));
            shapeTableRow.addView(shapeLayout);
        }

        // second run
        if (shapeTableRow.getParent() != null)
        {
            ((ViewGroup)shapeTableRow.getParent()).removeView(shapeTableRow);
        }
        shapesLayouts.addView(shapeTableRow);
        Helper.debugShapeCount(shapeTableRow, MainActivity.statusView);
    }

    public TableLayout drawShape(int cellSize, int[][] shape, int color)  // cellSize is cellWidth and cellHeight
    {
        TableLayout tableLayout = new TableLayout(context);
        //tableLayout.setBackgroundColor(background_color);

        tableLayout.setPadding(padding, padding, padding, padding);

        Drawable drawable = context.getDrawable(R.drawable.myblue_cell_shape);

        for (int i = 0; i < shape.length; i++)
        {
            TableRow tableRow = new TableRow(context);
            for (int j = 0; j < shape[i].length; j++)
            {
                TextView textView  = new TextView(context);

                if (shape[i][j] == 0)
                {
                    //textView.setBackground(context.getDrawable(R.drawable.white_outer_cell_shape));
                    //textView.setTextColor(Color.WHITE);
                    //textView.setText("0");
                    textView.setTag(Cell.EMPTY);
                }
                else if (shape[i][j] == 1)
                {
                    //textView.setBackground(context.getDrawable(R.drawable.myblue_cell_shape));
                    textView.setBackground(drawable);
                    textView.setTag(Cell.BUSY);
                    //textView.setTextColor(context.getResources().getColor(R.color.myblue));
                }

                textView.setMinimumWidth(cellSize);
                textView.setMinimumHeight(cellSize);
                //textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
            }
            tableLayout.addView(tableRow);
        }

        /*tableLayout.setOnClickListener(view -> {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
        });*/

        tableLayout.setOnLongClickListener(Shape::onLongClick);

        return tableLayout;
    }
}