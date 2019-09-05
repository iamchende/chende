package cd;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ThreadPoolsTest {
	/**
	 * 核心线程数：2,最大线程数：4,队列大小：8
	 * 添加第一个任务，线程池直接启动一个线程执行任务（1）
	 * 添加第二个任务，线程池也直接启动一个线程执行任务（2）
	 * 添加第三个任务，线程池不再启动线程，直接将任务放入队列中
	 * 添加第四个任务，线程池不再启动线程，直接将任务放入队列中
	 * 。。。
	 * 添加第十个任务，线程池不再启动线程，直接将任务放入队列中
	 * 添加第十一个任务，线程池启动一个线程执行任务（3），队列中的任务仍在排队
	 * 添加第十二个任务，线程池启动一个线程执行任务（4），队列中的任务仍在排队
	 * 添加第十三个任务，线程池满了，队列也满了，抛异常rejectedExecution
	 * 
	 * 如果有线程任务执行完了，则该线程立马执行队列头的任务。
	 */
	@Test
	public void test01(){
		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(8);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 5, TimeUnit.SECONDS, 
				workQueue, new ThreadPoolExecutor.AbortPolicy());
		
		for(int i = 0;i<20;i++){
			ThreadDemo thread = new ThreadDemo(i);
			pool.execute(thread);
		}
	}
}
class ThreadDemo implements Runnable{
	
	private int i = 0;
	public ThreadDemo(int i) {
		this.i=i;
	}

	@Override
	public void run() {
		System.out.println("我是" + Thread.currentThread().getName()+"休息1h,编号" + i);
		try {
			if(i != 10){
				Thread.sleep(1000*60*60);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

