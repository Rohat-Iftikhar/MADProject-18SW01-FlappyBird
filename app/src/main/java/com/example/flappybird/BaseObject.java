package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.Rect;
//a base class for pipe and bird objects -  has methods for both
public class BaseObject {
    protected float x,y;
    protected  int width, height;

    //to create rectangle around object to detect collisions
    protected Rect rect;

    /*
    Everything that is drawn in android is a Bitmap .
    We can create a Bitmap instance,
    either by using the Bitmap class which has methods that allow us to manipulate pixels in the 2d coordinate system,
    or we can can create a Bitmap from an image or a file or a resource by using the BitmapFactory class.
     */
    protected Bitmap bm;

    public BaseObject() {
    }

    public BaseObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Rect getRect() {
        return new Rect((int)this.x, (int)this.y, (int)this.x+this.width, (int)this.y+this.height);
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}
