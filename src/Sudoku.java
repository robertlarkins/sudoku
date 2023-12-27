import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class Sudoku {
	
	JButton solveButton;
	JPanel puzzleGrid;
	ArrayList<JTextField> gridPositions = new ArrayList<JTextField>();
	Cell[][] cellGrid = new Cell[9][9];
	
	public static void main(String[] args) {
		Sudoku gui = new Sudoku();
		gui.go();
	}

	public void go() {
		
		JFrame frame = new JFrame();
		
		JLabel label = new JLabel("Sudoku Puzzle Solver");
		
		JPanel puzzleTitle = new JPanel(new GridBagLayout());
		puzzleTitle.add(label);
		
		
		puzzleGrid  = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JTextField field;
		
		for(int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				field = new JTextField(1);
				if(!gridPositions.add(field)) System.out.println("Failed to add");
				c.gridx = j;
				c.gridy = i;
				puzzleGrid.add(field, c);
			}
		}
		
		
		
		solveButton = new JButton("Solve Puzzle");
		solveButton.addActionListener(new SolveButtonListener());
		
		JButton resetButton = new JButton("Reset");
		
		frame.getContentPane().add(BorderLayout.WEST, resetButton);
		frame.getContentPane().add(BorderLayout.NORTH, puzzleTitle);
		frame.getContentPane().add(BorderLayout.CENTER, puzzleGrid);
		frame.getContentPane().add(BorderLayout.SOUTH, solveButton);
		

		
		
		frame.setSize(300, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	/***************************************\
	|*  InnerClass
	|*
	|*
	|*
	|*
	|*
	\***************************************/
	class SolveButtonListener implements ActionListener {
		JTextField field;
		
		public void actionPerformed(ActionEvent event) {
			solveButton.setText("Solving");
			fillGrid();
         solveGrid();
			outputGrid();
			
			solveButton.setText("Solved");
		}
		
		public void fillGrid() {
			int counter = 0;
			
			
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					field = gridPositions.get(counter);
					
					counter++;
					
					if((field.getText() != null && field.getText().length() > 0) && asciiNumber(field.getText().charAt(0))) cellGrid[i][j] = new Cell(field.getText().charAt(0) - 48);
					else                      cellGrid[i][j] = new Cell();
				}
			}
		}
		
      private void solveGrid() {
          for(int a = 0; a < 200; a++) {
          for(int i = 0; i < 9; i++) {          //goes through the rows
              for(int j = 0; j < 9; j++) {      //goes through the columns
                  if(cellGrid[i][j].oneNumber()) continue; //if it already has its final number, then continue
                  
                  for(int k = 0; k < 9; k++) {  //checks the row and column
                      if(cellGrid[k][j].oneNumber() && !(cellGrid[k][j].equals(cellGrid[i][j]))) cellGrid[i][j].removeNumber(cellGrid[k][j].getFinalNumber()); //checks the row
                      if(cellGrid[i][k].oneNumber() && !(cellGrid[i][k].equals(cellGrid[i][j]))) cellGrid[i][j].removeNumber(cellGrid[i][k].getFinalNumber()); //checks the row   
                  }
              
                  //checks the box that the number is in
                  int x = 0;
                  int y = 0;
                  
                       if(i == 0 || i == 3 || i == 6) x =  0;
                  else if(i == 1 || i == 4 || i == 7) x = -1;
                  else if(i == 2 || i == 5 || i == 8) x = -2;
                       if(j == 0 || j == 3 || j == 6) y =  0;
                  else if(j == 1 || j == 4 || j == 7) y = -1;
                  else if(j == 2 || j == 5 || j == 8) y = -2;
                  
                  
                  for(int l = x; l <= x+2; l++) {
                      for(int m=y; m <= y+2; m++) {
                          if(cellGrid[i][j].equals(cellGrid[i+l][j+m])) continue;
                          if(cellGrid[i+l][j+m].oneNumber()) cellGrid[i][j].removeNumber(cellGrid[i+l][j+m].getFinalNumber());
                      }
                  }
                  
                  //checks to see if a cell has a possibility that all the other cells in its area doesnt
                  int no = 0;
                  boolean onlyPossibility = true;
                  
                  for(int b = 0; b < 9; b++) {
                      if(cellGrid[i][j].truePossibleNumber(b)) no = b+1;
                      else continue;
                      onlyPossibility = true;
                      
                      for(int l = x; l <= x+2 && onlyPossibility; l++) {
                          for(int m=y; m <= y+2 && onlyPossibility; m++) {
                              if(cellGrid[i][j].equals(cellGrid[i+l][j+m])) continue;
                              if(cellGrid[i+l][j+m].truePossibleNumber(b)) onlyPossibility = false;
                          }
                      }
                      
                      if(onlyPossibility) { cellGrid[i][j].setNumber(b+1); break; }
                  }
                  
                  //check to see if a cell contains a possibility that all other cells in its row doesnt have
                  int no2 = 0;
                  boolean onlyPossibility2 = true;
                  
                  for(int c = 0; c < 9; c++) {
                      if(cellGrid[i][j].truePossibleNumber(c)) no2 = c+1;
                      else continue;
                      onlyPossibility2 = true;
                      
                      for(int l=0; l < 9; l++) {
                          if(cellGrid[i][j].equals(cellGrid[l][j])) continue;
                          if(cellGrid[l][j].truePossibleNumber(c)) onlyPossibility2 = false;
                      }
                      
                      if(onlyPossibility2) { cellGrid[i][j].setNumber(c+1); break; }
                  }
                  
                  //check to see if a cell contains a possibility that all other cells in its column doesnt have
                  int no3 = 0;
                  boolean onlyPossibility3 = true;
                  
                  for(int d = 0; d < 9; d++) {
                      if(cellGrid[i][j].truePossibleNumber(d)) no3 = d+1;
                      else continue;
                      onlyPossibility3 = true;
                      
                      for(int l=0; l < 9; l++) {
                          if(cellGrid[i][j].equals(cellGrid[i][l])) continue;
                          if(cellGrid[i][l].truePossibleNumber(d)) onlyPossibility3 = false;
                      }
                      
                      if(onlyPossibility3) { cellGrid[i][j].setNumber(d+1); break; }
                  }
                  
                  //checks row for 2 cells that have 2 possibilites that other cells dont have
                  int poss1 = 0;
                  int poss2 = 0;
                  for(int e = 0; e < 9; e++) {
                      if(cellGrid[i][j].truePossibleNumber(e)) poss1 = e;
                      
                      for(int f = e+1; f < 9; f++) {
                          if(cellGrid[i][j].truePossibleNumber(f)) poss2 = f;
                          
                          boolean both = false;
                          int count = 0;
                          
                          for(int g = 0; g < 9; g++) {
                              if(cellGrid[i][j].equals(cellGrid[i][g])) continue;
                              if(cellGrid[i][g].truePossibleNumber(poss1) && cellGrid[i][g].truePossibleNumber(poss2)) { both = true; count++; }
                          }
                          
                          if(count == 1) {
                              //then go through and remove the dual possibilities
                          }
                      }
                  }
              }
          }
          }
      }
   }
      
		private boolean asciiNumber(char asciiCharacter) {
			if(asciiCharacter >= 48 && asciiCharacter <= 57) return true;
			return false;
		}
		
		public void outputGrid() {
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					System.out.print(cellGrid[i][j].getFinalNumber());
				}
				System.out.println("");
			}
		}
	}
	
	/***************************************\
	|*  InnerClass
	|*
	|*
	|*
	|*
	|*
	\***************************************/
	class ResetButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			for(int i = 0; i < gridPositions.size(); i++) {
				JTextField field = gridPositions.get(i);
				gridPositions.remove(i);
				field.setText("");
				gridPositions.add(i, field);
			}
			solveButton.setText("Solve Puzzle");
			
		}
	}
	
	
	
}
