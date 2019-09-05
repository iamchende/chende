package cd;
public class SynchronizedTest {
	class UserDemo{
		
		synchronized void say(){
			System.out.println("1say");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("1休息了1s");
		}
		synchronized void say(String content){
			System.out.println("2say:" + content);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("2休息了2s");
		}
		void say(String content, int i){
			synchronized(this){
				System.out.println("3say:"+content+":"+i);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("3休息了3s");
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		//上面三个方法用的是同一个锁【同一个实例】，因此会阻塞串行执行
		UserDemo u = new SynchronizedTest().new UserDemo();
		new Thread(()->{u.say();}).start();
		new Thread(()->{u.say("hello");}).start();
		new Thread(()->{u.say("i'am hello",0);}).start();
	}
}