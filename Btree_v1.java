import java.util.*;
import java.io.*;
public class Btree
{
    private HashMap<Integer, Node> node;
    private final int order = 7;
    private int nodeSize;
    private int maxByte;
    private int totalRecords;
    private int rootNode;
    private RandomAccessFile file;
    public Btree(String strFile) throws IOException
    {
        File file = new File(strFile);
        node = new HashMap<Integer, Node>();
        nodeSize = ((order - 1) * 3) + 2;
        maxByte = nodeSize * 8;
        if(!file.exists())
        {
            this.totalRecords = 0;
            this.rootNode = 0;
            this.file = new RandomAccessFile(file, "rwd");
            this.file.seek(0);
            this.file.writeLong(this.totalRecords);
            this.file.writeLong(this.rootNode);
        }
        else
        {
            this.file = new RandomAccessFile(file, "rwd");
            this.file.seek(0);
            this.readData(node);
        }
    }
    public void insert(int ID, int offNum) throws IOException
    {
        int insertRecord = -1;
        if(totalRecords <= 1)
        {
            insertRecord = 0;
        }
        else
        {
            insertRecord = findRec(ID, node, rootNode);
        }
        if(!node.containsKey(insertRecord))
        {
            Node bNode = new Node(order);
            node.put(insertRecord, bNode);
            totalRecords++;
        }
        int insertIndex = node.get(insertRecord).inserting(ID);
        if(node.get(insertRecord).getVal(insertIndex) > ID)
        {
            node.get(insertRecord).move(insertIndex);
        }
        node.get(insertRecord).insert(insertIndex, ID, offNum);
        writeData(insertRecord, node);
        split(insertRecord, node);
        printRecord(node);
    }
    public void split(int insertRecord, HashMap<Integer, Node> node) throws IOException
    {
        int num = node.get(insertRecord).getCount();
        if(num >= order)
        {
            int[] totalData = node.get(insertRecord).totalData();
            int mid = node.get(insertRecord).getMid();
            int parent = node.get(insertRecord).getVal(mid);
            int leftNum = insertRecord;
            int rightNum = totalRecords;
            Node right = new Node(order);
            node.put(rightNum, right);
            totalRecords++;
            int offNum = node.get(insertRecord).getVal(mid + 1);
            if(insertRecord == rootNode)
            {
                Node newRootNode = new Node(order);
                rootNode = totalRecords;
                totalRecords++;
                newRootNode.changeVal(1, leftNum);
                newRootNode.changeVal(2, parent);
                newRootNode.changeVal(3, offNum);
                newRootNode.changeVal(4, rightNum);
                node.put(rootNode, newRootNode);
                writeData(rootNode, node);
            }
            else
            {
                int a = node.get(rootNode).inserting(parent);
                if(node.get(rootNode).getVal(a) > parent)
                {
                    node.get(rootNode).move(a);
                }
                node.get(rootNode).insert(a, parent, offNum);
                node.get(rootNode).changeVal(a - 1, leftNum);
                node.get(rootNode).changeVal(a + 2, rightNum);
                writeData(rootNode, node);
            }
            int index = 1;
            node.get(rightNum).changeVal(0, rootNode);
            for(int i = mid + 2; i < totalData.length; i++)
            {
                node.get(rightNum).changeVal(index, totalData[i]);
                index++;
            }
            writeData(rightNum, node);
            node.get(insertRecord).split(rootNode);
            writeData(insertRecord, node);
            split(rootNode, node);
        }
    }
    public int findRec(int ID, HashMap<Integer, Node> node, int record)
    {
        int[] data = node.get(record).getData();
        for(int j = 2; j < data.length; j += 3)
        {
            if(data[j] > ID)
            {
                int children = node.get(record).getVal(j - 1);
                if(children != -1)
                {
                    record = children;
                    findRec(ID, node, record);
                }
            }
        }
        int lastKey = -1;
        for(int k = data.length - 3; k >= 2; k -= 3)
        {
            if(data[k] != -1)
            {
                lastKey = k;
                break;
            }
        }
        if(data[lastKey] < ID)
        {
            int child = node.get(record).getVal(lastKey + 2);
            if(child != -1)
            {
                record = child;
                findRec(ID, node, record);
            }
        }
        return record;
    }
    public void writeData(int record, HashMap<Integer, Node> node) throws IOException
    {
        int[] totalData = node.get(record).getData();
        this.file.seek(0);
        this.file.writeLong(this.totalRecords);
        this.file.seek(8);
        this.file.writeLong(this.rootNode);
        this.file.seek((record * maxByte) + 16);
        for(int elements : totalData)
        {
            this.file.writeLong(elements);
        }
    }
    public void readData(HashMap<Integer, Node> node) throws IOException
    {
        this.file.seek(0);
        totalRecords = (int) this.file.readLong();
        this.file.seek(8);
        rootNode = (int) this.file.readLong();
        int current = 0;
        while(true)
        {
            try
            {
                if(!node.containsKey(current))
                {
                    Node bN = new Node(order);
                    int[] data = bN.getData();
                    for(int l = 0; l < data.length; l++)
                    {
                        int element = (int) this.file.readLong();
                        bN.changeVal(l, element);
                    }
                    node.put(current, bN);
                }
                current++;
            }
            catch(EOFException ex)
            {
                break;
            }
        }
    }
    public static ArrayList<int[]> updateRecord()
    {
        ArrayList<int[]> array = new ArrayList<int[]>();
        for(int m = 0; m < totalRecords; m++)
        {
            array.add(node.get(m).getKeys(m));
        }
        return array;
    }
    public void exit() throws IOException
    {
        this.file.close();
    }
    public void printRecord(HashMap<Integer, Node> node)
    {
        String line = "";
        for(int n = 0; n < totalRecords; n++)
        {
            if(node.containsKey(n))
            {
                Node a = node.get(n);
                int[] b = a.getData();
                line = line + "Record# " + n + ": (";
                for(int o = 0; o < b.length - 1; o++)
                {
                    line = line + b[o] + ", ";
                }
                line = line + b[b.length - 1] + ")" + "\n";
            }
        }
        System.out.print(line + "\n" + "> ");
    }
}