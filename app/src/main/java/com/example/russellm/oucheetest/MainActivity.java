package com.example.russellm.oucheetest;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SoundPool sp1 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        final  SoundPool sp2 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        final  SoundPool sp3 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        /** soundId for Later handling of sound pool **/
        final int soundId1 = sp1.load(this, R.raw.ding, 1); // in 2nd param u have to pass your desire ringtone
        final int soundId2 = sp2.load(this,R.raw.wrong,1);
        final int soundId3 = sp3.load(this,R.raw.panpipeup,1);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.handtest);

        final Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        int pixel = bitmap.getPixel(100, 100);
        Log.d("pixelinit","" + pixel);

        final MediaPlayer mp1,mp2;
        mp1 = MediaPlayer.create(MainActivity.this, R.raw.panpipeup);
        mp2 = MediaPlayer.create(MainActivity.this, R.raw.wrong);

        final Hand h = new Hand();
        h.addFinger(new Finger(new Point(103,373),new Point(28,289)));
        h.addFinger(new Finger(new Point(159,309),new Point(145,175)));
        h.addFinger(new Finger(new Point(257,264),new Point(220,143)));
        h.addFinger(new Finger(new Point(297,162),new Point(280,287)));
        h.addFinger(new Finger(new Point(320,323),new Point(389,257)));
        final Finger fing = new Finger(new Point(159,309),new Point(145,175));

        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    Log.d("Touch Event", "Touch event at " + x + " " + y);

                    float eventX = event.getX();
                    float eventY = event.getY();
                    float[] eventXY = new float[] {eventX, eventY};

                    Matrix invertMatrix = new Matrix();
                    ((ImageView)v).getImageMatrix().invert(invertMatrix);

                    invertMatrix.mapPoints(eventXY);
                    int xInv = Integer.valueOf((int)eventXY[0]);
                    int yInv = Integer.valueOf((int)eventXY[1]);

                    Log.d("touch inv", "x: " + xInv + " y: " + yInv);


                    int pixel = bitmap.getPixel(xInv,yInv);
                    Log.d("pixel","" + pixel);




                    //then do what you want with the pixel data, e.g
                    int redValue = Color.red(pixel);
                    int blueValue = Color.blue(pixel);
                    int greenValue = Color.green(pixel);
                    Log.d("col", redValue + " " + blueValue + " " + greenValue);



                    TextView tv = (TextView)findViewById(R.id.textView);
                    if (pixel == 0) {
                        tv.setText("No problems");
                        /*
                        if (mp2.isPlaying()) {
                            mp2.stop();
                        }

                        if (mp1.isPlaying()) {
                            mp1.stop();
                            mp1.prepareAsync();
                            mp1.seekTo(0);
                        } else {
                            mp1.start();
                        }
                        */
                        //Uncomment to play good sound:
                        //sp1.play(soundId1, 1, 1, 0, 0, 1);
                    }



                    else {
                        tv.setText("Ouchee!!" + " x: " + x + " y: " + y + " wrong: " + h.fingerPos(new Point(x,y)));

                        /*
                        if (mp1.isPlaying()) {

                            mp1.stop();
                        }


                    if (mp2.isPlaying()) {
                        mp2.stop();
                        mp2.prepareAsync();
                        mp2.seekTo(0);
                    } else {
                        mp2.start();
                    }
                    */

                    sp2.play(soundId2, 1, 1, 0, 0, 1);
                    return true;



                    }


                    if (fing.pointIsLeft(new Point(x,y))) {
                        tv.setText("Is Left " + fing.toString());
                    }
                    else {
                        tv.setText("Is Right " + fing.toString());
                    }

                    int pos = h.fingerPos(new Point(x, y));
                    int lastRound = h.round;
                    boolean ok = h.logFingerPress(pos);
                    if (h.round != lastRound) {
                        sp3.play(soundId3, 1, 1, 0, 0, 1);
                    }
                    tv.setText(tv.getText().toString() + " pos: " + h.fingerPos(new Point(x, y)) + " ok?: " + ok + " last: " + h.lastValid + " next: " + h.nextValid);


                    TextView sv = (TextView)findViewById(R.id.scoreView);
                    sv.setText("Score: " + h.score);
                    TextView rv = (TextView)findViewById(R.id.roundView);
                    rv.setText("Round: " + h.round);



                }


                return true;
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
