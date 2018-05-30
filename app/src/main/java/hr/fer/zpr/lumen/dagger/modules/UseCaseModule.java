package hr.fer.zpr.lumen.dagger.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCategoriesUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCreateMoreLettersUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeGamePhaseUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeGreenOnCorrectUseCase;
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
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;

@Module
public class UseCaseModule {



    @Provides
    @Singleton
    ChangeCategoriesUseCase changeCategoriesUseCase(WordGameManager manager){
        return new ChangeCategoriesUseCase(manager);
    }

    @Provides
    @Singleton
    ChangeGamePhaseUseCase changeGamePhaseUseCase(WordGameManager manager){
        return new ChangeGamePhaseUseCase(manager);
    }

    @Provides
    @Singleton
    ChangeLanguageUseCase changeLanguageUseCase(WordGameManager manager){
        return new ChangeLanguageUseCase(manager);
    }

    @Provides
    @Singleton
    CheckIfAnswerCorrectUseCase checkIfAnswerCorrectUseCase(WordGameManager manager){
        return new CheckIfAnswerCorrectUseCase(manager);
    }

    @Provides
    @Singleton
    GetWordsUseCase getWordsUseCase(WordGameRepository repository){
        return new GetWordsUseCase(repository);
    }

    @Provides
    @Singleton
    InsertLetterInPositionUseCase insertLetterInPositionUseCase(WordGameManager manager){
        return new InsertLetterInPositionUseCase(manager);
    }

    @Provides
    @Singleton
    RemoveLetterFromFieldUseCase removeLetterFromFieldUseCase(WordGameManager manager){
        return new RemoveLetterFromFieldUseCase(manager);
    }

    @Provides
    @Singleton
    SetHintActiveUseCase setHintActiveUseCase(WordGameManager manager){
        return new SetHintActiveUseCase(manager);
    }

    @Provides
    @Singleton
    StartGameUseCase startGameUseCase(WordGameManager manager,WordGameRepository repository){
        return new StartGameUseCase(manager,repository);
    }

    @Provides
    @Singleton
    IsCreateMoreLettersActiveUseCase isCreateMoreLettersActiveUseCase(WordGameManager manager){
        return new IsCreateMoreLettersActiveUseCase(manager);
    }

    @Provides
    @Singleton
    SetCreateMoreLettersUseCase setCreateMoreLettersUseCase(WordGameManager manager){
        return new SetCreateMoreLettersUseCase(manager);
    }

    @Provides
    @Singleton
    ChangeCreateMoreLettersUseCase changeCreateMoreLettersUseCase(WordGameManager manager){
        return new ChangeCreateMoreLettersUseCase(manager);
    }

    @Provides
    @Singleton
    ChangeGreenOnCorrectUseCase changeGreenOnCorrectUseCase(WordGameManager manager){
        return new ChangeGreenOnCorrectUseCase(manager);
    }



}
