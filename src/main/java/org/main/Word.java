package org.main;

public class Word {
    public String value;
    public int frequency;
    public int windowNumber;
    public double tfidf;
    public boolean isBursty;

    public Word(String value){
        this.value = value;
        this.frequency = 0;
        this.windowNumber = 0;
        this.tfidf = 0;
        this.isBursty = false;
    }

    public void gotMatch() {
        this.frequency++;
    }
}
