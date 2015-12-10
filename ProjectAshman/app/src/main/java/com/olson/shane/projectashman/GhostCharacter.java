package com.olson.shane.projectashman;

/**
 * Created by Shane on 12/5/2015.
 */
public class GhostCharacter {

    private String direction;
    private int XPos;
    private int YPos;

    public GhostCharacter(String pdirection){
        direction = pdirection;
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
