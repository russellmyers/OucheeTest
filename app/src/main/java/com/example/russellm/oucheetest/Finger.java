package com.example.russellm.oucheetest;

import android.graphics.Point;

/**
 * Created by RussellM on 1/06/2016.
 */
public class Finger {

    Point nail;
    Point base;
    float m;
    float c;

    public Finger(Point inBase,Point inNail) {
        nail = inNail;
        base = inBase;
        if (nail.x == base.x) {
            m = 99999;
            c = 0;
        }
        else {
            m = 1.0f * (nail.y - base.y) / (nail.x - base.x);
            c = base.y - (m * base.x);
        }


    }

    public String toString() {
        return "base: " + base.toString() + " nail: " + nail.toString() + " m: " + m + " c: " + c;
    }

    public boolean pointIsLeft(Point p) {
        if (m == 99999) {
            return (p.x <= base.x);
        }
        else {
            if (m > 0) { //left on top
                return (p.y >= p.x * m + c);

            }
            else {
                return (p.y <= p.x * m + c);

            }
        }

    }
}
