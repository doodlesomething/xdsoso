/*
 * @author:doodlesomething@163.com
 * @date:2015-1-9
 * @version:1.0
 * @description: 自定义字典树
 * 
 */


import java.util.HashMap;
import java.util.Map;

public class TreeNode {
	

	
	private String character;	//the key of node
	private int frequency = -1;	
	private double antilog = -1;	//cost
	private Map<String,TreeNode> children;  
	
	
	/*
	 * @description:set or get method
	 */
	public void setCharacter(String ch) {
		this.character = ch;
	}
	
	public String getCharacter() {
		return character;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void setAntilog(double antilog) {
		this.antilog = antilog;
	}
	
	public double getAntilog() {
		return antilog;
	}
	
	
	
	/*
	 * @description:add new child
	 */
	public void addChild(TreeNode node) {
		if (children == null) {
			children = new HashMap<String,TreeNode>();
		}
		
		if (!children.containsKey(node.getCharacter())) {
			children.put(node.getCharacter(), node);
		}
		
	}
	
	
	/*
	 * @description:get a child
	 */
	public TreeNode getChild(String ch) {
		if (children == null || !children.containsKey(ch))
			return null;
		
		return children.get(ch);
	}
	
	//remove
	public void removeChild(String ch) {
		if (ch == null || !children.containsKey(ch))
			return;
		
		children.remove(ch);
		
	}
	
	
}
