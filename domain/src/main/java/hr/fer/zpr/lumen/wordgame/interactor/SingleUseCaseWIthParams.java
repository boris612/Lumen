package hr.fer.zpr.lumen.wordgame.interactor.word;

import io.reactivex.Single;

public interface SingleUseCaseWIthParams<Params,Result> {

    Single<Result> execute(Params request);
}
