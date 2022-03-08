package ua.com.it_cluster.blockdoku;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ua.com.it_cluster.blockdoku.R;

public class Board {
    private Context context;
    private TableLayout boardLayout;
    private TableLayout shapeLayout;

    public static int device_width = 0;
    public static int board_width = 0;

    Board(Context context, TableLayout boardLayout) {
        this.context = context;
        this.boardLayout = boardLayout;
        this.Create();
    }

    private void calculateWidthParameters() {
        TableRow header = new TableRow(context);
        TextView headerText = new TextView(context);
        //header.setBackgroundColor(Color.BLACK);
        headerText.setText("BlockDoku");
        headerText.setTag("BlockDoku");
        //headerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        header.addView(headerText);
        boardLayout.addView(header);

        device_width = Helper.getViewHeight(boardLayout);
        board_width = device_width / 10 * 9;
        Cell.width = board_width / 9;

        boardLayout.removeView(header);
    }

    public void generateBoard()
    {
        // TODO check screen orientation
        //tableLayout.setStretchAllColumns(true);
        calculateWidthParameters();
        int width = device_width / 10 * 9;

        int quadrant = 0;

        for (int outerRowIndex = 0; outerRowIndex < 3; outerRowIndex++)
        {
            TableRow outerRow = new TableRow(context);
            outerRow.setMinimumHeight(width/3);
            for (int outerColumnIndex = 0; outerColumnIndex < 3; outerColumnIndex++)
            {
                // Creating and inner table
                TableLayout innerTableLayout = new TableLayout(context);

                for (int innerRowIndex = 0; innerRowIndex < 3; innerRowIndex++)
                {
                    TableRow innerRow = new TableRow(context);
                    innerRow.setMinimumHeight(width/9);
                    for (int innerColIndex = 0; innerColIndex < 3; innerColIndex++)
                    {
                        TextView textView = new TextView(context);
                        textView.setText("");
                        if (quadrant % 2 == 1)
                            textView.setBackground(context.getDrawable(R.drawable.white_outer_cell_shape));
                        else
                            textView.setBackground(context.getDrawable(R.drawable.dark_outer_cell_shape));
                        textView.setMinimumHeight(width/9);
                        textView.setGravity(Gravity.CENTER);
                        innerRow.addView(textView, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1));
                    }
                    innerTableLayout.addView(innerRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                }

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
                outerRow.addView(innerTableLayout,lp);

                quadrant++;
            }

            boardLayout.addView(outerRow, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public void Create()
    {
        calculateWidthParameters();
        int width = board_width;

        for (int rowIndex = 0; rowIndex < 9; rowIndex++)
        {
            TableRow row = new TableRow(context);
            row.setMinimumHeight(width/9);

            for (int colIndex = 0; colIndex < 9; colIndex++)
            {
                TextView textView = new TextView(context);
                boolean flag = getChessColor(rowIndex, colIndex);
                if (flag)
                    textView.setBackground(context.getDrawable(R.drawable.white_outer_cell_shape));
                else
                    textView.setBackground(context.getDrawable(R.drawable.dark_outer_cell_shape));

                // TODO do we actually need minimum height for textView if we already set if to row?
                textView.setMinimumHeight(width/9);
                textView.setTag(Cell.EMPTY);
                row.addView(textView);
            }

            boardLayout.addView(row, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public static boolean getChessColor(int rowIndex, int colIndex) {
        boolean rowFlag = false;
        boolean colFlag = false;

        if ((rowIndex / 3) % 2 == 0)
            rowFlag = true;
        if ((colIndex / 3) % 2 == 0)
            colFlag = true;

        if (colFlag ^ rowFlag)
            return true;
        else
            return false;
    }

    public static void makeReadyCellsBusy(TableLayout boardLayout, Drawable drawable)
    {
        for (int i = 0; i < boardLayout.getChildCount();i++)
        {
            TableRow currentRow = (TableRow) boardLayout.getChildAt(i);
            for (int j = 0; j < currentRow.getChildCount(); j++)
            {
                TextView cell = (TextView) currentRow.getChildAt(j);
                if (cell.getTag().toString().equals(Cell.READY))
                {
                    cell.setBackground(drawable);
                    cell.setTag(Cell.BUSY);
                }
            }
        }
    }

    public static int[][]  searchForStrikes(TableLayout boardLayout)
    {
        int [][] board = new int[9][9];

        // HORIZONTAL STRIKES
        for (int rowIndex = 0; rowIndex < boardLayout.getChildCount();rowIndex++)
        {
            TableRow currentRow = (TableRow) boardLayout.getChildAt(rowIndex);
            boolean isHorizontalStrike = true;
            for (int colIndex = 0; colIndex < currentRow.getChildCount(); colIndex++)
            {
                TextView cell = (TextView) currentRow.getChildAt(colIndex);
                if (!cell.getTag().toString().equals(Cell.BUSY))
                {
                    isHorizontalStrike = false;
                    break;
                }
            }

            if (isHorizontalStrike)
            {
                for (int colIndex = 0; colIndex < 9; colIndex++)
                {
                    board[rowIndex][colIndex]++; //  rowIndex is row and it's fixed for certian horizontal strike
                }
            }
        }

        // VERTICAL STRIKES
        for (int colIndex = 0; colIndex < 9; colIndex++)
        {
            boolean isVerticalStrike = true;

            for (int rowIndex = 0; rowIndex < 9; rowIndex++)
            {
                TableRow row = (TableRow) boardLayout.getChildAt(rowIndex);
                TextView cell = (TextView) row.getChildAt(colIndex);
                if (!cell.getTag().toString().equals(Cell.BUSY))
                {
                    isVerticalStrike = false;
                    break;
                }
            }

            if (isVerticalStrike)
            {
                for (int rowIndex = 0; rowIndex < 9; rowIndex++)
                {
                    board[rowIndex][colIndex]++; // colIndex is columen index and it's fixed for certian vertial strike
                }
            }
        }

        // NINE BLOCK STRIKES
        for (int rowIndex= 0; rowIndex < 9; rowIndex = rowIndex + 3)
        {
            for (int colIndex = 0; colIndex < 9; colIndex = colIndex + 3)
            {
                boolean isNineBlockStrike = true;

                /*
                int r = rowIndex;
                int c = colIndex;

                while (r <= rowIndex + 2 && c <= colIndex + 2)
                {
                    if (c < colIndex + 2)
                    {
                        if (!getXY(boardLayout, r, c).equals(Cell.BUSY))
                        {
                            isNineBlockStrike = false;
                            break;
                        }
                        else
                        {
                            c++;
                        }
                    }

                    if  (c > colIndex + 2)
                    {
                        r++;
                        c = colIndex;
                    }

                    if (r > colIndex + 2)
                        break;
                }

                if (isNineBlockStrike)
                {
                    // Oh, f*ck this all ...
                }*/

                // LET'S just write just nine expressions
                if (    !getXY(boardLayout, rowIndex, colIndex).getTag().toString().equals(Cell.BUSY) ||
                        !getXY(boardLayout, rowIndex, colIndex+1).getTag().toString().equals(Cell.BUSY) ||
                        !getXY(boardLayout, rowIndex, colIndex+2).getTag().toString().equals(Cell.BUSY) ||
                        !getXY(boardLayout, rowIndex+1, colIndex).getTag().toString().equals(Cell.BUSY) ||
                        !getXY(boardLayout, rowIndex+1, colIndex+1).getTag().toString().equals(Cell.BUSY) ||
                        !getXY(boardLayout, rowIndex+1, colIndex+2).getTag().toString().equals(Cell.BUSY) ||
                        !getXY(boardLayout, rowIndex+2, colIndex).getTag().toString().equals(Cell.BUSY) ||
                        !getXY(boardLayout, rowIndex+2, colIndex+1).getTag().toString().equals(Cell.BUSY) ||
                        !getXY(boardLayout, rowIndex+2, colIndex+2).getTag().toString().equals(Cell.BUSY))
                {
                    isNineBlockStrike = false;
                }

                if (isNineBlockStrike)
                {
                    board[rowIndex][colIndex]++;
                    board[rowIndex][colIndex+1]++;
                    board[rowIndex][colIndex+2]++;
                    board[rowIndex+1][colIndex]++;
                    board[rowIndex+1][colIndex+1]++;
                    board[rowIndex+1][colIndex+2]++;
                    board[rowIndex+2][colIndex]++;
                    board[rowIndex+2][colIndex+1]++;
                    board[rowIndex+2][colIndex+2]++;
                }
            }
        }

        return board;
    }


    public static TextView getXY(TableLayout boardLayout,  int row, int col)
    {
        return (TextView) ((TableRow)boardLayout.getChildAt(row)).getChildAt(col);
    }



}
