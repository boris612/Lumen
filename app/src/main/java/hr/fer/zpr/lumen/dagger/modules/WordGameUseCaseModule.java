package hr.fer.zpr.lumen.dagger.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCategoriesUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCreateAllLettersUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCreateMoreLettersUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeGreenOnCorrectUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeLanguageUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeMessagesLanguageUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.InsertLetterInPositionUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.IsCreateAllLettersActiveUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.IsCreateMoreLettersActiveUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.RemoveLetterFromFieldUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.SetCreateAllLettersUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.SetCreateMoreLettersUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.StartGameUseCase;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;

@Module
public class WordGameUseCaseModule {


    @Provides
    @Singleton
    ChangeCategoriesUseCase changeCategoriesUseCase(WordGameManager manager) {
        return new ChangeCategoriesUseCase(manager);
    }

    @Provides
    @Singleton
    ChangeMessagesLanguageUseCase changeMessagesLanguageUseCase(WordGameManager manager){
        return new ChangeMessagesLanguageUseCase(manager);
    }


    @Provides
    @Singleton
    ChangeLanguageUseCase changeLanguageUseCase(WordGameManager manager) {
        return new ChangeLanguageUseCase(manager);
    }


    @Provides
    @Singleton
    InsertLetterInPositionUseCase insertLetterInPositionUseCase(WordGameManager manager) {
        return new InsertLetterInPositionUseCase(manager);
    }

    @Provides
    @Singleton
    RemoveLetterFromFieldUseCase removeLetterFromFieldUseCase(WordGameManager manager) {
        return new RemoveLetterFromFieldUseCase(manager);
    }


    @Provides
    @Singleton
    StartGameUseCase startGameUseCase(WordGameManager manager, WordGameRepository repository) {
        return new StartGameUseCase(manager, repository);
    }

    @Provides
    @Singleton
    IsCreateMoreLettersActiveUseCase isCreateMoreLettersActiveUseCase(WordGameManager manager) {
        return new IsCreateMoreLettersActiveUseCase(manager);
    }

    @Provides
    @Singleton
    SetCreateMoreLettersUseCase setCreateMoreLettersUseCase(WordGameManager manager) {
        return new SetCreateMoreLettersUseCase(manager);
    }

    @Provides
    @Singleton
    ChangeCreateMoreLettersUseCase changeCreateMoreLettersUseCase(WordGameManager manager) {
        return new ChangeCreateMoreLettersUseCase(manager);
    }

    @Provides
    @Singleton
    IsCreateAllLettersActiveUseCase isCreateAllLettersActiveUseCase(WordGameManager manager) {
        return new IsCreateAllLettersActiveUseCase(manager);
    }

    @Provides
    @Singleton
    SetCreateAllLettersUseCase setCreateAllLettersUseCase(WordGameManager manager) {
        return new SetCreateAllLettersUseCase(manager);
    }

    @Provides
    @Singleton
    ChangeCreateAllLettersUseCase changeCreateAllLettersUseCase(WordGameManager manager) {
        return new ChangeCreateAllLettersUseCase(manager);
    }

    @Provides
    @Singleton
    ChangeGreenOnCorrectUseCase changeGreenOnCorrectUseCase(WordGameManager manager) {
        return new ChangeGreenOnCorrectUseCase(manager);
    }


}
