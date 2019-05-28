import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class miniproject_2 
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		float input;
		System.out.print("請輸入門檻值(%):");
		input = scanner.nextFloat();
		//Timer timer;currentmillis
		
		String read = "D://mushrooms.txt";
		FileReader fr = new FileReader(read);
		BufferedReader bfr = new BufferedReader(fr);
		String write = "D://homework_2.txt";
		FileWriter wr = new FileWriter(write);
		BufferedWriter bwr = new BufferedWriter(wr);
		String g=null;//存放txt的每一行
		String[] get_char;//存放分割後的元素
		boolean flag=true;
		int t=3,data_time=0,product_time=0;
		float limit;
		//用來存放每一筆交易紀錄
		ArrayList<String> f = new ArrayList<String>();
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<ArrayList<String>>> allget = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<Integer>> alltime = new ArrayList<ArrayList<Integer>>();
		//產生元素
		ArrayList<String> elements = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		//計算次數
		ArrayList<Integer> time_get_1 = new ArrayList<Integer>();
		ArrayList<Integer> time_get_2 = new ArrayList<Integer>();
		//取得商品長度k
		ArrayList<ArrayList<String>> get_1 = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> get_2 = new ArrayList<ArrayList<String>>();
		
		while( (g=bfr.readLine())!=null )
		{
			get_char = g.split(" ");
			for(int i=0;i<get_char.length;i++)
			{  
				f.add(get_char[i]);
				if(!elements.contains(get_char[i]))
				{ 
					elements.add(get_char[i]);temp.add(get_char[i]);
					get_1.add(new ArrayList<String>(temp));temp.clear();
				}
			}
			data.add(new ArrayList<String>(f));
			f.clear();
			data_time++;
		}
		limit = data_time*(input/100);
		System.out.println("交易次數:" + data_time);
		elements = sort(elements);
		time_get_1 = count(1, data, get_1);
		get_2 = deal(comb(getelement(get_1)
				, new ArrayList<String>(), 2, getelement(get_1).size(), 2));
		time_get_2 = count(2, data, get_2);
		over((int) limit, time_get_1, get_1);
		over((int) limit, time_get_2, get_2);
		allget.add(get_1);allget.add(get_2);
		alltime.add(time_get_1);alltime.add(time_get_2);
		while(flag==true)
		{
			ArrayList<ArrayList<String>> get_t = product(allget.get(t-2), t-1);
			ArrayList<Integer> time_get_t = count(t, data, get_t);
			over((int) limit, time_get_t, get_t);
			if(get_t.size()!=0)
			{ allget.add(get_t);alltime.add(time_get_t);t++; }
			else
			{ flag = false; }
		}
		bwr.write("門檻值:" + limit);
		bwr.newLine();
		for(int i=0;i<allget.size();i++)
		{ 
			System.out.println(allget.get(i));System.out.println(alltime.get(i)); 
			for(int j=0;j<allget.get(i).size();j++)
			{ 
				bwr.write("[");
				for(int k=0;k<allget.get(i).get(j).size();k++)
				{ bwr.write(allget.get(i).get(j).get(k)+ " "); }
				bwr.write("]"); 
			}
			bwr.newLine();
			bwr.write("[");
			for(int j=0;j<alltime.get(i).size();j++)
			{ bwr.write(Integer.toString(alltime.get(i).get(j)) + " "); }
			bwr.write("]");
			bwr.newLine();
			product_time+=alltime.get(i).size();
		}
		System.out.println("相關商品組合數量:" + product_time);
		bwr.write("相關商品組合數量:" + product_time);
		bwr.flush();
		bwr.close();
		wr.close();
		bfr.close();
		fr.close();
	}
	/*----------排序----------*/
	public static ArrayList<String> sort(ArrayList<String> s)
	{
		ArrayList<String> p = new ArrayList<String>();
		ArrayList<Integer> k = new ArrayList<Integer>();
		for(int i =0;i<s.size();i++)
		{ k.add(Integer.parseInt(s.get(i))); }
		Collections.sort(k);
		for(int i =0;i<k.size();i++)
		{ p.add(String.valueOf(k.get(i))); }
		return p;
	}
	/*----------計算次數----------*/
	public static ArrayList<Integer> count(int k,ArrayList<ArrayList<String>> data
			,ArrayList<ArrayList<String>> g)
	{
		ArrayList<Integer> time = new ArrayList<Integer>();
		for(int i=0;i<data.size();i++)
		{
			for(int j=0;j<g.size();j++)
			{
				if(time.size()<g.size())
				{ time.add(0); }
				Set<String> compare = new HashSet<String>(data.get(i));
				compare.addAll(g.get(j));
				if(compare.size() == data.get(i).size())
				{ time.set(j, time.get(j)+1); }
			}
		}
		return time;
	}
	/*----------篩選(有無超過門檻值)----------*/
	public static void over(int k,ArrayList<Integer> a,ArrayList<ArrayList<String>> b)
	{
		for(int j=a.size()-1;j>=0;j--)
		{
			if(a.get(j) < k)
			{ b.remove(j);a.remove(j); }
		}
	}
	/*----------排列組合(回傳字串)(遞迴)----------*/
	public static String comb(ArrayList<String> f
			, ArrayList<String> t, int len, int m, int n) 
	{
	    String result = "";
	    for(int i=0;i<f.size();i++)
	    { t.add("0"); }
	    if (n == 0) 
	    {
	      for (int i = 0; i < len; i++)
	      { result += t.get(i)+" "; }
	      result += "\n";
	    } 
	    else 
	    {
	      t.set(n-1, f.get(m-1));
	      if (m > n - 1)
	      { result = comb(f, t, len, m - 1, n - 1); }
	      if (m > n) 
	      { result = comb(f, t, len, m - 1, n) + result; }
	    }
	    return result;
	}
	/*----------排列組合(切割)----------*/
	public static ArrayList<ArrayList<String>> deal(String s)
	{
		ArrayList<String> a = new ArrayList<String>();
		ArrayList<ArrayList<String>> b = new ArrayList<ArrayList<String>>();
		String[] k;String[] k2;
		k = s.split("\n");
		for(int i=0;i<k.length;i++)
		{
			k2 = k[i].split(" ");
			for(int j=0;j<k2.length;j++)
			{ a.add(k2[j]); }
			b.add(new ArrayList<String>(a));a.clear();
		}	
		return b;
	}
	/*----------產生元素----------*/
	public static ArrayList<String> getelement(ArrayList<ArrayList<String>> get)
	{
		Set<String> k = new HashSet<String>();
		for(int i=0;i<get.size();i++)
		{
			for(int j=0;j<get.get(i).size();j++)
			{ k.add(get.get(i).get(j)); }
		}
		ArrayList<String> p = new ArrayList<String>(k);
		p = sort(p);
		return p;
	}
	/*----------由長度k產生k+1的組合----------*/
	public static ArrayList<ArrayList<String>> product(ArrayList<ArrayList<String>> p,int num)
	{
		ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
		ArrayList<String> put = new ArrayList<String>();
		for(int i=0;i<p.size()-num;i++)
		{
			for(int j=i+1;j<p.size();j++)
			{
				ArrayList<String> first = p.get(i);
				ArrayList<String> second = p.get(j);
				Set<String> k = new HashSet<String>();
				for(int x=0;x<=first.size()-2;x++)
				{ k.add(first.get(x));k.add(second.get(x)); }
				if(k.size()==first.size()-1)
				{ 
					k.add(first.get(first.size()-1));k.add(second.get(second.size()-1));
					Iterator it = k.iterator();
					while(it.hasNext())
					{ put.add((String) it.next()); }
					temp.add(new ArrayList<String>(put));
					put.clear();
				}
			}
		}
		return temp;
	}
}

