package application;
import java.lang.reflect.Array;

public class TreeNodeA<T> {
	
	@SuppressWarnings("unchecked")
	public TreeNodeA<T>[] children = (TreeNodeA<T>[]) Array.newInstance(TreeNodeA.class, MAXCHILDREN);
	private static final int MAXCHILDREN=5;
	public int childCount=0;
	public T data;

	public static void quickTraverse(TreeNodeA<?> root) {
		for(int i=0;i<root.childCount;i++) {
		System.out.println(root.children[i].data+" is a child of "+root.data);
		quickTraverse(root.children[i]);
		}
	}
	
	public TreeNodeA(T data) {
		this.data=data;
	}
	
	public void addChild(TreeNodeA<T> cn) {
		if(childCount<children.length) children[childCount++]=cn;
	}
	
	public static void main(String[]args) {
		TreeNodeA<String> root=new TreeNodeA<>("Henry");
		TreeNodeA<String> a=new TreeNodeA<>("Mary");
		TreeNodeA<String> b=new TreeNodeA<>("Edward");
		TreeNodeA<String> c=new TreeNodeA<>("Liz");
		TreeNodeA<String> d=new TreeNodeA<>("George");
		TreeNodeA<String> e=new TreeNodeA<>("Anne");
		root.addChild(a);
		root.addChild(b);
		root.addChild(c);
		a.addChild(d);
		a.addChild(e);
		quickTraverse(root);
	}

}
