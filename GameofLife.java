import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.util.Arrays;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class GameofLife extends Canvas {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 20; // Additional pixels needed for the frame
    private static final int HEIGHT = 20; // Additional pixels needed for the frame
    private static boolean[][] cells; //keeps track of the each cell in the game of life
    private static boolean[][] cellsOld; //the cell statuses from the previous iteration
    
    //populates the cells with random values
    public static void populate(boolean[][] cells) {
    	for(int x = 0; x < cells.length; x++) {
    		for(int y = 0; y < cells[x].length; y++) {
    			cells[x][y] = Math.random() < 0.5; //randomly assigns the array a value of true or false
    			//cells[x][y] = x%10<3 && y%10==0;
    		}
    	}
    }
    
    public static synchronized void step(boolean[][] cells) { 
    	for(int y = 0; y < cells[0].length; y++) {
    		for(int x = 0; x < cells.length; x++) {
    			cellsOld[x][y] = cells[x][y];
    		}
    	}
    	
    	//keeps track of the number of neighbors that are alive
    	int aliveNeighbors;
    	for(int y = 0; y < cells[0].length; y++) {
    		for(int x = 0; x < cells.length; x++) {
    			final int X = x;
    			final int Y = y;
    			//iterate through the whole array, and apply the Game of Life rule
    			aliveNeighbors = numberOfNeighbors(cellsOld,x,y);
    			
    			if(aliveNeighbors < 2  || aliveNeighbors > 3) {
    				//cell dies if it has fewer than two or more than 3 live neighbors
    				cells[x][y] = false;
    			}else if(aliveNeighbors==3) {
    				//cell is born if has exactly 3 neighbors
    				cells[x][y] = true;
    			}else {
    				//otherwise, cell is unchanged
    				cells[x][y] = cellsOld[x][y];
    			}
    			assert(x==X);
    			assert(y==Y);
    		}
    	}
    }
    
    public static int numberOfNeighbors(boolean[][] cellsOld, int x, int y) {
    	int r = 0; //the value to return
    	for(int i = x-1; i <= x+1; i++) {
    		for(int j = y-1; j <= y+1; j++) {
    			if(!(i==x && j==y)) {//we don't want to include the cell itself in the count
					//if(x<3 && y<3)System.out.printf("X: %d, Y: %d is " + Boolean.toString(cellsOld[(i + WIDTH)%WIDTH][(j+HEIGHT)%HEIGHT]) + "\n", (i + WIDTH)%WIDTH,(j+HEIGHT)%HEIGHT);
		
					//increment the return value if the cell is alive. all this messing around with modulo 
					//is to deal with when we are on the edge
					if( cellsOld[(i + WIDTH)%WIDTH][(j+HEIGHT)%HEIGHT] ) r++; 
    			}
    		}
    	}
		//if(x<3 && y<3) System.out.printf("X: %d, Y: %d, has %d neighbors \n", x, y, r);

    	return r;	
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        //would use a while(true) loop, but the compiler didn't like that. Besides, this allows us to keep track of which
        //step number we're on, which is useful
        for(int parity = 0; parity<5; parity++){
	        for(int y = 0; y < HEIGHT; y++) {
	        	final int Y = y;
	        	new Thread(new Runnable() {
        		    public void run() {
    		        	for(int x = 0; x < WIDTH; x++) {
    		        		if(cells[x][Y]) g.setColor(Color.WHITE);
    		        		else g.setColor(Color.BLACK);
    		        		g.drawRect(x, Y, 0, 0);
    		        	}
        		    }
        		}).start();
	        }
	        //System.out.println(Arrays.deepToString(cells));
	        //System.out.println(Arrays.deepToString(cellsOld));
	        //System.out.println("\n\n\n");
	        step(cells);
        }
        
        
        try {
            Thread.sleep(2000);             // Sleep for 2 seconds
            System.exit(0);             // Closed the program
        }catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
    	cells = new boolean[WIDTH][HEIGHT];
    	cellsOld = new boolean[WIDTH][HEIGHT];
    	populate(cells);
    	GameofLife life = new GameofLife();
    	
    	
        JFrame frame = new JFrame("Game of Life");
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.add(life);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
}