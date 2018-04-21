package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Language;
import io.reactivex.Completable;

public class ChangeLanguageUseCase implements CompletableUseCaseWithParams<Language> {

    private WordGameManager manager;

    public ChangeLanguageUseCase(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(Language language) {
        return Completable.fromAction(()->manager.changeLanguage(language));
    }
}
