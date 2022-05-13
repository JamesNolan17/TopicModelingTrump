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

    public void updateFreq() {
        this.frequency++;
    }
    public void updateWindowNum(){ this.windowNumber++; }
    public void updateTfidf(int windowSize, int frequency, int totalWindowNum){
        if (this.frequency == 0) return;
        this.frequency = frequency;
        this.tfidf = ((double)frequency / (double)windowSize) * (-Math.log((double)windowNumber / (double)totalWindowNum));
    }

    public void clearFreq() {
        this.frequency = 0;
    }

    @Override
    public String toString() {
        return String.format("%s - freq%d - window%d - tfidf%f", this.value, this.frequency, this.windowNumber, this.tfidf);
    }
}
