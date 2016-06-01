package com.example.russellm.oucheetest;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by RussellM on 1/06/2016.
 */
public class Hand {
    ArrayList<Finger> fingers;

    int lastValid = -1; //last valid pos
    int nextValid = 0; //next valid pos
    int inc; //direction
    int score;
    int scoreInc;
    int round;

    public Hand() {
        fingers = new ArrayList<Finger>();
        inc = 1; //going up
        score = 0;
        scoreInc = 10;
        round = 1;
    }

    public void addFinger(Finger f) {
        fingers.add(f);

    }

    public int fingerPos(Point p) {
      int pos = 0;


      for (Finger f : fingers) {
          if (f.pointIsLeft(p)) {
              return pos;
          }
          ++pos;


      }
      return pos;
    }

    public int fingerWrong(Point p) {
        int bestDiff = 999999;
        int bestFing = -1;

        int fingNum = 0;
        for (Finger f : fingers) {
            int fingAvX = Math.abs((f.base.x + f.nail.x) / 2);
            if ((Math.abs(fingAvX - p.x) < bestDiff)) {
                bestDiff = Math.abs(fingAvX - p.x);
                bestFing = fingNum;
            }
            ++fingNum;
        }
        return bestFing;
    }




    public boolean logFingerPress(int pos) {
        boolean correct = false;


        if (nextValid == pos) {
            correct = true;

            if (nextValid == 0) {
                if (lastValid == fingers.size()) {
                    lastValid = 0;
                    inc *=-1;
                    nextValid = fingers.size() -1;
                }
                else if ((lastValid == 1) && (inc == -1)) {
                    nextValid = lastValid;
                    lastValid = 0;
                    inc = 1;
                    ++round;

                }
                else if (lastValid == -1) { //first
                    nextValid = 1;
                    lastValid = 0;
                }
                else {
                    nextValid = lastValid + inc;
                    lastValid = 0;
                }
            }
            else {
                score+=scoreInc;
                lastValid = nextValid;
                nextValid = 0;
            }
        }

        return correct;

    }





}
