package test;

import java.util.concurrent.RecursiveTask;

public class ParMaxSearcher extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 1L;
	
	private int maxima; 
	
	private BinTree tree;
	

	public ParMaxSearcher(BinTree tree) {
		this.tree = tree;
	}


	@Override
	protected Integer compute() {
		
		if(tree.getLeft()==null && tree.getRight()==null) { // stoping condition 
			return tree.get();
		}

		if(tree.getLeft()==null && tree.getRight()!=null) { // stoping condition 
			
			ParMaxSearcher pms_right1 = new ParMaxSearcher(tree.getRight());
			return Math.max(tree.get() ,pms_right1.compute());
		}

		if(tree.getLeft()!=null && tree.getRight()==null) { // stoping condition 
			
			ParMaxSearcher pms_left1 = new ParMaxSearcher(tree.getLeft());
			return Math.max(tree.get() ,pms_left1.compute());
		}
			
		ParMaxSearcher pms_left = new ParMaxSearcher(tree.getLeft()); 
		pms_left.fork();
		
		ParMaxSearcher pms_right = new ParMaxSearcher(tree.getRight()); 
		
		return Math.max(Math.max(pms_right.compute(), pms_left.join()), tree.get());

	}	

}
