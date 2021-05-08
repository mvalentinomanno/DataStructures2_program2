package program.pkg2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

//////////////////////////////////////////////////////////////

/* 
 *Benjamin Seal (Lab section 4), Michael Valentino-Manno (Lab section 5), Francis Taylor (Lab section 4)
 * 2/27/2018
 * Program to build a self balancing AVL tree with recursive insert and delete methods
 * Read in from input.txt in a folder named input, write to output.txt
 */

 /*
  * We used the TreeApp.java code in our project
  * Robert Lafore. 2002. Data Structures and Algorithms in Java (2 ed.). Sams, Indianapolis, IN, USA) 
  */

class Node {

    public int iData;           // data item (key)
    public double dData;        // data item
    public int hData;           // data item (height)
    public Node leftChild;      // this Node's left child
    public Node rightChild;     // this Node's right child

    Node(int id, double dd) { //node constructor
        iData = id;
        dData = dd;
        hData = 1; //each inserted node starts with a height of 1
        leftChild = null; //inserted nodes have no children initial
        rightChild = null;
    }

    public void displayNode() { // display ourself
        System.out.print('{');
        System.out.print(iData);
        System.out.print(", ");
        System.out.print(dData);
        System.out.print("} ");
    }
} // end Class Node

////////////////////////////////////////////////////////////////
class Tree {

    private boolean deleted;        //boolean that says if node was deleted
    private Node root;                 // first Node of Tree

    public Tree() {                    // constructor
        root = null;                   // no nodes in tree yet
    }

    public Node find(int key) {      // find node with given key

        Node current = root;         // (assumes non-empty tree)

        while (current.iData != key) {          // while no match
            if (key < current.iData) {          // go left?
                current = current.leftChild;
            } else {                              // or go right?
                current = current.rightChild;
            }
            if (current == null) // if no child
            {                                   // didn't find it
                return null;
            }
        }
        
        return current;                         // found it
    }  //end find()

    private int getheight(Node current) { //returns height of node passed

        if (current != null) { //returns height
            return current.hData;
        } else { //if node is null, height is 0
            return 0;
        }
    }

    private int checkbalance(Node current) { //balance factor calculation

        if (current != null) { //balance factor is height of right child - that of left
            return (getheight(current.rightChild) - getheight(current.leftChild));
        } else { //null nodes have no children, (BF of 0)
            return 0;
        }
    }

    private Node rotCase(Node current, int x) { //method that figures out which rotation case needs to be executed
        if (x == -2) { //left rotation, must check if left left or left right
            if (checkbalance(current.leftChild) <= 0) {//left left, child can have balance factor of 0
                return rotate(current, 2); //the 2 is the number in the rotate method that means to rotate right
            } else { //left right                      
                current.leftChild = rotate(current.leftChild, 1); //rotate left child left. The 1 means to rotate left
                return rotate(current, 2); //rotate parent right
            }
        }
        if (x == 2) { //right rotation, must check if right right or right left
            if (checkbalance(current.rightChild) >= 0) {//right right, child can have balance factor of 0
                return rotate(current, 1); //rotate left
            } else {//right left
                current.rightChild = rotate(current.rightChild, 2); //rotate child right
                return rotate(current, 1); //rotate parent left
            }
        }

        return current;
    }

    private Node rotate(Node current, int x) {

        if (x == 1) //Left Rotate
        {
            Node rCh = current.rightChild; //update references to rotate 
            Node rChlCh = rCh.leftChild;  //left around current
            rCh.leftChild = current;
            current.rightChild = rChlCh;

            if (current == root) { //if current was the root, after rotating left
                root = rCh;      //the new root is currents right child
            }
            
            int big = 0;
            if (getheight(current.leftChild) > getheight(current.rightChild)) {
                big = getheight(current.leftChild); //checks which childs height is largest
            } else {
                big = getheight(current.rightChild);
            }

            current.hData = big + 1; //updates the nodes new height with the height of its highest child + 1
            big = 0;
            if (getheight(rCh.leftChild) > getheight(rCh.rightChild)) {
                big = getheight(rCh.leftChild);
            } else {
                big = getheight(rCh.rightChild);
            }

            rCh.hData = big + 1; //updates the height
            return rCh;

        } else if (x == 2)//Right Rotate
        {
            Node lCh = current.leftChild; //update the references to rotate right
            Node lChrCh = lCh.rightChild; //around current
            lCh.rightChild = current; 
            current.leftChild = lChrCh;
            
            if (current == root) { //if current was the root, currents left child is new root after rotation
                root = lCh;
            }
            
            int big = 0;
            if (getheight(current.leftChild) > getheight(current.rightChild)) {
                big = getheight(current.leftChild); //finds childs max height
            } else {
                big = getheight(current.rightChild);
            }
            
            current.hData = big + 1; //updates height
            big = 0;
            if (getheight(lCh.leftChild) > getheight(lCh.rightChild)) {
                big = getheight(lCh.leftChild); //max height
            } else {
                big = getheight(lCh.rightChild);
            }
            
            lCh.hData = big + 1; //updates height
            return lCh;
            
        }

        return current;
    }

