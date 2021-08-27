package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* Appcompactactivity =new features of android studio - Base class for activities that
want to use features of new platforms on old devices */

//oncreate() = called when the activity is starting - initialize, callings (method) - first run
//oncreate() : started first time - initialize activity - should happen only once
// @xyz =annotations

public class MainActivity extends AppCompatActivity {
    public static TextView txt_score, txt_best_score, txt_score_over;
    public static ImageView main_title;
    public static RelativeLayout rl_game_over;
    public static Button btn_start;
    private GameView gv;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hiding Status Bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //getting display size of mobile
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_main);

        //xml to java
        txt_score = findViewById(R.id.txt_score);
        txt_best_score = findViewById(R.id.txt_best_score);
        txt_score_over = findViewById(R.id.txt_score_over);
        rl_game_over = findViewById(R.id.rl_game_over);
        btn_start = findViewById(R.id.btn_start);
        main_title = findViewById(R.id.main_title);
        gv = findViewById(R.id.gv);

        //------------------------------------------------------------

        //btn onclicklistener's
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gv.setStart(true);
                txt_score.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.INVISIBLE);
                main_title.setVisibility(View.INVISIBLE);
            }
        });

        rl_game_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_title.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.VISIBLE);
                rl_game_over.setVisibility(View.INVISIBLE);
                gv.setStart(false);
                gv.reset();
            }
        });

        //media player =sound
        mediaPlayer = MediaPlayer.create(this, R.raw.sillychipsong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    //activity states
    /*
    oncreate() : started first time - intialize activity - should happen only once
    onstart()
    onresume() : ready to use - running state where user interacts with app
    onpause() : paused - (example: minimised, or partially visible cuz of dialog).
    onstop()
    ondestroy()
     */

    // activity=screen act 2 -> 1
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    // act 1 -> 2
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }
}