package hr.fer.zpr.lumen.wordgame.interactor;

import java.util.Set;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Word;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
import io.reactivex.Single;

public class StartGameUseCase implements SingleUseCaseWIthParams<StartGameUseCase.Request,Word> {

    private final WordGameManager manager;
    private final WordGameRepository repository;

    public StartGameUseCase(final WordGameManager manager,final WordGameRepository repository) {
        this.manager = manager;
        this.repository = repository;
    }

    @Override
    public Single<Word> execute(final Request request) {
        return repository.getWordsFromCategories(request.categories,request.language).flatMap(words->manager.startGame(words));
    }

    public static final class Request{
        public final Set<Category> categories;
        public final Language language;

        public Request(Set<Category> categories, Language language) {
            this.categories = categories;
            this.language = language;
        }
    }
}
