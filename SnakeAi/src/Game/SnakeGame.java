package Game;

import java.util.List;

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SnakeGame {
	//static var to hold direction information -- correlates to cardinal positions
	public static final int NORTH = 0, SOUTH = 1, WEST = 2, EAST = 3;
	
	private int width = 25, height = 25;
	public static int scale = 35;
	
	//holds information for the snake and game..
	//a list of points of each part of the snake where the first index is the head
	private List<Point> snake;
	//energy of the snake.. each point is a movement of the snake
	private int energy = scale; //resets +scale when a fruit is collected
	//a point where the fruit is located
	private Point fruit;
	//the current direction of the snake
	private int direction = NORTH;
	
	public SnakeGame() {
		snake = new ArrayList<Point>();
		snake.add(new Point(width/2,height/2));
		fruit = new Point(0,0);
		moveFruit();
		display = new BufferedImage(width*scale,height*scale,BufferedImage.TYPE_INT_ARGB);
	}
	
	private BufferedImage display;
	
	public BufferedImage getDisplay() {
		return display;
	}
	
	public void render() {
		Graphics g = display.getGraphics();
		//fill the background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, display.getWidth(), display.getHeight());
		//render the bot stuff-
		g.setColor(Color.RED);
		int lineWidth = 7;
		for(int i = 0; i<lineWidth; i++)	{
			int offset = (scale-2)/2+i-lineWidth/2;
			g.drawLine((int)GUI.game.getHeadLocation().getX()*GUI.game.scale+offset, (int)GUI.game.getHeadLocation().getY()*GUI.game.scale+offset,
					(int)fruit.getX()*GUI.game.scale+offset, (int)fruit.getY()*GUI.game.scale+offset);
		}
		//render the fruit
		g.setColor(Color.RED);
		g.fillRect(fruit.x*scale+1, fruit.y*scale+1, scale-2, scale-2);
		//render the snake
		g.setColor(Color.BLUE);
		for (Point p : snake) 
			g.fillRect(p.x*scale+1, p.y*scale+1, scale-2, scale-2);
		g.setColor(Color.GREEN);
		Point head = this.getHeadLocation();
		g.fillRect(head.x*scale+1, head.y*scale+1, scale-2, scale-2);
		//color in the score in the top left corner
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial",Font.BOLD,scale));
		g.drawString("Score: "+this.getSnakeLength(), scale/2, scale);
		//color in the energy below it
		g.drawString("Energy: "+energy, scale/2, scale*2);
		
		g.drawString("Distance to self: "+this.getSnakeDistanceToSelf(), scale/2, scale*3);
		
		g.drawString("Distance to wall: "+this.getSnakeDistanceToWall(), scale/2, scale*4);
	}
	
	public void endGame(String cause) {
		JOptionPane.showMessageDialog(null, "GAME OVER!\n"+cause+"\nScore: "+this.getSnakeLength(), "GAME OVER!", JOptionPane.INFORMATION_MESSAGE);
		//restart the stuff
		GUI.richard.eatsTreat();
		moveFruit();
		snake = new ArrayList<Point>();
		snake.add(new Point(width/2,height/2));
		energy = scale;
	}
	
	public void update() {
		//the snake automatically moves
		Point head = this.getHeadLocation(); //a pointer to the first element of the snake array list
		
		for (int i = snake.size()-1; i > 0; i--) {
			Point previous = snake.get(i-1);
			Point p = snake.get(i);
			p.x = previous.x;
			p.y = previous.y;
		}
		
		switch (direction) {
		case NORTH:
			head.y--;
			break;
		case SOUTH:
			head.y++;
			break;
		case EAST:
			head.x++;
			break;
		case WEST:
			head.x--;
			break;
		}
		
		energy--;
		
		//now see if the head moved into any part of the snake
		for (int i = 1; i < snake.size(); i++) {
			if (i > snake.size()-1)
				break;
			Point p = snake.get(i);
			if (p != null && p.x == head.x && p.y == head.y) 
				//GAME OVER
				endGame("You ran into yourself!");
		}		
		
		//check if the head of the snake and the fruit of the snake are the same
		//if so then add another unit to the snake and move the fruit
		if (head.x==fruit.x && head.y==fruit.y) {
			snake.add(new Point(snake.get(snake.size()-1).x,snake.get(snake.size()-1).y));
			GUI.richard.eatsTreat();
			moveFruit();
			energy+=(scale);
		}
		
		//if the head has gone off the map then it dies
		if (head.x < 0 || head.x > width-1 || head.y < 0 || head.y > height-1)
			endGame("You tried to go off the map!");
		
		if (energy == 0)
			endGame("You ran out of energy!");
		
		//finally render the display
		render();
	}
	
	public void moveFruit() {
		fruit.x = (int)(Math.random()*width);
		fruit.y = (int)(Math.random()*height);
		//make sure the fruit is not in the snake
		for (Point p : snake) 
			if (p.x == fruit.x && p.y == fruit.y) {
				moveFruit();
				return; // do not continue calling the method
			}
	}
	
	public void changeDirection(int direction) {
		this.direction = direction;
		//ensure error detection
		if (this.direction < 0)
			this.direction = 0;
		if (this.direction > 3)
			this.direction = 3;
	}
	
	public int getDirection()	{
		return this.direction;
	}
	
	public Point getHeadLocation() {
		return snake.get(0);
	}
	
	public Point getFruitLocation() {
		return fruit;
	}
	public int getEnergy()	{
		return energy;
	}
	public int getLength()	{
		return snake.size();
	}
	//returns -1 if the snake is not going to hit itself.. otherwise it returns the distance
	public int getSnakeDistanceToSelf() {
		//look into the direction of the movement and see 
		Point head = getHeadLocation();
		int x = head.x, y = head.y;
		int count = 0;
		switch (direction) {
		case NORTH:
			//increment y downwards
			y--;
			while (!intersectsSnake(x,y)) {
				y--;
				if (y < 0)
					return -1;
				count++;
			}
			return count;
		case SOUTH:
			//increment y upwards
			y++;
			while (!intersectsSnake(x,y)) {
				y++;
				if (y > height-1)
					return -1;
				count++;
			}
			return count;
		case EAST:
			//increment x upwards
			x++;
			while (!intersectsSnake(x,y)) {
				x++;
				if (x > width-1)
					return -1;
				count++;
			}
			return count;
		case WEST:
			//increment x downwards
			x--;
			while (!intersectsSnake(x,y)) {
				x--;
				if (x < 0)
					return -1;
				count++;
			}
			return count;
		}
		return -1;
	}
	
	public int getSnakeDistanceToWall() {
		Point head = this.getHeadLocation();
		switch (direction) {
		case NORTH:
			return head.y;
		case SOUTH:
			return height-head.y;
		case EAST:
			return width-head.x;
		case WEST:
			return head.x;
		}
		return -1;
	}
	
	private boolean intersectsSnake(int x, int y) {
		for (Point p : snake) 
			if (x == p.x && y == p.y)
				return true;
		return false;
	}
	
	public int getSnakeLength() {
		return snake.size();
	}
	
	public int getMapWidth() {
		return width;
	}
	
	public int getMapHeight() {
		return height;
	}
}
