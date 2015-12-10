package com.olson.shane.projectashman;



/**
 * Created by Shane on 12/3/2015.
 */


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;


public class Maze extends View implements View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {


    //----------NEW VARIABLES------------

    private final int ASHMAN = 1;
    private final int GHOST = 2;
    private MazeArrayRow[] mCharacterPostions;
    private MazeArrayRow[] mMaze;
    private Character mAshMan;
    private Character[] mGhosts;
    private MediaPlayer mMediaPlayer;
    int mTimerCtr, levelNumber = 1, mHeightCanvas, mWidthCanvas, mCakenum, mIntermissionTimer;
    String UIDirection;
    Random mRandom;
    boolean isFirstInit = true;
    boolean isGameOver = false;
    boolean isGamePaused;
    boolean mIsWaitingOnIntermisson;
    private TheObserver mObserver;


    @Override
    public Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        // ... save everything
        bundle.putSerializable("mazeShape", mMaze);
        bundle.putSerializable("mazeCharacters", mCharacterPostions);
        bundle.putInt("level", levelNumber);
        bundle.putInt("cakes", mCakenum);
        bundle.putInt("intermission", mIntermissionTimer);
        bundle.putInt("timer", mTimerCtr);
        bundle.putString("dir", UIDirection);
        bundle.putSerializable("ashman", mAshMan);
        bundle.putSerializable("ghosts", mGhosts);
        bundle.putBoolean("firstinit", isFirstInit);
        bundle.putBoolean("gameover",isGameOver);
        bundle.putBoolean("gamepaused",isGamePaused);
        bundle.putBoolean("waitingintermission",mIsWaitingOnIntermisson);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

