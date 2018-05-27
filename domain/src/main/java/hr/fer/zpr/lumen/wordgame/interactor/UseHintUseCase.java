package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Single;

public class UseHintUseCase implements SingleUseCase<UseHintUseCase.Result>{

    private WordGameManager manager;

    public UseHintUseCase(WordGameManager manager){
        this.manager=manager;
    }

    @Override
    public Single<Result> execute() {

        Result result=new Result();
        result.canActivate=manager.setHint(true).blockingGet();
        result.index=manager.getIndexOfFirstIncorrectField().blockingGet();
        result.correctLetter=manager.getCorrectLetterForIndex(result.index).blockingGet();
        return Single.just(result);
    }

    public class Result{
        public boolean canActivate=false;
        public int index;
        public String correctLetter;
    }
}
