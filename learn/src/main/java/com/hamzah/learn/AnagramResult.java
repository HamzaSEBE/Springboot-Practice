package com.hamzah.learn;
import java.util.List;

public class AnagramResult {
    private List<String> words;
    private Boolean isAnagram;


    public AnagramResult() {}

    public AnagramResult(List<String> words, Boolean isAnagram) {
        this.words = words;
        this.isAnagram = isAnagram;
    }


    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public Boolean getIsAnagram() {
        return isAnagram;
    }

    public void setIsAnagram(Boolean isAnagram) {
        this.isAnagram = isAnagram;
    }
}
