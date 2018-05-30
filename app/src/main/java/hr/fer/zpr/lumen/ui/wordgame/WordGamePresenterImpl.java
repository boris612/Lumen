package hr.fer.zpr.lumen.ui.wordgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import hr.fer.zpr.lumen.dagger.application.LumenApplication;
import hr.fer.zpr.lumen.sound.SoundPlayer;
import hr.fer.zpr.lumen.ui.viewmodels.CoinModel;
import hr.fer.zpr.lumen.ui.wordgame.models.ImageModel;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterFieldModel;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterModel;
import hr.fer.zpr.lumen.ui.wordgame.models.StartingHintModel;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewUtil;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCategoriesUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeGamePhaseUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeLanguageUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.CheckIfAnswerCorrectUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.GetWordsUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.InsertLetterInPositionUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.IsCreateMoreLettersActiveUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.RemoveLetterFromFieldUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.SetCreateMoreLettersUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.SetHintActiveUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.StartGameUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.UseHintUseCase;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.manager.WordGamePhase;
import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.Word;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import wordgame.db.database.WordGameDatabase;
import wordgame.db.mapping.DataDomainMapper;

public class WordGamePresenterImpl implements WordGamePresenter {


    private static String CATEGORIES_NAME="categories";

    private static String LANGUAGE="language";

    private Disposable hintObservable;

    private Disposable gamePhaseObservable;



    @Inject
    WordGameManager manager;

    @Inject
    StartGameUseCase startGameUseCase;

    @Inject
    SetHintActiveUseCase setHintActiveUseCase;

    @Inject
    RemoveLetterFromFieldUseCase removeLetterFromFieldUseCase;

    @Inject
    InsertLetterInPositionUseCase insertLetterInPositionUseCase;

    @Inject
    GetWordsUseCase getWordsUseCase;

    @Inject
    IsCreateMoreLettersActiveUseCase isCreateMoreLettersActiveUseCase;

    @Inject
    SetCreateMoreLettersUseCase setCreateMoreLettersUseCase;

    @Inject
    CheckIfAnswerCorrectUseCase checkIfAnswerCorrectUseCase;

    @Inject
    ChangeLanguageUseCase changeLanguageUseCase;

    @Inject
    ChangeGamePhaseUseCase changeGamePhaseUseCase;

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

    private WordGameView view;

    private Context context;

    private int screenWidth;

    private int screenHeight;

    private Word currentWord;

    private List<LetterModel> letters;

    private List<LetterFieldModel> fields;

    private CoinModel coin;

    public WordGamePresenterImpl(LumenApplication application){
        application.getApplicationComponent().inject(this);
        this.context=application;
        screenWidth=context.getResources().getDisplayMetrics().widthPixels;
        screenHeight=context.getResources().getDisplayMetrics().heightPixels;
        manager.setCoins(preferences.getInt(ViewConstants.PREFERENCES_COINS,0));

    }

