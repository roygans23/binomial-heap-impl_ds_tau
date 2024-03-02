/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{
	public int size;
	public HeapNode last;
	public HeapNode min;

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	public HeapItem insert(int key, String info) 
	{   
		// create new binomial heap with one node for the inserted item, with rank 0
		HeapItem newItem = new HeapItem(null, key, info);

		HeapNode newNode = new HeapNode(newItem, null, null, null, 0);
		// the new node is its own next node
		newNode.next = newNode;

		// the HeapNode in which the HeapItem is stored in
		newItem.node = newNode;

		// create new binomial heap with the new node as the last node
		BinomialHeap newHeap = new BinomialHeap();
		newHeap.last = newNode;

		// increase the size of the heap
		size++;

		// meld the new heap with the current heap
		meld(newHeap);

		return newItem;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		return; // should be replaced by student code

	}

	/**
	 * 
	 * Return the minimal HeapItem
	 *
	 */
	public HeapItem findMin()
	{
		return min.item;
	} 

	/**
	 * 
	 * pre: 0 < diff < item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{   
		// decrease the key of the item to the minimum value and delete the minimal item, which is this item
		decreaseKey(item, Integer.MAX_VALUE);
		deleteMin();
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)
	{
		return; // should be replaced by student code   		
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return size;
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		return size() == 0;
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		// if the heap is empty, return 0
		if(empty()) return 0;

		// start from the last node and count the number of trees by following the next pointers
		int numOftrees = 1;
		HeapNode currNode = last;

		while(currNode.next != last){
			numOftrees++;
			currNode = currNode.next;
		}
		return numOftrees;
	}

	// link two trees of the same rank
	private HeapNode link(HeapNode tree1, HeapNode tree2){
		if (tree1.item.key > tree2.item.key) {
			// swap the trees so that tree1 is the smaller one
			HeapNode temp = tree1;
			tree1 = tree2;
			tree2 = temp;
		}

		// make tree2 the child of tree1
		if (tree1.child == null){
			tree2.next = tree2;
		}
		else{
			tree2.next = tree1.child.next;
			tree1.child.next = tree2;
		}
		tree1.child = tree2;

		return tree1;
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;

	/**
	 * Constructor for HeapNode class with parameters.
	 */
	public HeapNode(HeapItem item, HeapNode child, HeapNode next, HeapNode parent, int rank) {
		this.item = item;
		this.child = child;
		this.next = next;
		this.parent = parent;
		this.rank = rank;
	}
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public class HeapItem{
		public HeapNode node;
		public int key;
		public String info;

		/**
		 * Constructor for HeapItem class with parameters.
		 */
		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key = key;
			this.info = info;
		}
	}

	

}
