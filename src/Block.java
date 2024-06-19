public class Block {

    private int x, y;

    public States state;

    public Block(){

    }

    public Block(States state) {
        this.state = state;
    }

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        state = States.FREE;
    }

    public Block(int x, int y, States state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public void changeState() {
        state = /* equals: ? true : false */
                state == States.FREE ? States.BLOCK : States.FREE;
    }

    public void changeState(States state) {
        this.state = state;
    }

    public enum States{
        BLOCK,
        FREE
    }
}
