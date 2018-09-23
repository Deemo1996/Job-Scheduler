import java.util.ArrayList;
import java.util.List;

// set up a new aray list to build a redblack tree
public class RedBlackTree {
	ArrayList<int[]> a = new ArrayList<int[]>();
  // class RedBlackNode
	 class RedBlackNode {

	     // Possible color for this node 
	     public static final int BLACK = 0;
	     public static final int RED = 1;
	 	 // the elements of each node
	 	 public int id, executed_time, total_time, color;
	     //set up parent, left child and right child nodes
	     RedBlackNode parent;
	     RedBlackNode left;
	     RedBlackNode right;
	     //initiate the number of elements to the left or right
	     public int numLeft = 0;
	     public int numRight = 0;
	  
         //define redblack nodes
	     RedBlackNode(){
	         color = BLACK;
	         numLeft = 0;
	         numRight = 0;
	         parent = null;
	         left = null;
	         right = null;
	     }

	 	// constructor which sets id to the argument.
	 	RedBlackNode(int id, int executed_time, int total_time){
	         this();
	         this.id = id;
	         this.executed_time = executed_time;
	         this.total_time = total_time;
	 	}
	 }

	// Root initialized to nil.
	private RedBlackNode nil = new RedBlackNode();
	private RedBlackNode root = nil;
	
    //define the red black tree
    public RedBlackTree() {
        root.left = nil;
        root.right = nil;
        root.parent = nil;
    }

	// perform a leftRotate around x.
	private void leftRotate(RedBlackNode x){

		// call leftRotateFixup() which updates the numLeft and numRight values.
		leftRotateFixup(x);

		// Perform the left rotate 
		RedBlackNode y;
		y = x.right;
		x.right = y.left;

		// check for existence of y.left and make pointer changes
		if (!isNil(y.left))
			y.left.parent = x;
		y.parent = x.parent;

		// if x's parent is null, set y to root
		if (isNil(x.parent))
			root = y;

		// if x is the left child of it's parent, set y to x's old position
		else if (x.parent.left == x)
			x.parent.left = y;

		// if x is the right child of it's parent, set y to x's old position
		else
			x.parent.right = y;
		y.left = x;
		x.parent = y;
	}

	// update the numLeft & numRight values affected by leftRotate.
	private void leftRotateFixup(RedBlackNode x){

		//if only x, x.right and x.right.right always are not nil.
		if (isNil(x.left) && isNil(x.right.left)){
			x.numLeft = 0;
			x.numRight = 0;
			x.right.numLeft = 1;
		}

		// if x.right.left also exists in addition to above situation
		else if (isNil(x.left) && !isNil(x.right.left)){
			x.numLeft = 0;
			x.numRight = 1 + x.right.left.numLeft +
					x.right.left.numRight;
			x.right.numLeft = 2 + x.right.left.numLeft +
					  x.right.left.numRight;
		}

		// if x.left also exists in addition to the first situation
		else if (!isNil(x.left) && isNil(x.right.left)){
			x.numRight = 0;
			x.right.numLeft = 2 + x.left.numLeft + x.left.numRight;

		}
		// if x.left and x.right.left both exist in addtion to the first situation
		else{
			x.numRight = 1 + x.right.left.numLeft + x.right.left.numRight;
			x.right.numLeft = 3 + x.left.numLeft + x.left.numRight + x.right.left.numLeft + x.right.left.numRight;
		}
	}


	// perform a rightrotate around x
	private void rightRotate(RedBlackNode y){

		// call rightRotateFixup to adjust numRight and numLeft values
		rightRotateFixup(y);

        // perform the rotate 
        RedBlackNode x = y.left;
        y.left = x.right;

        // check for existence of x.right
        if (!isNil(x.right))
            x.right.parent = y;
        x.parent = y.parent;

        // if y.parent is nil
        if (isNil(y.parent))
            root = x;

        // if y is a right child of it's parent.
        else if (y.parent.right == y)
            y.parent.right = x;

        // if y is a left child of it's parent.
        else
            y.parent.left = x;
        x.right = y;
        y.parent = x;
	}


	
	// update the numLeft and numRight values affected by the rotate
	private void rightRotateFixup(RedBlackNode y){

		// if only y, y.left and y.left.left exists.
		if (isNil(y.right) && isNil(y.left.right)){
			y.numRight = 0;
			y.numLeft = 0;
			y.left.numRight = 1;
		}

		// if y.left.right also exists in addition to the first situation
		else if (isNil(y.right) && !isNil(y.left.right)){
			y.numRight = 0;
			y.numLeft = 1 + y.left.right.numRight +
				  y.left.right.numLeft;
			y.left.numRight = 2 + y.left.right.numRight +
				  y.left.right.numLeft;
		}

		// if y.right also exists in addition to the first situation
		else if (!isNil(y.right) && isNil(y.left.right)){
			y.numLeft = 0;
			y.left.numRight = 2 + y.right.numRight +y.right.numLeft;

		}

		// if y.right and y.left.right exist in addition to the first situation
		else{
			y.numLeft = 1 + y.left.right.numRight +
				  y.left.right.numLeft;
			y.left.numRight = 3 + y.right.numRight +
				  y.right.numLeft +
			y.left.right.numRight + y.left.right.numLeft;
		}

	}

//the insert operation
    public void insert(int id, int executed_time, int total_time) {
        insert(new RedBlackNode(id, executed_time, total_time));
    }

