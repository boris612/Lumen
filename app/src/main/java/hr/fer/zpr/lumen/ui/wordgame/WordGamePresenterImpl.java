package hr.fer.zpr.lumen.ui.wordgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import hr.fer.zpr.lumen.dagger.application.LumenApplication;
import hr.fer.zpr.lumen.player.SoundPlayer;
import hr.fer.zpr.lumen.ui.DebugUtil;
import hr.fer.zpr.lumen.ui.viewmodels.CoinModel;
import hr.fer.zpr.lumen.ui.wordgame.mapping.WordGameMapper;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterFieldModel;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterModel;
import hr.fer.zpr.lumen.ui.wordgame.models.StartingHintModel;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCategoriesUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeLanguageUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.InsertLetterInPositionUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.IsCreateAllLettersActiveUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.IsCreateMoreLettersActiveUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.RemoveLetterFromFieldUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.SetCreateAllLettersUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.SetCreateMoreLettersUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.StartGameUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.UseHintUseCase;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.manager.WordGamePhase;
import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.Word;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import wordgame.db.database.WordGameDatabase;
import wordgame.db.mapping.DataDomainMapper;

public class WordGamePresenterImpl implements WordGamePresenter {


    private static String CATEGORIES_NAME = "categories";

    private static String LANGUAGE = "language";
    @Inject
    WordGameManager manager;
    @Inject
    StartGameUseCase startGameUseCase;
    @Inject
    RemoveLetterFromFieldUseCase removeLetterFromFieldUseCase;
    @Inject
    InsertLetterInPositionUseCase insertLetterInPositionUseCase;
    @Inject
    IsCreateMoreLettersActiveUseCase isCreateMoreLettersActiveUseCase;
    @Inject
    SetCreateMoreLettersUseCase setCreateMoreLettersUseCase;
    @Inject
    IsCreateAllLettersActiveUseCase isCreateAllLettersActiveUseCase;
    @Inject
    SetCreateAllLettersUseCase setCreateAllLettersUseCase;
    @Inject
    ChangeLanguageUseCase changeLanguageUseCase;
    @Inject
    ChangeCategoriesUseCase changeCategoriesUseCase;
    @Inject
    SharedPreferences preferences;
    @Inject
    WordGameDatabase database;
    @Inject
    WordGameRepository repository;
    @Inject
    SoundPlayer player;
    @Inject
    UseHintUseCase useHintUseCase;
    @Inject
    WordGameMapper mapper;
    private List<Disposable> disposables = new ArrayList<>();
    private WordGameView view;


    private Context context;

    private Word currentWord;

    private List<LetterModel> letters;

    private List<LetterFieldModel> fields;

    private CoinModel coin;

    private Map<String, String> fieldLetter = new HashMap<>();

    public WordGamePresenterImpl(LumenApplication application) {
        application.getApplicationComponent().inject(this);
        this.context = application;
        manager.setCoins(preferences.getInt(ViewConstants.PREFERENCES_COINS, 0));

    }

    @Override
    public void presentWord(Word word) {
        try {
            if (letters != null) letters.clear();

            view.addDrawable(mapper.getImage(word));
        } catch (Exception e) {
            DebugUtil.LogDebug(e);
        }
        StartingHintModel model = mapper.hintModel(word);
        view.addDrawable(model);
        //if(manager.isCreateAllLettersActive().blockingGet()) view.getScrollView().setEnabled(true);
        presentHint(model);
    }


