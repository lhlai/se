package se.extractor.algorithm;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.ParserException;
/**
 * 计算网页相似度
 * @author Administrator
 */
public class PageDistance {
	
	public static double getPageSim(String urlStr1, String urlStr2) 
			throws ParserException, IOException{
		ArrayList<Node> pageNodes1 = new ArrayList<Node>();
		URL url = new URL(urlStr1);
		Node node;
		Lexer lexer = new Lexer(url.openConnection());
		lexer.setNodeFactory(new PrototypicalNodeFactory());
		while(null != (node = lexer.nextNode())){
			pageNodes1.add(node);
		}
		ArrayList<Node> pageNodes2 = new ArrayList<Node>();
		URL url2 = new URL(urlStr1);
	    lexer = new Lexer(url2.openConnection());
		lexer.setNodeFactory(new PrototypicalNodeFactory());
		while(null != (node = lexer.nextNode())){
			pageNodes2.add(node);
		}
		Node[] node1 = (Node[])pageNodes1.toArray(new Node[pageNodes1.size()]);
		Node[] node2 = (Node[])pageNodes2.toArray(new Node[pageNodes2.size()]);
		//node1与node2的最长公共子序列
		List<Node> node3 = PageDistance.longestCommonSubsequence(node1, node2);
//		System.out.println("pageNodes1是:"+pageNodes1);
		System.out.println("node2是:"+node2);
		System.out.println("node3是:"+node3);
		return (2.0*node3.size())/
				((double)pageNodes1.size()+(double)pageNodes2.size());
	}
    //计算两个集合序列的最长公共子序列
	public static <E> List<E> longestCommonSubsequence(E[] s1, E[] s2){
		//二维数组，初始化为0
		int[][] num = new int[s1.length+1][s2.length+1];
		
		//动态规划
		for(int i=1;i<=s1.length;i++){
			for(int j=1;j<=s2.length;j++){
				if(s1[i-1].equals(s2[j-1])){
					num[i][j] = 1 + num[i-1][j-1];
				}else{
					num[i][j] = Math.max(num[i-1][j], num[i][j-1]);
				}
			}
		}
		System.out.println("length of LCS = "+num[s1.length][s2.length]);
		
		int s1position = s1.length;
		int s2position = s2.length;
		List<E> result = new LinkedList<E>();
		
		while(s1position!=0&&s2position!=0){
			if(s1[s1position-1].equals(s2[s2position-1])){
				result.add(s1[s1position-1]);
				s1position--;
				s2position--;
			}else if(num[s1position][s1position-1]>=num[s1position-1][s1position]){
				s2position--;
			}else{
				s1position--;
			}
		}
		Collections.reverse(result);
		return result;
	}
}
