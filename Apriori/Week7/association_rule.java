import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
public class homework_3 
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		float input,rinput;
		System.out.println("�п�J���e��(%):");
		System.out.println("�п�J�H���(%):");
		input = scanner.nextFloat();
		rinput = scanner.nextFloat();
		//Timer timer;currentmillis
		
		String read = "D://mushrooms.txt";
		FileReader fr = new FileReader(read);
		BufferedReader bfr = new BufferedReader(fr);
		String write = "D://product_k.txt";
		FileWriter wr = new FileWriter(write);
		BufferedWriter bwr = new BufferedWriter(wr);
		String g=null;//�s��txt���C�@��
		String[] get_char;//�s����Ϋ᪺����
		boolean flag=true;
		int t=3,data_time=0,product_time=0;
		float limit;
		//�ΨӦs��C�@���������
		ArrayList<String> f = new ArrayList<String>();
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<ArrayList<String>>> allget = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<Integer>> alltime = new ArrayList<ArrayList<Integer>>();
		//���ͤ���
		ArrayList<String> elements = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		//�p�⦸��
		ArrayList<Integer> time_get_1 = new ArrayList<Integer>();
		ArrayList<Integer> time_get_2 = new ArrayList<Integer>();
		//���o�ӫ~����k
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
		System.out.println("�������:" + data_time);
		elements = sort(elements);
		time_get_1 = count(1, data, get_1);
		get_2 = deal(comb(getelement(get_1)
				, new ArrayList<String>(), 2, getelement(get_1).size(), 2));
		time_get_2 = count(2, data, get_2);
		over((int) limit, time_get_1, get_1);
		over((int) limit, time_get_2, get_2);
		allget.add(get_1);
		allget.add(get_2);
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
		rule(allget, alltime,rinput);
		bwr.write("���e��:" + limit);
		bwr.newLine();
		for(int i=0;i<allget.size();i++)
		{ 
			System.out.println(allget.get(i));System.out.println(alltime.get(i)); 
			String s = allget.get(i)+" ";
			bwr.write(s);
			bwr.newLine();
			String b = alltime.get(i)+" ";
			bwr.write(b);
			bwr.newLine();
			product_time+=alltime.get(i).size();
		}
		System.out.println("�����ӫ~�զX�ƶq:" + product_time);
		bwr.write("�����ӫ~�զX�ƶq:" + product_time);
		bwr.flush();
		bwr.close();
		wr.close();
		bfr.close();
		fr.close();
	}
	/*----------�ƦC�զX(�į�t)----------*/
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
	/*----------�Ƨ�----------*/
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
	/*----------�p�⦸��----------*/
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
	/*----------�z��(���L�W�L���e��)----------*/
	public static void over(int k,ArrayList<Integer> a,ArrayList<ArrayList<String>> b)
	{
		for(int j=a.size()-1;j>=0;j--)
		{
			if(a.get(j) < k)
			{ b.remove(j);a.remove(j); }
		}
	}
	/*----------�ƦC�զX(�^�Ǧr��)(���j)----------*/
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
	/*----------�ƦC�զX(����)----------*/
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
	/*----------���ͤ���----------*/
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
	/*----------�Ѫ���k����k+1���զX----------*/
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
	/*----------���ͳW�h----------*/
	public static void rule(ArrayList<ArrayList<ArrayList<String>>> data
			, ArrayList<ArrayList<Integer>> count, float input) throws IOException
	{
		String write = "D://rule.txt";
		FileWriter wr = new FileWriter(write);
		BufferedWriter bwr = new BufferedWriter(wr);
		for(int i=1;i<data.size();i++)
		{
			for(int j=0;j<data.get(i).size();j++)
			{	
				for(int m=1;m<data.get(i).get(j).size();m++)
				{
					ArrayList<ArrayList<String>> get_k = new ArrayList<ArrayList<String>>();
					get_k = deal(comb(data.get(i).get(j), new ArrayList<String>()
							, m, data.get(i).get(j).size(), m));
					ArrayList<String> p = 
							new ArrayList<String>(data.get(i).get(j));
					ArrayList<String> a = new ArrayList<String>();
					for(int l=0;l<get_k.size();l++)
					{
						for(int n=0;n<get_k.get(l).size();n++)
						{
							a.add(get_k.get(l).get(n));
							p.remove(get_k.get(l).get(n));
						}
						a = sort(a);
						
						int index = data.get(i).indexOf(data.get(i).get(j));
						if(data.get(a.size()-1).indexOf(a)!=-1)
						{
							float over = (float) count.get(i).get(index)
								/ (float) count.get(a.size()-1).get(data.get(a.size()-1).indexOf(a));
							String s = a + "->" + p + ":" + over;
							if(over>(input/100))
							{
								System.out.println(s);
								bwr.write(s);
								bwr.newLine();
							}
						}
						for(int n=a.size()-1;n>=0;n--)
						{
							p.add(0,a.get(n));
							a.remove(n);
						}
						p = sort(p);
					}
				}
			}
		}
		bwr.flush();
		bwr.close();
		wr.close();
	}
}
