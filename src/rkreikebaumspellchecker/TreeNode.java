package rkreikebaumspellchecker;

public class TreeNode<T extends Comparable <? super T>> {
	T data;
	TreeNode<T> rightChild;
	TreeNode<T> leftChild;
	
	public TreeNode() {
		super();
	}
}