	private void insert(RedBlackNode z) {

			// create a reference to root and initialize a node to nil
			RedBlackNode y = nil;
			RedBlackNode x = root;
			
			// when we haven't reached to the end of the tree
			while (!isNil(x)){
				y = x;

				// if z.id < the current id, go left
				if (z.id < x.id){
					// update x.numLeft as z < x
					x.numLeft++;
					x = x.left;
				}
				else if(z.id == x.id){
					x.executed_time = z.executed_time;
					return;
				}
				// if z.id >= x.id, go right.
				else{
					// update x.numGreater as z is => x
					x.numRight++;
					x = x.right;
				}
			}
			// y will hold z's parent
			z.parent = y;

			// depending on the value of y.id, put z as the left or right child of y
			if (isNil(y))
				root = z;
			else if (z.id < y.id)
				y.left = z;
			else
				y.right = z;

			// initialize z's children to nil and z's color to red
			z.left = nil;
			z.right = nil;
			z.color = RedBlackNode.RED;

			// call insertFixup(z)
			insertFixup(z);
	}


	// Fixes up the violation of the RedBlackTree properties that may have been caused during insert(z)
	private void insertFixup(RedBlackNode z){

		RedBlackNode y = nil;
		// while there is a violation of the RedBlackTree properties..
		while (z.parent.color == RedBlackNode.RED){

			// if z's parent is the the left child of it's parent.
			if (z.parent == z.parent.parent.left){

				// initialize y to z 's cousin
				y = z.parent.parent.right;

				// if y is red, recolor
				if (y.color == RedBlackNode.RED){
					z.parent.color = RedBlackNode.BLACK;
					y.color = RedBlackNode.BLACK;
					z.parent.parent.color = RedBlackNode.RED;
					z = z.parent.parent;
				}
				// if y is black, and z is a right child
				else if (z == z.parent.right){
					z = z.parent;
					leftRotate(z);
				}

				// if y is black, and z is a left child
				else{
					z.parent.color = RedBlackNode.BLACK;
					z.parent.parent.color = RedBlackNode.RED;
					rightRotate(z.parent.parent);
				}
			}

			// if z's parent is the right child of it's parent.
			else{

				// initialize y to z's cousin
				y = z.parent.parent.left;

				// if y is red, recolor
				if (y.color == RedBlackNode.RED){
					z.parent.color = RedBlackNode.BLACK;
					y.color = RedBlackNode.BLACK;
					z.parent.parent.color = RedBlackNode.RED;
					z = z.parent.parent;
				}

				// if y is black, and z is a left child
				else if (z == z.parent.left){
					z = z.parent;
					rightRotate(z);
				}
				// if y is black, and z is a right child
				else{
					z.parent.color = RedBlackNode.BLACK;
					z.parent.parent.color = RedBlackNode.RED;
					leftRotate(z.parent.parent);
				}
			}
		}
	// Color root black 
	root.color = RedBlackNode.BLACK;
	}

    //find the next node of the id inputted	
	public int[] ceil(int id) {
		boolean x = false;
		if (contain(id) == false) {
		insert(id,0,0);
		x = true;}
		RedBlackNode t = search(id);
		 RedBlackNode r = treeSuccessor(t);
		 int res[] = {r.id, r.executed_time, r.total_time};
		 if(x == true)
			delete(id);
		 return res;
	}
	
	//get the executed time and total time of minimum id of all inserted nodes
	public int findMin() {
		RedBlackNode min = treeMinimum(root);
		int[] res = {0,0,0};
		res[0] = min.id;
		res[1] = min.executed_time;
		res[2] = min.total_time;
		return res[0];
	}
	
	//find the minimum id of all inserted nodes
	public RedBlackNode treeMinimum(RedBlackNode node){
		// while there is a smaller id, keep going left
		while (!isNil(node.left))
			node = node.left;
		return node;
	}
	
