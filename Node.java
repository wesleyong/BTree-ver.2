import java.util.*;
import java.io.*;
public class Node
{
    private int order;
    private int infoSize;
    private int mid;
    private int count;
    private int[] data;
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
    public int getCount()
    {
        totalCount();
        return count;
    }
    public int getMid()
    {
        return mid;
    }
    public int[] totalData()
    {
        return data;
    }
    public int getVal(int i)
    {
        return data[i];
    }
    public void changeVal(int i, int newVal)
    {
        data[i] = newVal;
    }
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
    public void insert(int insertInd, int key, int offVal)
    {
        changeVal(insertInd, key);
        changeVal(insertInd + 1, offVal);
    }
    public void split(int parentID)
    {
        for(int m = mid; m < (infoSize + 3); m++)
        {
            this.changeVal(m, -1);
        }
        this.changeVal(0, parentID);
    }
    public int[] getData()
    {
        int[] dataNoSplit = new int[infoSize];
        for(int n = 0; n < data.length - 3; n++)
        {
            dataNoSplit[n] = data[n];
        }
        return dataNoSplit;
    }
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
