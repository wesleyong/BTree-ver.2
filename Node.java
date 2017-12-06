import java.util.*;
import java.io.*;
/**
* The Node class holds the info of each node
*
* @author Stephanie Robles, Genesis Gaspar and Wesley Ong
*/
public class Node
{
    private int order;
    private int infoSize;
    private int mid;
    private int count;
    private int[] data;
	/**
	* This implements the node data type
	*
	*/
    public Node(int orderValue)
    {
        order = orderValue;
        infoSize = ((order - 1) * 3) + 2;
        if(order % 2 != 0)
        {
            mid = (((order + 1) / 2) * 3) - 1;
        }
        else
        {
            mid = (((order) / 2) * 3) - 1;
        }
        count = 0;
        data = new int[infoSize + 3];
        for(int i = 0; i < data.length; i++)
        {
            data[i] = -1;
        }
    }
	/**
	* This computes the total count of the records
	* 
	*/
    public void totalCount()
    {
        count = 0;
        for(int j = 2; j < data.length; j += 3)
        {
            if(data[j] != -1)
            {
                count++;
            }
        }
    }
	/**
	* This method uses the totalCount method
	*
	* @return count
	*/
    public int getCount()
    {
        totalCount();
        return count;
    }
	/**
	* This method returns the middle key
	*
	* @return mid
	*/
    public int getMid()
    {
        return mid;
    }
	/**
	* This method gets the totalData
	*
	* @return data
	*/
    public int[] totalData()
    {
        return data;
    }
	/**
	* This method gets the value in the data array
	*
	* @return value at data[i]
	*/
    public int getVal(int i)
    {
        return data[i];
    }
	/**
	* This method changes the value at index i with newVal
	*
	* @param i is the index, newVal is the new value to replace the i index
	*/
    public void changeVal(int i, int newVal)
    {
        data[i] = newVal;
    }
	/**
	* This method determines where in the data values the new ID should be inserted 
	*
	* @param ID is the ID to be inserted
	* @return place
	*/
    public int inserting(int ID)
    {
        int place = -1;
        for(int k = 2; k < (infoSize + 3); k += 3)
        {
            if(this.getVal(k) == -1 || this.getVal(k) > ID)
            {
                place = k;
                return place;
            }
        }
        return place;
    }
	/**
	* This method moves the data by one space so that the new ID can fit
	*
	* @param insertInd is the index where we insert the new ID
	*/
    public void move(int insertInd)
    {
        totalCount();
        int num = (count * 3) + 1;
        while(num >= insertInd)
        {
            int temp = this.getVal(num);
            this.changeVal(num + 3, temp);
            num--;
        }
        for(int l = insertInd; l < insertInd + 3; l++)
        {
            this.changeVal(l, -1);
        }
    }
	/**
	* This method inserts the ID and equicalent offset to data
	*
	* @param insertInd is the index where we insert the new ID, key is the ID, offVal is the value added
	*/
    public void insert(int insertInd, int key, int offVal)
    {
        changeVal(insertInd, key);
        changeVal(insertInd + 1, offVal);
    }
	/**
	* This method handles what occurs when a split is needed
	*
	* @param parentID is the ID of the parent
	*/
    public void split(int parentID)
    {
        for(int m = mid; m < (infoSize + 3); m++)
        {
            this.changeVal(m, -1);
        }
        this.changeVal(0, parentID);
    }
	/**
	* This method returns data in the array, excluding the slots for the excess IDs
	*
	* @return dataNoSplit
	*/
    public int[] getData()
    {
        int[] dataNoSplit = new int[infoSize];
        for(int n = 0; n < data.length - 3; n++)
        {
            dataNoSplit[n] = data[n];
        }
        return dataNoSplit;
    }
	/**
	* This method gets the keys
	*
	* @param rec is the record where the key needs to be found
	*/
    public int[] getKeys(int rec)
    {
        int[] something = new int[((infoSize - 2) / 3) * 2];
        int i = 0;
        for(int o = 2; o < data.length - 3; o += 3)
        {
            something[i] = data[o];
            i++;
            something[i] = data[o+1];
            i++;
        }
        return something;
    }
}
