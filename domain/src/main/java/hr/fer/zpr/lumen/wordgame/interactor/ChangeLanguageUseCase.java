package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Completable;

public class ChangeLanguageUseCase implements CompletableUseCaseWithParams<String> {

    private WordGameManager manager;

    public ChangeLanguageUseCase(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(String language) {
        return Completable.fromAction(() -> manager.changeLanguage(language));
    }
}