    public void insert(int id, double dd) {
        root = insert(root, id, dd); //wrapper insert function
    }

    private Node insert(Node current, int id, double dd) { //recursive insert functino

        if (current == null) { //node is null
            Node newNode = new Node(id, dd); //makes new node
            return newNode;
        }
        //find where to insert new node
        if (current.iData > id) { //insert key is less than currents key
            current.leftChild = insert(current.leftChild, id, dd); //move left
        } else if (current.iData <= id) { //insert key is greater than current key
            current.rightChild = insert(current.rightChild, id, dd); //move right
        } 

        int big = 0;

        if (getheight(current.leftChild) > getheight(current.rightChild)) {
            big = getheight(current.leftChild); //finds max height of children
        } else {
            big = getheight(current.rightChild);
        }

        current.hData = big + 1; //update height

        if (checkbalance(current) == -2 || checkbalance(current) == 2) { //checks if a rotation is needed
            current = rotCase(current, checkbalance(current));
        }

        return current;
    }

    public boolean delete(int x) { //wrapper delete function
        deleted = false; //initial starts, nothing has been deleted
        delete(root, x); //call recursive delete function
        return deleted; //if something was deleted, delete returns as true
    }

    private Node delete(Node current, int iD) { //recursive delete function
        Node next;
        if (current == null) { //checks for null node
            return current;
        }
        if (iD < current.iData) { //if the key to be deleted is less than current key
            next = delete(current.leftChild, iD); //move left
            current.leftChild = next;
        } else if (iD > current.iData) { //if key to be deleted is greater than current
            next = delete(current.rightChild, iD); //move right
            current.rightChild = next;
        }
        if (iD == current.iData) {
            deleted = true; //node was found and will be deleted
            Node hold = null; //placeholder
            int noKid = 2; //number of kids the to be deleted node has
            if ((current.leftChild == null) || (current.rightChild == null)) { //if the node has 0 or 1 child
                if (current.rightChild == null) { //if the right child is null
                    hold = current.leftChild; //holder is set to left child
                    noKid--; //the number of kids not null is decremented
                } else if (current.leftChild == null) { //if the left child is null
                    hold = current.rightChild; //holder is set to right child
                    noKid--; //number of children is decremented
                }
                if (noKid == 0) { //no children on the node
                    current = null; //delete node
                }
                if (noKid == 1) { //1 child, delete node, and replace it with its child
                    current = hold; //delete node, and replace it with its child
                }
            }
            if (noKid == 2) { //if the node has two children
                Node temp = getSuccessor(current); //get the successor
                current.iData = temp.iData; //update necessary values
                current.dData = .9 + temp.iData;
                current.rightChild = delete(current.rightChild, temp.iData); //delete inorder successor
                temp = null;
            }

        }

        if (current == null) { //avoid null pointers
            return current; //early return so no null nodes have their height checked
        }

        int big = 0;

        if (getheight(current.leftChild) > getheight(current.rightChild)) {
            big = getheight(current.leftChild); //finds max height
        } else {
            big = getheight(current.rightChild);
        }

        current.hData = big + 1; //updates height

        //Left left Case
        if (checkbalance(current) == 2 || checkbalance(current) == -2) { //checks for critical unbalancing
            return rotCase(current, checkbalance(current)); //rotates tree
        }
        return current;
    }

