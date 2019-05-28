import java.io.*;
import java.util.Scanner;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.time.*;
public class find_length2
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException
	{
		//Scanner scanner = new Scanner(System.in);
		int input;
		//System.out.print("請輸入門檻值:");
		//input = scanner.nextInt();
		//Timer timer;currentmillis
		
		String read = "D://mushrooms.txt";
		FileReader fr = new FileReader(read);
		BufferedReader bfr = new BufferedReader(fr);
		//String write = "D://put.txt";
		//FileWriter wr = new FileWriter(write);
		//BufferedWriter bwr = new BufferedWriter(wr);
		String g=null;//存放txt的每一行
		String[] get_char;//存放分割後的元素
		//用來存放每一筆交易紀錄
		ArrayList<String> f = new ArrayList<String>();
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		//排序大小
		ArrayList<String> p = new ArrayList<String>();
		ArrayList<String> elements = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		//計算次數
		ArrayList<Integer> time = new ArrayList<Integer>();
		ArrayList<Integer> time_get2 = new ArrayList<Integer>();
		//取得商品長度k
		ArrayList<ArrayList<String>> get_1 = new ArrayList<ArrayList<String>>();
		String s_get_k;
		ArrayList<ArrayList<String>> get_k = new ArrayList<ArrayList<String>>();
		
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
		}
		time = count(1, data, get_1);
		s_get_k = comb(elements, new ArrayList<String>(), 2, elements.size(), 2);
		get_k = deal(s_get_k);
		time_get2 = count(2,data, get_k);
		//over(7574, time_get2, get_k);
		
		//System.out.println("交易紀錄:" + data);
		//System.out.println("商品長度為1:" + elements);
		//System.out.println(time);
		System.out.println("商品長度為2:" + get_k);
		System.out.println("出現次數:" + time_get2);
		/*
		for(int i=0;i<get_k.size();i++)
		{ 
			for(int j=0;j<get_k.get(i).size();j++)
			{ bwr.write(get_k.get(i).get(j)+" "); }
			bwr.write(Integer.toString(time_get2.get(i)));
			bwr.newLine();
		}*/
		//bwr.flush();
		//bwr.close();
		//wr.close();
		bfr.close();
		fr.close();
	}
	/*----------排列組合(效能差)----------*/
	public static ArrayList<ArrayList<String>> perm(ArrayList<String> buf,int k)
	{
		ArrayList<String> a = new ArrayList<String>();
		ArrayList<ArrayList<String>> b = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
		String[] t;
		for(int i=1 ; i<(1<<buf.size()) ; i++)
		{
		    String result = "";
			for(int j=0 ; j<buf.size() ; j++)
			{
				if((i & (1<<j)) != 0)
				{ result+=buf.get(j)+" "; }
			}
			t = result.split(" ");
			for(int j=0;j<t.length;j++)
			{ a.add(t[j]); }
			b.add((new ArrayList<String>(a)));a.clear();
		}
		for(int i=0;i<b.size();i++)
		{
			if(b.get(i).size() == k)
			{ temp.add(b.get(i)); }	
		}		
		return temp;
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
				if(k==1)
				{
					if(time.size()<g.size())
					{ time.add(0); }
					if(data.get(i).indexOf(g.get(j).get(0)) != -1)
					{ time.set(j, time.get(j)+1); }
				}
				if(k==2)
				{
					if(time.size()<g.size())
					{ time.add(0); }
					if(data.get(i).indexOf(g.get(j).get(0)) != -1
							&& data.get(i).indexOf(g.get(j).get(1)) != -1)
					{ time.set(j, time.get(j)+1); }
				}
			}
		}
		return time;
	}
	/*----------篩選----------*/
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
}
