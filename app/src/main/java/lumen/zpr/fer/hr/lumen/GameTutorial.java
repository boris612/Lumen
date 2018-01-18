package lumen.zpr.fer.hr.lumen;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Alen on 10.1.2018..
 */

public abstract class GameTutorial {
    protected long timeOfGameStart;
    protected long timeToActivate;
    protected TutorialState tutorialState;

    private enum TutorialState {
        WAITING,ACTIVE,SHUT_DOWN,GAME_NOT_STARTED
    }

    public GameTutorial(long timeToActivate) {
        tutorialState = TutorialState.GAME_NOT_STARTED;
        this.timeToActivate = timeToActivate;
    }

    public void gameStarted() {
        if(tutorialState!=TutorialState.SHUT_DOWN) {
            restart();
        }
    }

    public void update() {
        if(tutorialState == TutorialState.SHUT_DOWN || tutorialState == TutorialState.GAME_NOT_STARTED) {
            return;
        }
        if(tutorialState == TutorialState.WAITING && System.currentTimeMillis()-timeOfGameStart >= timeToActivate) {
            tutorialState = TutorialState.ACTIVE;
        }
        if(tutorialState == TutorialState.ACTIVE) {
            updateTutorialAnimation();
        }
    }

    protected abstract void updateTutorialAnimation();

    public void draw(Canvas canvas) {
        if(tutorialState == TutorialState.ACTIVE) {
            drawTutorial(canvas);
        }
    }

    protected abstract void drawTutorial(Canvas canvas);

    public void shutDown() {
        tutorialState = TutorialState.SHUT_DOWN;
    }

    public void restartIfNotShutDown() {
        if(tutorialState != TutorialState.SHUT_DOWN) {
            restart();
        }
    }

    private void restart() {
        tutorialState = TutorialState.WAITING;
        timeOfGameStart = System.currentTimeMillis();
    }


  //  protected void tutorialEnded() {
  //     tutorialState = TutorialState.ENDED;
   //}


}
