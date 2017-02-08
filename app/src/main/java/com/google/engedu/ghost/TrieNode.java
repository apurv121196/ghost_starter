package com.google.engedu.ghost;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode root=this;
        for(int i=0;i<s.length();i++)
        {
            if(root.children.get(Character.toString(s.charAt(i)))==null)
            {
                TrieNode temp = new TrieNode();
                root.children.put(Character.toString(s.charAt(i)), temp);
                root = temp;
            }
            else
            {
                root=root.children.get(Character.toString(s.charAt(i)));
            }
        }
        root.isWord=true;
        root.children.put("",null);
    }

    public boolean isWord(String s) {
        TrieNode root=this;
        for(int i=0;i<s.length();i++)
        {
            if(root.children.get(Character.toString(s.charAt(i)))==null)
            {
                Log.e("mytag","in isword() :- returning false for "+s);
                return false;
            }
            else
            {
                root=root.children.get(Character.toString(s.charAt(i)));
            }
        }
        if(root.children.get("")==null && root.isWord) {
            Log.e("mytag", "in isword() :- returning true for " + s);
            return true;
        }
        else return false;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode root=this;
        for(int i=0;i<s.length();i++)
        {
            if(root.children.get(Character.toString(s.charAt(i)))==null)return "";
            root=root.children.get(Character.toString(s.charAt(i)));
        }
        if(root.children.size()==0 && s.length()<4)return "";
        else
        {
            Log.e("mytag","starting with -->"+s);
            String ans = cal(root,s);
            Log.e("mytag",ans+"<-- got this one!");
            return ans;
        }

    }

    public String cal(TrieNode root,String s)
    {
        //Log.e("mytag",s+"<--here");
        boolean toss = (new Random()).nextBoolean();
        if( root==null || (toss==true && root.isWord))return s;
        ArrayList<String> arr = new ArrayList<String>(root.children.keySet());
        Random rand = new Random();
        int index = rand.nextInt(arr.size());
        Log.e("mytag","s was "+s);
        s+=arr.get(index);
        Log.e("mytag","now "+s);
        root=root.children.get(arr.get(index));
        return cal(root,s);
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