    @Override
    public void presentWord(Word word) {
        try {
            if(letters!=null) letters.clear();
            Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(word.wordImage.path));
            Rect bounds=new Rect();
            bounds.left=screenWidth/2-(int)(screenWidth* ViewConstants.IMAGE_WIDTH_FACTOR/2);
            bounds.right=screenWidth/2+(int)(screenWidth* ViewConstants.IMAGE_WIDTH_FACTOR/2);
            bounds.top=(int)(screenHeight*ViewConstants.IMAGE_TOP_SPACE_FACTOR);
            bounds.bottom=(int)(screenHeight*ViewConstants.IMAGE_HEIGHT_FACTOR);
            ImageModel model=new ImageModel(image,bounds);
            view.addDrawable(model);
        }catch(Exception e){
            Log.d("error",e.getMessage());
        }
        Rect rect=new Rect();
        rect.top=(int)(screenHeight* ViewConstants.STARTING_HINT_TOP_FACTOR);
        rect.bottom=rect.top+(int)(screenHeight*ViewConstants.STARTING_HINT_HEIGHT_FACTOR);
        rect.left=0;
        rect.right=screenWidth;
        StartingHintModel model=new StartingHintModel(word,rect,screenWidth,screenHeight,context);
        view.addDrawable(model);
        presentHint(model);
    }

    private void presentHint(StartingHintModel model) {
        hintObservable=Observable.interval(ViewConstants.TIME_BETWEEN_LETTERS,TimeUnit.MILLISECONDS.MILLISECONDS).takeWhile(e->model.hasLettersToShow()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                f->{playSound(model.getCurrentLetterSoundPath());
                    model.showNextLetter();}
                ,g->{},()->{
                    playSound(currentWord.sound.path);
                    gamePhaseObservable=Observable.interval(100,TimeUnit.MILLISECONDS)
                            .takeWhile(j->player.isPlaying())
                            .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(k->{},l->{},()->{view.removeDrawable(model);
                        changeGamePhaseUseCase.execute(WordGamePhase.PLAYING).blockingGet();
                        showLetters();});

                });


    }

    public void showLetters(){
        fields=createFields();
        view.addFields(fields);
        letters=createLetters();
        view.addLetters(letters);

    }

    private List<LetterModel> createLetters() {

        List<Letter> randomLetters=new ArrayList<>();
        boolean createMore=isCreateMoreLettersActiveUseCase.execute().blockingGet();
        if(createMore){
            int numToGenerate;
            if(currentWord.letters.size()+ViewConstants.MORE_LETTERS_TO_CREATE>ViewConstants.MAX_LETTER_NUMBER){
                numToGenerate=ViewConstants.MAX_LETTER_NUMBER-currentWord.letters.size();
            }
            else{
                numToGenerate=ViewConstants.MORE_LETTERS_TO_CREATE;
            }
            randomLetters=manager.getRandomLetters(numToGenerate).blockingGet();
        }
        int letterDimension=(int)(screenWidth/(currentWord.letters.size()*(1+ViewConstants.CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR))*ViewConstants.CHAR_FIELDS_WIDTH_FACTOR);
        if(letterDimension>screenWidth*ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR) letterDimension=(int) (screenWidth*ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR);
        int startingSpace=screenWidth/100;
        int fieldDimension=letterDimension;
        letterDimension=(int)(letterDimension*ViewConstants.LETTER_IMAGE_SCALE_FACTOR);
        int space=(int)(screenWidth-(randomLetters.size()+currentWord.letters.size())*letterDimension-startingSpace)/(randomLetters.size()+currentWord.letters.size());
        List<Rect> rects=new ArrayList<>();
        for(int i=0,n=randomLetters.size()+currentWord.letters.size();i<n;i++){
            Rect rect=new Rect();
            rect.left=startingSpace+i*(space+letterDimension);
            rect.right=rect.left+letterDimension;
            rect.top=(int)(screenHeight*ViewConstants.CHAR_FIELDS_Y_COORDINATE_FACTOR+fieldDimension+screenHeight*ViewConstants.FIELD_LETTER_SPACE_FACTOR);
            rect.bottom=rect.top+letterDimension;
            rects.add(rect);
        }
        List<LetterModel> letters=new ArrayList<>();
        randomLetters.addAll(currentWord.letters);
        Random random=new Random();
        for(Letter letter:randomLetters){
            try {
                Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(letter.image.path));
                int randIndex=random.nextInt(rects.size());
                letters.add(new LetterModel(letter.value,image,rects.get(randIndex)));
                rects.remove(randIndex);
            }catch(Exception e){
                Log.d("error",e.getMessage());
            }
        }


        return letters;
    }

    public List<LetterFieldModel> createFields(){
        List<LetterFieldModel> fields=new ArrayList<>();
        int fieldDimension=(int)(screenWidth/(currentWord.letters.size()*(1+ViewConstants.CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR))*ViewConstants.CHAR_FIELDS_WIDTH_FACTOR);
        if(fieldDimension>screenWidth*ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR) fieldDimension=(int) (screenWidth*ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR);
        int gapWidth=(int)(fieldDimension*ViewConstants.CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR);
        int startingX= ViewUtil.calculateStartingX(screenWidth,currentWord.letters.size(),fieldDimension,gapWidth);
        for(int i=0;i<currentWord.letters.size();i++){
            Rect rect=new Rect();
            rect.left=startingX+i*(fieldDimension+gapWidth);
            rect.top=(int)(screenHeight*ViewConstants.CHAR_FIELDS_Y_COORDINATE_FACTOR);
            rect.right= rect.left+fieldDimension;
            rect.bottom=rect.top+fieldDimension;
            fields.add(new LetterFieldModel(rect));
        }
        return fields;
    }


    @Override
    public void startGame() {
        Set<Category> categories=getCategories();
       String language= preferences.getString(LANGUAGE,"croatian");
        Single<Word> single=startGameUseCase.execute(new StartGameUseCase.Request(categories,new DataDomainMapper().languageFromString(language)));
        Word word=single.blockingGet();
        currentWord=word;
        presentWord(word);
    }

    private Set<Category> getCategories(){
        Set<Category> all=new HashSet<>();
        all.addAll(Arrays.asList(Category.ANIMALS,Category.FOOD,Category.FRUIT,Category.ITEMS,Category.NATURE,Category.TRANSPORT));
        Set<String> categories= preferences.getStringSet(CATEGORIES_NAME,null);
        if(categories==null){
            return all;
        }
        DataDomainMapper mapper=new DataDomainMapper();
        return mapper.categoriesFromStrings(categories);

    }

    public void setView(WordGameView view){
        this.view=view;
        try {
            Rect rect=new Rect();
            rect.top=0;
            rect.left=0;
            rect.bottom=(int)(screenHeight*ViewConstants.COIN_DIMENSION_FACTOR);
            rect.right=rect.bottom;
            coin=new CoinModel(BitmapFactory.decodeStream(context.getAssets().open(CoinModel.coinImagePath)),rect,preferences.getInt(ViewConstants.PREFERENCES_COINS,0));
            view.setCoin(coin);
        }catch(Exception e){
            Log.d("error",e.getMessage());
        }
    }

    private void playSound(String path){
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(path);
            player.play(afd);

        }catch(Exception e){
            Log.d("error",e.getMessage());
        }


    }

    @Override
    public boolean shouldHandleTouch() {
        if(manager.isGamePhasePlaying().blockingGet()) return true;
        return false;
    }

    @Override
    public void letterInserted(LetterModel letter, LetterFieldModel field) {
       boolean correct= insertLetterInPositionUseCase.execute(new InsertLetterInPositionUseCase.Params(new Letter(letter.getValue()),fields.indexOf(field))).blockingGet();
       if(!correct && manager.areAllFieldsFull().blockingGet()){
           try{
               player.play(context.getAssets().openFd(manager.getIncorrectMessage().blockingGet()));
           }catch(Exception e){
               Log.d("error",e.getMessage());
           }
       }
       if(correct && manager.isHintOnCorrectOn().blockingGet()){
            if(!manager.areAllFieldsFull().blockingGet() &&  !manager.isAnswerCorrect().blockingGet()){

                Completable.timer(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        field.setColor(Color.GREEN);
                    }
                    @Override
                    public void onComplete() {

                        field.setColor(Color.RED);
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                });
            }

       }
        if(manager.areAllFieldsFull().blockingGet()){
            String path;
            if(manager.isAnswerCorrect().blockingGet()){
                changeGamePhaseUseCase.execute(WordGamePhase.ENDING).blockingGet();
                path=manager.getCorrectMessage().blockingGet();
                coin.setCoins(manager.getCoins().blockingGet());
                SharedPreferences.Editor editor=preferences.edit();
                editor.putInt(ViewConstants.PREFERENCES_COINS,manager.getCoins().blockingGet());
                editor.commit();
                addTick();
                for(LetterFieldModel f:fields) f.setColor(Color.GREEN);
                try {
                    player.play(context.getAssets().openFd(path));
                    Completable.timer(3000,TimeUnit.MILLISECONDS,AndroidSchedulers.mainThread()).subscribe(()->nextWord());
                }catch(Exception e){
                    Log.d("error",e.getMessage());
                }
            }
        }
    }

    private void addTick() {
        try {
            Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(ViewConstants.TICK_IMAGE_PATH));
            Rect rect=new Rect();
            rect.left=screenWidth/2-screenWidth/5;
            rect.top=screenHeight/2-screenHeight/5;
            rect.right=screenWidth/2+screenWidth/5;
            rect.bottom=screenHeight/2+screenHeight/5;
            ImageModel model=new ImageModel(image,rect);
            view.addDrawable(model);
        }catch(Exception e){
            Log.d("error",e.getMessage());
        }
    }

    @Override
    public void letterRemoved(LetterFieldModel field) {
        removeLetterFromFieldUseCase.execute(fields.indexOf(field)).blockingGet();
    }

    @Override
    public void hintPressed() {
        if(manager.isHintActive().blockingGet()) return;

        UseHintUseCase.Result result=useHintUseCase.execute().blockingGet();
        if(!result.canActivate) return;
        coin.setCoins(manager.getCoins().blockingGet());
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(ViewConstants.PREFERENCES_COINS,manager.getCoins().blockingGet());
        editor.commit();
        LetterFieldModel field=fields.get(result.index);
        List<LetterModel> hintLetters=new ArrayList<>();
        outerLoop:for(LetterModel letter:letters){
            if(letter.getValue().equals(result.correctLetter)) {
                for(LetterFieldModel f:fields){
                    if(f.getLetterInside()!=null && f.getLetterInside()==letter ) continue outerLoop;
                }
                hintLetters.add(letter);
                letter.setHintActive(true);
            }

        }
        Observable.interval(500,TimeUnit.MILLISECONDS,AndroidSchedulers.mainThread()).takeWhile(e->!field.containsLetter() ||!field.getLetterInside().getValue().equals(result.correctLetter))
                .subscribe(f->{field.switchHintColor();
                for(LetterModel model:hintLetters) model.switchHintColor();},g->{},()->{
                    if(manager.isGamePhasePlaying().blockingGet())
                    field.setColor(Color.RED);
                    for(LetterModel letter:hintLetters) letter.setHintActive(false);
                    manager.setHint(false);
                });

    }

    private void nextWord(){
        view.clearDrawables();
        view.setCoin(coin);
        currentWord=manager.nextWord().blockingGet();
        changeGamePhaseUseCase.execute(WordGamePhase.PRESENTING).blockingGet();
        presentWord(currentWord);
    }

    @Override
    public void exit() {
        player.stopPlaying();
        hintObservable.dispose();
        gamePhaseObservable.dispose();
        view.clearDrawables();
        manager.setHint(false);

    }
}
