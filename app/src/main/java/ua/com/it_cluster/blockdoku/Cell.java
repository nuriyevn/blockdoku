package ua.com.it_cluster.blockdoku;

public class Cell {

    public static int width = 0;
    public static String EMPTY = "0";

    // Ready to be written, to become busy, intermediary state between empty and busy
    // only board cells can have this state
    public static String READY = "1";

    public static String BUSY = "2";

    public String  value = "";

    Cell()
    {
        value = "0";
    }
}
