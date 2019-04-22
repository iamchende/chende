package cn.suse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 搜索图中任意两个点之间所有的可达路径
 * 1.支持有向图、无向图
 * 2.微调可实现权重；此处默认距离就是权重，方便赋值
 * 类_190408SearchAllPath4Graph.java的实现描述：TODO 类实现描述 
 * @author chende 2019年4月8日 下午4:49:37
 */
public class _190408SearchAllPath4Graph {
	static DecimalFormat df = new DecimalFormat("#.00");
	private int bizNo = 0;
	double canArrive = 40;//可达的简单判断，认为两个节点间的距离小于等于这个数就是可达的。
    public static void main(String[] args) {
    	_190408SearchAllPath4Graph searchPath = new _190408SearchAllPath4Graph();
    	//1.构造n个点
    	List<Point> points = searchPath.getPointList("15,25|28,32|45,47|26,37");
    	//2.加入起点和终点
		Point start = searchPath.getPoint("0,6");
		Point end = searchPath.getPoint("50,65");
		points.add(start);
		points.add(end);
		//3.根据所有的点的可达性描述一个无向图出来，类似于hashMap的存储结构;有向图也是可以的，判断条件需要微调
		@SuppressWarnings("rawtypes")//数组不能用泛型
		LinkedList[] arr = new LinkedList[points.size()];
		for(Point p : points){
			if(p.equals(end))continue;//一旦到达终点就是一条路径，终点之后再搜索其他路径就没有意义了
			LinkedList<Point> list = new LinkedList<>();
			list.add(p);
			arr[p.getBizNo()]=list;
			for(Point t : points){
				double distance = searchPath.getDistance(p,t);
				if(0 < distance && distance < searchPath.canArrive){
					list.add(new Point(t.getX(),t.getY(),t.getBizNo(),distance));//相同的点，但是是不同的对象
				}
			}
		}
		searchPath.searchAllPath(start,end,arr);
	}
    /**
     * 实质是深度优先
     * @param start
     * @param end
     * @param arr
     */
    private void searchAllPath(Point start, Point end, @SuppressWarnings("rawtypes") LinkedList[] arr) {
		int num = 1;
		Stack<Point> stack = new Stack<>();
		a:while(true){
			if(stack.isEmpty()){//放入起点
				stack.push(start);
				start.setPush(true);
				continue;
			}
			if(stack.peek().equals(end)){//退出终点，此处的比较是坐标的比较
				//找到一条路径
				System.out.println("第"+(num++)+"条路径");
				print(stack);
				System.out.println();
				stack.pop();
			}
			@SuppressWarnings("unchecked")
			LinkedList<Point> linkedList = arr[stack.peek().getBizNo()];
			if(linkedList.size() > 1){
				for(Point p : linkedList.subList(1, linkedList.size())){
					if(!p.isPush() && !stack.contains(p)){
						stack.push(p);
						p.setPush(true);
						continue a;
					}
				}
			}
			//走到这里，说明当前栈顶元素已经没有可以使用的可达节点了，退出该节点
			Point pop = stack.pop();
			@SuppressWarnings("unchecked")
			LinkedList<Point> link = arr[pop.getBizNo()];
			for(Point p : link.subList(1, link.size())){
				p.setPush(false);
			}
			if(stack.isEmpty())return;
		}
	}
    public Point getPoint(String str){
		String[] split = str.split(",");
		return new Point(Integer.parseInt(split[0]),Integer.parseInt(split[1]),bizNo++);
	}
	public List<Point> getPointList(String str){
		List<Point> list = new ArrayList<>();
		String[] split = str.split("\\|");
		for(String s : split){
			list.add(getPoint(s));
		}
		return list;
	}
	public double getDistance(Point start, Point end) {
        double dx = Math.abs(start.getX()-end.getX());
        double dy = Math.abs(start.getY()-end.getY());
        return Double.valueOf(df.format(Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2))));
    }
	public static void print(Stack<Point> Points){
        List<Point> records =  new ArrayList<Point>(Points);
        StringBuffer sb = new StringBuffer();
        double distance = 0;
        for(Point p : records){
        	sb.append("->("+p.getX()+","+p.getY()+")");
        	distance += p.getDistance();//起点的这个值默认是0，不影响
        }
        System.out.print(sb.substring(2)+"	总距离："+df.format(distance));
    }
}
class Point {
	private int x;
	private int y;
	private int bizNo;//给每个点定一个编号
	private boolean push = false;//是否入栈
	private double distance;//表示能到达此节点的节点与本节点的距离（也可以表示权重）
	/**
	 * 这两个属性是后面广度深度搜索时加的
	 */
	private Point pre;
	private Point next;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getBizNo() {
		return bizNo;
	}
	public void setBizNo(int bizNo) {
		this.bizNo = bizNo;
	}
	public boolean isPush() {
		return push;
	}
	public void setPush(boolean push) {
		this.push = push;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	public Point(int x, int y, int bizNo) {
		super();
		this.x = x;
		this.y = y;
		this.bizNo = bizNo;
	}
	public Point(int x, int y, int bizNo, double distance) {
		super();
		this.x = x;
		this.y = y;
		this.bizNo = bizNo;
		this.distance = distance;
	}
	@Override
	public String toString() {
		return "Point [(" + x + "," + y + "), bizNo=" + bizNo + ", push=" + push + "]";
	}
	public Point getPre() {
		return pre;
	}
	public void setPre(Point pre) {
		this.pre = pre;
	}
	public Point getNext() {
		return next;
	}
	public void setNext(Point next) {
		this.next = next;
	}
}