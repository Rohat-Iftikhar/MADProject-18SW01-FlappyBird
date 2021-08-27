package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.ArrayList;

public class Bird extends BaseObject{

    //collection - java - arraylist, linkedlist
    //array vs arraylist  -- static vs dynamic -- initilization (vlaues etc) , araylist -add () (anywhere, anytime)
    /*Bitmap: an array of binary data representing the values of pixels in an image or display.
    Everything that is drawn in android is a Bitmap.
     */
    private ArrayList<Bitmap> arrBms = new ArrayList<>(); //Bitmap created to animate the bird (flight)

    // each iteration count increase by 1
    private int count, vFlap, idCurrentBitmap;
    private  float drop;


    public Bird(){
        this.count = 0;
        this.vFlap = 5;
        this.idCurrentBitmap = 0;
        this.drop = 0;
    }

    //to draw the bird on the canvas
    public void draw(Canvas canvas){
        drop();
        canvas.drawBitmap(this.getBm(),this.x,this.y ,null);
    }

    //auto drop the bird
    private void drop() {
        this.drop+=0.6;
        this.y+=this.drop;
    }

    public ArrayList<Bitmap> getArrBms() {
        return arrBms;
    }

    //To scale the bitmap according to the size of the bird
    public void setArrBms(ArrayList<Bitmap> arrBms) {
        this.arrBms = arrBms;
        for (int i =0; i < arrBms.size(); i++){
            this.arrBms.set(i,Bitmap.createScaledBitmap(this.arrBms.get(i), this.width, this.height,true));
        }
    }

    @Override
    public Bitmap getBm() {
        //this is done to animate the bird
        count++;
        if(this.count == this.vFlap){
            for(int i=0; i<arrBms.size(); i++){
                if(i == arrBms.size()-1){
                    this.idCurrentBitmap = 0;
                    break;
                }
                else if(this.idCurrentBitmap == i){
                    idCurrentBitmap = i+1;
                    break;
                }
            }
            count = 0;
        }//this is done to animate the bird

        //Matrix: images -get set  -external sources -  also for rotating the bird

        //rotation of the bird
        //when flies
        if(this.drop<0){
            Matrix matrix = new Matrix();
            matrix.postRotate(-25);
            return Bitmap.createBitmap(arrBms.get(idCurrentBitmap), 0, 0, arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight(), matrix, true);
        }
        //when falls
        else if(drop>=0){
            Matrix matrix = new Matrix();
            if(drop<70){
                matrix.postRotate(-25+(drop*2));
            }
            else{
                matrix.postRotate(45);
            }
            return Bitmap.createBitmap(arrBms.get(idCurrentBitmap), 0, 0, arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight(), matrix, true);

        }

        return this.arrBms.get(idCurrentBitmap);
    }

    public float getDrop() {
        return drop;
    }

    @Override
    public Rect getRect() {
        return new Rect((int)this.x+20, (int)this.y+20, (int)this.x+this.width-10, (int)this.y+this.height-10);
    }

    public void setDrop(float drop) {
        this.drop = drop;
    }
}
