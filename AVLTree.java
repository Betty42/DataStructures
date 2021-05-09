import java.util.ArrayList;
import java.util.List;
/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {

	private AVLNode root;
	public int height;
	public AVLNode min; // Create pointer to minimal node
	public AVLNode max; // Create pointer to maximal node
	
	// Empty constructor to create a null root
	public AVLTree() {
		this.root = null;
	}
	
	// A constructor to create a tree with root only
	// Sons are set in IAVLNode
	public AVLTree(AVLNode root) {

		this.root = root;
		this.min = root;
		this.max = root;
		this.root.setParent(null);
		this.height = this.root.getHeight();
		this.root = root.getHeight() == -1 ? null : root;
	}
	
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
	// Time complexity  O(1)
  public boolean empty() {
	  return this.root == null;
	  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   * Apply normal binary search to find node with O(logn) complexity
   */
	// Time complexity  O(logn)
  public String search(int k)
  {
	  // Start search from root
	  AVLNode currNode = (AVLNode) this.root;
	  if (currNode == null) return null;

	  while (currNode.isRealNode()) {
		  if (currNode.getKey() < k) {
			  currNode = (AVLNode)currNode.getRight();
		  } 
		  else if (currNode.getKey() > k) {
			  currNode = (AVLNode)currNode.getLeft();
		  } 
		  else {
			  return currNode.getValue();
				}
			}
	  	return null; 
		}
  /**
   * public AVLNode returnNode(int k)
   * 
   * this is a helping function to return the node
   * Apply normal binary search to find node with O(logn) complexity
   * returns the node with key k if it exists in the tree
   * otherwise, returns null
   */
//Time complexity  O(logn)
  public AVLNode returnNode(int k) {
	  
	// Start search from root
	  AVLNode currNode = (AVLNode) this.root;
	  if (currNode == null) return null;

	  while (currNode.isRealNode()) {
		  if (currNode.getKey() < k) {
			  currNode = (AVLNode) currNode.getRight();
		  } 
		  else if (currNode.getKey() > k) {
			  currNode = (AVLNode) currNode.getLeft();
		  } 
		  else {
			  return currNode;
		  }
	  }
	  return null; 
  }
  
  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k already exists in the tree.
   */
//Time complexity  O(logn)
   public int insert(int k, String i) {
	   
		if (search(k) != null) {
			return -1;
		}

		IAVLNode insertNode = new AVLNode(k, i);

		if (empty()) { // Tree is empty, let's create new root
			this.root = (AVLNode)insertNode;
			return 0;
		}

		IAVLNode currNode = insertRec(this.root, insertNode);
		int R = currNode.getHeight() - currNode.getRight().getHeight();
		int L = currNode.getHeight() - currNode.getLeft().getHeight();
		
		if (!((R==1 && L==1)||(R==1 && L==2)||(R==2 && L==1))) {
			return insertBalance((AVLNode) currNode, 0);
		}
		return 0;   
	}
   
   /**
    * public int insert(int k, String i)
    *
    * inserts an item with key k and info i to the AVL tree.
    * the tree must remain valid (keep its invariants).
    * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
    * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
    * returns -1 if an item with key k already exists in the tree.
    * A recursive helping function to find the node under which the new node will be placed
    */
// Time complexity  O(logn)
   public IAVLNode insertRec(IAVLNode currNode, IAVLNode insertNode) {

	   int insertKey = insertNode.getKey();
	   int currKey = currNode.getKey();

		if (insertKey < currKey && !currNode.getLeft().isRealNode()) {
			insertNode.setHeight(0);
			currNode.setLeft(insertNode);
			return currNode;
		}
		
		else if (insertKey > currKey && !currNode.getRight().isRealNode()) {
			insertNode.setHeight(0);
			currNode.setRight(insertNode);
			return currNode;
		}
		
		else if (insertKey < currKey && currNode.getLeft().isRealNode()) {
			return insertRec(currNode.getLeft(), insertNode);
		}

		else {
			return insertRec(currNode.getRight(), insertNode);
		}
	}


  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k was not found in the tree.
   */
// Time complexity  O(logn)
   public int delete(int k)
   {

		if (search(k) == null) {
			return -1;
		}
		
		AVLNode currNode = returnNode(k);

		if (currNode == this.root) { //check if node is root
			this.root = null;
			return 0;
		}
		else if (currNode.getRight().isRealNode() && !currNode.getLeft().isRealNode()) {
			AVLNode currNodeRight = (AVLNode)currNode.getRight();
			currNode.key = currNodeRight.getKey(); currNode.value = currNodeRight.getValue();
			currNode = currNodeRight;
		}
		else if (currNode.getLeft().isRealNode() && !currNode.getRight().isRealNode()) {
			AVLNode currNodeLeft = (AVLNode)currNode.getLeft();
			currNode.key = currNodeLeft.getKey(); currNode.value = currNodeLeft.getValue();
			currNode = currNodeLeft;
		}		
		else if (currNode.getLeft().isRealNode() && currNode.getRight().isRealNode()) {
			AVLNode successor = (AVLNode)currNode.getSuccessor();
			currNode.key = successor.getKey(); currNode.value = successor.getValue();
			currNode = successor;
		}

		AVLNode currP = (AVLNode)currNode.getParent();
		if (currP == null) {
				this.root = null;
		} 
		else if (currP.getLeft() == currNode) { // find where to delete
			currP.setLeft(new AVLNode());
        } 
		else {
			currP.setRight(new AVLNode());
		}
		
		return deleteBalance(currP,0); 
   }

// Time complexity  O(logn)  
	public int insertBalance(AVLNode currNode, int rotations) {

		int res = rotations;
		int balance = currNode.balanceFactor();

		if (balance == -1 || balance == 1) {
			currNode.height++;
			res++;
			if (currNode.getParent() != null) {
				return insertBalance((AVLNode)currNode.getParent(), res);
			}
		} 		
		else if (balance == -2) {
			AVLNode rotateNode = (AVLNode)currNode.getLeft();
			int balaceLeft = ((AVLNode)currNode.getLeft()).balanceFactor(); 
			if (balaceLeft == 1) {
				AVLNode rotateR = (AVLTree.AVLNode)rotateNode.getRight();
				res += 3 + leftRotate(rotateNode,rotateR);
				res += rightRotate(currNode,rotateR);			
				rotateNode.height--; 
				currNode.height--;
				((AVLNode)rotateNode.getParent()).height++;
			} 
			else if (balaceLeft == -1) {
				currNode.height--;
				res += 1 + rightRotate(currNode,rotateNode);
			}
			else { // Could happen when run from join function
				res += 1 + rightRotate(currNode,rotateNode);
				rotateNode.height++;
				if (rotateNode.getParent() != null) {
					return insertBalance((AVLNode)rotateNode.getParent(), res);
				}
			}
		} 
		else if (balance == 2) {
			AVLNode rotateNode = (AVLNode)currNode.getRight();
			int balanceRight = ((AVLNode)currNode.getRight()).balanceFactor();
			if (balanceRight == 1) {
				res += 1 + leftRotate(currNode, rotateNode);
				currNode.height--;
			}
			else if (balanceRight == -1) {
				AVLNode rotateL = (AVLNode)rotateNode.getLeft();
				res += 3+rightRotate(rotateNode, rotateL)+leftRotate(currNode, rotateL);
				rotateNode.height--;
				currNode.height--;
				((AVLNode)rotateNode.getParent()).height++;
			} 	 
			else { // Could happen when run from join function
				res += 1 + leftRotate(currNode, rotateNode);
				rotateNode.height++;
				if (rotateNode.getParent() != null) {
					return insertBalance((AVLNode) rotateNode.getParent(), res);
				}
			}
		}
		return res;
	}

	// Time complexity  O(logn)
	public int deleteBalance(AVLNode currNode, int rotations) {

		int res = rotations;
		int balance = currNode.balanceFactor();
		if (balance == 0) {
			currNode.height--;
			res++;
			if (currNode.getParent() != null) {
				return deleteBalance((AVLNode)currNode.getParent(),res);
			}
		} 
		else if (balance == 2) {
			int balanceRight = ((AVLNode) currNode.getRight()).balanceFactor();
			if (balanceRight == 0) {
				res += 2 + leftRotate(currNode, (AVLNode)currNode.getRight());
				currNode.height--;
				((AVLNode)currNode.getParent()).height++;
			} 
			else if (balanceRight == 1) {
				res += 2 + leftRotate(currNode, (AVLNode) currNode.getRight());
				currNode.setHeight(currNode.height-2);
				if (currNode.getParent().getParent() != null) {
					return deleteBalance((AVLNode)currNode.getParent().getParent(),res);
				}
			}
			else {
				AVLNode rotateL = (AVLNode)currNode.getRight().getLeft();
				res += 4+rightRotate((AVLNode)currNode.getRight(),rotateL)+leftRotate(currNode,rotateL);
				currNode.setHeight(currNode.height-2);
				((AVLNode)currNode.getParent()).height++;
				((AVLNode)currNode.getParent().getRight()).height--;				
				if (currNode.getParent().getParent() != null) {
					return deleteBalance((AVLNode)currNode.getParent().getParent(), res);
				}
			}
		} 
		else if (balance == -2) {
			int balanceLeft = ((AVLNode) currNode.getLeft()).balanceFactor();

			if (balanceLeft == 0) {
				res += 2 + rightRotate(currNode, (AVLNode)currNode.getLeft());
				currNode.height--;
				((AVLNode)currNode.getParent()).height++;
			} 
			else if (balanceLeft == 1) {
				AVLNode rotateR = (AVLNode)currNode.getLeft().getRight();
				res += 4+leftRotate((AVLNode)currNode.getLeft(),rotateR)+rightRotate(currNode,rotateR);
				currNode.setHeight(currNode.height-2);
				((AVLNode)currNode.getParent().getLeft()).height--;
				((AVLNode)currNode.getParent()).height++;
				if (currNode.getParent().getParent() != null) {
					return deleteBalance((AVLNode)currNode.getParent().getParent(), res);
				}
			}
			else {
				res += 2 + rightRotate(currNode, (AVLNode)currNode.getLeft());
				currNode.setHeight(currNode.height-2);
				if (currNode.getParent().getParent() != null) {
					return deleteBalance((AVLNode)currNode.getParent().getParent(), res);
				}
			} 
		}
		return res;
	}
	// Time complexity  O(1)
	public int leftRotate(AVLNode currNode, AVLNode rotateNode) {

		AVLNode currNodeP = (AVLNode)currNode.getParent();
		currNode.setRight((AVLNode)rotateNode.getLeft());
		
		if (currNodeP == null) {
			this.root = rotateNode;
			rotateNode.setParent(currNodeP);
		}
		else {
			if (rotateNode.getKey() > currNodeP.getKey()) {
				currNodeP.setRight(rotateNode);
			} 
			else {
				currNodeP.setLeft(rotateNode);
			}
		} 
		rotateNode.setLeft(currNode);
		return 1;
	}
	// Time complexity  O(1)
	public int rightRotate(AVLNode currNode, AVLNode rotateNode) {

		AVLNode currNodeP = (AVLNode) currNode.getParent();
		currNode.setLeft((AVLNode) rotateNode.getRight());

		if (currNodeP == null) {
			this.root = rotateNode;
			rotateNode.setParent(currNodeP);//should be null
		}
		else {
			if (rotateNode.getKey() > currNodeP.getKey()) {
				currNodeP.setRight(rotateNode);
			}
			else {
				currNodeP.setLeft(rotateNode);
			}
		} 
		rotateNode.setRight(currNode);
		return 1;
	}


   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
	// Time complexity  O(logn)
   public String min()
   {
	   if (this.empty()) {
			return null;  
	   }
	   IAVLNode min = this.root;
	   while (min.getLeft().isRealNode()) {
		   min = min.getLeft();
	   }
	   return min.getValue();
   }


   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
// Time complexity  O(logn)
   public String max()
   {
	   if (this.empty()) {
			return null;  
	   }
	   IAVLNode max = this.root;
	   while (max.getRight().isRealNode()) {
		   max = max.getRight();
	   }
	   return max.getValue();  
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
// Time complexity  O(n)
  public int[] keysToArray()
  {
		int treeSize = this.size();
	    int[] keysArray = new int[treeSize]; 
	    char data = 'k';
	    if (!empty()) {
	    	treeToArrayRec(root, keysArray, null, 0, data);
	    }
	    return keysArray;               
	}

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
//Time complexity  O(n)
  public String[] infoToArray()
  {
	  int treeSize = this.size();
	  String[] stringArray = new String[treeSize]; 
	  char data = 'v';
      if (!empty()) {
    	  treeToArrayRec(root, null, stringArray, 0, data);
      }
      return stringArray;                   
	}
  //complexity- O(n)
  public int treeToArrayRec(IAVLNode node, int[] keysArray, String[] stringArray, int i, char data) { 
	  
	  if (!node.isRealNode()) {
		  return i;
	  }
	  
	  i = treeToArrayRec(node.getLeft(), keysArray, stringArray, i, data);
	  if (data=='k') {
		  keysArray[i] = node.getKey();
	  }
	  else {
		  stringArray[i] = node.getValue();
	  }
	  return treeToArrayRec(node.getRight(), keysArray, stringArray, ++i, data);
  }


   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    * 
    * calls a helping function that runs over the tree nodes recursively 
    * and updates the size of the root
    * Time complexity is O(n) since runs over all of the nodes
    */   
//Time complexity  O(n)
   public int size() {
		return findSizeRec(this.root);
	}
   
	public int findSizeRec(IAVLNode root) {

		while (root != null && root.getHeight() != -1) {
			return findSizeRec(root.getLeft()) + findSizeRec(root.getRight()) + 1;

		} 
		return 0;
	}
	
	
	
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
	// Time complexity  O(1)
   public IAVLNode getRoot()
   {
		return this.root;
   }
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
    */   
// Time complexity  O(logn)
   public AVLTree[] split(int x)
   {
		AVLNode xNode = returnNode(x);
		AVLTree t1 = new AVLTree((AVLNode)xNode.getLeft());
		AVLTree t2 = new AVLTree((AVLNode)xNode.getRight());
		AVLNode tmpNode = xNode;
		AVLNode xParent = (AVLNode)xNode.getParent();
		
		while (xParent != null) {
			if (xParent.getLeft() == tmpNode) {
				t2.join(xParent, new AVLTree((AVLNode)xParent.getRight()));
			} 
			else {
				t1.join(xParent, new AVLTree((AVLNode)xParent.getLeft()));
			}
			tmpNode = xParent;
			xParent = (AVLNode) tmpNode.getParent();
		}
		return new AVLTree[]{t1,t2};  
	}
   
   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	  * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
    */   
// Time complexity  O(logn)
   public int join(IAVLNode x, AVLTree t)
   {

	   int tHeight = (t.empty()) ? 0 : t.root.getHeight();
	   int thisHeight = (empty()) ? 0 : this.root.getHeight();
	   int diff = tHeight-thisHeight;
	   int diffAbs = Math.abs(tHeight-thisHeight);
	   
	   if (this.empty()) {
		   t.insert(x.getKey(), x.getValue());
		   this.root = (AVLNode)t.getRoot();
		   return ++diffAbs;
	   }
	   else if (t.empty()) {
		   this.insert(x.getKey(), x.getValue());
		   return ++diffAbs;
	   }
	   
	   AVLTree highRankTree = (diff < 0) ? this : t;
	   AVLTree lowRankTree = (diff < 0) ? t : this;
	   int highRank = (highRankTree.empty()) ? 0 : highRankTree.root.getHeight();
	   int lowRank = (lowRankTree.empty()) ? 0 : lowRankTree.root.getHeight();
	   x.setHeight(1+lowRank);
	   
	   if (highRank==lowRank) {
		   if (highRankTree.min.getKey() > lowRankTree.max.getKey()) {
				x.setRight(lowRankTree.getRoot()); x.setLeft(highRankTree.getRoot());
		   } 
		   else {
				x.setRight(highRankTree.getRoot()); x.setLeft(lowRankTree.getRoot());	
		   }
		   x.setParent(null);
		   this.root = (AVLNode)x;
		   return ++diffAbs;
	   }
	   AVLNode tmpNode = highRankTree.root;
	   if (highRankTree.min.getKey() < lowRankTree.max.getKey()) {
			while (tmpNode.getHeight() > lowRank) {
				tmpNode = (AVLNode)tmpNode.getRight();
			}
			tmpNode.getParent().setRight(x);
			x.setRight(lowRankTree.getRoot()); x.setLeft(tmpNode);
	   } 
	   else {
			while (tmpNode.getHeight() > lowRank) {
				tmpNode = (AVLNode) tmpNode.getLeft();
			}
			tmpNode.getParent().setLeft(x);
			x.setRight(tmpNode); x.setLeft(lowRankTree.getRoot());
		}
	   insertBalance((AVLNode)x.getParent(),0);	
	   this.root = highRankTree.root;
	   return ++diffAbs;   
   }

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
		
	    private int key;
		private int height;
		private String value;
		private IAVLNode left;
		private IAVLNode right;
		private IAVLNode parent;
		
		// Create empty constructor for a virtual node
		public AVLNode() {

			this.key = -1;
			this.height = -1;
			this.value = null;
			this.left = null;
			this.right = null;
		}
		
		// Create a constructor for real nodes and virtual children
		public AVLNode(int key, String value) {

			this.key = key;
			this.value = value;
			this.left = new AVLNode();
			this.right = new AVLNode();
			this.left.setParent(this);
			this.right.setParent(this);

		}
		// Time complexity  O(1)
		public int getKey()
		{
			return this.key;
		}
		// Time complexity  O(1)
		public String getValue()
		{
			return this.value;
		}
		// Time complexity  O(1)
		public void setLeft(IAVLNode node)
		{
			this.left = node;
			node.setParent(this);
		}
		// Time complexity  O(1)
		public IAVLNode getLeft()
		{
			return this.left;
		}
		// Time complexity  O(1)
		public void setRight(IAVLNode node)
		{
			this.right = node;
			node.setParent(this);
		}
		// Time complexity  O(1)
		public IAVLNode getRight()
		{
			return this.right;
		}
		// Time complexity  O(1)
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		// Time complexity  O(1)
		public IAVLNode getParent()
		{
			return this.parent;
		}
		// Returns True if this is a non-virtual AVL node
		// Time complexity  O(1)
		public boolean isRealNode()
		{
			return (this.height != -1);
		}
		// Time complexity  O(1)
    public void setHeight(int height)
    {
		this.height = height;
    }
    public int getHeight()// Time complexity  O(1)
    {
		if (empty()) {
			return 0;
		}
    	return this.height;
    }
	
	// A helping function run when there's imbalance to return BF
    // Time complexity is O(1)
	public int balanceFactor() {

		int R = this.height - this.right.getHeight();
		int L = this.height - this.left.getHeight();

		return L - R;
	}
	
	// A helping function run during deletion when successor takes 
	// place of the deleted node
    // Time complexity is O(logn) since we are moving at tree height only once
	public IAVLNode getSuccessor() {
		IAVLNode successor = this;
		if (this.getRight().isRealNode()) {
			successor = this.getRight();
			
			while (successor.getLeft().isRealNode()) {
				successor = successor.getLeft();
			}
			return successor;			
		} 

		while (successor.getParent() != null && successor.getParent().getKey() > successor.getKey()) {
				successor = successor.getParent();
			}
		return successor.getParent();
		}
  	}
}
  

