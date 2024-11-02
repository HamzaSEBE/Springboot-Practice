package com.hamzah.learn;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Anagram {
    static Scanner input = new Scanner(System.in);
    static String word1;
    static String word2;
    public static void main(String[] args) {
        DrawMenu();
    }


    public static void DrawMenu() {
        try {
            System.out.println(" enter your choice");
            System.out.println("1. CheckAnagram");
            System.out.println("2. test file anagram");
            System.out.println("3. test json Anagram");
            System.out.println("4. quit");
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    startOperation(Anagram::CheckAnagram);
                    break;
                case 2:
                    FileAnagram file = new FileAnagram();
                    file.read();;
                    break;
                case 3:
                    JSONAnagram jsonAnagram= new JSONAnagram();
                    jsonAnagram.read();
                    break;
                default:
                    break;
            }

        } catch (NoSuchElementException | IOException e) {
            System.out.println("================= re enter A valid value please =================\n");
            input.nextLine();
            DrawMenu();
        }
    }
    private static void startOperation(Runnable runnable) {
        // Let user input the two words
        inputWords();

        // Start count operation time
        long startTime = System.nanoTime();

        // Run the passed check method
        runnable.run();

        // Calculate operation time
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Time: " + (totalTime/1000) + "mS");
    }

    private static void inputWords() {
        System.out.println("Enter the first word");
        word1 = input.next();

        System.out.println("Enter the second word");
        word2 = input.next();

        if (word1.length() < 2 || word2.length() < 2) {
            System.out.println("Error: please the word must be 2 letters or more");
            System.exit(0);
        }

        if (word1.length() != word2.length()) {
            System.out.println("========= Not Anagram =========");
            System.exit(0);
        }
    }

    public static void CheckAnagram() {
        Map<Character, Integer> firstWordMap = new HashMap<>();
        for (char ch : word1.toCharArray()) {
            Integer value = firstWordMap.get(ch);
            firstWordMap.put(ch, value == null ? 1 : value++);
        }

        Map<Character, Integer> secondWordMap = new HashMap<>();
        for (char ch : word2.toCharArray()) {
            Integer value = secondWordMap.get(ch);
            secondWordMap.put(ch, value == null ? 1 : value++);
        }

        System.out.println(firstWordMap);
        System.out.println(secondWordMap);

        if (firstWordMap.equals(secondWordMap)) {
            System.out.println("====== Anagram ====== ");
        } else {
            System.out.println("====== Not Anagram ====== ");
        }
    }
}
