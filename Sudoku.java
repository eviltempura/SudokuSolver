package game;

import java.util.Random;

public class Sudoku {
	
	public int[][] grid;
	private boolean[][] questionGrid;
	
	public Sudoku() {
		this.grid = new int[9][9];
		questionGrid = new boolean[9][9];
	}
	
	public void preset() {
		this.grid = new int[][]{
			{0,0,3,9,0,0,0,5,1},
			{5,4,6,0,1,8,3,0,0},
			{0,0,0,0,0,7,4,2,0},
			{0,0,9,0,5,0,0,3,0},
			{2,0,0,6,0,3,0,0,4},
			{0,8,0,0,7,0,2,0,0},
			{0,9,7,3,0,0,0,0,0},
			{0,0,1,8,2,0,9,4,7},
			{8,5,0,0,0,4,6,0,0},
		};
	}
	
	public void generateRandomSudoku(int countIndex, int limit) {
		if(countIndex >= limit){
			return;
		}
		Random random = new Random((long) (Math.random() * 1000));
		int value = random.nextInt(10);
		int row = random.nextInt(9);
		int col = random.nextInt(9);
		boolean lock = true;
		do{
			do{
				row = random.nextInt(9);
				col = random.nextInt(9);
			}while(grid[row][col] != 0);
			
			while(value == 0){
				value = random.nextInt(10);
			}
			
			grid[row][col] = value;
			if(!checkGrid(row, col) || !checkSGrid(row, col)){
				grid[row][col] = 0;
			}else{
				lock = false;
			}
		}while(!checkGrid(row, col) || !checkSGrid(row, col) || lock);
		++countIndex;
		generateRandomSudoku(countIndex, limit);
	}
	
	public void setValue(int row, int col, int value) {
		int trueRow = row - 1;
		int trueCol = col - 1;
		
		if(trueRow < 0 || trueRow > 9 || trueCol < 0 || trueCol > 9 || value < 0 || value > 9){
			throw new IllegalArgumentException("Please only enter value from 1 to 9");
		}else{
			grid[trueRow][trueCol] = value;
		}
	}
	
