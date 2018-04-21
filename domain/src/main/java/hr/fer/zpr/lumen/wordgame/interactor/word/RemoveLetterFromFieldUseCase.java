package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Letter;

public class RemoveLetterFromFieldUseCaseImpl implements RemoveLetterFromFieldUseCase {

    private WordGameManager manager;

    public RemoveLetterFromFieldUseCaseImpl(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public void removeLetterFromField(Letter letter) {
        manager.removeLetterFromField(letter);
    }
}
