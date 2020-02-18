import java.util.Random;

public class ElectedOfficialThread extends Thread{
    private int rank;
    private Integer topRank;
    private String name;
    private Thread parent;
    private ElectedOfficialThread leader;

    public ElectedOfficialThread(Integer topRank, RankThread parent){
        this.rank = new Random().nextInt();
        this.topRank = topRank;
        this.parent = parent;
        this.leader = this;
    }

    public void close() throws InterruptedException {
        this.join();
    }

    public int getRank() {
        return rank;
    }

    public void setTopRank(Integer topRank) {
        this.topRank = topRank;
    }
    public void setLeader(ElectedOfficialThread leader){
        this.leader = leader;
    }

    public ElectedOfficialThread getLeader() {
        return leader;
    }

    @Override
    public void run() {
            System.out.println(Thread.currentThread().getName() + " thinks the leader is: " + (this.getLeader().getName()) + "   --Rank:" + this.rank);
            synchronized (this.topRank){
                this.parent.interrupt();
            }
            if(currentThread().isInterrupted()){
                System.out.println(Thread.currentThread().getName() + " thinks the leader is: " + (this.getLeader().getName()) + "   --Rank:" + this.rank);
            }
    }
}
