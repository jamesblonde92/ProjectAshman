package com.olson.shane.projectashman;

import java.io.Serializable;

/**
 * Created by Shane on 12/4/2015.
 */
public class Character implements Serializable {

    //type of 1 == ASHMAN
    //type of 2 == GHOST

    private String direction;
    private int XPos;
    private int YPos;
    private boolean isDirChgNeeded, isCourseDiverted;

    public boolean getIsDirChgNeeded() {
        return isDirChgNeeded;
    }

    public void setIsDirChgNeeded(boolean isDirChgNeeded) {
        this.isDirChgNeeded = isDirChgNeeded;
    }

    public boolean getIsCourseDiverted() {
        return isCourseDiverted;
    }

    public void setIsCourseDiverted(boolean isCourseDiverted) {
        this.isCourseDiverted = isCourseDiverted;
    }

    public int getType() {
        return type;
    }

    private  int type;

    public Character(int pType){
        type = pType;
        direction = "up";
        isCourseDiverted = false;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String pdirection) {
        this.direction = pdirection;
    }



    public int getYPos() {
        return YPos;
    }
    public void setYPos(int YPos) {
        this.YPos = YPos;
    }
    public int getXPos() {
        return XPos;
    }
    public void setXPos(int XPos) {
        this.XPos = XPos;
    }
}

