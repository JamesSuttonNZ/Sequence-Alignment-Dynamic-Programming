package dynamicProgramming;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Sequence alignment dynamic programming algorithm
 * @author James Sutton
 *
 */
public class SequenceAlignment {
	
	static int match = 1, nonmatch = -1, space = -2;
	
	public static void main(String[] args){
		
		sequenceAlignment(args);
		
	}

	private static void sequenceAlignment(String[] args) {
		final long startTime = System.currentTimeMillis();

		//command line arguments
		//String X = args[0];
		//String Y = args[1];
		
		//reads in sequences from user input
		System.out.println("This algorithm is used to compare two DNA strands in genetics, use characters: A,C,T,G (recommended), adenine (A), cytosine (C), thymine (T), and guanine (G)");

		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Enter sequence 1: ");
		String X = reader.nextLine();
		System.out.println("Enter sequence 2: ");
		String Y = reader.nextLine();
		reader.close();
		
		System.out.println("\nInput:\n\nx = " + X + "\ny = "+Y+"\n");
		
		Alignment(X,Y);
		
		final long endTime = System.currentTimeMillis();

		System.out.println("\nTotal execution time: " + (endTime - startTime) + "ms");
	}

	private static void Alignment(String x, String y) {
		
		int opt[][] = new int[x.length()+1][y.length()+1]; //optimal solution scores
		
		int recover[][] = new int[x.length()+1][y.length()+1]; //recovery table
				
		//Initialize
		for(int i = 1; i < x.length()+1; i++){
			opt[i][0] = i*space;
			recover[i][0] = 2;
		}
		for(int j = 1; j < y.length()+1; j++){
			opt[0][j] = j*space;
			recover[0][j] = 1;
		}
		
		//calculate values
		for(int j = 1; j < y.length()+1; j++){
			for(int i = 1; i < x.length()+1; i++){
				boolean mat;
				int scoreDiag;
				if(x.charAt(i-1) == y.charAt(j-1)){
					scoreDiag = opt[i - 1][j - 1] + match;
					mat = true;
				}else{
					scoreDiag = opt[i - 1][j - 1] + nonmatch;
					mat = false;
				}
				int scoreLeft = opt[i][j - 1] + space;
				int scoreUp = opt[i - 1][j] + space;
				int used = Math.max(Math.max(scoreUp, scoreLeft), scoreDiag);
				opt[i][j]  = used;
				if(used == scoreDiag && !mat){
					recover[i][j] = -1; //-1 for diagonal nomatching
				}
				else if(used == scoreDiag && mat){
					recover[i][j] = 0; //0 for diagonal matching
				}
				else if(used == scoreLeft){
					recover[i][j] = 1; //1 for left
				}
				else if(used == scoreUp){
					recover[i][j] =  2; //2 for right
				}
			}
		}
		
		printResult(x, y, opt, recover);
	}

	private static void printResult(String x, String y, int[][] opt, int[][] recover) {
		int score = opt[x.length()][y.length()];
		
		int m = x.length();
		int n = y.length();
		int xCount = 0;
		int yCount = 0;
		int used = 0;
		String xSeq = "";
		String ySeq = "";
		String sSeq = "";
		
		while(m > 0 || n > 0){
			used = recover[m][n];
			if(used == -1){
				xSeq += x.charAt(x.length()-1 - xCount);
				ySeq += y.charAt(y.length()-1 - yCount);
				sSeq += "-";
				xCount++;
				yCount++;
				m--;
				n--;
			}
			else if(used == 0){
				xSeq += x.charAt(x.length()-1 - xCount);
				ySeq += y.charAt(y.length()-1 - yCount);
				sSeq += "+";
				xCount++;
				yCount++;
				m--;
				n--;
			}
			else if(used == 1){
				xSeq += " ";
				ySeq += y.charAt(y.length()-1 - yCount);
				sSeq += "*";
				yCount++;
				n--;
			}
			else if(used == 2){
				xSeq += x.charAt(x.length()-1 - xCount);;
				ySeq += " ";
				sSeq += "*";
				xCount++;
				m--;
			}
		}
		
		String xSequence = new StringBuilder(xSeq).reverse().toString();
		String ySequence = new StringBuilder(ySeq).reverse().toString();
		String sSequence = new StringBuilder(sSeq).reverse().toString();
		
		System.out.println("Output: \n");
		
		System.out.println(xSequence);
		System.out.println(ySequence);
		System.out.print(sSequence+"\t("+score+")\n");
		
//		matrices for testing
		System.out.println();
		for (int[] row : opt){
			System.out.println(Arrays.toString(row));
		}
		System.out.println();
		for (int[] row : recover){
			System.out.println(Arrays.toString(row));
		}
	}
	
}
