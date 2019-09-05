package cn.suse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 搜索图中任意两个点之间最短的可达路径
 * 
 * 下面是两种遍历算法，并不能直接用来求最短路径
 * 广度优先搜索：利用队列先放入起点，将每次将出队元素所有可达且未遍历过的元素入队列，队列空了则遍历完了。感觉就是以起点为圆心一层一层的遍历
 * 深度优先搜索：利用栈先放入起点，每次将节点可达的（且未访问的第一个节点入栈）节点入栈，没有可达的则出栈一个节点再遍历此时栈顶其他可达的节点，
 * 			 栈空则遍历完了。感觉就是先选择一条最长的路径（直到节点没有可达节点（未被访问的）），然后倒塑遍历，深度优先搜索类似于树的先序遍历。
 * 类_190408SearchShortestPath4Graph.java的实现描述：TODO 类实现描述 
 * @author chende 2019年4月8日 下午5:52:51
 * #将求最短路径调整为求所有路径,实际得到的并不是所有路径，而是以层为单位的几条路径
 */
public class _190408SearchShortestPath4Graph {
	double canArrive = 40;//可达的简单判断，认为两个节点间的距离小于等于这个数就是可达的。
    public static void main(String[] args) {
    	_190408SearchAllPath4Graph allPath = new _190408SearchAllPath4Graph();
    	_190408SearchShortestPath4Graph shortestPath = new _190408SearchShortestPath4Graph();
    	//1.构造n个点
    	List<Point> points = allPath.getPointList("15,25|28,32|45,47|26,37");
    	//2.加入起点和终点
		Point start = allPath.getPoint("0,6");
		Point end = allPath.getPoint("50,65");
		points.add(start);
		points.add(end);
		//3.根据所有的点的可达性描述一个无向图出来，类似于hashMap的存储结构;有向图也是可以的，判断条件需要微调
		Map<Integer, LinkedList<Point>> map = new HashMap<>();
		for(Point p : points){
			if(p.equals(end))continue;//一旦到达终点就是一条路径，终点之后再搜索其他路径就没有意义了
			LinkedList<Point> list = new LinkedList<>();
			map.put(p.getBizNo(), list);
			list.add(p);
			for(Point t : points){
				double distance = allPath.getDistance(p,t);
				if(0 < distance && distance < shortestPath.canArrive){
					list.add(t);//#1
					//list.add(new Point(t.getX(),t.getY(),t.getBizNo()));//相同的点，但是是不同的对象
				}
			}
		}
		//深度优先搜索算法-求最短路径
		System.out.println("深度优先搜索算法-求最短路径:");
		shortestPath.searPathByDepthFirstSearch();
		//广度优先搜索算法-求最短路径(不带权的最短路径)
		System.out.println("广度优先搜索算法-求最短路径(不带权的最短路径):");
		shortestPath.searPathByWidthFirstSearch(start,end,map);
	}
	private void searPathByDepthFirstSearch() {
		//_190408SearchAllPath4Graph里面就是深度优先，每次得到路径后取最小值即可得到最短路径
		System.out.println("_190408SearchAllPath4Graph里面就是深度优先，每次得到路径后取最小值即可得到最短路径");
	}
    private void searPathByWidthFirstSearch(final Point start, Point end, Map<Integer, LinkedList<Point>> map) {
    	Queue<Point> queue = new ArrayBlockingQueue<>(map.size()+1);//终点没有linkedList,#2
    	//Queue<Point> queue = new ArrayBlockingQueue<>((arr.length+1) * (arr.length+1));
    	start.setPush(true);
    	start.setDistance(0);
    	queue.offer(start);
    	while(!queue.isEmpty()){
    		Point poll = queue.poll();
    		if(poll.equals(end)){
    			//找到路径；这个不一定是最短路径（但是第一条路径一定是层数最短的之一）;适用于只求步长不管权值
    			print(poll);
    			break;//#3
    			//continue;
    		}
    		LinkedList<Point> linkedList = map.get(poll.getBizNo());
    		for(Point p : linkedList){
    			if(!p.isPush()){
    				p.setPush(true);
    				p.setPre(poll);//谁将其拉入队列中的
    				queue.offer(p);
    			}
    		}
    	}
    }
    public static void print(Point p){
        while(p.getPre() != null){
        	System.out.print("("+p.getX()+","+p.getY()+")<--");
        	p = p.getPre();
        }
        System.out.println("("+p.getX()+","+p.getY()+")");
    }
}