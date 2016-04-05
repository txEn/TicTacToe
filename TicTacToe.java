import java.util.*;

class Child{

    public char[][] state;
    public String mov;
    
    Child(char[][] state, String mov){
	this.state = state;
	this.mov=mov;
    }
    
}

class TicTacToe{

    public static boolean turn;       //true if it's the player's turn  
    public static String player;      //player symbol
    public static String comp;        //computer symbol    
    public static int algo;           //type of algorithm
    public static char[][] currState; //current state
    public static String[][] table = {{"A1", "B1", "C1"}, {"A2", "B2", "C2"}, {"A3", "B3", "C3"}};
    public static int nrStates;       //nr of successor states considered
    public static long timeStart;
    public static long timeEnd;
    public static long elapsedTime;    
    public static String compMove;    //variable used to store action chosen by AI

    public static int alphabeta(char[][] state, int depth, int a, int b, boolean maxPlay){

	//feito apartir do pseudocodigo da wikipedia do alfa beta (funciona)
	
	String temp = isTerminal(state);
	int v = 0;
	if(!temp.equals("NO")){
	    return getScore(state, temp, depth);
	}
	if(maxPlay){
	    v = Integer.MIN_VALUE;
	    for(Child act:successors(state, "max")){
		nrStates++;
		int tmp = v;
		v = max(v, alphabeta(act.state, depth+1, a, b, false));
		if(v > tmp && depth == 1)
		    compMove = act.mov; 
		a = max(a, v);
		if(b <= a)
		    break;		
	    }
	}else{
	    v = Integer.MAX_VALUE;
	    for(Child act:successors(state, "min")){
		nrStates++;
		v = min(v, alphabeta(act.state, depth+1, a, b, true));
		b = min(b, v);
		if(b <= a)
		    break;
	    }
	}
	return v;
    }
    
    public static int minimax(char[][] state, int d, boolean maxPlay){
	
	String temp = isTerminal(state);
	int v = 0;
	
	if(!temp.equals("NO")){
	    return getScore(state, temp, d);
	}

	if(maxPlay){
	    v = Integer.MIN_VALUE;
	    for(Child act:successors(state, "max")){
		nrStates++;
		int tmp = v;
		v = max(v, minimax(act.state, d+1, false));
		if(v > tmp && d == 1)
		    compMove = act.mov; 		
	    }
	}else{
	    v = Integer.MAX_VALUE;
	    for(Child act:successors(state, "min")){
		nrStates++;
		v = min(v, minimax(act.state, d+1, true));
	    }
	}
	return v;
    }    
    
    public static LinkedList<Child> successors(char[][] state, String type){

	LinkedList<Child> succ = new LinkedList<Child>();
	
        for(int i=0; i<3; i++){
	    for(int j=0; j<3; j++){
		if(state[i][j] == ' '){
		    if(type.equals("max")){
			char[][] temp = clone(state);
			temp[i][j] = comp.charAt(0);
			succ.add(new Child(temp, getMove(i, j)));
		    }else{
		        char[][] temp = clone(state);
			temp[i][j] = player.charAt(0);
			succ.add(new Child(temp, getMove(i, j)));
		    }
		}
	    }
	}
	
	return succ;
    }
    
    public static String getMove(int y, int x){
	return table[y][x];
    }

    public static int max(int v, int val){
	if(v > val)
	    return v;
	return val;
    }
    public static int min(int v, int val){
	if(v < val)
	    return v;
	return val;
    }

    public static int getScore(char[][] state, String winner, int d){

	//Subtrair depth à heuristica para priorizar os nós mais acima
	
	if(winner.equals("X")){
	    if(winner.equals(player))
		return -10+d;
	    else
		return 10-d;	    
	}
        if(winner.equals("O")){
	    if(winner.equals(player))
		return -10+d;
	    else
		return 10-d;
	}
	//draw
	return 0;
    }    

    public static void endGame(String winner){
	if(player.equals(winner))
	    System.out.println("You won.");
	else if(comp.equals(winner))
	    System.out.println("You lost.");
	else
	    System.out.println("It's a draw.\n" );	
    }
    
    public static String isTerminal(char[][] state){
	
	int j=0;
	
	//Horizontal
	for(int i=0; i<3; i++)
	    if(state[i][j] == state[i][j+1] && state[i][j] == state[i][j+2] && state[i][j] != ' ')
		return String.valueOf(state[i][j]);	    
	
	//Vertical
	for(int i=0; i<3; i++)
	    if(state[j][i] == state[j+1][i] && state[j][i] == state[j+2][i] && state[j][i] != ' ')
		return String.valueOf(state[j][i]);	    
	
	//Diagonal
        if(state[0][0] == state[1][1] && state[0][0] == state[2][2] && state[0][0] != ' '){
	    return String.valueOf(state[0][0]);
	}
	if(state[0][2] == state[1][1] && state[0][2] == state[2][0] && state[0][2] != ' '){
	    return String.valueOf(state[0][2]);
	}

	//Tie
	for(int i=0; i<3; i++)
	    for(int k=0; k<3; k++)
		if(state[i][k] == ' ')
		    return "NO";

	return "DRAW";

	
    }

