package com.hendrixer.liferpg.model;

/**
 * Created by Hendrixer on 12/25/14.
 */
public class Choice {
    private String mText;
    private int mNextPage;


    public Choice(String text, int nextPage){
        mText = text;
        mNextPage = nextPage;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getNextPage() {
        return mNextPage;
    }

    public void setNextPage(int nextPage) {
        this.mNextPage = nextPage;
    }

}
