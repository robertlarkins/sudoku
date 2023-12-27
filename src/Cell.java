public class Cell {
	boolean[] possibleNumbers;
	boolean onlyOneNumber;
	int finalNumber;
	
	public Cell() {
		possibleNumbers = new boolean[9];
		onlyOneNumber = false;
		finalNumber = 0;
		for(int i = 0; i < 9; i++) {
			possibleNumbers[i] = true;
		}
	}
	
	public Cell(int setNumber) {
		finalNumber = setNumber;
		onlyOneNumber = true;
	}
	
   public void setNumber(int number) {
       finalNumber   = number;
       onlyOneNumber = true;
   }
   
	public void removeNumber(int numberToRemove) {
		if(onlyOneNumber) return;
		possibleNumbers[numberToRemove - 1] = false;
		lastPossibleNumber();
	}
	
   private void lastPossibleNumber() {
      int countTrue = 0;
		int trueNumber = 0;
		for(int i = 0; i < 9 && countTrue <= 1; i++)
			if(possibleNumbers[i] == true){ countTrue++; trueNumber = i + 1; }
				
		if(countTrue == 1) { setNumber(trueNumber); }
   }
   
   public boolean truePossibleNumber (int place) {
      if(!onlyOneNumber && (place >= 0 && place <= 8)) {
          return  possibleNumbers[place];
      }
      else return false;
   }
   
	public boolean oneNumber() {
		return onlyOneNumber;
	}
	
	public int getFinalNumber() {
		return finalNumber;
	}
	
}
