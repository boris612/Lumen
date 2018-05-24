package wordgame.db.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

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
import wordgame.db.model.DbImage;
import wordgame.db.model.DbLetter;
import wordgame.db.model.DbWord;

public class WordGameRepositoryImpl implements WordGameRepository {


    private WordGameDatabase database;

    public WordGameRepositoryImpl(WordGameDatabase database){
        this.database=database;
    }

    @Override
    public Single<List<Word>> getAllWords(Language language) {
        DataDomainMapper mapper=new DataDomainMapper();
        List<Word> result=new ArrayList<>();
        int languageId=database.languageDao().findByValue(language.name()).id;
        List<DbWord> words=database.wordDao().getAllWordsForLanguage(languageId);
        for(DbWord word:words){
            List<DbCategory> categories=database.wordCategoriesRelationDao().getCategoriesForWord(word.id);
            DbImage image=database.wordImageRelationDao().getImageForWord(word.id);
            List<Letter> letters=WordGameUtil.getWordBuilderFromLanguage(language).split(word.word);
            Sound sound=new Sound(database.wordSoundRelationDao().getByWordAndLanguage(word.id,languageId).path);
            result.add(new Word(word.word,language,sound,mapper.DatabaseCategoriesToDomain(categories),mapper.DatabaseImageToDomain(image),getLettersWithImages(letters,language).blockingGet()));
        }
        return Single.just(result);
    }

    @Override
    public Single<List<Word>> getWordsFromCategories(Set<Category> categories, Language language) {
        return Single.just(buildWordsFromCategories(categories,language));
    }



    @Override
    public Single<List<Letter>> getRandomletters(Language language,int n) {
        List<DbLetter> letters=database.letterDao().getAllLetters();
        List<Letter> result=new ArrayList<>();
        Random random=new Random();
        for(int i=0;i<n;i++){
            int index=random.nextInt(letters.size());
            Sound sound=new Sound(database.letterSoundRelationDao().findByLetterAndLanguage(letters.get(index).id,database.languageDao().findByValue(language.name().toLowerCase()).id).soundPath);
            result.add(new Letter(letters.get(index).value,new Image(letters.get(index).imagePath),sound));
            letters.remove(index);
        }
        return Single.just(result);
    }

    @Override
    public Single<List<Letter>> getLettersWithImages(List<Letter> letters,Language language) {
        List<Letter> result=new ArrayList<>();
        DataDomainMapper mapper=new DataDomainMapper();
        int languageId=database.languageDao().findByValue(language.name().toLowerCase()).id;
        for(Letter letter:letters){
            DbLetter dbl=database.letterDao().getByValue(letter.value);
            Letter let=new Letter(dbl.value,new Image(dbl.imagePath),new Sound(database.letterSoundRelationDao().findByLetterAndLanguage(dbl.id,languageId).soundPath));
            result.add(let);
        }
        return Single.just(result);
    }

    private List<Word> buildWordsFromCategories(Set<Category> categories,Language language){
        List<Integer> dbCategoryIds=new ArrayList<>();
        for(Category category:categories){
            dbCategoryIds.add(database.categoryDao().findByValue(category.name().toLowerCase()).id);
        }
        int languageId=database.languageDao().findByValue(language.name().toLowerCase()).id;
        List<DbWord> words=database.wordDao().getWordsForCategories(dbCategoryIds,languageId);
        words=new ArrayList<>(new HashSet<>(words));
        List<Word> result=new ArrayList<>();
        for(DbWord word:words){
            List<DbCategory> dbcategories=database.wordCategoriesRelationDao().getCategoriesForWord(word.id);
            DbImage image=database.wordImageRelationDao().getImageForWord(word.id);
            List<Letter> letters= WordGameUtil.getWordBuilderFromLanguage(language).split(word.word);
            Sound sound=new Sound(database.wordSoundRelationDao().getByWordAndLanguage(word.id,languageId).path);
            result.add(new Word(word.word,language,sound,DataDomainMapper.DatabaseCategoriesToDomain(dbcategories),DataDomainMapper.DatabaseImageToDomain(image),getLettersWithImages(letters,language).blockingGet()));
        }
        return new ArrayList<>(new HashSet<>(result));
    }

}
