import java.util.Arrays;

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{

	private static int numOfLinks;
	private static int numOfCuts;
	private int numOfTrees;
	private int numOfMarked;
	private int size; 

	private HeapNode min;
	private HeapNode first;
	
   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    * Time Complexity is O(1).
    *   
    */
    public boolean isEmpty()
    {
    	return first == null;    
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * 
    * Returns the new node created.
    * Time Complexity is O(1). 
    */
    public HeapNode insert(int key)
    {    
    	HeapNode node = new HeapNode(key);
    	size++;
    	numOfTrees++;
    	
    	// If tree is empty create first node
    	if (isEmpty()) {
    		first = node;
    		min = node;
    		min.next = node;
    		min.prev = node;
    	}  
    	// Change pointers to brothers and update min
    	else {
    		node.next = first;
    		node.prev = first.prev;
    		node.prev.next = node;
    		first.prev = node;
    		first = node;
        	min = (node.key < min.key) ? node : min ;
    	}
    	return node;     
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    * Time Complexity is O(n) WC and O(log(n)) amortized.
    */
    public void deleteMin()
    {
    	if (isEmpty() || size == 1) {
    		min = null; first = null;   		
    		size = 0; numOfTrees = 0;
    		return;
    	}
    	// create the buckets
    	HeapNode[] buckets = new HeapNode[Integer.toBinaryString(size).length()+1]; 
    	size--; 	
    	
    	HeapNode node = first;
    	int lim = numOfTrees;
    	int i = 0;
    	int rank = min.rank;
    	int j = 0;
    	
    	//iterate over the roots to pass them to buckets
    	while (i < lim) { 
    		if (node == min) {
    	     	HeapNode child = min.child;
    	     	//iterate over min's children and pass to buckets
    	     	while (j < rank) {  
    	     		if (child.marked) { 
    	     			child.marked = false;
    	     			numOfMarked--; 
    	     		}
    	     		HeapNode next = child.next;
    	     		child.parent = null;
    	     		child.prev = child;
    	     		child.next = child;
    	     		//create the links to min's chlidren
    	         	while (buckets[child.rank] != null) { 
    	         		child = link(buckets[child.rank], child);
    	         		buckets[child.rank-1] = null;
    	     		}
    	         	buckets[child.rank] = child;
       	     		child = next;
       	     		j++;
    	     	}
    			node = node.next;
    			i++;
    		}
    		//create links to the rest of the tree roots
    		else {
    			HeapNode next = node.next;
    			node.parent = null;
    			node.prev = node;
    			node.next = node;
    	     	while (buckets[node.rank] != null) { 
    	 			node = link(buckets[node.rank], node);
    	 			buckets[node.rank-1] = null;
    	 		}
    	     	buckets[node.rank] = node;    			
    			node = next;
    			i++;
    		}
    	}
    	first = null;
    	numOfTrees = 0;
    	//call to method to recreate the heap
    	fromBuckets(buckets); 	
    }
    
    /** private void fromBuckets(HeapNode[] buckets)
     *  connecting the trees in buckets back to a heap
     *  Time Complexity is O(log(n)).
     */
     
     private void fromBuckets(HeapNode[] buckets) {
     	
    	HeapNode node = first;
    	// iterate over buckets
     	for (HeapNode bucket : buckets) {
     		if (bucket != null) {
     			numOfTrees++; 
     			if (first == null) { 
     				first = bucket;
     				node = first;
     				min = first;
     				continue;
     			}
     			node.next = bucket;
     			bucket.prev = node;
     			node = node.next;
     			min = (node.key < min.key) ? node : min ;	
     		}		
     	} 
     	//connect first and last root
     	node.next = first; 
     	first.prev = node; 
     }
     

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    * Complexity is O(1).
    */
    public HeapNode findMin()
    {
    	return min;    
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    * Complexity is O(1).
    */
    public void meld (FibonacciHeap heap2)
    {
    	if ((isEmpty() && heap2.isEmpty()) || heap2.isEmpty()) {
    		return;  
    	}
    	// check is 1 is empty and meld
    	else if (this.isEmpty()) {
    		this.first = heap2.first;
    		this.min = heap2.findMin();
    		this.size = heap2.size();
    		this.numOfTrees = heap2.numOfTrees;
    		this.numOfMarked = heap2.numOfMarked;
    		return;
    	}
    	// change the ponters to meld the heaps
        min.next.prev = heap2.min.prev;
        heap2.min.prev.next = min.next;
        min.next = heap2.min;
        heap2.min.prev = min;
        // update min, size, marked
    	min = (heap2.min.key < min.key) ? heap2.min : min ;
    	size = size + heap2.size(); 
    	numOfMarked += heap2.numOfMarked;
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    * Complexity is O(1). 
    */
    public int size()
    {
    	return size; 
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * Complexity is O(n).
    */
    public int[] countersRep()
    {
    	int[] arr = new int[Integer.toBinaryString(size).length()+1];
    	
    	if (isEmpty()) { 
    		return arr;
    	}
    	// iterate over roots and update rank
    	HeapNode node = first;
    	int i = 0;
    	
    	while (i < numOfTrees) { 
    		arr[node.rank]++; 
    		node = node.next; 
    		i++;
    	}
    	return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    * Time Complexity is O(n) WC and O(log(n)) amortized.
    */
    public void delete(HeapNode x) 
    {    
    	this.decreaseKey(x, Integer.MAX_VALUE);
    	this.deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    * Time complexity is O(logn) WC and O(1) amortized.
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.key -= delta; 
    	min = (x.key < min.key) ? x : min ;
    	// if the tree is correct, no need to cut
    	if ((x.parent == null) || (x.parent.key < x.key)) {
    		return;
    	}   	
    	cascadingCuts(x);
    }
    /**
     * private void cascadingCuts(HeapNode x)
     * The function cuts x from the tree
     * checks if parent needs to be cut
     * if so, calls itself recursively
     * Time complexity is O(logn) WC and O(1) amortized.
     */
    
    private void cascadingCuts(HeapNode x) {
    	HeapNode xParent = x.parent;
    	cut(x);
    	if (xParent.parent == null) { 
    		return;
    	}
    	else if (!xParent.marked) {
    		xParent.marked = true; 
    		numOfMarked++;
    		return;
    	}
    	cascadingCuts(xParent);    	
    }
    
    /**
     * private void cut(HeapNode x) 
     * cuts x from the tree and updates the pointers
     * Time Complexity is O(1).
     */
    
    private void cut(HeapNode x) {
    	numOfCuts++;
    	numOfTrees++;
    	HeapNode xParent = x.parent;
    	xParent.rank--;
    	// update parents mark
    	if (x.marked) {
    		numOfMarked--;
    		x.marked = false;
    	}    	    	
    	if (x.next == x) {
    		xParent.child = null;
    	}
    	else {
    		if (xParent.child==x) { 
    			xParent.child=x.next;
    		}
    		x.prev.next = x.next;
    		x.next.prev = x.prev;
    	}  
    	//update pointer
    	x.parent = null;
    	x.next = first;
    	first.prev.next = x;
    	x.prev = first.prev;
    	first.prev = x;
    	first = x;
    }
    
    /** private HeapNode link(HeapNode root1, HeapNode root2)
     * both trees have the same rank 
     * linking the trees after checking the higher one
     * Time complexity is O(1).
     */
    private HeapNode link(HeapNode root1, HeapNode root2) {
    	numOfLinks++;
    	numOfTrees--; 
        HeapNode high, low;
        low = (root1.getKey() > root2.getKey()) ? root1 : root2 ;
        high = (root1.getKey() > root2.getKey()) ? root2 : root1 ;
	         
    	if (high.child == null) {
    		high.child = low;
    	}
    	else {
        	HeapNode highChild = high.child;
        	high.child = low;
        	low.next = highChild;
        	highChild.prev.next = low; 
        	low.prev = highChild.prev; 
        	highChild.prev = low;
    	}
		low.parent = high;
		high.rank++;
    	return high;
    }
   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    * Time complexity is O(1)
    */
    public int potential() 
    {    
    	return numOfTrees+2*numOfMarked; 
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    * Time complexity is O(1)
    */
    public static int totalLinks()
    {    
    	return numOfLinks; 
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods).
    * Time complexity is O(1) 
    */
    public static int totalCuts()
    {    
    	return numOfCuts; 
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k*deg(H)). 
    * You are not allowed to change H.
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        if (k <= 0) {
            return new int[0];
        }
        //create new array
        int[] arr = new int[k];
        
        if (k == 1) {
            arr[0] = H.min.key;
        }
        
        else {
            FibonacciHeap heap = new FibonacciHeap(); 
            heap.insert(H.min.key);             
            heap.min.KMinPointer = H.min; 
            int i = 0;
            while (i < k) {
                arr[i] = heap.min.key; // heap's min
                HeapNode child = heap.min.KMinPointer.child; 
                heap.deleteMin();
                //there are more levels to go
                if (child != null && child.key != arr[i]) { 
                    HeapNode lastChild = child;
                    while (child.next != lastChild) { 
                    	//insert nodes
                        heap.insert(child.key).KMinPointer = child;
                        child = child.next;
                    }
                    heap.insert(child.key).KMinPointer = child;
                }
            i++;
            }
        }
        return arr;
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{

	public int key;
	private int rank = 0;
	private boolean marked = false;
	private HeapNode child;
	private HeapNode next = this;
	private HeapNode prev = this;
   	private HeapNode parent;
   	private HeapNode KMinPointer = null;

  	public HeapNode(int key) {
	    this.key = key;
      }

  	public int getKey() {
	    return this.key;
      }

    }
}
