import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.html.MinimalHTMLWriter;


public class FPGrowth 
{
	private static int mini_support = 0;
	private static double time1, time2;
	private static int totaltime = 0;
	private static int line = 0;
	
	/*-----treenode-----*/
	public static class TreeNode 
	{
		private int count,item;
		private TreeNode parent;
		private ArrayList<TreeNode> children = new ArrayList<TreeNode>();
		private TreeNode nextNode;
		
		public int getItem()
		{ return item; }
		
		public void setItem(int u)
		{ item = u; }
		
		public TreeNode getParentNode()
		{ return parent; }
		
		public void setParentNode(TreeNode ParentNode)
		{ parent = ParentNode; }
		
		public ArrayList<TreeNode> getChildNodes() 
		{ return children; }
		
		public void setChildNodes(ArrayList<TreeNode> childNodes) 
		{ children = childNodes; }
		
		public int getCounts() 
		{ return count; }
		
		public void increaseCounts() 
		{ count = count + 1; }
		
		public TreeNode getNextNode() 
		{ return nextNode; }
		
		public void setNextNode(TreeNode next) 
		{ nextNode = next; }
		
		public void setCounts(int c) 
		{ count = c; }
	}

	/*-----�̷�map���ǱƧ�-----*/
	public static void itemsort(final Map<Integer, Integer> itemMap,ArrayList<ArrayList<Integer>> item)
	{
		for(int i=0; i<item.size(); i++)
		{
			Collections.sort(item.get(i), new Comparator<Integer>(){

				public int compare(Integer key1, Integer key2) 
				{ return itemMap.get(key2) - itemMap.get(key1); }
			});
		}	
	}
	
	/*-----�إ�head table-----*/
	public static ArrayList<TreeNode> BuildHeadTable(ArrayList<ArrayList<Integer>> item)
	{
		ArrayList<TreeNode> head = new ArrayList<TreeNode>();
		Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();
		
		/*-----�p�⦸�Ʃ�Jmap��-----*/
		for(int i=0;i<item.size();i++)
		{
			for(int j=0;j<item.get(i).size();j++)
			{
				if(itemMap.get(item.get(i).get(j)) == null)
				{ itemMap.put(item.get(i).get(j), 1); }
				else
				{ itemMap.put(item.get(i).get(j), itemMap.get(item.get(i).get(j))+1); }
			}
		}
		
		/*-----�P�_�O�_�p��support-----*/
		Iterator<Integer> itr = itemMap.keySet().iterator();
		ArrayList<Integer> abandonSet = new ArrayList<Integer>();
		while(itr.hasNext())
		{
			int key = itr.next();
			if( itemMap.get(key) < mini_support )
			{
				itr.remove();
				abandonSet.add(key);
			}
			else
			{
				TreeNode node = new TreeNode();
				node.increaseCounts();
				node.setItem(key);
				node.setCounts(itemMap.get(key));
				head.add(node);
			}
		}

		for(ArrayList<Integer> items : item) 
		{ items.removeAll(abandonSet); }
		
		itemsort(itemMap, item);
		
		/*-----�Ƨ�treenode-----*/
		Collections.sort(head, new Comparator<TreeNode>() {
			public int compare(TreeNode key1, TreeNode key2) 
			{ return key2.getCounts() - key1.getCounts(); }
		});
		
		return head;
	}
	
	/*-----�M��l�`�I-----*/
	public static TreeNode findChildNode(int item, TreeNode currentNode)
	{
		ArrayList<TreeNode> childs = currentNode.getChildNodes();
		if( childs != null )
		{
			/*-----���X�l�`�I-----*/
			for(int i=0;i<childs.size();i++)
			{
				/*-----���Mitem�ۦP���`�I�ɫh�^��-----*/
				if( childs.get(i).getItem() == item )
				{ return childs.get(i); }
			}
		}
		return null;
	}
	
	/*-----�Nhead table�Pnode�ۦP���s��(�걵head table)-----*/
	public static void addSameNode(TreeNode tn, ArrayList<TreeNode> head) 
	{
		TreeNode currentNode = null;
		
		/*-----���Xhead table-----*/
		for(int i=0;i<head.size();i++) 
		{
			if(head.get(i).getItem() == tn.getItem()) 
			{
				currentNode = head.get(i);
				/*-----ex.(49)�|����s�����̫�@��49���`�I-----*/
				while(currentNode.getNextNode() != null) 
				{ currentNode = currentNode.getNextNode(); }
				currentNode.setNextNode(tn);
			}
		}
	}
	
