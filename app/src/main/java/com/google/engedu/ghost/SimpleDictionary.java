package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }


    @Override
    public String getAnyWordStartingWith(String prefix) {

        if(prefix.equals(""))
        {
            return words.get((new Random()).nextInt(words.size()));
        }
        else
        {
            int index=BinarySearch(words,prefix,0,words.size()-1);
            if(index==-1)return "";
            else {
                int x_left=-1;
                int x_right=-1;
                int tempp=index;
                while (tempp>-1 && words.get(tempp).length()>prefix.length() &&
                        words.get(tempp).substring(0,prefix.length()).equals(prefix))tempp--;
                x_left=tempp+1;
                tempp=index;
                while (tempp<words.size() && words.get(tempp).length()>prefix.length() &&
                        words.get(tempp).substring(0,prefix.length()).equals(prefix))tempp++;
                x_right=tempp-1;
                Random rand=new Random();
                int ind = rand.nextInt(x_right-x_left)+x_left;
                return words.get(ind);

            }
        }
    }

    public int BinarySearch(ArrayList<String> words,String prefix,int start,int end)
    {
        if(start>end)return -1;
        int mid=start+(end-start)/2;
        String tmp=words.get(mid);
        if(tmp.length()>prefix.length() && tmp.substring(0,prefix.length()).equals(prefix)) return mid;
        else if(tmp.compareTo(prefix)>0) return BinarySearch(words,prefix,start,mid-1);
        else if(tmp.compareTo(prefix)<0) return BinarySearch(words,prefix,mid+1,end);
        else if(tmp.compareTo(prefix)==0) return -1;
        return -1;
    }


    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
