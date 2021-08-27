package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Pipe extends BaseObject {
    public static int speed;
    public Pipe(float x, float y, int width, int height){
        super(x, y, width, height);
        speed = 10*Constants.SCREEN_WIDTH/1000;
    }

    public void draw(Canvas canvas){
        this.x-=speed; //pipe going from right to left
        canvas.drawBitmap(this.bm, this.x, this.y, null);
    }

    //Random heights of incoming pipes
    public void randomY(){
        Random r = new Random();
        this.y = r.nextInt((this.height/4)+5) - this.height/4;
    }

    @Override
    public Rect getRect() {
        return new Rect((int)this.x+10, (int)this.y, (int)this.x+this.width-10, (int)this.y+this.height);
    }

    //scalling the bitmap according to the pipe
    @Override
    public void setBm(Bitmap bm) {
        this.bm = Bitmap.createScaledBitmap(bm,width,height,true);
    }
}
