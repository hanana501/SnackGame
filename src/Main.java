import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main extends JPanel implements KeyListener{
	
	public static final int CELL_SIZE = 20; // 設定每格大小
	public static int width = 400;
	public static int height = 400;
	public static int row = height / CELL_SIZE;
	public static int column = width / CELL_SIZE;
	private Snake snake;
	private Fruit fruit;
	private Timer t;
	private int speed = 100;
	public static String direction;
	private boolean allowKeyPress; 
	private int score;
	private int highest_score;
	String desktop = System.getProperty("user.home") + "/Desktop/";
	String myFile = desktop + "scorefile.txt";
	
	public Main() {
		read_highest_score();
		reset();
		addKeyListener(this); 
	}
	
	private void setTimer() {
		t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() { // 設定在某時間點執行某事件
			@Override
			public void run() {
				repaint();
			}
		}, 0, speed);
	}
	
	private void reset() {
		score = 0;
		if (snake != null) {
			snake.getSnakeBody().clear();
		}
		allowKeyPress = true;
		snake = new Snake();
		fruit = new Fruit();
		direction = "Right";
		setTimer();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		// check if the snake bites itself
		ArrayList<Node> snake_body = snake.getSnakeBody();
		Node head = snake_body.get(0);
		for (int i = 1; i < snake_body.size(); i++) {
			if(snake_body.get(i).x == head.x && snake_body.get(i).y == head.y) {
				allowKeyPress = false;
				t.cancel();
				t.purge();
				int response = JOptionPane.showOptionDialog(this, "Game Over!!!!\nYour score is " + score +". \nThe highest score was " + highest_score +".\nWould you like to start over?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,  null, JOptionPane.YES_OPTION);
				write_a_file(score);
				switch (response) {
					case JOptionPane.CLOSED_OPTION:
					case JOptionPane.NO_OPTION:
						System.exit(0);
						break;
					case JOptionPane.YES_OPTION:
						reset();
						return;
				}
			}
		}
		
		// set BG
		g.setColor(new Color(76, 84, 69));
		g.fillRect(0, 0, width, height);
		fruit.drawFruit(g);
		snake.drawSnake(g);
		
		// remove snake tail and put it in head
		int snakeX = snake.getSnakeBody().get(0).x;
		int snakeY = snake.getSnakeBody().get(0).y;
		if(direction.equals("Up")) {
			snakeY -= CELL_SIZE;
		}else if(direction.equals("Down")) {
			snakeY += CELL_SIZE;
		}else if(direction.equals("Left")) {
			snakeX -= CELL_SIZE;
		}else if(direction.equals("Right")) {
			snakeX += CELL_SIZE;
		}
		Node newHead = new Node(snakeX, snakeY);
		
		// check if snake eats the fruit
		if (snake.getSnakeBody().get(0).x == fruit.getX() && snake.getSnakeBody().get(0).y == fruit.getY()) {
			/* to-do-list:
			1.set fruit to a new location -> 到Fruit.java
			2.drawFruit
			3.score++ */
			fruit.setNewLocation(snake);
			fruit.drawFruit(g);
			score++;
		} else {
			snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
		}
		
		snake.getSnakeBody().add(0, newHead);
		
		allowKeyPress = true;
		requestFocusInWindow();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Snake Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new Main());
		window.pack(); // pack()方法是要通知frame將其尺寸設定為可以將其內部所有的元件包起來的大小
		window.setLocationRelativeTo(null); // 設定視窗顯示的位置(null是置中)
		window.setVisible(true);
		window.setResizable(false); //設定視窗大小不能被改變
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.print(e.getKeyCode()); // 可以看出上下左右的code
		if (allowKeyPress) {
			if(e.getKeyCode() == 37 && !direction.equals("Right")) {
				direction = "Left";
			}else if(e.getKeyCode() == 38 && !direction.equals("Down")) {
				direction = "Up";
			}else if(e.getKeyCode() == 39 && !direction.equals("Left")) {
				direction = "Right";
			}else if(e.getKeyCode() == 40 && !direction.equals("Up")) {
				direction = "Down";
			}
			allowKeyPress = false;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	public void read_highest_score() {
		try {
			File myObj = new File(myFile);
			Scanner myReader = new Scanner(myObj);
			highest_score = myReader.nextInt();
			myReader.close();
		} catch (FileNotFoundException e) {
			highest_score = 0;
			try {
				File myObj = new File(myFile);
				if (myObj.createNewFile()) {
					System.out.println("File created:" + myObj.getName());
				}
				FileWriter myWriter = new FileWriter(myObj.getName());
				myWriter.write("" + 0);
				myWriter.close();
			} catch(IOException err) {
				System.out.println("An error occurred");
				err.printStackTrace();
			}
		}
	}
	
	public void write_a_file(int score) {
		try {
			FileWriter myWriter = new FileWriter(myFile);
			if (score > highest_score) {
				myWriter.write("" + score);
				highest_score = score;
			}else {
				myWriter.write("" + highest_score);
			}
			myWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}