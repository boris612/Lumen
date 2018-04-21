package hr.fer.zpr.lumen.wordgame.interactor.word;

import java.util.Set;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Category;
import io.reactivex.Completable;

public class ChangeCategoriesUseCaseImpl implements CompletableUseCaseWithParams<Set<Category>> {

    private WordGameManager manager;

    public ChangeCategoriesUseCaseImpl(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(Set<Category> categories) {
        return Completable.create(manager::changeCategories(categories));
    }
}
