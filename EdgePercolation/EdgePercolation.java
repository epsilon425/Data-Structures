// We are given two positive ints M and N, and a probability parameter
// p (a double, between 0.0 and 1.0).
//
// We imagine a grid of vertices with M rows (row index i ranges from
// 1 to M) and N+1 columns (column index j ranges from 0 to N).  For
// example, when M=3 and N=4, we have this grid of M*(N+1) = 15
// vertices, where each vertex is denoted by a "*":
//
//      j=0   j=1   j=2   j=3   j=4
//
//  i=1  *-----*-----*-----*-----*
//       |     |     |     |     |
//       |     |     |     |     |
//  i=2  *-----*-----*-----*-----*
//       |     |     |     |     |
//       |     |     |     |     |
//  i=3  *-----*-----*-----*-----*
//
//
// Now imagine we do a probabilistic experiment, where the pictured
// grid edges (between the vertices) are each either present or
// absent, according to the following rules:
//
//    * The M-1 vertical edges in the left column (where j=0)
//      are always present.
//
//    * The M-1 vertical edges in the right column (where j=N)
//      are always present.
//
//    * All other edges (M*N horizontal edges, and (M-1)*(N-1)
//      vertical edges in the internal columns) are each present with
//      probability p.  Each choice is independent of the others.
//
// We say this random grid "percolates" if there is a path of edges
// connecting the left column (where j=0) with the right column (where
// j=N).  Otherwise, it does not percolate.
//
// Let f(M,N,p) be the probability that the grid percolates, a number
// between 0.0 and 1.0.  We want to estimate f(M,N,p) by Monte Carlo
// trials.  That is, just repeat the experiment T times, and report
// the fraction of times (count/T) that it percolated.

class UF
{
    private int[] id;
    private int[] sz;
    public UF(int N) {
        id = new int[N];
	sz = new int[N];
        for (int i = 0; i < N; ++i) id[i] = i;
	for (int i = 0; i < N; ++i) sz[i] = 1;
    }
    public int find(int p) { 
   	while(p!=id[p]) p = id[p]; 
    	return p;	
    }
	
    public boolean connected(int p, int q) { return find(p)==find(q); }
    
    public void union(int p, int q) {
        int idp = find(p), idq=find(q);
        if (idp == idq) return;
	if (sz[idp]<sz[idq]) {id[idp]=idq; sz[idq] = sz[idp]+sz[idq];}
	else		     {id[idq]=idp; sz[idp] = sz[idp]+sz[idq];}
 
    }
}

public class EdgePercolation
{
   

    public static boolean trial(int M, int N, double p)
    {
	UF graph  = new UF((M*(N+1)));
	int[][]vertices;
	int count = 0;
	vertices = new int[M+1][N+1];
	

	for(int i=1;i<=M;i++){
		for(int j=0;j<N+1;j++)
		{
		vertices[i][j]=count;
		count++;
		}
	
	}
	for(int i=1;i<=M;i++)	
	{ 
		for(int j=0;j<N+1;j++)
		{ 
			
			if(j==N){
				if(i!=M){
					graph.union(vertices[i][j],vertices[i+1][j]);
				}
			}

			else if(j==0){
				if(i!=M){
					graph.union(vertices[i][j],vertices[i+1][j]);	

					if(Math.random()<p){					
						graph.union(vertices[i][j],vertices[i][1]);					
					}				
				}
			}
			else if(i==M){
				if(j!=N){
					if(Math.random()<p){
						graph.union(vertices[i][j],vertices[i][j+1]);
					}
				}
			}

			else {
				
				if(Math.random()<p) graph.union(vertices[i][j],vertices[i][j+1]);
				if(Math.random()<p) graph.union(vertices[i][j],vertices[i+1][j]);			
			
			}
			
		}
	}
	

	
			
	
	if(graph.connected(vertices[1][0],vertices[1][N]))return true;
        return false; 
    }


    public static double f(int M, int N, double p, int T)
    {
	int count=0;
	double perco=0;
	double percentage=0;
	while(count!=T){
	if(trial (M, N, p)) perco++;
	count++;	
	}
	percentage = (perco/(double)T);
	return percentage;
    }


    public static void main(String[] args)
    {
        if (args.length != 4) {
            System.err.println("expected four arguments: M N p T");
            return;
        }
        int M = Integer.parseInt(args[0]);
        int N = Integer.parseInt(args[1]);
        double p = Double.parseDouble(args[2]);
        int T = Integer.parseInt(args[3]);
        System.out.print("f(M="+M+", N="+N+", p="+p+", T="+T+") is ");
        System.out.println(f(M, N, p, T));
    }
}
