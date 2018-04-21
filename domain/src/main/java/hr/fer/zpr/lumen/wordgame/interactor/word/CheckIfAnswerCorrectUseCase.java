package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;

public class CheckIfAnswerCorrectUseCaseImpl implements CheckIfAnswerCorrectUseCase {

    private WordGameManager manager;

    public CheckIfAnswerCorrectUseCaseImpl(WordGameManager manager){
        this.manager=manager;
    }

    @Override
    public boolean checkIfAnswerCorrect() {
        return manager.isAnswerCorrect();
    }
}
