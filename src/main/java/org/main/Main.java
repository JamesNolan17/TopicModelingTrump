package org.main;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


public class Main {
    //Position of elements in each tweetData
    static int Tweet = 0;
    static int Retweets = 1;
    static int Favorites = 2;
    static int Datetime = 3;

    //Configurable Variables
    static int roundSize = 50; //Size of batch to calculate tf-idf
    static String pathHeader = "src/main/java/org/resource/";

    public static void main(String[] args) throws IOException {
        //Variable global
        List<String[]> CSVmetaData;
        ArrayList<String> stopwordList;
        HashMap<String, Word> wordHashMap = new HashMap<>();
        HashMap<UUID, Tweet> tweetHashMap = new HashMap<>();
        HashMap<String, Integer> windowMap = new HashMap<>(); //Only word - frequency

        int tweetIndex = 0;
        int roundIndex = 1;
        //Load CSV File
        FileReader csvFile = new FileReader(pathHeader + "trump_20200530_clean.csv");
        //Load stopword File
        File stopwordTweetFile = new File(pathHeader + "stopwords-tweet.txt");
        File stopwordEnglishFile = new File(pathHeader + "stopwords-en.txt");
        //Create stopword table
        Scanner stopwordTweetReader = new Scanner(stopwordTweetFile);
        Scanner stopwordEnglishReader = new Scanner(stopwordEnglishFile);
        stopwordList = new ArrayList<>(Arrays.asList(stopwordTweetReader.nextLine().split(",")));
        while (stopwordEnglishReader.hasNextLine()) stopwordList.add(stopwordEnglishReader.nextLine());
        //Read tweetData CSV table
        try (CSVReader reader = new CSVReader(csvFile)) {
            CSVmetaData = reader.readAll();
            for (String[] tweetData : CSVmetaData) {
                //Variable for each tweet
                String[] rawWordList;
                ArrayList<String> wordList;
                /* Stage 1: Pre-Processing */
                //Modify content
                String content = tweetData[Tweet];
                //Sanitize tweets
                content = cleanString(content);
                //Lemmatize tweets & split string to list
                //StanfordLemmatizer slem = new StanfordLemmatizer();
                rawWordList = content.split(" ");
                //List<String> rawWordList = slem.lemmatize(content);
                wordList = new ArrayList<>();
                //Remove stop words
                for (String word : rawWordList)
                    if (!stopwordList.contains(word) && word.length() > 0) wordList.add(word);
                //Put this tweet inside the tweet map
                Tweet tweet = new Tweet(wordList);

                System.out.println(tweet);
                tweetHashMap.put(tweet.id,tweet);

                /* Stage 2: Maintain hashMaps */
                for (String word: wordList){
                    if (!wordHashMap.containsKey(word)) wordHashMap.put(word,new Word(word));
                    if (!windowMap.containsKey(word)) windowMap.put(word, 0);
                    //Update frequency
                    windowMap.put(word, windowMap.get(word)+1);
                }


                /* Stage 2: Compute TFIDF */
                if (tweetIndex % roundSize == 0){
                    for (String word: windowMap.keySet()){
                        wordHashMap.get(word).updateWindowNum();
                        wordHashMap.get(word).updateTfidf(roundSize, windowMap.get(word),roundIndex);
                        System.out.println(roundIndex);
                        System.out.println(wordHashMap.get(word));
                    }
                }

                System.out.println(wordList);
                //Increment index
                tweetIndex++;
                if (tweetIndex % roundSize == 0) {
                    roundIndex++;
                    for (String word: wordHashMap.keySet()) {
                        wordHashMap.get(word).clearFreq();
                        windowMap.remove(word);
                    }
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public static String cleanString(String commentstr) {
        //Remove URL
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i), "").trim();
            i++;
        }

        //Remove symbols and numbers
        commentstr = commentstr.replaceAll("[^a-zA-Z]", " ");
        commentstr = commentstr.toLowerCase();
        return commentstr;
    }
}


