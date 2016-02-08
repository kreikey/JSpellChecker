package rkreikebaumspellchecker;

import java.util.*;

public class BinTree<T extends Comparable<? super T>> {
	
//	private List<TreeNode<T>> nodeBuffer;
	private TreeNode<T> root;
	private TreeNode<T> preceding;
	private TreeNode<T> succeeding;
	private boolean changedWithLoad;

	public BinTree() {
		super();
		changedWithLoad = false;
	}
	
	// This method is fairly big and ugly. I could have gone the recursive route, but that would have necessitated
	// copying Professor Strenn's code verbatim, and I couldn't get myself to do it.
	public boolean insert(T input) {
		TreeNode<T> curNode = root;
		boolean success = true;
		
		// If root is null we just set root. curNode will remain null, and the following While loop will not execute.
		if (root == null) {
			root = new TreeNode<T>();
			root.data = input;
		}
		
		while (curNode != null) {
			if (input.compareTo(curNode.data) < 0) {
				if (curNode.leftChild == null) {
					curNode.leftChild = new TreeNode<T>();
					curNode.leftChild.data = input;
					curNode = null;
				}
				else {
					curNode = curNode.leftChild;
				}
			}
			else if (input.compareTo(curNode.data) > 0) {
				if (curNode.rightChild == null) {
					curNode.rightChild = new TreeNode<T>();
					curNode.rightChild.data = input;
					curNode = null;
				}
				else {
					curNode = curNode.rightChild;
				}
			}
			// input == curNode. Will get executed at some point if the node to insert is already in the tree.
			// in this case, return false to let the calling code know that the insert failed.
			else {
				curNode = null;
				success = false;
			}
		}
		return success;
	}
	
	// Again, I could have copied/modified Professor Strenn's code, but I'd rather do it myself.
	// It's not that I don't understand how to do it recursively... I've implemented binary tree with
	// recursive-style methods in the D programming language before. Quite elegant, but the same thing
	// can easily be done iteratively unless you have to keep track of multiple branches.
	public T find(T info) {
		// Returns the data item in the tree if found. Otherwise, returns null.
		// Keeps a buffer of visited nodes and whether the last visited node was a left or right child
		// and uses this information to set the previous and next nodes.
		
		// Declare Variables
		TreeNode<T>curNode = root;
		TreeNode<T>someNode = new TreeNode<T>();

		// Initialize Variables
		someNode.data = info;
		preceding = null;
		succeeding = null;
		
		// I finally figured out the "correct" algorithm to get preceding and succeeding.
		// simple, concise, and it just works.
		while (curNode != null) {
			if (someNode.data.compareTo(curNode.data) < 0) {	// if someNode < curNode
				succeeding = curNode;
				curNode = curNode.leftChild;
			}
			else if (someNode.data.compareTo(curNode.data) > 0) {	// if someNode > curNode
				preceding = curNode;
				curNode = curNode.rightChild;
			}
			// if someNode == curNode, we must return curNode. 
			// someNode == curNode here because someNode is not less than or greater than curNode
			else {
				return curNode.data;
			}
		}
		
		return null;
	}

	public T getPreceding() {
		return (preceding != null) ? preceding.data : null;
	}

	public T getSucceeding() {
		return (succeeding != null) ? succeeding.data : null;
	}

	public List<T> saveInOrder() {
		List<T> nodeStore = new ArrayList<T>(10000);
		saveInOrderInternal(root, nodeStore);
		return nodeStore;
	}

	// We save in order because that's how our list is stored on disk.
	// To correctly load an ordered list, we use our LoadList() method.
	private void saveInOrderInternal(TreeNode<T> curNode, List<T> nodeStore) {
		if (curNode == null) {
			return;
		}
		saveInOrderInternal(curNode.leftChild, nodeStore);
		nodeStore.add(curNode.data);
		saveInOrderInternal(curNode.rightChild, nodeStore);
	}
	
	// Because loading a list of elements is so specific to the type of back end storage we're using,
	// I've implemented it here rather than in Dictionary.
	// Because I keep track of whether or not the tree changes, I need a wrapper function and a global variable.
	public boolean loadList(List<T> items) {
		changedWithLoad = false;
		loadListInternal(items);
		return changedWithLoad;
	}
	
	private void loadListInternal(List<T> items) {
		int index, left, right;
		if (items.size() == 0)
			return;
		// This line makes the tree fairly well-balanced, but doesn't create a complete tree.
//		index = ((items.size() > 2) && ((items.size() - 1) / 2 % 2 == 0)) ? (items.size()/2 + 1) : (items.size()/2);	// Another awesome use of the ternary operator.
		// The following lines recursively build a complete tree.
		index = items.size()/2;
		left = index;
		right = items.size() - index - 1;
		while (!(isFull(left) || isFull(right))) {
			index++;
			left = index;
			right = items.size() - index - 1;
		}
		
		if(insert(items.get(index)))
			changedWithLoad = true;
		loadListInternal(items.subList(0, index));
		loadListInternal(items.subList(index + 1, items.size()));		
	}
	
	public boolean isEmpty() {
		return (root == null) ? true : false;
	}
	
	private boolean isFull(int n) {
		int i = 0, exp = 0;
		while (i <= n) {
			if (n == i)
				return true;
			i += Math.pow(2, exp++);
		}
		return false;
	}
}