package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Single;

import static io.reactivex.Single.just;

public class CheckIfAnswerCorrectUseCase implements SingleUseCase<Boolean> {

    private WordGameManager manager;

    public CheckIfAnswerCorrectUseCase(WordGameManager manager){
        this.manager=manager;
    }

    @Override
    public Single<Boolean> execute() {
        return manager.isAnswerCorrect();
    }
}
