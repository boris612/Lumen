package hr.fer.zpr.lumen.wordgame.interactor.word;

import io.reactivex.Completable;

public interface CompletableUseCaseWithParams<Params> {

    Completable execute(Params params);
}
