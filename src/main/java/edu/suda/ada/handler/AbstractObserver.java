package edu.suda.ada.handler;

import org.ethereum.core.BlockSummary;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by LiYang on 2016/12/2.
 */
public abstract class AbstractObserver implements Observer{
    protected List<Future<Integer>> results = new LinkedList<>();
    protected ExecutorService executorService;

    public abstract void update(BlockSummary blockSummary);

    boolean loopForAvailable(List<Future<Integer>> futures){
        boolean available = false;
        for (Iterator<Future<Integer>> it = futures.iterator(); it.hasNext();){
            Future<Integer> future = it.next();
            if (future.isDone()){
                it.remove();
                available = true;
            }
        }
        return available;
    }

    void waitForAvailable(List<Future<Integer>> futures){
        while (!loopForAvailable(futures)){
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
