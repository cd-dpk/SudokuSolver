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
	// Sudoku Board's two dimension is expressed by 'board' into three dimensions
	// Each Cell is an array of boolean
	// The possible values of cell are stored by an array of boolean
	// If a cell has exact one 'true', then it is fixed and the value is 'index+1' of 'true'  
	// 'hand' is an array of all nine '[1-9]' values.

	static boolean [][][] board= new boolean [9][9][9];
	static boolean [] hand = new boolean[9];
	static boolean isGameOn = false;
	static {
		for (int i = 0; i < hand.length; i++) {
			hand[i] = true;
		}
	}
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
				if (!isFixed(board[i][j])) {
					setPossibleValuesForCell(i, j);
				}
				System.out.print(stringOf(board[i][j])+" ");
				if (!isFixed(board[i][j])) {
					isGameOn = true;
				}
			}
			System.out.println();
		}
	}

	private static String stringOf(boolean []data){
		String str="";
		for (int i = 0; i < data.length; i++) {
			if (data[i]) {
				str+= (i+1)+",";
			}
		}
		if (str.equals("")) {
			return "0";
		}
		return "["+str+"]";
		
	}
	
	private static boolean[] and(boolean [] data1, boolean [] data2){
		boolean [] res = new boolean[data1.length];
		for (int i = 0; i < data1.length && i< data2.length; i++) {
			res[i] = data1[i] && data2[i];
		}
		return res;
	} 
	private static boolean[] or(boolean[] data1, boolean [] data2){
		boolean [] res = new boolean[data1.length];
		for (int i = 0; i < data1.length && i< data2.length; i++) {
				res[i] = data1[i] || data2[i];
		}
		return res;		
	}
	private static boolean[] diff(boolean[] data1, boolean [] data2){
		boolean [] res = new boolean[data1.length];
		for (int i = 0; i < data1.length && i< data2.length; i++) {
				res[i] = (data1[i]  &&  !data2[i]);
		}
		return res;		
	}
	
	private static boolean isFixed(boolean [] data){
		return (countNoOfValues(data) == 1) ;
	}
	
	private static int countNoOfValues(boolean [] data){
		int count = 0 ;
		for (int i = 0; i < data.length; i++) {
			if (data[i]) {
				count++;
			}
		}
		return count;
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
						board[i][j][token-1] = true;
					}
					j++;
				}
				System.out.println();
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			scanner.close();
		}
	}
	
	private static void setPossibleValuesForCell(int i, int j){
		boolean [] flag = new boolean[9];	
		
		// store all fixed values of row i on flag
		for (int k = 0; k < 9; k++) {
				if (isFixed(board[i][k]) && k!=j ) {
					flag = or(flag, board[i][k]);					
				}
		}
		// store all fixed values of column j on flag
		for (int k = 0; k < 9; k++) {
				if (isFixed(board[k][j]) && k!=i ) {
					flag = or(flag, board[k][j]);
				}
		}
		
		// traverse 3X3 matrix of (i,j) 
		// store all fixed values of 3X3 matrix   on flag
		int temp = 3*(i/3)+(j/3);
		int initialRowIndexOf3X3 = 3*(temp/3);
		int initialColIndexOf3X3 = 3*(temp%3);
		// traverse 3X3 matrix on anti-clock wise
		// initial position  of Traverse  
		if (isFixed(board[initialRowIndexOf3X3][initialColIndexOf3X3])) {
			flag = or(flag, board[initialRowIndexOf3X3][initialColIndexOf3X3]); 			
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
			if (isFixed(board[initialRowIndexOf3X3][initialColIndexOf3X3])) {
				flag = or(flag, board[initialRowIndexOf3X3][initialColIndexOf3X3]); 
			}
			counter++;
		}
		// center position of 3X3 matrix
		if (isFixed(board[initialRowIndexOf3X3][initialColIndexOf3X3])) {
			flag = or(flag, board[initialRowIndexOf3X3][initialColIndexOf3X3]); 		
		}
		// keep those values whose are in hand but not in flag
		board[i][j] = diff(hand, flag);
		//board[i][j] = flag;
	}	
}