    private void presentHint(StartingHintModel model) {
        disposables.add(Observable.interval(ViewConstants.TIME_BETWEEN_LETTERS, TimeUnit.MILLISECONDS)
                .takeWhile(e -> model.hasLettersToShow())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        f -> {
                            playSound(model.getCurrentLetterSoundPath());
                            model.showNextLetter();
                        }
                        , g -> {
                        }, () -> {
                            playSound(currentWord.sound.path);
                            disposables.add(Observable.interval(100, TimeUnit.MILLISECONDS)
                                    .takeWhile(j -> player.isPlaying())
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .subscribe(k -> {
                                    }, l -> {
                                    }, () -> {
                                        view.removeDrawable(model);
                                        manager.changePhase(WordGamePhase.PLAYING);
                                        showLetters();
                                    }));

                        }));


    }


    public void showLetters() {
        fields = mapper.createFields(currentWord);
        view.addFields(fields);
        int numToGenerate;
        if (currentWord.letters.size() + ViewConstants.MORE_LETTERS_TO_CREATE > ViewConstants.MAX_LETTER_NUMBER) {
            numToGenerate = ViewConstants.MAX_LETTER_NUMBER - currentWord.letters.size();
        } else {
            numToGenerate = ViewConstants.MORE_LETTERS_TO_CREATE;
        }
        List<Letter> randomLetters = null;
        if (manager.isCreateMoreLettersActive().blockingGet()) {
            randomLetters = manager.getRandomLetters(numToGenerate).blockingGet();
            letters = mapper.mapLetters(currentWord, randomLetters);
        }
        else if (manager.isCreateAllLettersActive().blockingGet()) {
            randomLetters = manager.getAllLetters().blockingGet();
            letters = mapper.mapAllLetters(currentWord, randomLetters);
            view.addAllLetters(letters);
            return;
        }else{
            letters = mapper.mapLetters(currentWord, randomLetters);
        }
        view.addLetters(letters);


    }

    @Override
    public void startGame() {
        Set<Category> categories = getCategories();
        String language = preferences.getString(LANGUAGE, ViewConstants.DEFAULT_LANGUAGE);
        Single<Word> single = startGameUseCase.execute(new StartGameUseCase.Request(categories, new DataDomainMapper().languageFromString(language)));
        Word word = single.blockingGet();
        currentWord = word;
        presentWord(word);
    }

    private Set<Category> getCategories() {
        Set<Category> all = new HashSet<>();
        all.addAll(Arrays.asList(Category.ANIMALS, Category.FOOD, Category.FRUIT, Category.ITEMS, Category.NATURE, Category.TRANSPORT));
        Set<String> categories = preferences.getStringSet(CATEGORIES_NAME, null);
        if (categories == null) {
            return all;
        }

        return DataDomainMapper.categoriesFromStrings(categories);

    }

    public void setView(WordGameView view) {
        this.view = view;
        coin=mapper.getCoinModel(preferences.getInt(ViewConstants.PREFERENCES_COINS,0));
        view.setCoin(coin);
    }

    private void playSound(String path) {
            player.play(path);
    }

    @Override
    public boolean shouldHandleTouch() {
        if (manager.isGamePhasePlaying().blockingGet()) return true;
        return false;
    }

    @Override
    public void letterInserted(LetterModel letter, LetterFieldModel field) {
        boolean correct = insertLetterInPositionUseCase.execute(new InsertLetterInPositionUseCase.Params(new Letter(letter.getValue()), fields.indexOf(field))).blockingGet();

        /*//FLASH GREEN ON CORRECT
        if (correct && manager.isHintOnCorrectOn().blockingGet()) {
                field.setColor(Color.GREEN);
                disposables.add(Completable.timer(500, TimeUnit.MILLISECONDS.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(() -> {if(manager.isGamePhasePlaying().blockingGet())field.setColor(Color.RED);}));
        }*/

        if(correct) fieldLetter.put(field.toString(),letter.getValue());

        //MARK GREEN RIGHT AWAY AFTER ADDING THE LETTER
        if(correct && manager.isHintInstantlyOn().blockingGet()) field.setColor(Color.GREEN);

        if (manager.areAllFieldsFull().blockingGet()) {
            if (manager.isAnswerCorrect().blockingGet()) {
                manager.changePhase(WordGamePhase.ENDING);
                coin.setCoins(manager.getCoins().blockingGet());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(ViewConstants.PREFERENCES_COINS, manager.getCoins().blockingGet());
                editor.commit();
                view.addDrawable(mapper.getTick());

                for (LetterFieldModel f : fields) f.setColor(Color.GREEN);
                try {
                    disposables.add(Completable.timer(3000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(() -> nextWord()));
                } catch (Exception e) {
                    DebugUtil.LogDebug(e);
                }
            }
            //MARK GREEN CORRECT LETTERS
            else if(manager.isHintWhenFullOn().blockingGet()){
                for (LetterFieldModel f : fields)
                    if(f.getLetterInside().getValue().equals(fieldLetter.get(f.toString())))
                        f.setColor(Color.GREEN);


            }
        }
    }

    @Override
    public void letterRemoved(LetterFieldModel field) {
        removeLetterFromFieldUseCase.execute(fields.indexOf(field)).blockingGet();
        field.setColor(Color.RED);
    }

    @Override
    public void hintPressed() {
        if (manager.isHintActive().blockingGet()) return;

        UseHintUseCase.Result result = useHintUseCase.execute().blockingGet();
        if (!result.canActivate) return;
        coin.setCoins(manager.getCoins().blockingGet());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ViewConstants.PREFERENCES_COINS, manager.getCoins().blockingGet());
        editor.commit();
        LetterFieldModel field = fields.get(result.index);
        List<LetterModel> hintLetters = new ArrayList<>();
        outerLoop:
        for (LetterModel letter : letters) {
            if (letter.getValue().equals(result.correctLetter)) {
                for (LetterFieldModel f : fields) {
                    if (f.getLetterInside() != null && f.getLetterInside() == letter)
                        continue outerLoop;
                }
                hintLetters.add(letter);
                letter.setHintActive(true);
            }

        }
        disposables.add(Observable.interval(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).takeWhile(e -> !field.containsLetter() || !field.getLetterInside().getValue().equals(result.correctLetter))
                .subscribe(f -> {
                    field.switchHintColor();
                    for (LetterModel model : hintLetters) model.switchHintColor();
                }, g -> {
                }, () -> {
                    if (manager.isGamePhasePlaying().blockingGet())
                        field.setColor(Color.RED);
                    for (LetterModel letter : hintLetters) letter.setHintActive(false);
                    manager.setHint(false);
                }));

    }

    private void nextWord() {
        manager.setCoins(preferences.getInt(ViewConstants.PREFERENCES_COINS, 0));
        coin.setCoins(manager.getCoins().blockingGet());
        view.clearDrawables();
        //if(manager.isCreateAllLettersActive().blockingGet()) view.getScrollView().setEnabled(false);
        view.setCoin(coin);
        currentWord = manager.nextWord().blockingGet();
        manager.changePhase(WordGamePhase.PRESENTING);
        presentWord(currentWord);
    }

    public List<LetterFieldModel> getFields(){
        return fields;
    }

    @Override
    public void exit() {
        player.stopPlaying();
        view.clearDrawables();
        manager.setHint(false);
        for (Disposable d : disposables) d.dispose();
    }

}