	// find the next id of node x
	public RedBlackNode treeSuccessor(RedBlackNode x){

		// if x.left is not nil, call treeMinimum(x.right) and return it's value
		if (!isNil(x.right) )
			return treeMinimum(x.right);
		RedBlackNode y = x.parent;

		// if x is it's parent's right child
		while (!isNil(y) && x == y.right){
			// Keep moving up in the tree
			x = y;
			y = y.parent;
		}
		return y;
	}

    //find the next node of id inputted
	public int[] floor(int id) {
		boolean x = false;
		if (contain(id) == false) {
		insert(id,0,0);
		x = true;}
		RedBlackNode t = search(id);
		 RedBlackNode r = treeFailure(t);
		 int res[] = {r.id, r.executed_time, r.total_time};
		 if(x == true)
			delete(id);
		 return res;
	}
	
	//get the id, execution time and total time from the maximum id
	public int findMax() {
		RedBlackNode max = treeMaximum(root);
		int[] res = {0,0,0};
		res[0] = max.id;
		res[1] = max.executed_time;
		res[2] = max.total_time;
		return res[0];
	}
	
	//find the max id of all nodes inserted
	public RedBlackNode treeMaximum(RedBlackNode node) {
		while(!isNil(node.right))
			node = node.right;
		return node;
	}
	
	//find the previous id of node x
	public RedBlackNode treeFailure(RedBlackNode x){
		
		// if x.left is not nil, call treeMinimum(x.left) and return it's value
		if (!isNil(x.left) )
			return treeMaximum(x.left);
		RedBlackNode y = x.parent;

		// if x is it's parent's left child
		while (!isNil(y) && x == y.left){
			// Keep moving up in the tree
			x = y;
			y = y.parent;
		}
		return y;
	}

	//delete the node with input id
	public void delete(int id) {
		RedBlackNode z = search(id);
		remove(z);
	}


	// remove node z 
	public void remove(RedBlackNode v){
		RedBlackNode z = search(v.id);

		// create variables
		RedBlackNode x = nil;
		RedBlackNode y = nil;

		// if either one of z's children is nil, then we must remove z
		if (isNil(z.left) || isNil(z.right))
			y = z;

		// else we must remove the successor of z
		else y = treeSuccessor(z);

		// Let x be the left or right child of y (y can only have one child)
		if (!isNil(y.left))
			x = y.left;
		else
			x = y.right;

		// link x's parent to y's parent
		x.parent = y.parent;

		// If y's parent is nil, then x is the root
		if (isNil(y.parent))
			root = x;

		// if y is a left child, set x to be y's left sibling
		else if (!isNil(y.parent.left) && y.parent.left == y)
			y.parent.left = x;

		// if y is a right child, set x to be y's right sibling
		else if (!isNil(y.parent.right) && y.parent.right == y)
			y.parent.right = x;

		// if y != z, trasfer y's id into z.
		if (y != z){
			z.id = y.id;
		}

		// update the numLeft and numRight numbers which might need updating due to the deletion of z.id.
		fixNodeData(x,y);

		// If y's color is black, it is a violation of the red black tree properties so call removeFixup()
		if (y.color == RedBlackNode.BLACK)
			removeFixup(x);
	}

    //initialize the red black tree after deletion
	private void fixNodeData(RedBlackNode x, RedBlackNode y){

		// initialize two variables which will help us traverse the tree
		RedBlackNode current = nil;
		RedBlackNode track = nil;

		// if x is nil, then we will start updating at y.parent and set track to y, y.parent's child
		if (isNil(x)){
			current = y.parent;
			track = y;
		}

		// if x is not nil, then we start updating at x.parent and set track to x, x.parent's child
		else{
			current = x.parent;
			track = x;
		}

		// while we haven't reached the root
		while (!isNil(current)){
			// if the node we deleted has a different id than the current node
			if (y.id != current.id) {

				// if the node we deleted is greater than current.id then decrement current.numRight
				if (y.id > current.id) 
					current.numRight--;

				// if the node we deleted is less than current.id thendecrement current.numLeft
				if (y.id < current.id) 
					current.numLeft--;
			}

			// if the node we deleted has the same id as the current node we are checking
			else{
				
				// the cases where the current node has any nil children and update appropriately
				if (isNil(current.left))
					current.numLeft--;
				else if (isNil(current.right))
					current.numRight--;

				// if current has two children, we must determine whether track is it's left or right child and update 
				else if (track == current.right)
					current.numRight--;
				else if (track == current.left)
					current.numLeft--;
			}

			// update track and current
			track = current;
			current = current.parent;
		}
	}

