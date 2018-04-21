package hr.fer.zpr.lumen.wordgame.interactor;

import io.reactivex.Completable;

public interface CompletableUseCaseWithParams<Params> {

    Completable execute(Params params);
}
