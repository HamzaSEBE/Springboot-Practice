package com.example.Anagram.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
@Controller
public class AnagramController {
    @GetMapping("/Form1")
    public String form1(){

        return "Form1";
    }
    @PostMapping("/check")
    public String checkAnagram(@RequestParam String word1, @RequestParam String word2, Model model) {
        boolean isAnagram = isAnagram(word1, word2);
        model.addAttribute("word1", word1);
        model.addAttribute("word2", word2);
        model.addAttribute("result", isAnagram ? "The words are anagrams!" : "The words are NOT anagrams.");
        return "Form1";
    }

    private boolean isAnagram(String word1, String word2) {
        char[] arr1 = word1.replaceAll("\\s", "").toLowerCase().toCharArray();
        char[] arr2 = word2.replaceAll("\\s", "").toLowerCase().toCharArray();
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        return Arrays.equals(arr1, arr2);
    }
}













