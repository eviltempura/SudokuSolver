package game;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.InputMismatchException;

import javax.swing.*;

public class GUI extends JPanel {
	
	private static final long serialVersionUID = -6969577339976262803L;
	private JTextArea display;
	private JScrollPane scrollPane;
	private JTextField enterRow, enterCol, enterValue;
	private JButton enter, solve;
	private Sudoku game;
	
	public GUI() {
		int width = 600;
		int height = 360;
		
		display = new JTextArea();
		scrollPane = new JScrollPane(display);
		scrollPane.setPreferredSize(new Dimension(width, 7 * height / 10));
		add(scrollPane);
		
		add(new JLabel("Row: "));
		enterRow = new JTextField(5);
		add(enterRow);
		
		add(new JLabel("Col: "));
		enterCol = new JTextField(5);
		add(enterCol);
		
		add(new JLabel("Value: "));
		enterValue = new JTextField(5);
		add(enterValue);
		
		
		enter = new JButton("Enter");
		enter.addActionListener(new EnterListener());
		add(enter);
		
		solve = new JButton("Solve");
		solve.addActionListener(new SolveListener());
		add(solve);
		
		JFrame frame = new JFrame("Sudoku Game");
		frame.setSize(width, height);
		frame.setContentPane(this);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(new NewGameListener());
		JMenuItem preset = new JMenuItem("Preset");
		preset.addActionListener(new PresetListener());
		JMenuItem random = new JMenuItem("Rndom");
		random.addActionListener(new RandomListener());
		
		menu.add(newGame);
		menu.add(preset);
		menu.add(random);
		
		frame.setJMenuBar(menuBar);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int upperLeftCornerX = (screenSize.width - frame.getWidth()) / 2;
		int upperLeftCornerY = (screenSize.height - frame.getHeight()) / 2;
		frame.setLocation(upperLeftCornerX, upperLeftCornerY);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}
	
	private class NewGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			game = new Sudoku();
			display.setText(game.toString());
		}
		
	}
	
	private class EnterListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int row = Integer.parseInt(enterRow.getText());
				int col = Integer.parseInt(enterCol.getText());
				int value = Integer.parseInt(enterValue.getText());
				game.setValue(row, col, value);
				display.setText(game.toString());
			}catch(InputMismatchException ee) {
				JOptionPane.showMessageDialog(null, "Please only enter integers");
			}catch(IllegalArgumentException ee){
				JOptionPane.showMessageDialog(null, ee.getMessage());
			}
		}
		
	}
	
	private class SolveListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			background(display);
		}
		
	}
	
	private void background(JTextArea display){
		Thread worker = new Thread(new Runnable() {
			public void run() {
				game.setQuestion();
				long start = System.currentTimeMillis();
				solveAux(0,0,display);
				long end = System.currentTimeMillis();
				if(game.finalCheck()){
					JOptionPane.showMessageDialog(null, "Solved in " + ((end - start)) + " ms");
				}else{
					game.showQuestion();
					display.setText(game.toString());
					JOptionPane.showMessageDialog(null, "No solution found");
				}
			}
		});
		worker.start();
	}
	
	private void solveAux(int row, int col, JTextArea display){
		if(game.isQuestion(row, col)){
			if(row <= 8 && col < 8){
				solveAux(row, col + 1, display);
				if(game.finalCheck()){
					return;
				}
			}else if(row < 8 && col == 8){
				solveAux(row + 1, 0, display);
				if(game.finalCheck()){
					return;
				}
			}else if(row == 8 && col == 8){
				return;
			}
		}else{
			for(int value = 1; value <= 9; value++){
				game.grid[row][col] = value;
				display.setText(game.toString());
				if(game.checkGrid(row, col) && game.checkSGrid(row, col)){
					if(row <= 8 && col < 8){
						solveAux(row, col + 1, display);
						if(game.finalCheck()){
							return;
						}
					}else if(row < 8 && col == 8){
						solveAux(row + 1, 0, display);
						if(game.finalCheck()){
							return;
						}
					}else if(row == 8 && col == 8){
						return;
					}
				}
			}
		}
		
		if(game.finalCheck()){
			display.setText(game.toString());
			return;
		}
		
		if(!game.isQuestion(row, col) && !game.finalCheck()){
			game.grid[row][col] = 0;
		}
	}
	
	private class PresetListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			game = new Sudoku();
			game.preset();
			display.setText(game.toString());
		}
		
	}
	
	private class RandomListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			game = new Sudoku();
			Thread t = new Thread(new Runnable(){
				public void run() {
					game.generateRandomSudoku(0, 17);
				}
			});
			t.start();
			try{
				t.join();
			}catch(InterruptedException ee){
				JOptionPane.showMessageDialog(null, ee.getMessage());
			}
			display.setText(game.toString());
		}
		
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new GUI();
			}
		});
	}
}
