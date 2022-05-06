package org.main;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


public class Main {
    static int Tweet = 0;
    static int Retweets = 1;
    static int Favorites = 2;
    static int Datetime = 3;
    static ArrayList<String> stopwordList;

    public static void main(String[] args) throws IOException {
        String pathHeader = "src/main/java/org/resource/";
        FileReader csvFile = new FileReader(pathHeader + "trump_20200530_clean.csv");
        File stopwordTweetFile = new File(pathHeader + "stopwords-tweet.txt");
        File stopwordEnglishFile = new File(pathHeader + "stopwords-en.txt");
        Scanner stopwordTweetReader = new Scanner(stopwordTweetFile);
        Scanner stopwordEnglishReader = new Scanner(stopwordEnglishFile);
        stopwordList = new ArrayList<>(Arrays.asList(stopwordTweetReader.nextLine().split(",")));
        while (stopwordEnglishReader.hasNextLine()) stopwordList.add(stopwordEnglishReader.nextLine());
        System.out.printf(stopwordList.toString());
        try (CSVReader reader = new CSVReader(csvFile)) {
            List<String[]> r = reader.readAll();
            for (String[] tweetData : r) {
                //Modify content
                String content = tweetData[Tweet];
                //Sanitize tweets
                content = cleanString(content);
                //Split string to list
                String[] rawWordList = content.split(" ");
                ArrayList<String> wordList = new ArrayList<String>();
                //Remove stop words
                for (String word : rawWordList)
                    if (!stopwordList.contains(word) && word.length()>0) wordList.add(word);

                System.out.println(wordList);
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
        commentstr = commentstr.replaceAll("[^a-zA-Z]"," ");
        commentstr = commentstr.toLowerCase();
        return commentstr;
    }
}
