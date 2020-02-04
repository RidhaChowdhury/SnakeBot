package Bot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Random;
import Game.*;

public class SnakeBot {
	//---Variables
		String[][] selfLoathing =
		{{"God why did you make me", "does this ever end", "i wish you programed a way for me to end myself"
		}
		};
		Point targetLocation;
		Point head;
		boolean huntingFruit = true;
		boolean stacking = false;
	//---Constructors
		public SnakeBot()	{
		}
	//---Methods
		//--Doers
			public void update()	{
				logicLoop();
			}
				//boolean inRisk
				public void logicLoop()	{
					variableUpdateLoop();
					hazardCheck();
					//if(inRisk)
					movementLoop();
				}
					public void variableUpdateLoop()	{
						if(!stacking)
							targetLocation = GUI.game.getFruitLocation();
						head = GUI.game.getHeadLocation();
					}
					
					public void hazardCheck()	{
						if(GUI.game.getSnakeDistanceToSelf()!=-1 && GUI.game.getSnakeDistanceToSelf()<5)	{
							Random turn = new Random();
							if(GUI.game.getDirection()==(SnakeGame.NORTH)||GUI.game.getDirection()==(SnakeGame.SOUTH))	{
								if(turn.nextBoolean())
									GUI.game.changeDirection(SnakeGame.WEST);
							}
							if(GUI.game.getDirection()==(SnakeGame.EAST)||GUI.game.getDirection()==(SnakeGame.WEST))	{
								if(turn.nextBoolean())
									GUI.game.changeDirection(SnakeGame.NORTH);
							}
							eatsTreat();
						}
					}
					
					boolean xLinedUp = false;
					boolean yLinedUp = false;
					boolean xFirst;
					
					boolean firstMove = true;
					boolean secondMove = true;
					public void movementLoop()	{
						head = GUI.game.getHeadLocation();
						if(head.getX()==targetLocation.getX())
							xLinedUp = true;
						else
							xLinedUp = false;
						if(head.getY()==targetLocation.getY())
							yLinedUp = true;
						else
							yLinedUp = false;
						
						if(firstMove)	{
							if(GUI.game.getDirection()==(SnakeGame.NORTH)||GUI.game.getDirection()==(SnakeGame.SOUTH)&&!xLinedUp)	{
								inputXMovement();
								xFirst = true;
							}
							else if(GUI.game.getDirection()==(SnakeGame.EAST)||GUI.game.getDirection()==(SnakeGame.WEST)&&!yLinedUp)	{
								inputYMovement();
								xFirst = false;
							}
							firstMove = false;
						}
						else if(secondMove)	{
							if(xFirst)	{
								if(xLinedUp)
									inputYMovement();
							}
							else
								if(yLinedUp)	{
									inputXMovement();
								}
							
						}
						
					}
						private void inputXMovement()	{
							head = GUI.game.getHeadLocation();
							if((head.getX()>targetLocation.getX()))
								GUI.game.changeDirection(SnakeGame.WEST);
							else
								GUI.game.changeDirection(SnakeGame.EAST);
						}
						private void inputYMovement()	{
							if((head.getY()>targetLocation.getY()))
								GUI.game.changeDirection(SnakeGame.NORTH);
							else
								GUI.game.changeDirection(SnakeGame.SOUTH);
						}
						public void eatsTreat()	{
							 xLinedUp = false;
							 yLinedUp = false;
							 xFirst = false;
							
							 firstMove = true;
							 secondMove = true;
						}
					
					int loathWords;
					boolean doneLoath = true;
					int loathLength = 30;
					int loopsLeftLoath;
					int loathSpot;
					public void drawSelfLoathing(Graphics g)	{
						if(doneLoath)	{
							Random whichLoathRand = new Random();
							loathWords = whichLoathRand.nextInt(selfLoathing.length);
							
							doneLoath = false;
							loathSpot = 0;
							loopsLeftLoath = loathLength;
						}
						loopsLeftLoath--;
						if(loopsLeftLoath <= 0)	{
							if(loopsLeftLoath <= 0 && loathSpot + 1 < selfLoathing[loathWords].length)	{
								loathSpot++;
								loopsLeftLoath = loathLength;
							}
							else
								doneLoath = true;
						}
						
						
						g.drawString(selfLoathing[loathWords][loathSpot], (int)GUI.game.getHeadLocation().getX()+10, (int)GUI.game.getHeadLocation().getY()-10);
					}
			public double calculateTreatDistance(Point treat)	{
				Point head = GUI.game.getHeadLocation();
				double distanceX = Math.abs(head.getX() - treat.getX());
				double distanceY = Math.abs(head.getY() - treat.getY());
				double totalDistance = Math.abs(Math.sqrt((distanceX*distanceX)+(distanceY*distanceY)));
				return totalDistance;
			}
			
		//---Getters
			
		//---Setters
}
