package hr.fer.zpr.lumen.wordgame.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Letter;

public class LetterGenerator {

    Map<Language,List<String>> letters=new HashMap<>();

    public LetterGenerator(){
        letters.put(Language.CROATIAN, Arrays.asList("a","b","c","č","ć","d","đ","dž","e","f","g","h","i","j","k","l","lj","m","n","nj","o","p","r","s","š","t","u","v","z","ž"));
        letters.put(Language.ENGLISH,Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"));
    }

    public List<Letter>  getRandomLetters(int n,Language language){
        List<Letter> randomLetters=new ArrayList<>();
        List<String> alphabet=letters.get(language);
        Random random=new Random();
        for(int i=0;i<n;i++){
            randomLetters.add(new Letter(alphabet.get(random.nextInt(alphabet.size()))));
        }
        return randomLetters;
    }
}
