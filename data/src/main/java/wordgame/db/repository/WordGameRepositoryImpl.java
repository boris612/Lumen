package wordgame.db.repository;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import hr.fer.zpr.lumen.wordgame.Util.WordGameUtil;
import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Image;
import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.Sound;
import hr.fer.zpr.lumen.wordgame.model.Word;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
import io.reactivex.Single;
import wordgame.db.database.WordGameDatabase;
import wordgame.db.mapping.DataDomainMapper;
import wordgame.db.model.DbCategory;
import wordgame.db.model.DbCorrectMessage;
import wordgame.db.model.DbImage;
import wordgame.db.model.DbIncorrectMessage;
import wordgame.db.model.DbLetter;
import wordgame.db.model.DbLetterLanguageRelation;
import wordgame.db.model.DbWordLanguageRelation;

public class WordGameRepositoryImpl implements WordGameRepository {


    private WordGameDatabase database;

    public WordGameRepositoryImpl(WordGameDatabase database) {
        this.database = database;
    }

    @Override
    public Single<List<Word>> getAllWords(Language language) {
        DataDomainMapper mapper = new DataDomainMapper();
        List<Word> result = new ArrayList<>();
        int languageId = database.languageDao().findByValue(language.name()).id;
        List<DbWordLanguageRelation> words = database.wordLanguageRelationDao().getAllWordsForLanguage(languageId);
        for (DbWordLanguageRelation word : words) {
            List<DbCategory> categories = database.wordCategoriesRelationDao().getCategoriesForWord(word.wordId);
            DbImage image = database.imageDao().getImageForWord(word.wordId);
            List<Letter> letters = WordGameUtil.getWordBuilderFromLanguage(language).split(word.word);
            Sound sound = new Sound(word.soundPath);
            result.add(new Word(word.word, language, sound, mapper.DatabaseCategoriesToDomain(categories), mapper.DatabaseImageToDomain(image), getLettersWithImages(letters, language).blockingGet()));
        }
        return Single.just(result);
    }

    @Override
    public Single<List<Word>> getWordsFromCategories(Set<Category> categories, Language language) {
        return Single.just(buildWordsFromCategories(categories, language));
    }


    @Override
    public Single<List<Letter>> getRandomLettersForLanguage(Language language, int n) {
        List<DbLetter> letters = database.letterDao().getAllLettersForLanguage(database.languageDao().findByValue(language.name().toLowerCase()).id);
        List<Letter> result = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(letters.size());
            DbLetterLanguageRelation dblr=database.letterLanguageDao().findByLetterAndLanguage(letters.get(index).id,database.languageDao().findByValue(language.name().toLowerCase()).id);
            Sound sound = new Sound(dblr.soundPath);
            result.add(new Letter(letters.get(index).value, new Image(database.imageDao().getImageById(letters.get(index).imageId).path), sound));
            letters.remove(index);
        }
        return Single.just(result);
    }

    @Override
    public Single<List<Letter>> getAllLettersForLanguage(Language language){
        List<DbLetter> letters = database.letterDao().getAllLettersForLanguage(database.languageDao().findByValue(language.name().toLowerCase()).id);
        int alphabetSize=letters.size();
        List<Letter> result = new ArrayList<>();
        for(int i=0;i<alphabetSize;i++){
            DbLetterLanguageRelation dblr=database.letterLanguageDao().findByLetterAndLanguage(letters.get(i).id,database.languageDao().findByValue(language.name().toLowerCase()).id);
            Sound sound = new Sound(dblr.soundPath);
            result.add(new Letter(letters.get(i).value, new Image(database.imageDao().getImageById(letters.get(i).imageId).path), sound));
        }
        letters.removeAll(letters);
        if(new String("CROATIAN").equalsIgnoreCase(language.name())){
            Locale croatian = new Locale("hr", "HR");
            Collator collator = Collator.getInstance(croatian);
            Collections.sort(result, (c1, c2) -> collator.compare(c1.toString(), c2.toString()));
        }
        else Collections.sort(result, (l1, l2) -> l1.toString().compareTo(l2.toString()));

        return Single.just(result);
    }


    @Override
    public Single<List<Letter>> getLettersWithImages(List<Letter> letters, Language language) {
        List<Letter> result = new ArrayList<>();
        int languageId = database.languageDao().findByValue(language.name().toLowerCase()).id;
        for (Letter letter : letters) {
            DbLetter dbl = database.letterDao().getByValue(letter.value);
            DbLetterLanguageRelation dblr=database.letterLanguageDao().findByLetterAndLanguage(database.letterDao().getByValue(letter.value).id,languageId);
            Letter let = new Letter(dbl.value, new Image(database.imageDao().getImageById(dbl.imageId).path), new Sound(dblr.soundPath));
            result.add(let);
        }
        return Single.just(result);
    }

    private List<Word> buildWordsFromCategories(Set<Category> categories, Language language) {
        List<Integer> dbCategoryIds = new ArrayList<>();
        for (Category category : categories) {
            dbCategoryIds.add(database.categoryDao().findByValue(category.name().toLowerCase()).id);
        }
        int languageId = database.languageDao().findByValue(language.name().toLowerCase()).id;
        List<DbWordLanguageRelation> words = database.wordLanguageRelationDao().getWordsForCategories(dbCategoryIds, languageId);
        words = new ArrayList<>(new HashSet<>(words));
        List<Word> result = new ArrayList<>();
        for (DbWordLanguageRelation word : words) {
            List<DbCategory> dbcategories = database.wordCategoriesRelationDao().getCategoriesForWord(word.wordId);
            DbImage image = database.imageDao().getImageForWord(word.wordId);
            List<Letter> letters = WordGameUtil.getWordBuilderFromLanguage(language).split(word.word);
            Sound sound = new Sound(word.soundPath);
            result.add(new Word(word.word, language, sound, DataDomainMapper.DatabaseCategoriesToDomain(dbcategories), DataDomainMapper.DatabaseImageToDomain(image), getLettersWithImages(letters, language).blockingGet()));
        }
        return new ArrayList<>(new HashSet<>(result));
    }

    @Override
    public Single<String> incorrectMessage(Language language) {
        List<DbIncorrectMessage> messages = database.incorrectDao().getMessages(database.languageDao().findByValue(language.name().toLowerCase()).id);
        Random r = new Random();
        return Single.just(messages.get(r.nextInt(messages.size())).path);
    }

    @Override
    public Single<String> getCorrectMessage(Language language) {
        List<DbCorrectMessage> messages = database.correctDao().getMessages(database.languageDao().findByValue(language.name().toLowerCase()).id);
        Random r = new Random();
        return Single.just(messages.get(r.nextInt(messages.size())).path);
    }
}
