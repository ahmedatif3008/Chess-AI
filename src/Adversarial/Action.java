package Adversarial;

public class Action {
    private String finalMove;
    private Integer utility;

    public Action(String move, Integer score) {
        this.finalMove = move;
        this.utility = score;
    }

    public String get_first() {
        return finalMove;
    }

    public int get_second() {
        return utility;
    }

    public void set_first(String finalMove) {
        this.finalMove = finalMove;
    }

    public void set_second(Integer utility) {
        this.utility = utility;
    }
}
