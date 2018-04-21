package hr.fer.zpr.lumen.wordgame.interactor.word;

import io.reactivex.Single;

public interface SingleUseCaseWIthRequest<Request,Result> {

    Single<Result> execute(Request request);
}
