package com.mycompany.stringdata;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class StringDataParser
{
    String data;
    String newString;
    char[] vowels = {'a', 'A', 'e', 'E', 'i', 'I', 'o', 'O', 'u', 'U'};
    ArrayList<String> wordList;
    public StringDataParser()
    {
        data = " ";
        newString = " ";        
    }
    public StringDataParser(String input)
    {
        data = input;
        newString = " ";
    }
    public String returnString()
    {
        return data;
    }
    public void setString(String input)
    {
        data = input;
    }
    public String reverseString()
    {
        newString = " ";
        int j = data.length()-1;
        for (int i = 0; i < data.length(); i++)
        {
            newString = newString + data.charAt(j);
            j--;
        }
        return newString;
    }
    public String pigLatin()
    {
        newString = " ";
        wordList = seperateIntoWords(data);
        for (int i = 0; i < wordList.size(); i++)
        {
            wordList.set(i, latinEncode(wordList.get(i)));
        }
        return newString;
    }
    
    private ArrayList<String> seperateIntoWords(String in)
    //called from pigLatin() Ln 42 & countWords() Ln 164      
    {
        ArrayList<String> list = new ArrayList();
        in = in.replaceAll("( +)", " ").trim();
        int x = 0;  
        int start = 0;
        int end = 0;
        for (int i = 0; i < in.length(); i++)
        {
            if (in.charAt(i) == ' ')
            {
                end = i; 
                list.add(in.substring(start, end));
                start = i + 1;
            }
        }
        list.add(in.substring(end, in.length()));
        return list;
    }
    
    private String latinEncode(String in)
    //called from pigLatin() Ln 45                       
    {
        newString = in;
        boolean hasVowelStart = false;
        char first = newString.charAt(0);
        /*Check the first letter in the string for a vowel as a vowel start has a different rule for pig latin to consonant*/
        for (int i = 0; i < vowels.length; i++)
        {
            if (!hasVowelStart)
            /*once a vowel has been detected and parsed - break needed */
            {
                if (first == vowels[i])
                {
                    newString = vowelStart(in);
                    hasVowelStart = true;
                }
            }
        }
        /* if hasVowelStart = false at this point then consonant encoding needed */
        if (!hasVowelStart)
        {
            newString = constStart(in);
        }        
        return newString;
    }
    
    private String vowelStart(String in)
    //called from latinEncode() Ln 80                    
    {
        newString = in.concat("-hay");
        return newString;
    }
    
    private String constStart(String in)
    //called from latinEncode() Ln 88                    
    {
        boolean hasVowelSecond = false;
        if (in.length() > 1)            /*try - catch for when word is 1 character long */
        {
            for (int i = 1; i < vowels.length; i++)
            {
                if (!hasVowelSecond)
                {
                    if (in.charAt(1) == vowels[i])
                    {
                        newString = in.concat(in.charAt(0) + "ay");
                        newString = newString.substring(1);
                        hasVowelSecond = true;
                    }
                }
            }
            if (!hasVowelSecond)
            {
                newString = in.concat(in.charAt(0) + "");
                newString = newString.concat(in.charAt(1) + "ay");
                newString = newString.substring(2);
            }
        }
        else
        {
            newString = in.concat("ay");
        }
        return newString;
    }
    public int[] countVowels()
    {
        int[] count = {0, 0, 0, 0, 0};
        for (int i = 0; i < data.length(); i++)
        {
            switch (data.charAt(i))
            {
                case 'a':
                case 'A':
                    count[0] = count[0] + 1;
                    break;
                case 'e':
                case 'E':
                    count[1] = count[1] + 1;
                    break;
                case 'i':
                case 'I':
                    count[2] = count[2] + 1;
                    break;
                case 'o':
                case 'O':
                    count[3] = count[3] + 1;
                    break;
                case 'u':
                case 'U':
                    count[4] = count[4] + 1;
                    break;
                default:
                    break;
            }
        }
        return count;
    }
    public int countWords()
    {
        wordList = seperateIntoWords(data);
        return wordList.size();
    }
    public boolean palindrome(String s)
    {
        //if length is 0 or 1 then palindrome
        if (s.length() < 2)      
        {
            return true;
        }
        /*if condidition for palindrome still true then recursively check next
        characters in string
        */
        if (s.charAt(0) == s.charAt(s.length()-1))
        {
            return palindrome(s.substring(1,s.length()-1));
        }
        return false;
    }
    public boolean fileCheck(String path)
    {
        String isFileValid = path.toLowerCase();
        return isFileValid.endsWith(".txt");
    }
    public ArrayList<String> readFile(String path, boolean selected) throws IOException
    {
        ArrayList<String> list = new ArrayList();
        /*
        first read line, then until it cant read anymore lines (page end)
        then iterate by reading next line
         */
        try (BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            /*
            first read line, then until it cant read anymore lines (page end)
            then iterate by reading next line
            */
            for (String line = reader.readLine(); line != null;
                    line = reader.readLine())
            {
                list.add(line + "\n");
            }
        }
        if (selected)
        {
            list = highLightSyntax(list);
        }
        return list;
    } 
    public ArrayList<String> highLightSyntax(ArrayList<String> list)
    {
        ArrayList<ArrayList> terms = syntaxTerms();
        ArrayList<String> parsedLines = new ArrayList();
        String temp = "";
        for (String eachLine : list)
        {
            for (int j = 0; j < terms.size(); j++)
            {
                eachLine = parseFileLine(eachLine, terms.get(j), j);
            }
            parsedLines.add(eachLine);
        }
        return parsedLines;
    }
    public ArrayList<ArrayList> syntaxTerms()
    {
        ArrayList<ArrayList> list = new ArrayList();
        ArrayList<String> colourList = new ArrayList();
        colourList.add("public");
        colourList.add("private");
        colourList.add("class");
        colourList.add("void");
        colourList.add("static");
        colourList.add("return");
        list.add(colourList);
        colourList = new ArrayList();
        colourList.add("int ");
        colourList.add("char ");
        colourList.add("double ");
        colourList.add("boolean ");
        colourList.add("String ");
        list.add(colourList);
        colourList = new ArrayList();
        colourList.add("\"");
        colourList.add("'");
        list.add(colourList);
        return list;
    }
    public String parseFileLine(String l, ArrayList<String> syn, int i)
    {      
        String temp = l.toLowerCase();
        for (String syntax: syn)
        {
            if (temp.contains(syntax))
            {
                switch (i)
                {
                    case 0 -> {
                        l = l.replaceAll(syntax, "--" + syntax + "--");
                        break;
                    }
                    case 1 ->
                    {
                        l = l.replaceAll(syntax, "%%" + syntax + "%%");
                        break;
                    }
                    case 2 ->
                    {
                        l = l.replaceAll(syntax, "++" + syntax + "++");
                        break;
                    }
                }
            }            
        }
        return l;
    }
    private String variableSyntax(String line, String syn)
    {
        line = line.strip();
        String[] l = line.split(" ");
        System.out.println(l[0] + l[1]);
        return line;        
    }
    public void saveFile(ArrayList<String> list, String path) throws IOException
    {
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(path));
        for (int i = 0; i < list.size(); i++)
        {
            writer.write(list.get(i) + "\n");
        }
        writer.close();
    }    
}
