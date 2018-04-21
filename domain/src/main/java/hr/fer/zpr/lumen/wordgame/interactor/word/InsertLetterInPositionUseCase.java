package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Letter;

public class InsertLetterInPositionUseCaseImpl implements InsertLetterInPositionUseCase {

    private WordGameManager manager;

    public InsertLetterInPositionUseCaseImpl(WordGameManager manager){
        this.manager=manager;
    }
    @Override
    public void insertLetterInPosition(Letter letter, int position) {
        manager.insertLetterintoField(letter,position);
    }
}
