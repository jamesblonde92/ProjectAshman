package com.olson.shane.projectashman;

import java.io.Serializable;

/**
 * Created by Shane on 12/3/2015.
 */
public class MazeArrayRow implements Serializable {

    private final int WALL = 0;
    private final int EMPTY = 1;
    private final int CAKE = 2;

    private int[] rowState;

    //note that in this constructor, c0 could be read as column 0
    public MazeArrayRow(int c0, int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12, int c13){
        rowState = new int[14];
        rowState[0] = c0;rowState[1] = c1;rowState[2] = c2;
        rowState[3] = c3;rowState[4] = c4;rowState[5] = c5;
        rowState[6] = c6;rowState[7] = c7;rowState[8] = c8;
        rowState[9] = c9;rowState[10] = c10;rowState[11] = c11;
        rowState[12] = c12;rowState[13] = c13;
    }

    public int[] getRowState() {
        return rowState;
    }
}
