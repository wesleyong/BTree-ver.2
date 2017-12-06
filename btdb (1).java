import java.util.*;
import java.io.*;
public class btdb
{
    public static void main(String[] args) throws IOException
    {
        Btree tree = new Btree(args[0]);
        ArrayList<int[]> array = Btree.updateRecord();
        ValuesFile values = new ValuesFile(args[1], array);
        Scanner sc = new Scanner(System.in);
        String command = "";
        String ID = "";
        String key = "";
        int IDKey = -1;
        String temp = sc.nextLine();
        String[] input = temp.split(" ");
        if(input.length > 0)
        {
            command = input[0];
        }
        else
        {
            command = "ERROR: INVALID COMMAND";
        }
        if(input.length > 1)
        {
            ID = input[1];
            if(integerConverter(ID))
            {
                IDKey = Integer.parseInt(ID);
            }
        }
        else
        {
            ID = "There is no ID";
        }
        if(input.length > 2)
        {
            key = "";
            for(int i = 2; i < input.length - 1; i++)
            {
                key += input[i] + " ";
            }
            key += input[input.length - 1];
        }
        else
        {
            key = "";
        }
        while(!command.equals("exit"))
        {
            solves(command, IDKey, key, tree, values);
            temp = sc.nextLine();
            input = temp.split(" ");
            if(input.length > 0)
            {
                command = input[0];
            }
            else
            {
                command = "ERROR: INVALID COMMAND";
            }
            if(input.length > 1)
            {
                ID = input[1];
                if(integerConverter(ID))
                {
                    IDKey = Integer.parseInt(ID);
                }
                else
                {
                    IDKey = -1;
                }
            }
            else
            {
                ID = "There is no ID";
                IDKey = -1;
            }
            if(input.length > 2)
			 {
				 key = "";
				 for(int i = 2; i < input.length - 1; i++)
				 {
					 key += input[i] + " ";
				 }
				 key += input[input.length - 1];
			 }
			 else
			 {
				 key = "";
			 }
        }
        sc.close();
    }
    public static void solves(String command, int IDKey, String key, Btree tree, ValuesFile values) throws IOException
	{
		if(command.equals("insert") && IDKey != -1)
		{
			boolean go = values.newID(IDKey);
			values.insert(IDKey, key);
			if(go == true)
			{
				int offSet = values.getOffSet(IDKey);
				tree.insert(IDKey, offSet);
			}
		}
		else if(command.equals("update") && IDKey != -1)
		{
			values.update(IDKey, key);
		}
		else if(command.equals("select") && IDKey != -1)
		{
			values.select(IDKey);
		}
		else if(command.equals("exit"))
		{	
			tree.exit();
			values.exit();
		}
		else
		{
			System.out.println("ERROR: INVALID COMMAND");
		}
	}
	public static boolean integerConverter(String temp)
	{
		 try 
		 { 
			 Integer.parseInt(temp); 
		 } 
		 catch(NumberFormatException e) 
		 { 
	        return false; 
		 }
		 catch(NullPointerException e) 
		 {
	        return false;
		 }
		 return true;
	}
}
