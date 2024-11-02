package com.hamzah.learn;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JSONAnagram {


    public void read() throws FileNotFoundException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.txt");
        Scanner scan = new Scanner(is);


        List<AnagramResult> results = new ArrayList<>();

        while (scan.hasNext()) {
            String anagram1 = scan.next().toLowerCase();
            String anagram2 = scan.next().toLowerCase();


            Map<Character, Integer> s1 = createFrequencyMap(anagram1);
            Map<Character, Integer> s2 = createFrequencyMap(anagram2);


            Boolean isAnagram = s1.equals(s2);


            results.add(new AnagramResult(List.of(anagram1, anagram2), isAnagram));

            // Print result to console (optional)
            if (isAnagram) {
                System.out.println(anagram1 + " " + anagram2 + " ====== this is Anagram ====== \n");
            } else {
                System.out.println(anagram1 + " " + anagram2 + " ====== this is Not Anagram ====== \n");
            }
        }
        scan.close();


        writeResultsToJson(results, "IsAnagram.json");
    }

    public Map<Character, Integer> createFrequencyMap(String word) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : word.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    public void writeResultsToJson(List<AnagramResult> results, String isAnagram) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(isAnagram), results);
    }



}