           mMaze =(MazeArrayRow[]) bundle.getSerializable("mazeShape");
           mCharacterPostions =(MazeArrayRow[]) bundle.getSerializable("mazeCharacters");
           levelNumber = bundle.getInt("level");
           mCakenum = bundle.getInt("cakes");
           mIntermissionTimer = bundle.getInt("intermission");
           mTimerCtr = bundle.getInt("timer");
           UIDirection = bundle.getString("dir");
           mAshMan =(Character) bundle.getSerializable("ashman");
           mGhosts =(Character[]) bundle.getSerializable("ghosts");
           isFirstInit = bundle.getBoolean("firstinit");
           isGameOver = bundle.getBoolean("gameover");
           isGamePaused = bundle.getBoolean("gamepaused");
           mIsWaitingOnIntermisson = bundle.getBoolean("waitingintermission");
            // ... load everything
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }

    public void setIsGamePaused(boolean isGamePaused) {
        this.isGamePaused = isGamePaused;
    }

    public void setUIDirection(String UIDirection) {
        this.UIDirection = UIDirection;
    }

    public Maze(Context context) {
        super(context);
        init();
    }

    public Maze(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Maze(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }





    public void init(){

        mAshMan = new Character(ASHMAN);
        mRandom = new Random();
        mCharacterPostions = new MazeArrayRow[]{
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                new MazeArrayRow(0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        };



        if (levelNumber == 1) {// make first maze on original startup-------------------------------------
            mGhosts = new Character[4];
            for(int x = 0; x < mGhosts.length; x++){
                mGhosts[x] = new Character(GHOST);
            }
            mAshMan.setXPos(5); mAshMan.setYPos(7);
            mGhosts[0].setXPos(0); mGhosts[0].setYPos(0);
            mGhosts[1].setXPos(13); mGhosts[1].setYPos(13);
            mGhosts[2].setXPos(3); mGhosts[2].setYPos(13);
            mGhosts[3].setXPos(8); mGhosts[3].setYPos(5);
            mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 1;
            for( Character ghost: mGhosts){
                mCharacterPostions[ghost.getYPos()].getRowState()[ghost.getXPos()] = 2;
            }
            UIDirection = "up";
            mMaze = new MazeArrayRow[]{ //build maze layout
                    new MazeArrayRow(2,2,2,0,0,0,0,2,0,0,0,0,0,0),
                    new MazeArrayRow(2,0,2,0,2,2,2,2,0,2,2,2,2,2),
                    new MazeArrayRow(2,0,2,0,2,0,0,2,2,2,2,0,2,2),
                    new MazeArrayRow(2,0,2,0,2,2,0,0,0,0,2,0,2,2),
                    new MazeArrayRow(2,0,2,2,2,0,0,2,2,2,2,0,2,2),
                    new MazeArrayRow(2,0,0,0,2,2,0,2,2,2,2,0,0,2),
                    new MazeArrayRow(2,2,2,0,2,2,2,2,0,2,2,2,2,2),
                    new MazeArrayRow(0,0,2,0,2,1,2,0,0,0,2,0,0,2),
                    new MazeArrayRow(2,2,2,0,2,2,2,2,0,2,2,0,2,2),
                    new MazeArrayRow(2,0,2,0,2,0,0,2,0,2,0,2,2,2),
                    new MazeArrayRow(2,0,2,0,2,0,0,2,2,2,0,2,0,0),
                    new MazeArrayRow(2,0,2,2,2,2,0,2,0,0,0,2,0,2),
                    new MazeArrayRow(2,0,2,2,2,2,0,2,2,2,2,2,0,2),
                    new MazeArrayRow(2,0,0,2,2,2,2,2,0,0,2,2,2,2)
            };
        }// end level one only code-----------------------------------------------------------------
        if(levelNumber == 2){ //this creates the second level
            mGhosts = new Character[8];
            for(int x = 0; x < mGhosts.length; x++){
                mGhosts[x] = new Character(GHOST);
            }
            mAshMan.setXPos(5); mAshMan.setYPos(7);
            mGhosts[0].setXPos(0); mGhosts[0].setYPos(0);
            mGhosts[1].setXPos(13); mGhosts[1].setYPos(13);
            mGhosts[2].setXPos(3); mGhosts[2].setYPos(13);
            mGhosts[3].setXPos(8); mGhosts[3].setYPos(6);
            mGhosts[4].setXPos(8); mGhosts[4].setYPos(6);
            mGhosts[5].setXPos(8); mGhosts[5].setYPos(6);
            mGhosts[6].setXPos(0); mGhosts[6].setYPos(0);
            mGhosts[7].setXPos(13); mGhosts[7].setYPos(13);
            mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 1;
            for( Character ghost: mGhosts){
                mCharacterPostions[ghost.getYPos()].getRowState()[ghost.getXPos()] = 2;
            }
            UIDirection = "up";
            mMaze = new MazeArrayRow[]{ //build maze layout
                    new MazeArrayRow(2,2,0,2,2,2,0,2,2,2,2,0,2,2),
                    new MazeArrayRow(2,2,2,2,2,2,2,2,2,2,2,0,2,2),
                    new MazeArrayRow(2,2,0,0,0,0,2,2,0,0,2,0,2,2),
                    new MazeArrayRow(0,2,0,2,0,0,2,0,0,0,2,0,2,2),
                    new MazeArrayRow(2,2,0,2,0,0,2,2,0,0,2,0,2,2),
                    new MazeArrayRow(2,2,0,2,0,0,2,2,0,0,2,0,2,2),
                    new MazeArrayRow(2,2,2,2,2,0,2,2,2,0,2,2,2,2),
                    new MazeArrayRow(2,2,2,0,2,2,2,2,2,2,2,2,2,2),
                    new MazeArrayRow(2,2,0,2,0,0,0,2,0,0,2,0,2,2),
                    new MazeArrayRow(2,2,0,2,0,0,2,2,0,0,2,0,0,2),
                    new MazeArrayRow(2,0,0,2,0,0,2,0,0,0,2,0,2,2),
                    new MazeArrayRow(2,2,0,2,0,0,2,2,0,0,2,0,2,0),
                    new MazeArrayRow(2,2,2,2,2,2,2,2,2,2,2,2,2,2),
                    new MazeArrayRow(2,2,2,2,2,2,2,2,2,2,2,2,2,2)
            };

        }



        //--------------------------------Code that is always called---------------------------------------------------
        prepareGameSound(R.raw.intermission);
        isGamePaused = true;
        mIsWaitingOnIntermisson = true;
        //-----------------------------------------------------------------------------------
        if (isFirstInit){//On first loading into a the game overall
            isFirstInit = false;
            this.setOnTouchListener(this);
            //Game clock! Things start moving.

            mTimerCtr = 1;
            mIntermissionTimer = 0;
            final Handler movementHandler =
                    new Handler();
            final Runnable timer =
                    new Runnable() {
                        @Override
                        public void run() {
                            if(isGameOver)
                                prepareGameSound(R.raw.won);
                            else if(isGamePaused){
                                if(mIsWaitingOnIntermisson) {
                                    mIntermissionTimer++;
                                    if(mIntermissionTimer == 65) {
                                        isGamePaused = false;
                                        mIsWaitingOnIntermisson = false;
                                        mIntermissionTimer = 0;
                                    }
                                }
                            }
                            else
                                move();
                            if(mTimerCtr < 10)
                                mTimerCtr++;
                            else
                                mTimerCtr = 1;

                            movementHandler.postDelayed(this, 80);

                        }
                    };
            timer.run();

        }// end code for first init -------------------------------------------

    }



    // this is to set the observer
    public void setObserver(TheObserver observer){
        mObserver = observer;
    }

    // here be the magic
    private void setDisplayCakeNum(){
        if( mObserver != null ){
            mObserver.callback(mCakenum);
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        float aspectRatio = (float) 1;
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int calculatedHeight = Math.round((float) originalWidth / aspectRatio);
        int finalWidth, finalHeight;
        if (calculatedHeight > originalHeight) {
            finalWidth = Math.round( (originalHeight * aspectRatio));
            finalHeight = originalHeight;
        }
        else {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }
        setMeasuredDimension(finalWidth, finalHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeightCanvas = h;
        mWidthCanvas = w;
    }

    private int getColor(int inState){
        int WALL = Color.BLACK;
        switch (inState){
            case 0:
                return WALL;
            case 1:
                int EMPTY = Color.BLUE;
                return EMPTY;
            case 2:
                int CAKE = Color.BLUE;
                return CAKE;
        }
        return WALL;
    }

    public void makeSquare(int xCoord, int yCoord, Canvas canvas, int color){

        Paint paintWall = new Paint();
        paintWall.setColor(color);
        canvas.drawRect(xCoord, yCoord, xCoord + 1, yCoord + 1, paintWall);
    }

    public void makeCake(int xCoord, int yCoord, Canvas canvas){

        Paint paintCake = new Paint();
        paintCake.setColor(Color.YELLOW);
        canvas.drawCircle((float) (xCoord + 0.5), (float) (yCoord + 0.5), (float) 0.15, paintCake);
    }


    public void makeCharacterSubframe(Character character, float xCoord, float yCoord, Canvas canvas){

        Paint paintCake = new Paint();
        Paint mouth = new Paint();
        mouth.setColor(Color.BLUE);
        if(character.getType() == GHOST) {
            paintCake.setColor(Color.RED);
            canvas.drawCircle((float) (xCoord + 0.5), (float) (yCoord + 0.5), (float) 0.35, paintCake);
        }
        else{
            paintCake.setColor(Color.GREEN);
            if(mTimerCtr % 2 == 0){
                canvas.drawCircle((float) (xCoord + 0.5), (float) (yCoord + 0.5), (float) 0.35, paintCake);
//                Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
//                        R.drawable.ashman_mouth);
            }
            else
            canvas.drawCircle((float) (xCoord + 0.5), (float) (yCoord + 0.5), (float) 0.35, paintCake);

        }
    }

    private Path makeMouth(Character character, float x, float y){
        String dir = character.getDirection();
        Path path = new Path();

        x = (float) 10;
        y = (float) 10;
        switch(dir){
            case "up":
                path.moveTo(x, y);
                path.lineTo(x - (float) .4, y - (float) .4);
                path.lineTo(x + (float) .4, y - (float) .6);
                path.lineTo(x, y);
                path.close();
                break;
            case "right":
                path.moveTo(x, y);
                path.lineTo(x - (float) .4, y - (float) .4);
                path.lineTo(x + (float).4, y - (float).6);
                path.lineTo(x, y);
                path.close();
                break;
            case "down":
                path.moveTo(x, y);
                path.lineTo(x - (float) .4, y - (float) .4);
                path.lineTo(x + (float).4, y - (float).6);
                path.lineTo(x, y);
                path.close();
                break;
            case"left":
                path.moveTo(x, y);
                path.lineTo(x - (float) .4, y - (float) .4);
                path.lineTo(x + (float).4, y - (float).6);
                path.lineTo(x, y);
                path.close();
                break;
        }

        return path;
    }

    private void subframeMovement(Canvas canvas, Character character){
        int speed;
        if(character.getType() == GHOST && levelNumber == 1)
            speed = 10;
        else
            speed = 5;

        float step = (float) mTimerCtr % speed;
        float mult = (float) .2;
        float moveIncr = step * mult;

        if(character.getType() == GHOST && levelNumber == 1)
            moveIncr = moveIncr /(float) 2;

        if(moveIncr == 0) moveIncr = 1;
        String dir = character.getDirection();
        if (dir.equals("up")) {
            if(character.getYPos() - 1 >= 0 && mMaze[character.getYPos() - 1].getRowState()[character.getXPos()] != 0)
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos() - moveIncr, canvas);
            else if(character.getType() == ASHMAN){
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos(), canvas);
            }
            else{
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos(), canvas);
                faceValidDirection(character);
            }
        }
        else if (dir.equals("right")) {
            if (character.getXPos() + 1 < mMaze.length && mMaze[character.getYPos()].getRowState()[character.getXPos() + 1] != 0)
                makeCharacterSubframe(character, (float) character.getXPos() + moveIncr, (float) character.getYPos(), canvas);
            else if(character.getType() == ASHMAN){
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos(), canvas);
            }
            else{
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos(), canvas);
                faceValidDirection(character);
            }


        } else if (dir.equals("down")) {
            if (character.getYPos() + 1 < mMaze.length && mMaze[character.getYPos() + 1].getRowState()[character.getXPos()] != 0)
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos() + moveIncr, canvas);
            else if(character.getType() == ASHMAN){
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos(), canvas);
            }
            else{
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos(), canvas);
                faceValidDirection(character);
            }


        } else if (dir.equals("left")) {
            if (character.getXPos() - 1 >= 0 && mMaze[character.getYPos()].getRowState()[character.getXPos() - 1] != 0)
                makeCharacterSubframe(character, (float) character.getXPos() - moveIncr, (float) character.getYPos(), canvas);
            else if(character.getType() == ASHMAN){
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos(), canvas);
            }
            else{
                makeCharacterSubframe(character, (float) character.getXPos(), (float) character.getYPos(), canvas);
                faceValidDirection(character);
            }


        }


        if(step == 0 && character.getType() == GHOST){
            character.setIsCourseDiverted(true);
        }
    }

    private void faceValidDirection(Character character){
        if(character.getYPos() - 1 >= 0  && mMaze[character.getYPos() - 1].getRowState()[character.getXPos()] != 0){
            character.setDirection("up");
        }

        else if(character.getXPos() + 1 < mMaze.length  && mMaze[character.getYPos()].getRowState()[character.getXPos() + 1] != 0){
            character.setDirection("right");
        }

        else if(character.getYPos() + 1 < mMaze.length  && mMaze[character.getYPos() + 1].getRowState()[character.getXPos()] != 0){
            character.setDirection("down");
        }
        else if(character.getXPos() - 1 >= 0  && mMaze[character.getYPos()].getRowState()[character.getXPos() - 1] != 0){
            character.setDirection("left");
        }
        character.setIsDirChgNeeded(false);
    }


    private void move(){
        if(levelNumber == 1){
            if(mTimerCtr % 10 == 0 ){
                for(int x = 0; x < mGhosts.length; x++){
                    moveGhost(x);
                }
            }
        }
        else if (levelNumber == 2){
            if(mTimerCtr % 5 == 0) {
                for (int x = 0; x < mGhosts.length; x++) {
                    moveGhost(x);
                }
            }
        }

        if(mTimerCtr % 5 == 0){
            moveAshMan();
            checkForCake();
        }




        for (Character ghost : mGhosts) {
            if (mTimerCtr % 10 == 0) {
                changeGhostDirection(ghost);
            }
        }
//            if (ghost.getIsDirChgNeeded() && ghost.getIsCourseDiverted()) {
//                changeGhostDirection(ghost);
//                ghost.setIsCourseDiverted(false);
//            }

        invalidate();


    }

    private void collision(){
            prepareGameSound(R.raw.death);
            isGamePaused = true;
            mIsWaitingOnIntermisson = false;
            Toast.makeText(getContext(), "You're toast, buddy!", Toast.LENGTH_SHORT).show();
    }

    private void moveAshMan(){

        String dir = mAshMan.getDirection();

        switch (dir) {
            case "up":
                if (mAshMan.getYPos() - 1 >= 0 && mMaze[mAshMan.getYPos() - 1].getRowState()[mAshMan.getXPos()] != 0) {
                    //remove ashman from previous position
                    mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 0;
                    //Check for Collision in box ahead
                    if (mCharacterPostions[mAshMan.getYPos() - 1].getRowState()[mAshMan.getXPos()] == 2) {collision();}
                    else {//Change ashman position in grid
                        mAshMan.setYPos(mAshMan.getYPos() - 1);
                        mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 1;

                    }
                }
                break;
            case "right":
                if (mAshMan.getXPos() + 1 < mMaze.length && mMaze[mAshMan.getYPos()].getRowState()[mAshMan.getXPos() + 1] != 0) {
                    //remove ashman from previous position
                    mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 0;
                    //Check for Collision in box ahead
                    if (mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos() + 1] == 2) {collision();}
                    else {//Change ashman position in grid
                        mAshMan.setXPos(mAshMan.getXPos() + 1);
                        mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 1;
                    }
                }
                break;
            case "down":
                if (mAshMan.getYPos() + 1 < mMaze.length && mMaze[mAshMan.getYPos() + 1].getRowState()[mAshMan.getXPos()] != 0) {
                    //remove ashman from previous position
                    mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 0;
                    //Change ashman position in grid
                    if (mCharacterPostions[mAshMan.getYPos() + 1].getRowState()[mAshMan.getXPos()] == 2) {collision();}
                    else {//Change ashman position in grid
                        mAshMan.setYPos(mAshMan.getYPos() + 1);
                        mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 1;
                    }
                }
                break;
            case "left":
                if (mAshMan.getXPos() - 1 >= 0 && mMaze[mAshMan.getYPos()].getRowState()[mAshMan.getXPos() - 1] != 0) {
                    //remove ashman from previous position
                    mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 0;
                    //Change ashman position in grid
                    if (mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos() - 1] == 2) {collision();}
                    else {//Change ashman position in grid
                        mAshMan.setXPos(mAshMan.getXPos() - 1);
                        mCharacterPostions[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 1;
                    }
                }
                break;
        }
        mAshMan.setDirection(UIDirection);
    }

    private void moveGhost(int gNum){

        String dir = mGhosts[gNum].getDirection();


        if(dir.equals("up")){

            if(mGhosts[gNum].getYPos() - 1 >= 0  && mMaze[mGhosts[gNum].getYPos() - 1].getRowState()[mGhosts[gNum].getXPos()] != 0){
                //remove Ghost from previous position
                mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 0;
                //Check for Collision in box ahead
                if(mCharacterPostions[mGhosts[gNum].getYPos() - 1].getRowState()[mGhosts[gNum].getXPos()] == 1){ collision(); }
                else {//Change Ghost position in grid
                    mGhosts[gNum].setYPos(mGhosts[gNum].getYPos() - 1);
                    mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 2;
                    mGhosts[gNum].setIsDirChgNeeded(true);

                }
            }
            else{
                makeValidMove(gNum);
            }

        }
        else if(dir.equals("right")){

            if(mGhosts[gNum].getXPos() + 1 < mMaze.length  && mMaze[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos() + 1] != 0){
                //remove Ghost from previous position
                mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 0;
                //Check for Collision in box ahead
                if(mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos() + 1] == 1){ collision(); }
                else {//Change Ghost position in grid
                    mGhosts[gNum].setXPos(mGhosts[gNum].getXPos() + 1);
                    mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 2;
                    mGhosts[gNum].setIsDirChgNeeded(true);

                }
            }
            else{
                makeValidMove(gNum);
            }

        }
        else if(dir.equals("down")){

            if(mGhosts[gNum].getYPos() + 1 < mMaze.length  && mMaze[mGhosts[gNum].getYPos() + 1].getRowState()[mGhosts[gNum].getXPos()] != 0){
                //remove Ghost from previous position
                mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 0;
                //Check for Collision in box ahead
                if(mCharacterPostions[mGhosts[gNum].getYPos() + 1].getRowState()[mGhosts[gNum].getXPos()] == 1){ collision(); }
                else {//Change Ghost position in grid
                    mGhosts[gNum].setYPos(mGhosts[gNum].getYPos() + 1);
                    mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 2;
                    mGhosts[gNum].setIsDirChgNeeded(true);

                }
            }
            else{
                makeValidMove(gNum);
            }
        }
        else if(dir.equals("left")){

            if(mGhosts[gNum].getXPos() - 1 >= 0  && mMaze[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos() - 1] != 0){
                //remove Ghost from previous position
                mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 0;
                //Check for Collision in box ahead
                if(mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos() - 1] == 1){ collision(); }
                else {//Change Ghost position in grid
                    mGhosts[gNum].setXPos(mGhosts[gNum].getXPos() - 1);
                    mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 2;
                    mGhosts[gNum].setIsDirChgNeeded(true);

                }
            }
            else{
               makeValidMove(gNum);
            }
        }
    }

    private void changeGhostDirection(Character ghost){

        int randNum = mRandom.nextInt(4);
        String randDirection;
        switch(randNum){
            case 0: randDirection = "up"; break;
            case 1: randDirection = "right"; break;
            case 2: randDirection = "left"; break;
            default: randDirection = "down"; break;
        }
        ghost.setDirection(randDirection);

    }

    private void makeValidMove(int gNum){
        if(mGhosts[gNum].getYPos() - 1 >= 0  && mMaze[mGhosts[gNum].getYPos() - 1].getRowState()[mGhosts[gNum].getXPos()] != 0){
            //remove Ghost from previous position
            mGhosts[gNum].setDirection("up");
            invalidate();
            mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 0;
            //Check for Collision in box ahead
            if(mCharacterPostions[mGhosts[gNum].getYPos() - 1].getRowState()[mGhosts[gNum].getXPos()] == 1){ collision(); }
            else {//Change Ghost position in grid
                mGhosts[gNum].setYPos(mGhosts[gNum].getYPos() - 1);
                mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 2;

            }
        }

        else if(mGhosts[gNum].getXPos() + 1 < mMaze.length  && mMaze[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos() + 1] != 0){
            //remove Ghost from previous position
            mGhosts[gNum].setDirection("right");
            invalidate();
            mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 0;
            //Check for Collision in box ahead
            if(mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos() + 1] == 1){ collision(); }
            else {//Change Ghost position in grid
                mGhosts[gNum].setXPos(mGhosts[gNum].getXPos() + 1);
                mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 2;

            }
        }

        else if(mGhosts[gNum].getYPos() + 1 < mMaze.length  && mMaze[mGhosts[gNum].getYPos() + 1].getRowState()[mGhosts[gNum].getXPos()] != 0){
            //remove Ghost from previous position
            mGhosts[gNum].setDirection("down");
            invalidate();
            mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 0;
            //Check for Collision in box ahead
            if(mCharacterPostions[mGhosts[gNum].getYPos() + 1].getRowState()[mGhosts[gNum].getXPos()] == 1){ collision(); }
            else {//Change Ghost position in grid
                mGhosts[gNum].setYPos(mGhosts[gNum].getYPos() + 1);
                mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 2;

            }
        }

        else if(mGhosts[gNum].getXPos() - 1 >= 0  && mMaze[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos() - 1] != 0){
            //remove Ghost from previous position
            mGhosts[gNum].setDirection("left");
            invalidate();
            mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 0;
            //Check for Collision in box ahead
            if(mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos() - 1] == 1){ collision(); }
            else {//Change Ghost position in grid
                mGhosts[gNum].setXPos(mGhosts[gNum].getXPos() - 1);
                mCharacterPostions[mGhosts[gNum].getYPos()].getRowState()[mGhosts[gNum].getXPos()] = 2;

            }
        }
        mGhosts[gNum].setIsDirChgNeeded(false);
    }

    private void checkForCake(){
        if (mMaze[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] == 2){
            mMaze[mAshMan.getYPos()].getRowState()[mAshMan.getXPos()] = 1;//remove cake from maze
            prepareGameSound(R.raw.eatcake);
            mCakenum--;
            setDisplayCakeNum();
            if(mCakenum == 0){
                proceedToNextLevel();
            }
        }
    }

    private void proceedToNextLevel(){
        if(levelNumber == 2){
            Toast.makeText(getContext(), "You've beaten the game! Thanks for playing!", Toast.LENGTH_LONG).show();
            prepareGameSound(R.raw.won);
            isGamePaused = true;
        }
        else{
            Toast.makeText(getContext(), "Level " + levelNumber + " complete!", Toast.LENGTH_SHORT).show();
            levelNumber++;
            //mIsWaitingOnIntermisson = true;
            init();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        float canvasWidth =  ( mWidthCanvas / 14);
        float canvasHeight = ( mHeightCanvas / 14);
        canvas.scale(canvasWidth, canvasHeight);
        canvas.save();
        //Actual maze drawing
        mCakenum = 0;
        for(int x = 0; x < mMaze.length; x++){
            for(int y = 0; y < mMaze[x].getRowState().length; y++) {
                int cellState = mMaze[y].getRowState()[x];
                int charState = mCharacterPostions[y].getRowState()[x];
                makeSquare(x, y, canvas, getColor(cellState));
                if (cellState == 2) { //There is a cake in the cell
                    makeCake(x, y, canvas);
                    mCakenum++;
                }
                if(isGamePaused){
                    if (charState == 1)
                        makeCharacterSubframe(mAshMan, (float) x, (float) y, canvas);
                    if (charState == 2)
                        makeCharacterSubframe(mGhosts[0], (float) x, (float) y, canvas);
                }
            }
        }
        if(!isGamePaused) {
            subframeMovement(canvas, mAshMan);
            for (Character ghost : mGhosts) {
                subframeMovement(canvas, ghost);
            }
        }
    }




    private void prepareGameSound(int FDID){
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            AssetFileDescriptor aFD = getContext().getResources().openRawResourceFd(FDID);
            mMediaPlayer.setDataSource(aFD.getFileDescriptor(), aFD.getStartOffset(), aFD.getLength());
            mMediaPlayer.prepare();
        } catch (Exception e){
            Toast.makeText(getContext(), "Sound Broke!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
        mMediaPlayer = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!mIsWaitingOnIntermisson){
            mIsWaitingOnIntermisson = true;
            init();
        }
        return true;
    }

    public void cheatRemoveCakes(){
        for(int x = 0; x < mMaze.length; x++){
            for(int y = 0; y < mMaze[x].getRowState().length; y++) {
                if(!(y == 13 && x == 13)) {
                    int cellState = mMaze[y].getRowState()[x];
                    if (cellState == 2) { //There is a cake in the cell
                        mMaze[y].getRowState()[x] = 1;
                    }
                }
            }
        }
        mCakenum = 1;
        setDisplayCakeNum();
    }
}
