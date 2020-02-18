import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class SockMatching {
    private static Vector<Sock> sockList;
    private static Vector<SockPair> washerQueue;
    private static int sockCount = 0;

    public static class SockThread extends  Thread {
        String color;
        CountDownLatch latch;
        SockThread(String color, CountDownLatch latch){
            this.color = color;
            this.latch = latch;
        }
        @Override
        public void run() {
            int numSocks = (new Random()).nextInt(100);
            sockCount+=numSocks;
            for(int i = 0; i < numSocks; i++){
                    sockList.add(new Sock(this.color));
                    System.out.println(this.color.toUpperCase() + ": Produced " + (i+1) + "/" + numSocks + " " + this.color + " socks.");
            }
            latch.countDown();
            try {
                currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static class MatchingThread extends Thread{
        @Override
        public void run() {
            while(!sockList.isEmpty()){
                Sock first = sockList.firstElement();
                for(Sock potential : sockList){
                    if(potential.getColor().equals(first.getColor())){
                        synchronized (washerQueue){
                            washerQueue.add(new SockPair(first, potential));
                            System.out.println("Sending pair of " + first.getColor() +" socks to washer. " +
                                    washerQueue.size() + " in Queue. " +
                                    sockList.size() + " socks remaining.");
                            sockList.removeElement(first);
                            sockList.removeElement(potential);
                            break;
                        }
                    }
                }
            }
            try {
                currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class WasherThread extends Thread{
        @Override
        public void run() {
            synchronized (washerQueue){
                while(!washerQueue.isEmpty()){
                    SockPair front = washerQueue.firstElement();
                    washerQueue.removeElement(front);
                    System.out.println("Washer destroyed a pair of " + front.getColor()+ " socks. " + sockList.size() + " remaining.");
                    if(washerQueue.size() == 0){
                        try {
                            washerQueue.wait(100);
                            if(washerQueue.isEmpty() && sockList.isEmpty()){
                                Thread.currentThread().join();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        sockList = new Vector<>();
        washerQueue = new Vector<>();
        CountDownLatch latch = new CountDownLatch(4);
        (new SockThread("red", latch)).start();
        (new SockThread("blue", latch)).start();
        (new SockThread("green", latch)).start();
        (new SockThread("orange", latch)).start();

        latch.await();
        (new MatchingThread()).start();
        (new WasherThread()).start();
    }
}