	public String toString(){
		StringBuffer info = new StringBuffer();
		
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 9; col++){
				info.append(grid[row][col]);
				info.append(" ");
				if(col == 2 || col == 5){
					info.append("| ");
				}
			}
			info.append("\n");
			if(row == 2 || row == 5){
				for(int col = 0; col < 15; col++){
					info.append("-");
				}
				info.append("\n");
			}
		}
		
		return info.toString();
	}
	
	public boolean solve() {
		setQuestion();
		return solveAux(0, 0);
	}
	
	public boolean solveAux(int row, int col){
		if(isQuestion(row, col)){
			if(row <= 8 && col < 8){
				solveAux(row, col + 1);
			}else if(row < 8 && col == 8){
				solveAux(row + 1, 0);
			}else if(row == 8 && col == 8){
				return check();
			}
		}else{
			for(int value = 1; value <= 9; value++){
				grid[row][col] = value;
				if(checkGrid(row, col) && checkSGrid(row, col)){
					if(row <= 8 && col < 8){
						solveAux(row, col + 1);
					}else if(row < 8 && col == 8){
						solveAux(row + 1, 0);
					}else if(row == 8 && col == 8){
						return check();
					}
				}
			}
		}
		
		if(!isQuestion(row, col)){
			grid[row][col] = 0;
		}
		return finalCheck();
	}
	
	public boolean isQuestion(int row, int col) {
		return questionGrid[row][col];
	}
	
	public void showQuestion(){
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 9; col++){
				if(!isQuestion(row, col)){
					grid[row][col] = 0;
				}
			}
		}
	}
	
	public void setQuestion() {
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 9; col++){
				questionGrid[row][col] = grid[row][col] != 0;
			}
		}
	}
	
	public boolean checkGrid(int indexRow, int indexCol){
		int count = 0;
		for(int col = 0; col < 9; col++){
			for(int col2 = 0; col2 < 9; col2++){
				if(grid[indexRow][col] == grid[indexRow][col2]
				&& grid[indexRow][col] != 0 && grid[indexRow][col2] != 0){
					count++;
				}
				if(count >= 2){
					return false;
				}
			}
			count = 0;
		}
		
		count = 0;
		for(int row = 0; row < 9; row++){
			for(int row2 = 0; row2 < 9; row2++){
				if(grid[row][indexCol] == grid[row2][indexCol]
				&& grid[row][indexCol] != 0 && grid[row2][indexCol] != 0){
					count++;
				}
				if(count >= 2){
					return false;
				}
			}
			count = 0;
		}
		
		return true;
	}
	
	public boolean checkSGrid(int indexRow, int indexCol) {
		int[][] sGrid = new int[3][3];
		if(indexRow >= 0 && indexRow <= 2 && indexCol >= 0 && indexCol <= 2){
			for(int row = 0; row < 3; row++){
				for(int col = 0; col < 3; col++){
					sGrid[row][col] = grid[row][col];
				}
			}
		}else if(indexRow >= 3 && indexRow <= 5 && indexCol >= 0 && indexCol <= 2){
			for(int row = 3; row < 6; row++){
				for(int col = 0; col < 3; col++){
					sGrid[row - 3][col] = grid[row][col];
				}
			}
		}else if(indexRow >= 6 && indexRow <= 8 && indexCol >= 0 && indexCol <= 2){
			for(int row = 6; row < 9; row++){
				for(int col = 0; col < 3; col++){
					sGrid[row - 6][col] = grid[row][col];
				}
			}
		}else if(indexRow >= 0 && indexRow <= 2 && indexCol >= 3 && indexCol <= 5){
			for(int row = 0; row < 3; row++){
				for(int col = 3; col < 6; col++){
					sGrid[row][col - 3] = grid[row][col];
				}
			}
		}else if(indexRow >= 3 && indexRow <= 5 && indexCol >= 3 && indexCol <= 5){
			for(int row = 3; row < 6; row++){
				for(int col = 3; col < 6; col++){
					sGrid[row - 3][col - 3] = grid[row][col];
				}
			}
		}else if(indexRow >= 6 && indexRow <= 8 && indexCol >= 3 && indexCol <= 5){
			for(int row = 6; row < 9; row++){
				for(int col = 3; col < 6; col++){
					sGrid[row - 6][col - 3] = grid[row][col];
				}
			}
		}else if(indexRow >= 0 && indexRow <= 2 && indexCol >= 6 && indexCol <= 8){
			for(int row = 0; row < 3; row++){
				for(int col = 6; col < 9; col++){
					sGrid[row][col - 6] = grid[row][col];
				}
			}
		}else if(indexRow >= 3 && indexRow <= 5 && indexCol >= 6 && indexCol <= 8){
			for(int row = 3; row < 6; row++){
				for(int col = 6; col < 9; col++){
					sGrid[row - 3][col - 6] = grid[row][col];
				}
			}
		}else if(indexRow >= 6 && indexRow <= 8 && indexCol >= 6 && indexCol <= 8){
			for(int row = 6; row < 9; row++){
				for(int col = 6; col < 9; col++){
					sGrid[row - 6][col - 6] = grid[row][col];
				}
			}
		}
		
		int count = 0;
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 3; col++){
				
				for(int row2 = 0; row2 < 3; row2++){
					for(int col2 = 0; col2 < 3; col2++){
						if(sGrid[row][col] == sGrid[row2][col2]
						&& sGrid[row][col]!= 0 && sGrid[row2][col2] != 0){
							count++;
						}
					}
				}
				
				if(count >= 2){
					return false;
				}else{
					count = 0;
				}
				
			}
		}
		
		return true;
	}
	
	public boolean check() {
		int[][][] sGrids = new int[9][3][3];
		
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 3; col++){
				sGrids[0][row][col] = grid[row][col];
			}
		}
		
		for(int row = 3; row < 6; row++){
			for(int col = 0; col < 3; col++){
				sGrids[1][row - 3][col] = grid[row][col];
			}
		}
		
		for(int row = 6; row < 9; row++){
			for(int col = 0; col < 3; col++){
				sGrids[2][row - 6][col] = grid[row][col];
			}
		}
		
		for(int row = 0; row < 3; row++){
			for(int col = 3; col < 6; col++){
				sGrids[3][row][col - 3] = grid[row][col];
			}
		}
		
		for(int row = 3; row < 6; row++){
			for(int col = 3; col < 6; col++){
				sGrids[4][row - 3][col - 3] = grid[row][col];
			}
		}
		
		for(int row = 6; row < 9; row++){
			for(int col = 3; col < 6; col++){
				sGrids[5][row - 6][col - 3] = grid[row][col];
			}
		}
		
		for(int row = 0; row < 3; row++){
			for(int col = 6; col < 9; col++){
				sGrids[6][row][col - 6] = grid[row][col];
			}
		}
		
		for(int row = 3; row < 6; row++){
			for(int col = 6; col < 9; col++){
				sGrids[7][row - 3][col - 6] = grid[row][col];
			}
		}
		
		for(int row = 6; row < 9; row++){
			for(int col = 6; col < 9; col++){
				sGrids[8][row - 6][col - 6] = grid[row][col];
			}
		}
		
		int count = 0;
		for(int i = 0; i < 9; i++){
			count = 0;
			
			for(int row = 0; row < 3; row++){
				for(int col = 0; col < 3; col++){
					
					for(int row2 = 0; row2 < 3; row2++){
						for(int col2 = 0; col2 < 3; col2++){
							if(sGrids[i][row][col] == sGrids[i][row2][col2]
							&& sGrids[i][row][col]!= 0 && sGrids[i][row2][col2] != 0){
								count++;
							}
						}
					}
					
					if(count >= 2){
						return false;
					}else{
						count = 0;
					}
					
				}
			}
		}
		
		count = 0;
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 9; col++){
				for(int col2 = 0; col2 < 9; col2++){
					if(grid[row][col] == grid[row][col2]
					&& grid[row][col] != 0 && grid[row][col2] != 0){
						count++;
					}
					
					if(count >= 2){
						return false;
					}
				}
				
				count = 0;
			}
		}
		
		count = 0;
		for(int col = 0; col < 9; col++){
			for(int row = 0; row < 9; row++){
				for(int row2 = 0; row2 < 9; row2++){
					if(grid[row][col] == grid[row2][col]
					&& grid[row][col] != 0 && grid[row2][col] != 0){
						count++;
					}
					
					if(count >= 2){
						return false;
					}
				}
				
				count = 0;
			}
		}
		
		return true;
	}
	
	public boolean finalCheck(){
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 9; col++){
				if(grid[row][col] == 0){
					return false;
				}
			}
		}
		
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 9; col++){
				if(!checkGrid(row, col)){
					return false;
				}
			}
		}
		
		if(!checkSGrid(0, 0)){
			return false;
		}
		
		if(!checkSGrid(3, 0)){
			return false;
		}
		
		if(!checkSGrid(6, 0)){
			return false;
		}
		
		if(!checkSGrid(0, 3)){
			return false;
		}
		
		if(!checkSGrid(3, 3)){
			return false;
		}
		
		if(!checkSGrid(6, 3)){
			return false;
		}
		
		if(!checkSGrid(0, 6)){
			return false;
		}
		
		if(!checkSGrid(3, 6)){
			return false;
		}
		
		if(!checkSGrid(6, 6)){
			return false;
		}
		
		return true;
	}
}
