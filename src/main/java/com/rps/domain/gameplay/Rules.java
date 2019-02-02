package com.rps.domain.gameplay;

public class Rules {

    public ActionType evaluate(ActionType actionType1, ActionType actionType2) {
        if (actionType1.getValue() == actionType2.getValue()) {
            return ActionType.TIE;
        } else if ((isRock(actionType1) && isPaper(actionType2)) || (isPaper(actionType1) && isRock(actionType2))) {
            return ActionType.PAPER;
        } else if ((isRock(actionType1) && isScissors(actionType2)) || (isScissors(actionType1) && isRock(
                actionType2))) {
            return ActionType.ROCK;
        } else if ((isPaper(actionType1) && isScissors(actionType2)) || (isScissors(actionType1) && isPaper(
                actionType2))) {
            return ActionType.SCISSORS;
        }
        return ActionType.TIE;
    }

    private boolean isRock(ActionType actionType) {
        return actionType.getValue() == ActionType.ROCK.getValue();
    }

    private boolean isPaper(ActionType actionType) {
        return actionType.getValue() == ActionType.PAPER.getValue();
    }

    private boolean isScissors(ActionType actionType) {
        return actionType.getValue() == ActionType.SCISSORS.getValue();
    }

}
