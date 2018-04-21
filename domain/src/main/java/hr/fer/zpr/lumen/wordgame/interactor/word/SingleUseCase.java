package hr.fer.zpr.lumen.wordgame.interactor.word;

import io.reactivex.Single;

public interface SingleUseCase<Result> {
    Single<Result> execute();
}
