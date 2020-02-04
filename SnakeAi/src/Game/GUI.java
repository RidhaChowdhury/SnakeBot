package Game;

import javax.swing.*;

import Bot.SnakeBot;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;

public class GUI extends JPanel {
	
	private BufferedImage screen;
	public static SnakeGame game;
	public static SnakeBot richard = new SnakeBot();
	
	public GUI() {
		screen = new BufferedImage(600,600,BufferedImage.TYPE_INT_ARGB);
		game = new SnakeGame();
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					game.changeDirection(SnakeGame.NORTH);
					break;
				case KeyEvent.VK_S:
					game.changeDirection(SnakeGame.SOUTH);
					break;
				case KeyEvent.VK_A:
					game.changeDirection(SnakeGame.WEST);
					break;
				case KeyEvent.VK_D:
					game.changeDirection(SnakeGame.EAST	);
					break;
				}
			}
		});
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(50);
						loop();
					} catch (Exception e) {
						
					}
				}
			}
		});
		t.start();
	}
	
	public void loop() {
		game.update();
		richard.update();
		Graphics g = screen.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		g.drawImage(game.getDisplay(), 0, 0, screen.getWidth(), screen.getHeight(), null);
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(screen,0,0,getWidth(),getHeight(),null);
	}
}
