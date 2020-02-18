import sun.jvm.hotspot.runtime.Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class RankThread extends Thread{
    public static Integer rank = Integer.MIN_VALUE;
    public ElectedOfficialThread leader;

    @Override
    public void run() {
        int n = new Random().nextInt(120);
        List<ElectedOfficialThread> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(new ElectedOfficialThread(rank, this));
            list.get(list.size()-1).start();
            if(currentThread().isInterrupted()){
                System.out.println("A new elected official thread has been created");
                for (ElectedOfficialThread thread : list) {
                    if(thread.getRank() >= rank){
                        synchronized (thread){
                            rank = thread.getRank();
                            this.leader = thread;
                        }
                        for (ElectedOfficialThread nextThread:list) {
                            nextThread.setLeader(leader);
                            nextThread.interrupt();
                        }
                    }
                }
            }
        }
        System.out.println("\n\nThe final leader is " + leader.getName() + " with a rank of " + leader.getRank());
    }
}
