package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Completable;

public class ChangeMessagesLanguageUseCase implements CompletableUseCaseWithParams<String> {


    private WordGameManager manager;

    public ChangeMessagesLanguageUseCase(WordGameManager manager){
        this.manager=manager;
    }

    @Override
    public Completable execute(String s) {
        return Completable.fromAction(()->manager.setMessagesLanguage(s));
    }
}