    //returns node with next-highest value after delNode
    //goes right child, then right child's left descendants
    private Node getSuccessor(Node delNode) {
        Node successorParent = delNode;
        Node successor = delNode;
        Node current = delNode.rightChild;        // go to the right child
        while (current != null) {                 // until no more
            successorParent = successor;          // left children
            successor = current;
            current = current.leftChild;
        }

        if (successor != delNode.rightChild) {    // if successor not right child,
            //make connections
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        return successor;
    }

    public void traverse(int traverseType) { //selectes traversal type

        switch (traverseType) {

            case 1:

                System.out.print("\nPreorder traversal: ");

                preOrder(root);

                break;

            case 2:

                System.out.print("\nInorder traversal: ");

                inOrder(root);

                break;

            case 3:

                System.out.print("\nPostorder traversal: ");

                postOrder(root);

                break;

            default:

                System.out.print("Invalid traversal type\n");

                break;

        }

        System.out.println();

    }

    private void preOrder(Node localRoot) { 

        if (localRoot != null) {

            System.out.print(localRoot.iData + " ");

            preOrder(localRoot.leftChild);

            preOrder(localRoot.rightChild);

        }

    }

    private void inOrder(Node localRoot) {

        if (localRoot != null) {

            inOrder(localRoot.leftChild);

            System.out.print(localRoot.iData + " ");

            inOrder(localRoot.rightChild);

        }

    }

    private void postOrder(Node localRoot) {

        if (localRoot != null) {

            postOrder(localRoot.leftChild);

            postOrder(localRoot.rightChild);

            System.out.print(localRoot.iData + " ");

        }

    }
 
    public void displayTree() { //displays tree to console

        Stack<Node> globalStack = new Stack<Node>();

        globalStack.push(root);

        int nBlanks = 32;

        boolean isRowEmpty = false;

        System.out.println(
                ".................................................................");

        while (isRowEmpty == false) {

            Stack<Node> localStack = new Stack<Node>();

            isRowEmpty = true;

            for (int j = 0; j < nBlanks; j++) {

                System.out.print(' ');

            }

            while (globalStack.isEmpty() == false) {

                Node temp = (Node) globalStack.pop();

                if (temp != null) {

                    System.out.print(temp.iData);

                    localStack.push(temp.leftChild);

                    localStack.push(temp.rightChild);

                    if (temp.leftChild != null || temp.rightChild != null) {

                        isRowEmpty = false;

                    }

                } else {

                    System.out.print("--");

                    localStack.push(null);

                    localStack.push(null);

                }

                for (int j = 0; j < nBlanks * 2 - 2; j++) {

                    System.out.print(' ');

                }

            } // end while globalStack not empty

            System.out.println();

            nBlanks /= 2;

            while (localStack.isEmpty() == false) {

                globalStack.push(localStack.pop());

            } // end while isRowEmpty is false

            System.out.println(
                    ".................................................................");

        } // end displayTree()

    }

    public Node findMin() { //finds min value in tree

        Node min = root; //start at root

        while (min.leftChild != null) { //march left
            min = min.leftChild;
        }

        return min;

    }

    public Node findMax() { //finds max value in tree

        Node max = root;

        while (max.rightChild != null) { //march right
            max = max.rightChild;
        }

        return max;

    }

// end class Tree
}

////////////////////////////////////////////////////////////////
class TreeApp {

    public static void main(String[] args) throws IOException {

        Tree theTree = new Tree();

        BufferedReader reader = null; //buffer reader used to read in from text file

        try {
            File input = new File("input/input.txt"); //read in from  input.txt, in folder named input
            reader = new BufferedReader(new FileReader(input));
            String line; //line from text file
            
            while ((line = reader.readLine()) != null) { //goes until a line is null
                String[] word = line.split(" "); //splits line at spaces
                switch (word[0]) { //uses the first word to determine function 
                    case "insert": //inserts
                        String[] info = line.split(" ");
                        String[] nums = info[1].split(","); //seperates numbers
                        System.out.print("Inserting: ");
                        System.out.println(info[1]); //insert number
                        System.out.println();

                        for (int i = 0; i < nums.length; i++) { 
                            int x = Integer.parseInt(nums[i]);
                            double y = x + .9;
                            theTree.insert(x, x + 0.9); //insert all numbers given
                        }
                        break;

                    case "find":

                        String[] seperate = line.split(" "); //splits line
                        String[] num = seperate[1].split(","); 
                        
                        for (int i = 0; i < num.length; i++) {
                            int x = Integer.parseInt(num[i]);
                            Node found = theTree.find(x); //finds node
                            
                            if (found != null) { //if found, prints found
                                System.out.print("Found: ");
                                found.displayNode();
                                System.out.print("\n");
                            } else { //not found
                                System.out.print("Could not find ");
                                System.out.print(x + "\n");
                            }
                        }

                        break;

                    case "delete":

                        String[] sep = line.split(" "); //finds keys to delete
                        String[] rek = sep[1].split(",");

                        for (int i = 0; i < rek.length; i++) {
                            int x = Integer.parseInt(rek[i]); 

                            if (theTree.delete(x)) { //deletes, and checks if was deleted
                                System.out.print("Deleted: " + x + '\n'); 
                            } else { //if not found
                                System.out.print(x + " was not found/not deleted" + '\n');
                            }
                        }

                        break;

                    case "traverse":

                        String[] splt = line.split(" "); //finds out which traversal
                        String[] tap = splt[1].split(","); //type needs to be done

                        for (int i = 0; i < tap.length; i++) {
                            int x = Integer.parseInt(tap[i]);
                            theTree.traverse(x);
                        }

                        break;

                    case "min":

                        Node min = theTree.findMin(); //finds min

                        if (min != null) { //checks if found
                            System.out.println();
                            System.out.print("Min: ");
                            min.displayNode(); //if found, display
                            double x = min.iData;
                            System.out.print("\n");
                        } else {
                            System.out.print("Could not find "); //not found

                        }

                        break;

                    case "max":

                        Node max = theTree.findMax();
 
                        if (max != null) { //if found
                            System.out.println(); 
                            System.out.print("Max: ");
                            max.displayNode();
                            System.out.print("\n");
                        } else { //if not found
                            System.out.print("Could not find ");
                        }
                        
                        break;

                    case "show":

                        theTree.displayTree(); //display
                        break;

                    default:

                        break;

                }

                //System.out.println(line);
            }

        } catch (IOException e) { //catch for buffer reader

            e.printStackTrace();

        } finally {

            try {

                reader.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    } // end main()

}  // end TreeApp class

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
