import java.util.*;
import java.io.*;
/**
* The btdb is the main driver class
*
* @author Stephanie Robles, Genesis Gaspar and Wesley Ong
*/
public class btdb
{
	/**
	* This is the main method and it contains the scanner. It creates a BTree class and a ValuesFile class.
	*
	*/
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
	/**
	* This is solves the command inputs and handles invalid inputs
	*
	* @param command is the command read by the scanner, IDKey is the ID provided with the key,
	* key is the word, tree is the BTree class, values is the ValuesFile class
	*/
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
	/**
	* This method checks if a given string can be converted to an integer
	*
	* @param temp is the string to be converted
	*/
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
