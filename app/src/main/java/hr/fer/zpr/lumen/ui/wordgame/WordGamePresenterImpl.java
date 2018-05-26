package hr.fer.zpr.lumen.ui.wordgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import hr.fer.zpr.lumen.LumenApplication;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.sound.SoundPlayer;
import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;
import hr.fer.zpr.lumen.ui.wordgame.WordGamePresenter;
import hr.fer.zpr.lumen.ui.wordgame.WordGameView;
import hr.fer.zpr.lumen.ui.wordgame.models.CoinModel;
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
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.manager.WordGamePhase;
import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.Word;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
import io.reactivex.Completable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import wordgame.db.database.WordGameDatabase;
import wordgame.db.mapping.DataDomainMapper;

public class WordGamePresenterImpl implements WordGamePresenter {


    private static String CATEGORIES_NAME="categories";

    private static String LANGUAGE="language";


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

    private WordGameView view;

    private Context context;

    private int screenWidth;

    private int screenHeight;

    private Word currentWord;

    public WordGamePresenterImpl(LumenApplication application){
        application.getApplicationComponent().inject(this);
        this.context=application;
        screenWidth=context.getResources().getDisplayMetrics().widthPixels;
        screenHeight=context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void presentWord(Word word) {
        try {
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
        io.reactivex.Observable.interval(ViewConstants.TIME_BETWEEN_LETTERS, TimeUnit.MILLISECONDS).takeWhile(e->model.hasLettersToShow()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        playSound(model.getCurrentLetterSoundPath());
                        model.showNextLetter();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        playSound(currentWord.sound.path);
                        io.reactivex.Observable
                                .interval(100,TimeUnit.MILLISECONDS)
                                .takeWhile(e->player.isPlaying())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Long aLong) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        view.removeDrawable(model);
                                        manager.changePhase(WordGamePhase.PLAYING);
                                        showLetters();
                                    }
                                });



                    }
                });
    }

    public void showLetters(){

        view.addFields(createFields());
        view.addLetters(createLetters());

    }

    private List<LetterModel> createLetters() {

        List<Letter> randomLetters=new ArrayList<>();
        boolean createMore=isCreateMoreLettersActiveUseCase.execute().blockingGet();
        createMore=true;
        if(createMore){
            int numToGenerate;
            if(currentWord.letters.size()+ViewConstants.MORE_LETTERS_TO_CREATE>ViewConstants.MAX_LETTER_NUMBER){
                numToGenerate=ViewConstants.MAX_LETTER_NUMBER-currentWord.letters.size();
            }
            else{
                numToGenerate=ViewConstants.MORE_LETTERS_TO_CREATE;
            }
            randomLetters=repository.getRandomletters(currentWord.language,numToGenerate).blockingGet();
        }
        int letterDimension=(int)(screenWidth/(currentWord.letters.size()*(1+ViewConstants.CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR))*ViewConstants.CHAR_FIELDS_WIDTH_FACTOR);
        if(letterDimension>screenWidth*ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR) letterDimension=(int) (screenWidth*ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR);
        int startingSpace=screenWidth/50;
        int fieldDimension=letterDimension;
        letterDimension=(int)(letterDimension*ViewConstants.LETTER_IMAGE_SCALE_FACTOR);
        int space=(int)(screenWidth*ViewConstants.LETTER_SPACE_FACTOR);
        List<Rect> rects=new ArrayList<>();
        for(int i=0,n=randomLetters.size()+currentWord.letters.size();i<n;i++){
            Rect rect=new Rect();
            rect.left=startingSpace+i*(space+letterDimension);//+screenWidth/20;
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
    public void activateHint() {

    }

    @Override
    public void newWord() {

    }

    @Override
    public void update() {

    }

    @Override
    public void startGame() {
        Set<Category> categories=getCategories();
       String language= preferences.getString(LANGUAGE,"croatian");
        Single<Word> single=startGameUseCase.execute(new StartGameUseCase.Request(categories,new DataDomainMapper().languageFromString(language)));
        Word word=single.blockingGet();
        currentWord=word;
        try {
            Rect rect=new Rect();
            rect.top=0;
            rect.left=0;
            rect.bottom=(int)(screenHeight*ViewConstants.COIN_DIMENSION_FACTOR);
            rect.right=rect.bottom;
            CoinModel model = new CoinModel(BitmapFactory.decodeStream(context.getAssets().open(CoinModel.coinImagePath)),rect,100);
            view.addDrawable(model);
        }catch(Exception e){Log.d("error",e.getMessage());}
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
    }

    private void playSound(String path){
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(path);
            player.play(afd);

        }catch(Exception e){
            Log.d("error",e.getMessage());
        }


    }

}