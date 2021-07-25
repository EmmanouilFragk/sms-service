package gr.fragkiadakis.springmysql;

import java.util.Arrays;
import java.util.List;

// contains the functions for the first 3 questions
public class FunctionProvider {
    // 1st
    static String findSeven(int[] array) {
        StringBuilder sb = new StringBuilder();
        // convert int array to string
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
        }
        // if a number contains number seven will be included in concat string
        if (sb.toString().contains("7")) return "Found";
        else return "there is no 7 in the array";
    }

    // 2nd
    static int digitSum(int num) {
        //recursive until number contains only one digit
        String result = String.valueOf(num);
        int sum = 0;
        while (result.length() > 1) {

            char [] chars = result.toCharArray();
            //conversion to char to get numeric values
            for (int i = 0; i < chars.length; i++) {
                sum+= Character.getNumericValue(chars[i]);
            }
            result = String.valueOf(digitSum(sum));
        }

        return Integer.valueOf(result);

    }

    // 3rd
    static String doRemake(String words) {
        List<String> wordsList = Arrays.asList(words.split(" ", -1));
        //add all the punctuation marks you want.
        String punctuations = ".,:;";
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(String word: wordsList) {
            String punctuation= "";
            // if word contains punctuation move it to the end
            if (punctuations.contains(word.substring(word.length()-1))) {
                punctuation = word.substring(word.length()-1);
                word = word.substring(0, word.length()-1);
            }
            // vowel, add way to the end
            if (word.substring(0, 1).toLowerCase().matches("[aeiou]")) {

                wordsList.set(index, word + "way");
                sb.append(wordsList.get(index));
            }
            else {
                // starts with non vowel do the letter rotation
                String ans = word.substring(1) + word.substring(0, 1).toLowerCase();
                ans+= "ay";
                if (Character.isUpperCase(word.charAt(0))) {
                    String firstLetter = ans.substring(0,1);
                    String restWord = ans.substring(1);
                    ans = firstLetter.toUpperCase() + restWord;
                }
                sb.append(ans);
            }
            sb.append(punctuation + " ");
            index++;
        }
        return sb.toString();
    }
}
