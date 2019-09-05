package cn.suse;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提取标签中的文本内容
 * 类_190422TagContentExtractor.java的实现描述：TODO 类实现描述 
 * @author chende 2019年4月22日 上午11:24:23
 */
public class _190422TagContentExtractor {
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		int testCases = Integer.parseInt(in.nextLine());
		while(testCases>0){
			String line = in.nextLine();
			/**
			 * 4
			 * <h1>Nayeem loves counseling</h1>
			 * <h1><h1>Sanjay has no watch</h1></h1><par>So wait for a while</par>
			 * <Amee>safat codes like a ninja</amee>
			 * <SA premium>Imtiaz has a secret crush</SA premium>
			 * @param xml
			 * @return
			 */
          	//Write your code here
			Pattern p = Pattern.compile("<(.+)>([^<]+)</\\1>");
			Matcher m = p.matcher(line);
			if(m.find()){
				m.reset();
				while(m.find()){
					System.out.println(m.group(2));
				}
			}else{
				System.out.println("None");
			}
			testCases--;
		}
	}
}