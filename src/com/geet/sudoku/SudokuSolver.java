package com.geet.sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
/**
 * 
 * @author geet
 * This program solves 'Sudoku'
 */
public class SudokuSolver {
	// Sudoku Board is expressed by 'board' 
	// which is 9X9 dimension short array
	// Bit operation is used to solve the puzzle
	// We suppose that: 
	// 9 =~ 0000 0001 0000 0000
	// 8 =~ 0000 0000 1000 0000
	// 7 =~ 0000 0000 0100 0000
	// 6 =~ 0000 0000 0010 0000
	// 5 =~ 0000 0000 0001 0000
	// 4 =~ 0000 0000 0000 1000
	// 3 =~ 0000 0000 0000 0100
	// 2 =~ 0000 0000 0000 0010
	// 1 =~ 0000 0000 0000 0001 
	// Cell value(s) is expressed bits
	// if a cell value has possible two values (3,5), 
	// it is expressed by  bitwise or of 3 and 5; it is 0000 0000 0001 0100
	// if a cell has all possible values of(1...9) then is 0000 0001 1111 1111
	// 'hand' is an bit expression of  all nine '[1-9]' values.

	static boolean[][] status = new boolean[9][9];
	static short [][] board= new short [9][9];
	static short hand = 511; // denotes 0000 0001 1111 1111
	static boolean isGameOn = false;
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		takeInput();
		isGameOn = true;
		int traverseCount = 0;
		while (isGameOn) {
			if (traverseCount>=100) {
				System.err.println("Max Traverse Exceeded!!!");
				System.exit(0);
			}
			System.out.println("Traverse "+ traverseCount);
			traverseBoard();
			traverseCount++;
		}
		System.out.println("Total Execution Time:"+(System.currentTimeMillis()-start)+" ms");
	}	
	private static void traverseBoard(){
		isGameOn = false;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (!status[i][j]) {
					setPossibleValuesForCell(i, j);
				}
				System.out.print(valueOf(board[i][j])+" ");
				checkForFix(i, j, board[i][j]);
				if (!(status[i][j])) {
					isGameOn = true;
				}
			}
			System.out.println();
		}
	}
	private static String valueOf(short data){
		int i=0;
		while (data!=0) {
			data >>= 1;
			i++;
		}
		return i+"";
	}
	
	private static void checkForFix(int i, int j, short data){
		if (data == 1 | data == 2 |data ==4| data == 8 |data==16|data==32|data==64|data==128|data==256) {
			status[i][j] = true;
		}else{
			status[i][j] = false;
		}
	}
	
	
	
	private  static void takeInput(){
		File inputFile = new File("src/com/geet/sudoku/input.txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(inputFile);
			int i=0,j=0;
			while (scanner.hasNextLine()) {
				j=0;
				StringTokenizer stringTokenizer = new StringTokenizer(scanner.nextLine(), " ",false);
				while (stringTokenizer.hasMoreTokens()) {
					int token = Integer.parseInt(stringTokenizer.nextToken());
					System.out.print(token+" ");
					if (token>=1 && token<=9) {
						board[i][j] = (short) Math.pow(2, token-1);
						status[i][j] = true;
					}else{
						board[i][j] = 0;
					}
					j++;
				}
				System.out.println();
				i++;
			}
			System.out.println("Inputed!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			scanner.close();
		}
	}
	
	private static void setPossibleValuesForCell(int i, int j){
		short  flag = 0x00;	
		
		// store all fixed values of row i on flag
		for (int k = 0; k < 9; k++) {
				if ((status[i][k]) && k!=j ) {
					flag |= board[i][k];					
				}
		}
		// store all fixed values of column j on flag
		for (int k = 0; k < 9; k++) {
				if ((status[k][j]) && k!=i ) {
					flag |= board[k][j];
				}
		}
		
		// traverse 3X3 matrix of (i,j) 
		// store all fixed values of 3X3 matrix   on flag
		int temp = 3*(i/3)+(j/3);
		int initialRowIndexOf3X3 = 3*(temp/3);
		int initialColIndexOf3X3 = 3*(temp%3);
		// traverse 3X3 matrix on anti-clock wise
		// initial position  of Traverse  
		if ((status[initialRowIndexOf3X3][initialColIndexOf3X3])) {
			flag |= board[initialRowIndexOf3X3][initialColIndexOf3X3]; 			
		}
		int counter = 1;
		while (counter<12) {
			if (counter/3 ==0) {
				//row increase
				initialRowIndexOf3X3++;
			}else if(counter/3 == 1){
				if (counter%3==0) {
					counter++;
				}
				//column increase
				initialColIndexOf3X3++;
			}else if(counter/3 == 2){
				if (counter%3==0) {
					counter++;
				}//row decrease
				initialRowIndexOf3X3--;
			}else if(counter/3 ==3){
				if (counter%3==0) {
					counter++;
				}
				initialColIndexOf3X3--;
				//column decrease
			}
			if (status[initialRowIndexOf3X3][initialColIndexOf3X3]) {
				flag |= board[initialRowIndexOf3X3][initialColIndexOf3X3]; 
			}
			counter++;
		}
		// center position of 3X3 matrix
		if (status[initialRowIndexOf3X3][initialColIndexOf3X3]) {
			flag |= board[initialRowIndexOf3X3][initialColIndexOf3X3]; 		
		}
		// keep those values whose are in hand but not in flag
		//TODO
		board[i][j] = (short) (hand & ~flag);
		//board[i][j] = flag;
	}	
}
