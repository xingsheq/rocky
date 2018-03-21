package rocky.com;

import rocky.com.util.UnSafeUtils;
import sun.misc.Unsafe;

public class CASCounter {
    private volatile long counter = 0;
    private Unsafe unsafe= UnSafeUtils.getUnsafe();
    private long offset;

    public CASCounter() throws Exception {
        offset = unsafe.objectFieldOffset(CASCounter.class.getDeclaredField("counter"));
    }


    public void increment() {
        long before = counter;
        while (!unsafe.compareAndSwapLong(this, offset, before, before + 1)) {
            before = counter;
        }
    }

    public long getCounter() {
        return counter;
    }

    public static void main(String[] args) {
        try {
            CASCounter casCounter=new CASCounter();
            Producer producer=new Producer(casCounter);
            for (int i = 0; i < 5; i++) {
                new Thread(producer).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class Producer implements Runnable{
    CASCounter casCounter;
    public Producer(CASCounter counter){
        casCounter=counter;
    }
    @Override
    public void run() {
        while (true){
            casCounter.increment();
            System.out.println(Thread.currentThread().getName()+" increment");
            System.out.println(Thread.currentThread().getName()+" casCounter = "+casCounter.getCounter());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

