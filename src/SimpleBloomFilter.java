/*
 * @author doodlesomething@163.com
 * @date 2014-12-30
 * @version 1.0
 * @description 布隆过滤器 --用于过滤海量url中已经访问过的url节省存储空间
 			访问过的位为1，否则为0

 */

import java.util.BitSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;




public class SimpleBloomFilter {
	
	private static final int DEFAULT_SIZE = 1 << 24;
	//质数因子
	private static final int[] seeds = new int[]{3,5,7,11,13,31,37,61,};
	public  BitSet bits = new BitSet(DEFAULT_SIZE);
	private SimpleHash[] hashInstances = new SimpleHash[seeds.length];
	
	public SimpleBloomFilter() {
		for(int i = 0; i < seeds.length; i++) {
			hashInstances[i] = new SimpleHash(DEFAULT_SIZE,seeds[i]);
		}
		//蒋所有的位置为0
		for(int i =0; i < DEFAULT_SIZE; i++) {
			bits.set(i, false);
		}
	}
	
	
	/*
	 * @descripiont set bits to 1
	 */
	public void add(String value) {
		for (SimpleHash hi:hashInstances) {
			bits.set(hi.hash(value),true);
		}
	}
	
	/*
	 * @description:jude the url is contain or not
	 */
	public boolean contains(String value) {
		if (value == null)
			return false;
		
		boolean result = true;
		for (SimpleHash hi:hashInstances) {
			result = result && bits.get(hi.hash(value));
		}
		
		return result;
	}
	
	
	/*
	 * @description:save the bloomfilter bits 
	 * if there something wrong 
	 */
	public void saveBit(String fileName) {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
	
		
		try {
			fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(bits);
	
		}catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			if (fos != null) {
				try {
					fos.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			if (oos != null) {
				try {
					oos.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/*
	 * @desription: read the bits from file to build
	 * bloomfilter again
	 */
	public void readBit(String FileName){
		File file = new File(FileName);
		
		if (!file.exists()) 
			return;
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try {
			fis = new FileInputStream(FileName);
			ois = new ObjectInputStream(fis);
			bits.clear();
			
			try {
				bits = (BitSet) ois.readObject();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			if (fis != null) {
				try {
					fis.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			if (ois != null) {
				try {
					ois.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	
	/*
	 * @description:this class provide hash value
	 */
	public class SimpleHash {
		private int capacity;
		private int seed;
		
		public SimpleHash(int capacity,int seed) {
			this.capacity = capacity;
			this.seed = seed;
		}
		
		public int hash(String value) {
			int result = 0;
			int len = value.length();
			
			for(int i = 0; i < len; i++) {
				result = seed * result + value.charAt(i);
			}
			
			result = (capacity - 1) & result;
			
			return result;
		}
	}
	
	
	
}
 

