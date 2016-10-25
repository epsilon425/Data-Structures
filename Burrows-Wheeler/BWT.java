 
public class BWT
{
	public static int R = 256;
    	public static int N;              
   	public static String text;         
    	public static int[] index;        
    	public static int[] rank;          
    	public static int[] newrank;       
   	public static int offset;
	public static char[] charset;
    // The forward Burrows-Wheeler Transform.

    public static void doit()
    {
        for (offset = 1; offset < N; offset += offset)
        {
            int count = 0;
            for (int i = 1; i <= N; i++) {
                if (rank[index[i]] == rank[index[i-1]]) count++;
                else if (count > 0) {
                    int left = i-1-count;
                    int right = i-1;
                    quicksort(left, right);

                    int r = rank[index[left]];
                    for (int j = left + 1; j <= right; j++) {
                        if (less(index[j-1], index[j]))  {
                            r = rank[index[left]] + j - left;
                        }
                        newrank[index[j]] = r;
                    }

                    for (int j = left + 1; j <= right; j++) {
                        rank[index[j]] = newrank[index[j]];
                    }

                    count = 0;
                }
            }
        }
    }

    // sort by leading char
    public static void msd()
    {
        // calculate frequencies
        int[] freq = new int[R];
        for (int i = 0; i < N; i++)
            freq[text.charAt(i)]++;

        // calculate cumulative frequencies
        int[] cumm = new int[R];
        for (int i = 1; i < R; i++)
            cumm[i] = cumm[i-1] + freq[i-1];

        // compute ranks
        for (int i = 0; i < N; i++)
            rank[i] = cumm[text.charAt(i)];

        // sort by first char
        for (int i = 0; i < N; i++)
            index[cumm[text.charAt(i)]++] = i;
    }

    public static boolean less(int v, int w)
    {
        assert rank[v] == rank[w];
        if (v + offset >= N) v -= N;
        if (w + offset >= N) w -= N;
        return rank[v + offset] < rank[w + offset];
    }

    public static void exch(int i, int j)
    {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    public static void quicksort(int l, int r)
    {
        if (r <= l) return;
        int i = partition(l, r);
        quicksort(l, i-1);
        quicksort(i+1, r);
    }

    public static int partition(int l, int r)
    {
        int i = l-1, j = r;
        int v = index[r];       
        while (true)
        {
            while (less(index[++i], v))
                ;
            while (less(v, index[--j]))
                if (j == l) break;
            if (i >= j) break;
            exch(i, j);
        }
        exch(i, r);
        return i;
    }
    static String transform(String input, char mark)
    {
	
        assert input.indexOf(mark)<0;
	input = input+mark;
        int len = input.length();
	N    = input.length();
        text = input;
        index   = new int[N+1];
        rank    = new int[N+1];
        newrank = new int[N+1];


        index[N] = N;
        rank[N] = -1;

        msd();
        doit();

        String[] shifts = new String[len];
	StringBuilder texttext = new StringBuilder();
	StringBuilder ret = new StringBuilder();	
	texttext.append(text);
	texttext.append(text);
	for (int i =0; i< N; i++)
	{
		ret.append(texttext.charAt((index[i] + N)-1));	
	}
	
        return ret.toString();
    }

    public static void sort(char[] input, char[] output, int[] order)
    {
	int N = input.length;

        // compute frequency counts
        int[] count = new int[R+1];
        for (int i = 0; i < N; ++i)
            ++count[input[i] + 1];


        // compute cumulative counts
        for (int r = 0; r < R; ++r)
            count[r+1] += count[r];

        // Now for each char code c, count[c] equals the number
        // of chars in the input with code strictly less than c.
        assert count[0] == 0;
        assert count[R] == N;

        // Finally, move each char from the input to the output.
        // For BWT, this is where you finally figure out T.
        for (int i = 0; i < N; ++i)
	{
            order[i] = count[input[i]];
	    output[count[input[i]]++] = input[i];
	   
		
	}
    }


    // The reverse Burrows-Wheeler Transform.
    // The mark char should appear in the bw string.
    static String transformBack(String bw, char mark)
    {
        int at = bw.indexOf(mark), size = bw.length();
	
        // The mark should appear once, but not twice.
        assert at >= 0;
        assert bw.indexOf(mark, at+1) < 0;

	StringBuilder reverse = new StringBuilder();  
	reverse.append(bw);
	int length = reverse.length();
	char[] rever = new char[length]; //original bw input (L array)
	char[] rever2 = new char[length];//sorted bw input (F array)
	int[] order = new int[length];   //the T array
	int[] finorder = new int[length+length];//final completed order 
	
	
	reverse.getChars(0,length,rever,0);//put the stringbuilder reverse in rever char array 

	
	sort(rever,rever2,order); //sort the rever and put the result in rever2 
	

	
	for(int i=0,j=0;i<finorder.length;i++) //fill up the final order array 
	{
	finorder[i]=j;
	j=order[j];
	}	
	
	StringBuilder reversefin = new StringBuilder();
	for(int i=finorder.length-1;i>=0;i--)
	{
	reversefin.append(rever2[finorder[i]]);
	}
	
	int x = reversefin.indexOf("%")+1;
	
	int y = reversefin.indexOf("%",x);
	
	return reversefin.substring(x,y);
        
        
    }
}
