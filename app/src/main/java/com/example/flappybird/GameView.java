package com.example.flappybird;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/*View- Basic Building Block of UI - Super Class of all GUI components. (TextView, ImageView etc)
Rectangle on screen that has any type of content.
Drawing and handling events
Base class of "Widgets": to create Interactive UI components.
*/

//To display the gameplay and character.
public class GameView extends View {
    private Bird bird;
    private Handler handler;
    private Runnable r;
    private ArrayList<Pipe> arrPipes;
    private int sumpipe, distance;
    private int score, bestscore = 0;
    private boolean start;
    private Context context;
    private int soundJump;
    private float volume;
    private boolean loadedsound;
    private SoundPool soundPool;


//  public View (Context context) : This constructor is used when creating view from code.
/*  The constructor below is used when we are making view and it's components in xml file and
    and want to have all its attributes and components in java
    thus, XML -> Java */

    // Since this is a custom view, we're manually overriding draw method.
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        //Shared Preferences for saving highscore
        /*The SharedPreferences class provides a general framework that allows you to save and
        retrieve persistent key-value pairs of primitive data types. You can use
        SharedPreferences to save any primitive data: booleans, floats, ints, longs, and strings.
        This data will persist across user sessions (even if your application is killed).*/
        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE); //getting Highscore
        if(sp != null){
            bestscore = sp.getInt("bestscore", 0);
        }
        score = 0 ;
        start = false;
        initBird();
        initPipe();
        handler = new Handler();


        //concurrency -multi threading/jobs( 2 threads working )
        //creating loop to update interface
        /*
        Runnable () -interface
        override  run()
         */
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            } //to force a view to draw. Since it's loop it'll animate

        };

        //build- build gradle version= attribute SDK_INT(Const)
        if(Build.VERSION.SDK_INT>=21){
            //AudioAttributes - audio
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            //A SoundPool is a collection of sound samples that can be loaded into memory from a resource inside the APK
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool = builder.build();
        }else{
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                loadedsound = true;
            }
        });
        soundJump = this.soundPool.load(context, R.raw.jump_02, 1);
    }

    private void initPipe() {
        sumpipe = 6; //6 pipes per screen
        distance = 350*Constants.SCREEN_HEIGHT/1920;
        arrPipes = new ArrayList<>();

        //initializing the initial position of water pipes
        for (int i = 0; i < sumpipe; i++){
            //downward pipe
            if(i<sumpipe/2){
                this.arrPipes.add(new Pipe(Constants.SCREEN_WIDTH+i*((Constants.SCREEN_WIDTH+200*Constants.SCREEN_WIDTH/1000)/(sumpipe/2)),
                0, 200*Constants.SCREEN_WIDTH/1000, Constants.SCREEN_HEIGHT/2));

                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe2));
                this.arrPipes.get(this.arrPipes.size()-1).randomY();
            }
            //upward pipe
            else{
                this.arrPipes.add(new Pipe(this.arrPipes.get(i-sumpipe/2).getX(), this.arrPipes.get(i-sumpipe/2).getY()
                + this.arrPipes.get(i-sumpipe/2).getHeight() + this.distance, 200*Constants.SCREEN_WIDTH/1000, Constants.SCREEN_HEIGHT/2));

                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe1));
            }
        }
    }

    //Making bird in gameview
    private void initBird() {
        bird = new Bird();

        //size
        bird.setWidth(100*Constants.SCREEN_WIDTH/1000);
        bird.setHeight(100*Constants.SCREEN_HEIGHT/1920);

        //coordinates
        bird.setX(100*Constants.SCREEN_WIDTH/1000);
        bird.setY(Constants.SCREEN_HEIGHT/2 - bird.getHeight()/2);

        ArrayList<Bitmap> arrBms = new ArrayList<>();
        //BitmapFactory: Creates Bitmap objects from various sources, including files, streams, and byte-arrays.
        //to get sprites of the bird in the Bitmap Arraylist
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird1));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird2));
        bird.setArrBms(arrBms); //done to scale the bitmap according to the bird's width and height
    }

/*draw(Canvas canvas)
Manually render this view (and all of its children) to the given Canvas.

A canvas is a surface for drawing.

To draw something, you need 4 basic components:
A Bitmap to hold the pixels,
a Canvas to host the draw calls (writing into the bitmap),
a drawing primitive (e.g. Rect, Path, text, Bitmap),
and a paint (to describe the colors and styles for the drawing).
*/
    public void draw(Canvas canvas){
        super.draw(canvas);
        if(start){
            bird.draw(canvas); //render the bird on gameview

            //conditions for score to increase
            for(int i = 0; i < sumpipe; i++){

                //gameover condition - collision
                //collide - fly too high - fall
                if(bird.getRect().intersect(arrPipes.get(i).getRect()) || bird.getY()-bird.getHeight()<0 || bird.getY()>Constants.SCREEN_HEIGHT){
                    Pipe.speed = 0;
                    MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_best_score.setText("Best: "+ bestscore);

                    MainActivity.txt_score.setVisibility(INVISIBLE);
                    MainActivity.rl_game_over.setVisibility(VISIBLE);
                }

                //Scoring condition
                if(this.bird.getX()+this.bird.getWidth() > arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2 // under the pipe
                        && this.bird.getX()+this.bird.getWidth() <= arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2+Pipe.speed //before pipe
                        && i<sumpipe/2){

                    score++;
                    if(score>bestscore){
                        bestscore = score;
                        //SharedPreferences - saving the score
                        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("bestscore", bestscore);
                        editor.apply();
                    }
                    MainActivity.txt_score.setText(""+score);
                }

                //if pipe goes off the screen then reset position
                if(this.arrPipes.get(i).getX() < -arrPipes.get(i).getWidth()){
                    this.arrPipes.get(i).setX(Constants.SCREEN_WIDTH);
                    if(i < sumpipe/2){
                        arrPipes.get(i).randomY();
                    }else{
                        arrPipes.get(i).setY(this.arrPipes.get(i-sumpipe/2).getY()
                                +this.arrPipes.get(i-sumpipe/2).getHeight() + this.distance);
                    }
                }
                this.arrPipes.get(i).draw(canvas);
            }
        }

        else{
            if(bird.getY() > Constants.SCREEN_HEIGHT/2){
                bird.setDrop(-15*Constants.SCREEN_HEIGHT/1920);
            }
            bird.draw(canvas);
        }

/*
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawRect(bird.getRect(), paint);
        for (int i = 0; i < arrPipes.size(); i++){
            canvas.drawRect(arrPipes.get(i).getRect(), paint);
        }
*/
        handler.postDelayed(r,10); //updates draw method every 0.01 seconds
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            bird.setDrop(-15);
            if(loadedsound){
                int streamId = this.soundPool.play(this.soundJump, (float)0.5, (float) 0.5, 1, 0, 1f);
            }
        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity.txt_score.setText("0");
        score = 0;
        initPipe();
        initBird();
    }
}
