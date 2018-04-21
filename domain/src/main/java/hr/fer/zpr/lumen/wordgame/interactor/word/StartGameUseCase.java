package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;

public class StartGameUseCaseImpl implements StartGameUseCase {

    private WordGameManager manager;

    public StartGameUseCaseImpl(WordGameManager manager){
        this.manager=manager;
    }
    @Override
    public void startGame() {
        manager.startGame();
    }
}
