package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import io.reactivex.Completable;

public class InsertLetterInPositionUseCase implements CompletableUseCaseWithParams<InsertLetterInPositionUseCase.Params> {

    private WordGameManager manager;

    public InsertLetterInPositionUseCase(WordGameManager manager){
        this.manager=manager;
    }
    @Override
    public Completable execute(Params params) {
        return Completable.fromAction(()->manager.insertLetterintoField(params.letter,params.position));
    }

    public static class Params{
        public Letter letter;
        public int position;

        public Params(Letter letter,int position){
            this.letter=letter;
            this.position=position;
        }
    }
}


