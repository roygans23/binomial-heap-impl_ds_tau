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
		newHeap.size = 1;
		newHeap.min = newNode;

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
		if (empty()) {
			return; // Heap is empty, nothing to delete
		}
		//Find the tree before the minimum Tree
		HeapNode beforeMin = min.next;
		while (beforeMin.next != min) {
			beforeMin = beforeMin.next;
		}
		//if minimum Tree is the only tree in the heap:
		if (beforeMin==min){
			//if minimum Tree is of rank 0:
			if (min.child == null) {
				min = null;
				last = null;
				min = null;
				size -= 1;
			}
			//if minTree had children:
			else{
				last = min.child;
				min.child.parent = null;
				min.child = null;
				findNewMin();
				size -= 1;
			}	
		}
		//there is more than one tree in the heap
		else {
			//if minTree is rank 0:
			if (min.child == null) {
				beforeMin.next = min.next;
				findNewMin();
				size -= 1;
			}
			//if minTree had children:
			else{
				beforeMin.next = min.next;
				BinomialHeap newHeap = new BinomialHeap();
				newHeap.last = min.child;
				min.child.parent = null;
				min.child = null;
				meld(newHeap);
			}	
		}
	}


	/**
	 * 
	 * Return the minimal HeapItem of the new Tree
	 *
	 */
	private void findNewMin()
	{
		HeapNode currentNode = new HeapNode();
		currentNode = last;
		this.min = last;
		while (currentNode.next != last){
			if (currentNode.item.key < min.item.key){
				this.min = currentNode;
			}
			currentNode = currentNode.next;
		}
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
		item.key -= diff; // Decrease the key
		// Fix the heap if necessary
		HeapNode currentNode = item.node;
		while (currentNode.parent != null && currentNode.item.key < currentNode.parent.item.key) {
			// Swap the item with its parent
			HeapItem tempItem = currentNode.item;
			currentNode.item = currentNode.parent.item;
			currentNode.parent.item = tempItem;

			// Update the reference in the nodes
			HeapNode tempNode = currentNode.item.node;
			currentNode.item.node = currentNode.parent.item.node;
			currentNode.parent.item.node = tempNode;
		
			currentNode = currentNode.parent;
		}
		//change the min if necessary
		if (item.key < min.item.key) {
			min.item = item; // Update the min node if necessary
		}
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
		//we fix the fields of min and size accordingly
		this.min = (min.item.key < heap2.min.item.key) ? min : heap2.min;
		this.size += heap2.size;

		BinomialHeap meldedHeap = new BinomialHeap();
		int higherRank = (last.rank > heap2.last.rank) ? last.rank : heap2.last.rank;
		//we create three arrays, the first two representing the 2 heaps and the last one representing the final heap
		HeapNode[] heapThisArray = new HeapNode[higherRank+1];
		HeapNode[] heap2Array = new HeapNode[higherRank+1];
		HeapNode[] finalHeap = new HeapNode[higherRank+2];

		//here we add the nodes to the arrays according to the rank (index =rank)
		HeapNode currentNode = last.next;
		while (currentNode != last) {
			heapThisArray[currentNode.rank] = currentNode;
			currentNode = currentNode.next;
		}
		heapThisArray[last.rank] = last;
		currentNode = heap2.last;
		while (currentNode != heap2.last) {
			heap2Array[currentNode.rank] = currentNode;
			currentNode = currentNode.next;
		}
		heap2Array[heap2.last.rank] = heap2.last;

		HeapNode remainder = null;
	
		for(int i = 0; i <= higherRank; i++)
		{
			HeapNode currHeapNode1 = heapThisArray[i];
			HeapNode currHeapNode2 = heap2Array[i];

			//if both trees exist of rank i
			if (currHeapNode1 != null && currHeapNode2 != null)
			{
				HeapNode linkedHeapNode = HeapNode.link(currHeapNode1, currHeapNode2);
				
				if(remainder != null)
				{
					finalHeap[i] = remainder;
				}
				remainder = linkedHeapNode;
			}
			//there exists only one tree of rank i
			else{
				//only first tree exists
				if(currHeapNode1 != null)
				{
					if(remainder != null)
					{
						HeapNode linkedHeapNode = HeapNode.link(currHeapNode1, remainder);
						remainder = linkedHeapNode;
					}
					else
					{
						finalHeap[i] = currHeapNode1;
					}
				}
				//only second tree exists
				else{
					if(currHeapNode2 != null)
					{
						if(remainder != null)
						{
							HeapNode linkedHeapNode = HeapNode.link(currHeapNode2, remainder);
							remainder = linkedHeapNode;
						}
						else
						{
							finalHeap[i] = currHeapNode2;
						}
					}
					//there exists no trees of rank i
					else{
						if(remainder != null)
						{
							finalHeap[i] = remainder;
							remainder = null;
						}
					}
				}
			}
		}

		//we found the last node (the node with the highest rank)
		for(int i = finalHeap.length; i <= 0; i--)
		{
			if(finalHeap[i] != null)
			{
				meldedHeap.last = finalHeap[i];
				break;
			}
		}
		HeapNode currentHeap = meldedHeap.last;
		//we add to the new heap all the nodes in finalHeap array
		for (int i = 0; i < finalHeap.length; i++) {
			if (finalHeap[i] != null && finalHeap[i] != meldedHeap.last) {
				currentHeap.next = finalHeap[i];
				currentHeap = currentHeap.next;
			}
		}
		currentHeap.next = meldedHeap.last;
		this.last = meldedHeap.last;
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

		public HeapNode() {
		}

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

		// link two trees of the same rank
		public static HeapNode link(HeapNode tree1, HeapNode tree2){
			if (tree1.item.key > tree2.item.key) {
				// swap the trees so that tree1 is the smaller one
				HeapNode temp = tree1;
				tree1 = tree2;
				tree2 = temp;
			}
			// make tree2 the child of tree1
			
			if (tree1.child == null){  //two of the trees are of rank 0
				tree2.next = tree2;
			}
			else{
				tree2.next = tree1.child.next;
				tree1.child.next = tree2;
			}
			tree1.child = tree2;
			tree2.parent = tree1;

			return tree1;
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
