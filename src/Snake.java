import java.awt.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Snake {
	
	private ArrayList<Node> snakeBody;
	
	public Snake() {
		snakeBody = new ArrayList<>();
		snakeBody.add(new Node(80,0));
		snakeBody.add(new Node(60,0));
		snakeBody.add(new Node(40,0));
		snakeBody.add(new Node(20,0));
		}
	
	public ArrayList<Node> getSnakeBody(){
		return snakeBody;
	}
	
	public void drawSnake(Graphics g) {
		
		for(int i = 0; i < snakeBody.size(); i++) {
			// 設定頭跟身體顏色
			if (i == 0) {
				g.setColor(new Color(229, 144, 105));
			}else {
				g.setColor(new Color(236, 216, 179));
			}
			
			Node n = snakeBody.get(i);
			
			// 處理蛇的身體超過邊界的問題
			if (n.x >= Main.width) { // 超出右邊界，要從左邊出來
				n.x = 0;
			}
			if(n.x < 0 ) { // 超出左邊界，要從右邊出來
				n.x = Main.width - Main.CELL_SIZE;
			}
			if (n.y >= Main.height) { // 超出下邊界，要從上面出來
				n.y = 0;
			}
			if (n.y < 0) { // 超出上邊界，要從下面出來
				n.y = Main.height - Main.CELL_SIZE;
			}
			g.fillOval(n.x, n.y, Main.CELL_SIZE, Main.CELL_SIZE);
		}
	}
}
