package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Language;

public class ChangeLanguageUseCaseImpl implements ChangeLanguageUseCase {

    private WordGameManager manager;

    public ChangeLanguageUseCaseImpl(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public void changeLanguage(Language language) {
        manager.changeLanguage(language);
    }
}
