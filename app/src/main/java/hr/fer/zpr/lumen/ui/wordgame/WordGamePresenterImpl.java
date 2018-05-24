package hr.fer.zpr.lumen.ui.wordgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import hr.fer.zpr.lumen.LumenApplication;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.ui.wordgame.WordGamePresenter;
import hr.fer.zpr.lumen.ui.wordgame.WordGameView;
import hr.fer.zpr.lumen.ui.wordgame.models.CoinModel;
import hr.fer.zpr.lumen.ui.wordgame.models.ImageModel;
import hr.fer.zpr.lumen.ui.wordgame.models.StartingHintModel;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCategoriesUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeGamePhaseUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeLanguageUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.CheckIfAnswerCorrectUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.GetWordsUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.InsertLetterInPositionUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.RemoveLetterFromFieldUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.SetHintActiveUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.StartGameUseCase;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.manager.WordGamePhase;
import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Word;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
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

    private WordGameView view;

    private Context context;

    private int screenWidth;

    private int screenHeight;

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
        io.reactivex.Observable.interval(1000, TimeUnit.MILLISECONDS).takeWhile(e->model.hasLettersToShow()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        model.showNextLetter();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        view.removeDrawable(model);
                        playSound();
                        manager.changePhase(WordGamePhase.PLAYING);
                    }
                });
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

    private void playSound(){}

}
