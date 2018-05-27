package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import io.reactivex.Completable;
import io.reactivex.Single;

public class InsertLetterInPositionUseCase implements SingleUseCaseWIthParams<InsertLetterInPositionUseCase.Params,Boolean> {

    private WordGameManager manager;

    public InsertLetterInPositionUseCase(WordGameManager manager){
        this.manager=manager;
    }
    @Override
    public Single<Boolean> execute(Params params) {
        return manager.insertLetterintoField(params.letter.value,params.position);
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


