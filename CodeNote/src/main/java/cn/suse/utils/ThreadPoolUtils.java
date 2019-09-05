package cn.suse.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils{
    
    /**
     * 使用内存队列线程池
     */
    private static ThreadPoolExecutor   threadPool;
    
    /**
     * 线程池队列最大大小
     */
    private static int threadPoolQueueMaxSize = 1000;
    
    /**
     * 内存队列
     */
    private static ArrayBlockingQueue<Runnable> queue;
    
    static{
        queue = new ArrayBlockingQueue<>(threadPoolQueueMaxSize);
        //初始化线程池
        threadPool = new ThreadPoolExecutor(
        		Runtime.getRuntime().availableProcessors(), 
        		Runtime.getRuntime().availableProcessors() * 2 + 1, 
        		5l,
                TimeUnit.MINUTES,
                queue,
                new ThreadPoolExecutor.AbortPolicy());
    }
    
    /**
     * 添加任务
     * @param task
     */
    public static void addTask(Runnable task){
        threadPool.execute(task);
    }

    /**
     * 获取池大小
     * @return
     */
    public static int getPoolSize(){
    	return threadPool.getMaximumPoolSize();
    }

    /**
     * 添加带有返回值的任务
     * @param task
     * @return
     */
    public static <T> Future addCallableTask(Callable<T> task){
        return threadPool.submit(task);
    }
    public static void main(String[] args) throws InterruptedException, ExecutionException {
    	addTask(new Runnable(){

			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName());
			}
    		
    	});
    	Future<String> callResult = addCallableTask(new Callable<String>(){
    		
    		@Override
    		public String call() throws Exception {
    			return Thread.currentThread().getName();
    		}
    		
    	});
    	System.out.println(callResult.get());
	}
}