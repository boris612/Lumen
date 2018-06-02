package hr.fer.zpr.lumen.wordgame.interactor;

import io.reactivex.Single;

public interface SingleUseCaseWIthParams<Params, Result> {

    Single<Result> execute(Params request);
}
