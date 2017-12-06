import java.util.*;
import java.io.*;
/**
* The ValuesFile stores the string values
*
* @author Stephanie Robles, Genesis Gaspar and Wesley Ong
*/
public class ValuesFile
{
    private int records;
    private HashMap<Integer, Integer> IDAndRecord;
    private RandomAccessFile file;
    private ArrayList<int[]> list;
	/**
	*  It creates a file if a file doesnt exist yet or reads the data in the file
	*
	* @param strFile is the file made, array is used updates the HashMap
	*/
    public ValuesFile(String strFile, ArrayList<int[]> array) throws IOException
    {
        IDAndRecord = new HashMap<Integer, Integer>();
        File file = new File(strFile);
        list = array;
        if(file.exists() != true)
        {
            this.records = 0;
            this.file = new RandomAccessFile(file, "rwd");
            this.file.seek(0);
            this.file.writeLong(this.records);
        }
        else
        {
            this.file = new RandomAccessFile(file, "rwd");
            this.file.seek(0);
            this.records = (int) this.file.readLong();
            updateVal(list);
        }
        System.out.print("> ");
    }
	/**
	* This method reads the records
	*
	*/
    public long getRec() throws IOException
    {
        this.file.seek(0);
        return this.file.readLong();
    }
	/**
	* This method gets by how much it offsets
	*
	* @param ID is the key
	*/
    public int getOffSet(int ID)
    {
        return IDAndRecord.get(ID);
    }
	/**
	* This method gets the line with the corresponding ID
	*
	* @param ID is the key
	*/
    public String getLine(int ID) throws IOException
    {
        int key = IDAndRecord.get(ID);
        this.file.seek(8 + ((key) * 256));
        int length = this.file.readShort();
        byte[] array = new byte[length];
        this.file.seek(8 + ((key * 256) + 16));
        this.file.read(array);
        String str = new String(array, "UTF8");
        return str;
    }
	/**
	* This method inserts the key and string into the records
	*
	* @param ID is the key, str is String record
	*/
    public void insert(int ID, String str) throws IOException
    {
        if(IDAndRecord.containsKey(ID) != true)
        {
            IDAndRecord.put(ID, records);
            int key = IDAndRecord.get(ID);
            this.file.seek(8 + (key * 256));
            byte[] array = str.getBytes("UTF-8");
            this.file.writeShort(array.length);
            this.file.seek(((key * 256) + 16) + 8);
            this.file.write(array);
            this.file.seek(0);
            records += 1;
            this.file.writeLong(this.records);
            System.out.print("< " + ID + " inserted" + "\n" + "> ");
        }
        else
        {
            System.out.print("< ERROR: " + ID + " already exists" + "\n" + "> ");
        }
    }
	/**
	* This method changes the String in the ID key
	*
	* @param ID is the key, val is String replacing the one in ID
	*/
    public void update(int ID, String val) throws IOException
    {
        if(IDAndRecord.containsKey(ID))
        {
            int key = IDAndRecord.get(ID);
            this.file.seek((key * 256) + 8);
            byte[] array = val.getBytes("UTF-8");
            this.file.writeShort(array.length);
            this.file.seek((key * 256) + 8 + 16);
            this.file.write(array);
            System.out.print("< " + ID + " updated" + "\n" + "> ");
        }
        else
        {
            System.out.print("< ERROR: " + ID + " does not exist" + "\n" + "> ");
        }
    }
	/**
	* This method selects the word in the ID
	*
	* @param ID is the key
	*/
    public void select(int ID) throws IOException
    {
        if(IDAndRecord.containsKey(ID))
        {
            String line = this.getLine(ID);
            System.out.print("< " + ID + " => " + line + "\n" + "> ");
        }
        else
        {
            System.out.print("< ERROR: " + ID + "does not exist" + "\n" + "> ");
        }
    }
	/**
	* This method exits the application
	*
	*/
    public void exit() throws IOException
    {
        this.file.close();
    }
	/**
	* This method updates the value key
	*
	* @param list is an ArrayList of integers from the BTree's updateRecord
	*/
    public void updateVal(ArrayList<int[]> list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            int[] temp = list.get(i);
            for(int j = 0; j < temp.length; j += 2)
            {
                if(temp[j] != -1)
                {
                    IDAndRecord.put(temp[j], temp[j + 1]);
                }
            }
        }
    }
	/**
	* This method creates a new ID for where there is space
	*
	* @param ID is the key made
	*/
    public boolean newID(int ID)
    {
        if(IDAndRecord.containsKey(ID))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}