    public static boolean evalMove(String move){
	switch(move){
	case "A1" : return (currState[0][0]==' ')?true:false;
	case "B1" : return (currState[0][1]==' ')?true:false; 
	case "C1" : return (currState[0][2]==' ')?true:false; 
	case "A2" : return (currState[1][0]==' ')?true:false; 
	case "B2" : return (currState[1][1]==' ')?true:false; 
	case "C2" : return (currState[1][2]==' ')?true:false; 
	case "A3" : return (currState[2][0]==' ')?true:false; 
	case "B3" : return (currState[2][1]==' ')?true:false; 
	case "C3" : return (currState[2][2]==' ')?true:false; 
	default: return false;
	}
    }
    public static void play(String move){
	String tmp="";
	if(turn == true)
	    tmp = player;
	else
	    tmp = comp;
	
	switch(move){
	case "A1" : currState[0][0]=tmp.charAt(0); break;
	case "B1" : currState[0][1]=tmp.charAt(0); break;
	case "C1" : currState[0][2]=tmp.charAt(0); break;
	case "A2" : currState[1][0]=tmp.charAt(0); break;
	case "B2" : currState[1][1]=tmp.charAt(0); break;
	case "C2" : currState[1][2]=tmp.charAt(0); break;
	case "A3" : currState[2][0]=tmp.charAt(0); break;
	case "B3" : currState[2][1]=tmp.charAt(0); break;
	case "C3" : currState[2][2]=tmp.charAt(0); break;
	default: System.exit(1); //Won't happen.
	}
    }
   
    public static char[][] clone(char[][] state){
	char[][] clone = new char[3][3];
	for(int i=0; i<3; i++)
	    for(int j=0; j<3; j++)
		clone[i][j] = state[i][j];

	return clone;
    }
    
    public static void printState(char[][] state){
	//state[y][x]
	System.out.println();
	if(turn == true)
	    System.out.println("Computer's move");
	else
	    System.out.println("Your move");
	System.out.println();
	System.out.println("\t A   B   C");
	System.out.println("\t   |   |   ");
	System.out.println("\t "+state[0][0]+" | "+state[0][1]+" | "+state[0][2]+"  1");
	System.out.println("\t   |   |   ");					
	System.out.println("\t-----------");
	System.out.println("\t   |   |   ");
	System.out.println("\t "+state[1][0]+" | "+state[1][1]+" | "+state[1][2]+"  2");
	System.out.println("\t   |   |   ");
	System.out.println("\t-----------");
	System.out.println("\t   |   |   ");
	System.out.println("\t "+state[2][0]+" | "+state[2][1]+" | "+state[2][2]+"  3");
	System.out.println("\t   |   |   ");	
	System.out.println();
    }
    
    public static void main(String args[]){

	Scanner input = new Scanner(System.in);
	currState = new char[3][3];
	nrStates=0;
	
	System.out.println();
	System.out.println("[TicTacToe]");
	System.out.println();	
	System.out.println("Select the type of adversary.");
	System.out.println("1 -> Minimax");
	System.out.println("2 -> Alpha-Beta");	
        algo = input.nextInt();	
	while(algo != 1 && algo != 2){
	    System.out.println("Wrong type of adversary, try again. (1 - Minimax 2 - Alpha-Beta");
	    algo = input.nextInt();
	}	
        System.out.println("Choose your symbol (X or O).");	
        player = input.next();
	while(!player.equals("X") && !player.equals("O")){
	    System.out.println("Wrong symbol, choose another (X or O).");
	    player = input.next();
	}	
	if(player.equals("X"))
	    comp = "O";
	else
	    comp = "X";

	System.out.println("Want to begin first? (yes or no)");
        turn = false;
	
	if(input.next().toLowerCase().equals("yes")){
	    turn = true;
	}
	
	
	for(char[] row: currState){
	    Arrays.fill(row, ' ');
	}

	String win;
	
	while((win = isTerminal(currState)) == "NO"){

	    String move="";
	    printState(currState);

	    if(turn){
		//player's turn
		System.out.print("[makeMove]$ ");
	        move = input.next();
		while(!evalMove(move)){
		    System.out.println("Your move was invalid.");
		    System.out.println();
		    System.out.print("[makeMove]$ ");
		    move = input.next();
		}
		play(move);
		turn = !turn;
		continue;
	    }
	    //computer's turn
	    timeStart = System.nanoTime();
	    if(algo == 1)
		minimax(currState, 1, true);
	    else if(algo == 2)
		alphabeta(currState, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
	    timeEnd = System.nanoTime();
	    elapsedTime = timeEnd - timeStart;
	    System.out.printf("Execution time:  %.16f\n", (double)elapsedTime/1000000000.0);
	    move = compMove;	    
	    if(evalMove(move)){
		play(move);
	    }
	    System.out.println(nrStates + " sucessor states.");
	    nrStates=0;

	    turn = !turn;
	}
	printState(currState);
	endGame(win);
	
    }

}
