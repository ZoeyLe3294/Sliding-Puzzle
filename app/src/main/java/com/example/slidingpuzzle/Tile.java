package com.example.slidingpuzzle;

import android.graphics.Bitmap;

public class Tile {
    Bitmap puzzlepartImage;
    int correctPosition;

    public Tile(Bitmap puzzlepartImage, int correctPosition) {
        this.puzzlepartImage = puzzlepartImage;
        this.correctPosition = correctPosition;
    }

    public Bitmap getPuzzlepartImage() {
        return puzzlepartImage;
    }

    public void setPuzzlepartImage(Bitmap puzzlepartImage) {
        this.puzzlepartImage = puzzlepartImage;
    }

    public int getCorrectPosition() {
        return correctPosition;
    }

    public void setCorrectPosition(int correctPosition) {
        this.correctPosition = correctPosition;
    }
}
