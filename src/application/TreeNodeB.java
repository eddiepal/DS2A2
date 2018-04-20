package application;
import java.util.ArrayList;
import java.util.List;

public class TreeNodeB<T> {
		public List<TreeNodeB<T>> children = new ArrayList<>(); // Could use LinkedList (or any List) instead
		public T data;




		public TreeNodeB(T data) {
			System.out.println("test");
			this.data = data;

		}

		public void addChild(TreeNodeB<T> cn) {
			System.out.println("test2");
			children.add(cn);
		}
		
		public static void quickTraverse(TreeNodeB<?> root) {
			for(TreeNodeB<?> child : root.children) {
			System.out.println(child.data+" is a child of "+root.data);
			quickTraverse(child);
			}
		}

		
		public static void main(String[] args) {
			System.out.println("test3");
			TreeNodeB<String> root=new TreeNodeB<>("Harry");
			TreeNodeB<String> a=new TreeNodeB<>("Maggie");
			TreeNodeB<String> b=new TreeNodeB<>("Eddie");
			TreeNodeB<String> c=new TreeNodeB<>("Lizzy");
			TreeNodeB<String> d=new TreeNodeB<>("Georgie");
			TreeNodeB<String> e=new TreeNodeB<>("Annie");
			TreeNodeB<String> f=new TreeNodeB<>("Tommy");
			TreeNodeB<String> g=new TreeNodeB<>("Teddy");
			TreeNodeB<String> h=new TreeNodeB<>("Bob");
			root.addChild(a);
			root.addChild(b);
			root.addChild(c);
			a.addChild(d);
			a.addChild(e);
			c.addChild(f);
			f.addChild(g);
			f.addChild(h);
			quickTraverse(root);
		}
}
