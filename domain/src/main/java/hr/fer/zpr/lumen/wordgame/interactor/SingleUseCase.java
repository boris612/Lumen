package hr.fer.zpr.lumen.wordgame.interactor;

import io.reactivex.Single;

public interface SingleUseCase<Result> {
    Single<Result> execute();
}
