package hr.fer.zpr.lumen;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zpr.lumen.database.DBHelper;
import hr.fer.zpr.lumen.math.ProbabilityDistribution;


/**
 * Created by Kristijan on 8.11.2017.
 */

public class WordSupply {
    int currentWordId;
    private DBHelper helper;
    //private List<Integer> wordIds;
    private String language;
    private Random rand;
    private ProbabilityDistribution wordProbDistr;
    private double PROBABILITY_SCALE_FACTOR = 0.8;

    public WordSupply(Context context, String lang, Set<String> cat) {
        helper = new DBHelper(context);
        language = lang;

        Set<Integer> wordIds = new TreeSet<>();
        for (String s : cat) {
            for (int id : helper.getWords(lang, s)) {
                wordIds.add(id);
            }
        }

        wordProbDistr = new ProbabilityDistribution();
        for (int id : wordIds) {
            wordProbDistr.addChoice(id);
        }
        rand = new Random();
        goToNext();
   }

    public LangDependentString getWord() {
        return new CroatianString(helper.getWord(currentWordId));
    }

    public String getImagePath() {
        return helper.getWordImagePath(currentWordId);
    }

    public String getWordRecordingPath() {
        return helper.getWordSoundPath(currentWordId);
    }

    public List<String> getLettersRecordingPaths() {
        LangDependentString letters = getWord().toUpperCase();

        List<String> paths = new ArrayList<>();

        for (int i=0;i<letters.length();i++){
            paths.add(helper.getLetterSoundPath(letters.charAt(i).toUpperCase(),language));
        }
        LogUtil.d("PATHS",paths.toString());
        return paths;
    }

    public void goToNext() {
        double selection = rand.nextDouble();
        LogUtil.d("DISTR","selection: "+Double.toString(selection));
        for(ProbabilityDistribution.DistributionInterval interval: wordProbDistr.getDistributionAsIntervalCollection()) {
            if(interval.getIntervalStart() <= selection && interval.getIntervalEnd() >= selection) {
                currentWordId = (int)interval.getIntervalChoice();
                LogUtil.d("DISTR","selection id: "+currentWordId);
                wordProbDistr.increaseChoiceProbabilityByScaleFactor(interval.getIntervalChoice(), PROBABILITY_SCALE_FACTOR);
                return;
            }
        }
    }

}
