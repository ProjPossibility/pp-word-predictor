/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package absolute;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author prakhar
 */
public class KeyboardLayout
{
    char key;
    int matrix_x;
    int matrix_y;
    
    public ArrayList< ArrayList<KeyPosition> > theList;
    //private int width;
    //private int height;
// 
    /*public KBButton(int x,int y)
    {
        width=180;
        height=50;
        matrix_x=x;
        matrix_y=y;
        rightCo_x=180*(matrix_x+1)-1;
        if(y==0)
            rightCo_y=0;
        else rightCo_y=50*matrix_y-1;
    }*/
    /*public KBButton(int x,int y,int s1)
    {
        width=s1;
        height=s1;
        matrix_x=x;
        matrix_y=y;
        //rightCo_x=t_w+width;
        //rightCo_y= ???????
    }*/
    
    public KeyboardLayout ()
    {
        //super(25); // super's constructor 
        // self initialize the list to be give to the interface team
        
        // call super 's constructor to be 25 size
        theList = new ArrayList(25);
        
        for (int index_j =0; index_j < 5; index_j++)
        {
            for (int index_i=0; index_i < 5; index_i++)
            {
                theList.add(getArrayIndex(index_i,index_j), this.KeySizeCalc(index_i, index_j));
            }
        }
    }
    
    /*
    public static void main(String[] args)
    {
        System.out.println("hi mom!");
        //KeyPostion kb;
        ArrayList init_kb=new ArrayList();    
        /*for(int j=0;j<5;j++)
            for(int i=0;i<5;i++)
            {
                kb=new KBButton(i,j);
                //init_kb.add(kb);
            }* /
        /*for(int i=0;i<25;i++)
        {
            System.out.print(((KBButton)init_kb.get(i)).rightCo_x+"+");
            System.out.println(((KBButton)init_kb.get(i)).rightCo_y);
        }* /
        KBButton kb1=new KBButton();
        init_kb=(ArrayList)kb1.KeySizeCalc(0,0);
    }
     * */
    
    // if index is out of bounds of grid, return null
    // else return a new KBButton given size
    private KeyPosition getNewButton(int i, int j, int size) {
        if (i<0 || j<0 || i>4 || j>4) // these are the out of bounds of the 5x5 array
            return null;
        
        return new KeyPosition(i,j,size *3, size);
    }
    
    private int getArrayIndex(int i, int j) {
        // gets index in a flat array given a 5x5 matrix
        final int GRID_WIDTH=5;
        //final int GRID_HEIGHT=5;
        
        return (GRID_WIDTH * j + i);
    }
    

    public ArrayList KeySizeCalc(int i,int j) // i(x axis) and j(y axis) are the indexes on the grid
    {
        final int SIZE_1=100, SIZE_2=50,SIZE_3=25;   //s1,s2,s3 are the three new sizes of the keys
        
        // sentinel for null object
        KeyPosition kpSentinel = new KeyPosition(99, 99, 99, 99,99, 99, 99);
        
        ArrayList<KeyPosition> returnArray=new ArrayList<KeyPosition>(25);
        
        //initialize all to null
        for (int index=0; index<25; index++)
            returnArray.add(index, kpSentinel);
        
        // created KeyPosition object at location i,j 
        //1 box of size 1 
        returnArray.set(getArrayIndex(i,j),new KeyPosition(i,j,SIZE_1 * 3, SIZE_1));
        
        KeyPosition tempButton;
        
        
        
        
       
        /// objects of size 2  ... set ONLY if available
        tempButton = getNewButton(i-1,j-1,SIZE_2);
        if (tempButton!=null) returnArray.set(getArrayIndex(i-1,j-1),tempButton);
        
        tempButton = getNewButton(i-1,j,SIZE_2);
        if (tempButton!=null) returnArray.set(getArrayIndex(i-1,j),tempButton);
        
        tempButton = getNewButton(i,j-1,SIZE_2);
        if (tempButton!=null) returnArray.set(getArrayIndex(i,j-1),tempButton);
        
        tempButton = getNewButton(i+1,j-1,SIZE_2);
        if (tempButton!=null) returnArray.set(getArrayIndex(i+1,j-1),tempButton);
        
        tempButton = getNewButton(i+1,j,SIZE_2);
        if (tempButton!=null) returnArray.set(getArrayIndex(i+1,j),tempButton);
        
        tempButton = getNewButton(i+1,j+1,SIZE_2);
        if (tempButton!=null) returnArray.set(getArrayIndex(i+1,j+1),tempButton);
        
        tempButton = getNewButton(i,j+1,SIZE_2);
        if (tempButton!=null) returnArray.set(getArrayIndex(i,j+1),tempButton);
        
        tempButton = getNewButton(i-1,j+1,SIZE_2);
        if (tempButton!=null) returnArray.set(getArrayIndex(i-1,j+1),tempButton);
        
        
        
        // size 3 buttons, loop through all x and y coords
        for (int index_i=0; index_i < 5; index_i++)
        {
            for (int index_j =0; index_j < 5; index_j++)
            {
                // if it's not set yet, make it SIZE_3 button
                if (returnArray.get(getArrayIndex(index_i,index_j)) == kpSentinel)
                    returnArray.set(getArrayIndex(index_i,index_j), getNewButton(index_i,index_j,SIZE_3));
            }
        }
        
        
        
        // calculate top and left for the array list
        int curr_top=0, curr_left=0, next_top=0; //next vars
        KeyPosition kpLoc;
        for (int index_j=0; index_j < 5; index_j++) // height(y)
        {
            curr_left = 0;
            //next_left = 0;
            for (int index_i =0; index_i < 5; index_i++) //width(x)
            {
                kpLoc = returnArray.get(getArrayIndex(index_i,index_j));
                
                // set to current top / left
                kpLoc.setXCoord(curr_left);
                kpLoc.setYCoord(curr_top);
                
                // get max width and height
                curr_left += kpLoc.getWidth();
                if (next_top < curr_top + kpLoc.getHeight())
                    next_top = curr_top + kpLoc.getHeight();
            }
            
            curr_top = next_top;
        }
        
        
        
        
        /*
        
       // ls.add(kb);
        for(int g=0;g<25;g++)
        {
            KeyPosition kp = returnArray.get(g);
            //System.out.println(
            //        "x,y=" + kp.getXArrayPos() + "," + kp.getYArrayPos() + "-" + 
            //        kp.getHeight()+" "+kp.getWidth());
            System.out.println(
                        "x,y=" + kp.getXArrayPos() + "," + kp.getYArrayPos() + "-" + 
                        kp.getXCoord()+" "+kp.getYCoord());
        }
         * */
        
        return returnArray;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        KeyboardLayout kb = new KeyboardLayout();
        
        System.out.println( ( (KeyPosition)kb.theList.get(0).get(0) ).getWidth()  );
    }

}
