	// Restores the Red Black properties that may have been violated during the removal of a node in remove(RedBlackNode v)
	private void removeFixup(RedBlackNode x){
		RedBlackNode w;

		// if the tree isn't fixed completely
		while (x != root && x.color == RedBlackNode.BLACK){

			// if x is it's parent's left child
			if (x == x.parent.left){

				// set w = x's sibling
				w = x.parent.right;

				// if w's color is red.
				if (w.color == RedBlackNode.RED){
					w.color = RedBlackNode.BLACK;
					x.parent.color = RedBlackNode.RED;
					leftRotate(x.parent);
					w = x.parent.right;
				}

				// if both of w's children are black
				if (w.left.color == RedBlackNode.BLACK &&
							w.right.color == RedBlackNode.BLACK){
					w.color = RedBlackNode.RED;
					x = x.parent;
				}
				//if not
				else{
					// if w's right child is black
					if (w.right.color == RedBlackNode.BLACK){
						w.left.color = RedBlackNode.BLACK;
						w.color = RedBlackNode.RED;
						rightRotate(w);
						w = x.parent.right;
					}
					// if w = black, w.right = red
					w.color = x.parent.color;
					x.parent.color = RedBlackNode.BLACK;
					w.right.color = RedBlackNode.BLACK;
					leftRotate(x.parent);
					x = root;
				}
			}
			// if x is it's parent's right child
			else{

				// set w to x's sibling
				w = x.parent.left;

				// if w's color is red
				if (w.color == RedBlackNode.RED){
					w.color = RedBlackNode.BLACK;
					x.parent.color = RedBlackNode.RED;
					rightRotate(x.parent);
					w = x.parent.left;
				}

				// if both of w's children are black
				if (w.right.color == RedBlackNode.BLACK &&
							w.left.color == RedBlackNode.BLACK){
					w.color = RedBlackNode.RED;
					x = x.parent;
				}

				// if not
				else{
					// if w's left child is black
					 if (w.left.color == RedBlackNode.BLACK){
						w.right.color = RedBlackNode.BLACK;
						w.color = RedBlackNode.RED;
						leftRotate(w);
						w = x.parent.left;
					}

					// if w = black, and w.left = red
					w.color = x.parent.color;
					x.parent.color = RedBlackNode.BLACK;
					w.left.color = RedBlackNode.BLACK;
					rightRotate(x.parent);
					x = root;
				}
			}
		}
		// set x to black to ensure there is no violation of tree properties
		x.color = RedBlackNode.BLACK;
	}

	// find a node with id and returns the id, executed time and total time
	public int[] get(int id) {

		RedBlackNode x = search(id);

		int[] res = {0,0,0};
		if(contain(id) == false) return res;
		res[0] = x.id;
		res[1] = x.executed_time;
		res[2] = x.total_time;
		return res;
	}
	
	//a convenient funtion to determine if the tree contains the node with input id
	public boolean contain(int id) {
		RedBlackNode x = search(id);
		return isNil(x) == false;
	}
	
	//find the node with input id
	public RedBlackNode search(int id){
       
		// initialize a pointer to the root to traverse the tree
		RedBlackNode current = root;

		// while we haven't reached the end of the tree
		while (!isNil(current)){

			// if we have found a node with a id equal to id
			if (current.id == id) {
				return current;
			}
			
			// go left or right based on value of current and id
			else if (current.id < id) 
				current = current.right;

			// go left or right based on value of current and id
			else
				current = current.left;
		}

		// we have not found a node with input id
		return current;
	}

	//find the nodes in the range of the two input ids
	public ArrayList<int[]> printRange(int id1, int id2) {
		
		if(id2 < findMin() || id1 > findMax()) {
			int res[] = {0, 0, 0};
			a.add(res);
			
		}
		else findRange(root, id1, id2);
		return a;
	}
	
	public  void findRange(RedBlackNode x, int id1, int id2) {
		
		if(isNil(x)) return;
		if(x.id > id1) findRange(x.left, id1, id2);
		if(x.id < id2) findRange(x.right, id1, id2);
		if(x.id <= id2 && x.id >= id1) {
			int[] res = {x.id, x.executed_time, x.total_time};
			a.add(res);
		}
	}
	
    //a convenient function to determine whether a node is null
	private boolean isNil(RedBlackNode node){
		return node == nil;
	}

	//get the size of the redblacktree
	public int size(){
		return root.numLeft + root.numRight + 1;
	}	
	public static void main(String[] args) {
		RedBlackTree rbt = new RedBlackTree();
		rbt.insert(50,4,200);
		rbt.insert(19,8,472);;
		rbt.insert(19, 5, 472);
		rbt.insert(30,0,300);
		rbt.insert(1250,0,142);
	
		//int res[] = rbt.floor(20);
		//rbt.printRange(18, 59);
		//int[] res1 = rbt.ceil(30);
		//System.out.println("(" + res1[0] + "," + res1[1] + "," + res1[2] + ")");
		int[] res2 = rbt.floor(54);
		System.out.println("(" + res2[0] + "," + res2[1] + "," + res2[2] + ")");
		
		
		
		
	
	}
	
}
