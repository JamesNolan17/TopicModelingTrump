package org.main;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;

public class Tweet {
    public UUID id;
    public int clusterID;
    public ArrayList<String> wordList;
    public boolean hasBursty;

    public Tweet(ArrayList<String> wordList){
        this.id = UUID.randomUUID();
        this.clusterID = -1;
        this.wordList = wordList;
        this.hasBursty = false;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}

