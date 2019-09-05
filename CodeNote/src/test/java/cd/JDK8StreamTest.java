package cd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class JDK8StreamTest {
	
	private List<UserDemo> list;
	
	@Data
	private class UserDemo{
		private Long id;
		private String name;
		private int age;
		private Double salary;
		private Date birth;
		public UserDemo(Long id, String name, int age, Double salary, Date birth) {
			this.id = id;
			this.name = name;
			this.age = age;
			this.salary = salary;
			this.birth = birth;
		}
	}
	@Before
	public void preData(){
		List<UserDemo> list = new ArrayList<>();
		for(int i=0;i<20;i++){
			list.add(new UserDemo((long)i, "name"+(i%4), i, i*12345.0, new Date()));
		}
		this.list = list;
	}
	@Test
	public void testFilter(){
		List<UserDemo> collect = list.stream().filter(u-> u.getId()>18).collect(Collectors.toList());
		log.info("collect:{}", collect);
	}
	@Test
	public void testForEach(){
		list.stream().forEach(u -> System.out.print(u.getId()));
	}
	@Test
	public void testSorted(){
		List<UserDemo> collect = list.stream().sorted(Comparator.comparing(UserDemo::getName)).collect(Collectors.toList());
		log.info("collect:{}", collect);
		collect = list.stream().sorted(Comparator.comparing(UserDemo::getName).reversed()).collect(Collectors.toList());
		log.info("collect:{}", collect);
	}
	@Test
	public void testEItemMap2List(){
		List<Long> collect = list.stream().map(UserDemo::getId).collect(Collectors.toList());
		log.info("collect:{}", collect);
	}
	@Test
	public void testCopyList(){
		List<UserDemo> list2 = list.stream().collect(Collectors.toCollection(LinkedList::new));
		log.info("resutl:{},compare:{},Type:{}", list2, list == list2, list2.getClass().getSimpleName());
		List<UserDemo> list3 = list.stream().collect(Collectors.toList());
		log.info("resutl:{},compare:{},Type:{}", list2, list == list3, list3.getClass().getSimpleName());
	}
	@Test
	public void testMap2Set(){
		Set<UserDemo> collect = list.stream().collect(Collectors.toSet());
		log.info("collect:{}", collect);
		Set<String> collect2 = list.stream().map(UserDemo::getName).collect(Collectors.toSet());
		log.info("collect2:{}", collect2);
	}
	@Test
	public void testMap2Array(){
		UserDemo[] collect = list.stream().toArray(UserDemo[]::new);
		log.info("collect:{}", collect.length);
		UserDemo[] array = list.toArray(new UserDemo[list.size()]);
		log.info("array:{}", array.length);
	}
	@Test
	public void testItem2String(){
		String str = list.stream().map(UserDemo::getName).collect(Collectors.joining("@")).toString();
		log.info("str:{}", str);
	}
	@Test
	public void testItem2Map(){
		Map<Long, String> collect = list.stream().collect(Collectors.toMap(UserDemo::getId, UserDemo::getName));
		log.info("collect:{}", collect);
		//key重复不是覆盖，而是报错Duplicate key
		Map<String, UserDemo> collect2 = list.stream().collect(Collectors.toMap(UserDemo::getName, u -> u));
		log.info("collect2:{}", collect2);
		Map<Long, UserDemo> collect3 = list.stream().collect(Collectors.toMap(UserDemo::getId, Function.identity()));
		log.info("collect3:{}", collect3);
		//用如下方式处理key重复，即后面的覆盖前面的
		Map<String, UserDemo> collect4 = list.stream().collect(Collectors.toMap(UserDemo::getName, Function.identity(), (key1, key2) -> key2));
		log.info("collect4:{}", collect4);
		//用具体的linkedHashMap来保存数据
		LinkedHashMap<String, UserDemo> collect5 = list.stream().collect(Collectors.toMap(UserDemo::getName, Function.identity(), (key1, key2) -> key2, LinkedHashMap::new));
		log.info("collect5:{}", collect5);
	}
	@Test
	public void testItem2Function(){
		List<Integer> asList = Arrays.asList(1,2,3,4,5,6,7,8,9,2);
		List<Integer> collect = asList.stream().map(i -> i*i).collect(Collectors.toList());
		log.info("collect:{}", collect);
		List<String> asList2 = Arrays.asList("a","b","c","d","e");
		List<String> collect2 = asList2.stream().map(String::toUpperCase).collect(Collectors.toList());
		log.info("collect2:{}", collect2);
		Optional<Integer> max = asList.stream().max(Comparator.comparing(Function.identity()));
		log.info("max:{}", max.get());
		Optional<Integer> min = asList.stream().min(Comparator.comparing(i -> i));
		log.info("min:{}", min.get());
		long count = asList.stream().filter(i -> i>7).count();
		log.info("count:{}", count);
		List<Integer> collect3 = asList.stream().distinct().collect(Collectors.toList());
		log.info("collect3:{}", collect3);
		List<Integer> collect4 = asList.stream().limit(6l).collect(Collectors.toList());//取前6个对象
		log.info("collect4:{}", collect4);
		List<Integer> collect5 = asList.stream().skip(6l).collect(Collectors.toList());//忽略前6个对象
		log.info("collect5:{}", collect5);
		//This method exists mainly to support debugging;peek方法只有元素被处理才会被调用。反正不建议用就对了！！！
		List<Integer> collect6 = asList.stream().filter(i -> i>7).peek(i ->{i++;i+=2;}).collect(Collectors.toList());
		log.info("collect6:{}", collect6);
	}
	@Test
	public void testFlatMap(){
		List<List<Integer>> list = Arrays.asList(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
        List<Integer> collect = list.stream().flatMap(child -> child.stream()).collect(Collectors.toList());
		//这里用Stream.of(list)得到的是List<List<Integer>>类型
        log.info("collect:{}", collect);
	}
	@Test
	public void testMath(){
		log.info("{}", list.stream().allMatch(u -> u.getId()>15));
		log.info("{}", list.stream().anyMatch(u -> u.getId()>15));
		log.info("{}", list.stream().noneMatch(u -> u.getId()<1));
	}
	@Test
	public void testFind(){
		/**
		 * 都是返回的过滤元素后得到的集合中的元素，findFirst表示集合的第一个元素，findAny如果是串行流则也是返回第一个，如果是并行流则不一定。后者效率略高
		 */
		Optional<UserDemo> findAny = list.stream().filter(u -> u.getId()>15).findAny();
		if(findAny.isPresent()){
			log.info("{}", findAny.get());
		}
		Optional<UserDemo> findFirst = list.stream().filter(u -> u.getId()>15).findFirst();
		if(findFirst.isPresent()){
			log.info("{}", findFirst.get());
		}
	}
	@Test
	public void testReduce() {
		List<Integer> asList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 2);
		// 一个参数
		Integer integer = asList.stream().reduce(new BinaryOperator<Integer>() {
			@Override
			public Integer apply(Integer i1, Integer i2) {
				return i1 + i2;
			}
		}).get();
		log.info("integer:{}", integer);
		// 二个参数,将第一个参数作为流中的第一个元素进行计算，类型与流中元素的类型一致。返回值也变成第一个元素的类型了
		Integer integer2 = asList.stream().reduce(100, new BinaryOperator<Integer>() {
			@Override
			public Integer apply(Integer i1, Integer i2) {
				return i1 + i2;
			}
		});
		log.info("integer2:{}", integer2);
		// 三个参数
		//并行计算，第一步三个数分别和初始值10相加，然后将这三个结果相加
		Integer reduce = Stream.of(1, 2, 3).parallel().reduce(10, new BiFunction<Integer, Integer, Integer>() {
			@Override
			public Integer apply(Integer i1, Integer i2) {
				return i1 + i2;
			}
		}, new BinaryOperator<Integer>() {
			@Override
			public Integer apply(Integer i1, Integer i2) {
				return i1 + i2;
			}
		});
		log.info("并行计算reduce:{}", reduce);
		//默认串行计算，第三个参数无意义（中间这个）；和两个参数的效果一样
		Integer reduce2 = Stream.of(1, 2, 3).reduce(10, new BiFunction<Integer, Integer, Integer>() {
			@Override
			public Integer apply(Integer i1, Integer i2) {
				return i1 + i2;
			}
		}, new BinaryOperator<Integer>() {
			@Override
			public Integer apply(Integer i1, Integer i2) {
				return i1 + i2;
			}
		});
		log.info("串行计算reduce2:{}", reduce2);
	}
}
