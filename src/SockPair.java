public class SockPair {
    private Sock sock1;
    private Sock sock2;

    public SockPair(Sock sock1, Sock sock2){
        this.sock1 = sock1;
        this.sock2 = sock2;
    }
    public String getColor(){
        return sock1.getColor();
    }
}
