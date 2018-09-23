
public class MinT {
	//add new names
	private static int[] pq;
	private  int N;
	private static int[] idq;
	
	//set up a convenient function to exchange two elements in an array
	public static void exch(int a[], int c, int d) {
		int m = a[c];
		a[c] = a[d];
		a[d] = m;
	}
	
	//set up two arrays, one is a min tree to sort execution time, another is the corresponding id of these execution time
	public MinT(int N) {
		pq = (int[]) new int[N];
		idq = (int[]) new int[N];
	}
	
	//when the number of nodes in the min tree becomes 0, this tree is empty.
	public boolean isEmpty() {
		return N == 0;
	}
	
	//the insert operation
	public  void insert(int id, int x) {
		//when the items in the min tree reach its' size, double it
		if(N == pq.length - 1) {
	     	int[] pqcopy = new int[2 * pq.length];
		    int[] idcopy = new int[2 * pq.length];
		    for (int i = 0; i <= N; i++) {
			    pqcopy[i] = pq[i];
			    idcopy[i] = idq[i];
		    }
		    pq = pqcopy;
		    idq = idcopy;
		}
		//insert node into the min tree
		N++;
		pq[N] = x;
		idq[N] = id;
		swim(N);
	}
	
	//the delete min operation
	public  int[] delMin() {
		//set up an array to save the id and execution time of the minimum execution time
		int[] minre = new int[2];
		int[] empty = {0,0 };
		//delete the node with minimum execution time 
		if(N == 0) return empty;
		int minet = pq[1];
		exch(pq, 1, N);
		int minid = idq[1];
		exch(idq, 1, N);
		N--;
		sink(1);
		//get the id and execution time
		minre[0] = minet;
		minre[1] = minid;
		return minre;	
	}
	
	//put the inserted new node to its correct place in the min tree
	public static void swim(int k) {
		while(k > 1 && pq[k] < pq[k / 2]) {
			exch(pq, k, k / 2);
			exch(idq, k, k / 2);
			k = k / 2;
		}
	}
	
	//after the delete min operation, initialize the min tree in its property 
	public  void sink(int k) {
		while(2 * k <= N) {
			int j = 2 * k;
			if(j < N && pq[j] > pq[j + 1]) j++;
			if(pq[j] > pq[k]) break;	
			else {
				exch(pq, k, j);
				exch(idq, k, j);
			}
			k = j;
		}
	}
}