	/*-----�إ�FP Tree-----*/
	public static TreeNode buildFPTree(ArrayList<ArrayList<Integer>> itemSet, ArrayList<TreeNode> head)
	{
		TreeNode root = new TreeNode();
		TreeNode currentNode = root;

		for(int i=0;i<itemSet.size();i++) 
		{
			for(int j=0;j<itemSet.get(i).size();j++)
			{
				TreeNode temp = findChildNode(itemSet.get(i).get(j), currentNode);
				if(temp == null) 
				{
					temp = new TreeNode();
					temp.setItem(itemSet.get(i).get(j));
					temp.setParentNode(currentNode);
					currentNode.getChildNodes().add(temp);
					addSameNode(temp, head);
				}
				currentNode = temp;
				temp.increaseCounts();
			}
			currentNode = root;
		}
		//TraverseTree(root, 0);
		return root;
	}
	
	/*-----Scan DataBase-----*/
	public static ArrayList<ArrayList<Integer>> readFile(String path) throws IOException
	{
		FileReader fr = new FileReader(path);
		BufferedReader bfr = new BufferedReader(fr);
		String str;
		
		ArrayList<ArrayList<Integer>> dataSet = new ArrayList<ArrayList<Integer>>();
		
		while((str = bfr.readLine()) != null) 
		{
			line++;
			ArrayList<Integer> tempList = new ArrayList<Integer>();
			String[] s = str.split(" ");
			for(int i = 0; i < s.length; i++) 
			{ tempList.add(Integer.parseInt(s[i])); }
			dataSet.add(tempList);
		}
		bfr.close();
		fr.close();
		return dataSet;
	}
	
	public static void FPAlgorithm(ArrayList<ArrayList<Integer>> itemSet, ArrayList<Integer> candidatePattern) throws IOException 
	{
		
		// build head table
		ArrayList<TreeNode> head = BuildHeadTable(itemSet);
		
		// build FP tree
		TreeNode root = buildFPTree(itemSet, head);

		// recursion exit
		if(root.getChildNodes().size() == 0) 
		{ return; }
		
		if( head.size() <= 0 || root == null )
		{ return; }
		/*-----���Xhead table�Ҧ���node-----*/
		for(TreeNode hd : head)
		{
			ArrayList<Integer> pattern = new ArrayList<Integer>();
			pattern.add(hd.getItem());totaltime++;
			
			if(candidatePattern != null) 
			{ pattern.addAll(candidatePattern); }
			System.out.println(pattern);
			
			//System.out.println("����" + hd.getItem() + "�����󦡸�Ʈw");
			/*-----���ͱ��󦡸�Ʈw-----*/
			ArrayList<ArrayList<Integer>> newItemSet = new ArrayList<ArrayList<Integer>>();
			TreeNode currentNode = hd.getNextNode();
			
			while (currentNode != null)
			{
                int count = currentNode.getCounts();
                ArrayList<Integer> parentNodes = new ArrayList<Integer>();
                TreeNode parent = currentNode;
                
                /*-----���X���`�I�å[�J-----*/
                while( (parent = parent.getParentNode()).getCounts() > 0)
                { parentNodes.add(parent.getItem()); }
                
                while( count-- > 0 )
                { newItemSet.add(parentNodes); }
             
                currentNode = currentNode.getNextNode();
            }
            // recursive process
			FPAlgorithm(newItemSet, pattern);
		}
		
		
	}
	
	public static void TraverseTree(TreeNode n,int d)
	{
		System.out.println("(" + n.getItem() + "," + n.getCounts() + ")");
		d++;

		for(int i=0;i<n.getChildNodes().size();i++)
		{
			for(int j=0;j<d;j++)
			{
				System.out.print(" ");
			}
			System.out.print("|-->");
			TraverseTree(n.getChildNodes().get(i),d); 
		}
		
	}
	
	public static void main(String[] args) throws IOException
	{	
		Scanner scanner = new Scanner(System.in);
		System.out.println("�п�J���e��(%): ");
		float input = scanner.nextFloat();
		ArrayList<ArrayList<Integer>> ds = readFile("D://Mushrooms.txt");
		mini_support = Math.round(line * (input/100));
		System.out.println("���e��: " + mini_support);
		time1 = System.currentTimeMillis();
		FPAlgorithm(ds, null);
		time2 = System.currentTimeMillis();
		System.out.println("�������: " + line);
		System.out.println("�����ӫ~�զX�ƶq: " + totaltime);
		System.out.println("��O�ɶ��G " + (time2 - time1) + " ms");
	}
}