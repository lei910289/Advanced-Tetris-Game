import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.naming.InitialContext;


class MainCav extends Canvas implements MouseListener, MouseMotionListener, MouseWheelListener {
	Random random = new Random();
	int level = 1, line = 0, score = 0, factor = 0, level_up = 0, speed = 700, curt = 1, curtId = 0;
	float speedFactor;
	int next = random.nextInt(7);
	Shapes shapes = new Shapes();
	Color[] colors = shapes.getColor();
	boolean[][] curtShape = shapes.getShape(curt, curtId), nextShape = shapes.getShape(next, 0);
	int[] curtBounds = shapes.getBound(curt, curtId);
	private int xO, yO, curtX, curtY, nextX, nextY, dx, dy, row, col, leftBound, rightBound, upBound, bottomBound, unit;
	private boolean mouse;
	private boolean end = false;
	private boolean changeCur;
	Color[][] board;

	public  MainCav(int factor, int level_up, float speedFactor, int row, int col, int unit) {
		// initial board
		board = new Color[row][col];
		this.row = row;
		this.col = col;
		dx = col - 10;
		dy = row - 20;
		// initial bound and unit
		this.unit = unit;
		leftBound = 40;
		rightBound = 40 + col * unit;
		upBound = 56;
		bottomBound = 56 + row * unit;
		// initial shape default position
		curtX = 40 + (col / 2 - 2) * unit;
		curtY = 56;
		nextX = 40 + (col + 2) * unit;
		nextY = 56;
		
		// initial factor, level_up and speedFactor
		this.factor = factor;
		this.level_up = level_up;
		this.speedFactor = speedFactor;
		
		xO = 40;
		yO = 56;
//		board[19][1] = Color.black;
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		InitialContext();
		for (int i = 0; i < board.length; i++) {
			Arrays.fill(board[i], null);
		}
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(!end){
					if (!mouse) {
						if (touchBottom()) {
							saveCurt();
							delete();
							if (gameOver()) {
								end = true;
							}
							curt = next;
							curtId = 0;
							next = random.nextInt(7);
							curtX = 40 + (col / 2 - 2) * unit;
							curtY = 56;
						} else {
							curtY += unit;
						}
					}
					repaint();
					
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread1.start();
	}
	public void InitialContext() {
		
	}
	void initgr(Graphics g) {
	}

	int iX(int x) {
		return xO + (x - 1) * unit;
	}
	int iY(int y) {
		return yO + (y - 1) * unit;
	}
	int round(float x) {
		return Math.round(x);
	}


	public void drawCube(int x, int y, Graphics g, Color color) {
		g.setColor(Color.BLACK);
		g.drawRect(x, y, unit, unit);
		g.setColor(color);
		g.fillRect(x, y, unit, unit);
	}

	public  void freshCurtShape(int x, int y, boolean[][] curtShape, Graphics g) {
		for (int i = 0; i < curtShape.length; i++) {
			for (int j = 0; j < curtShape[i].length; j++) {

				if (curtShape[i][j]) {
					drawCube(x + j * unit, y + i * unit, g, colors[curt]);
				}
			}
		}
	}
	
	public  void freshNextShape(int x, int y, boolean[][] nextShape, Graphics g) {
		for (int i = 0; i < nextShape.length; i++) {
			for (int j = 0; j < nextShape[i].length; j++) {

				if (nextShape[i][j]) {
					drawCube(x + j * unit, y + i * unit, g, colors[next]);
				}
			}
		}
	}
	public void paint(Graphics g) {
		initgr(g);
		// draw main canv
		g.drawRect(xO, yO, col * unit, row * unit);
		// draw next shape canv
		g.drawRect(xO + (col + 1) * unit, yO, 6 * unit, 4 * unit);

		
		if(!end && mouse){
			g.setColor(new Color(0, 102, 204));
			g.setFont(new Font("Font.TYPE1_FONT", Font.BOLD, round(1.3F * unit)));
			g.drawRect(xO + (col /2 - 3) * unit, yO + (row / 2) * unit, 6 * unit, 2 * unit);
			g.drawString("PAUSE", xO + (col /2 - 3) * unit + 20, yO + (1 + row / 2) * unit + 12);
			if (changeCur) {	
				next = curt;
				curt = random.nextInt(7);
				curtId = 0;
				changeCur = false;
				score -= level * speedFactor;
				System.out.println(score);
				repaint();
			}
		}

		Font font = new Font("Font.TYPE1_FONT", Font.BOLD, Math.round(0.8F * unit));
		g.setFont(font);
		g.setColor(Color.BLACK);
		
		g.drawString("Level:         " + level, xO + (col + 2) * unit, yO + 4 * unit + 3 * unit);	
		g.drawString("Line:          " + line, xO + (col + 2) * unit, yO + 4 * unit + 6 * unit);	
		g.drawString("Score:        " + score, xO + (col + 2) * unit, yO + 4 * unit + 9 * unit);	

		// draw quit
		g.drawRect(xO + (12 + dx) * unit, yO + (18 + dy) * unit, round(4F * unit), round(1.6F * unit));
		g.drawString("QUIT", xO + (12 + dx) * unit + 20, yO + (19 + dy) * unit + 7);
		curtShape = shapes.getShape(curt, curtId);
		nextShape = shapes.getShape(next, 0);
		curtBounds = shapes.getBound(curt, curtId);
		freshNextShape(nextX, nextY, nextShape, g);
		freshBoard(g);
		if (end) {
			showGameOver(g);
			return;
		}
		freshCurtShape(curtX, curtY, curtShape, g);
	}
	

	public boolean gameOver() {
		for (int i = 0; i < board[0].length; i++) {
			if (board[0][i] != null) return true;
		}
		return false;
	}

	public void showGameOver(Graphics g) {
		g.setColor(new Color(0, 102, 204));
		g.drawRect(xO + (col /2 - 3) * unit, yO + (row / 2) * unit, 6 * unit, 2 * unit);
		g.drawString("Game Over", xO + (col /2 - 3) * unit + 20, yO + (1 + row / 2) * unit + 12);
	}

	public void saveCurt() {
		for (int i = 0; i < curtShape.length; i++) {
			for (int j = 0; j < curtShape[0].length; j++) {
				if (curtShape[i][j] == false) continue;
				int xIndex = (curtY - 56) / unit + i;
				int yIndex = (curtX - 40) / unit + j;
				if (xIndex < 0 || xIndex >= row || yIndex < 0 || yIndex >= col) return;
				board[xIndex][yIndex] = colors[curt];
			}
		}
	}
	
	public void delete() {
		for (int i = 0; i < board.length; i++) {
			int j = 0;
			for (; j < board[0].length; j++) {
				if (board[i][j] == null) break;
			}
			if (j == board[0].length) {
				line += 1;
				score += level * factor;
				if (line >= level_up) {
					line = 0;
					level++;
					speed = (int)(speed / (1 + level * speedFactor));
				}
				for (int k = i; k >= 0; k--) {
					if (k == 0) Arrays.fill(board[0], null);
					else board[k] = board[k - 1]; 
				}
			}
		}
	}
	public boolean touchBottom() {
		for (int i = 0; i < curtShape.length; i++) {
			for (int j = 0; j < curtShape[0].length; j++) {
				if (curtShape[i][j] == false) continue;
				int xIndex = (curtY - 56) / unit + i;
				int yIndex = (curtX - 40) / unit + j;
				if (xIndex == row - 1 || board[xIndex + 1][yIndex] != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public void freshBoard(Graphics g) {

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j]== null) continue;
				drawCube(40 + j * unit, 56 + i * unit, g, board[i][j]);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
//		xO + (12 + dx) * unit, yO + (18 + dy) * unit, round(4F * unit), round(1.6F * unit)
		int x = e.getX();
		int y = e.getY();
		int leftX = xO + (12 + dx) * unit;
		int rightX = xO + (12 + dx) * unit + round(4F * unit);
		int upperY = yO + (18 + dy) * unit;
		int lowerY = yO + (18 + dy) * unit + round(1.6F * unit);
		if(x  >= leftX && x <= rightX && y >= upperY && y <= lowerY){
			System.exit(0);
		}
	}
	

	// This part is for the function of pause the game

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(x >= xO && x <= xO + col * unit && y >= yO && y <= yO + row * unit){
			mouse = true;
		}else{
			mouse = false;
		}
		int mouseX = (y - 56) / unit;
		int mouseY = (x - 40) / unit;
		for (int i = 0; i < curtShape.length; i++) {
			for (int j = 0; j < curtShape[0].length; j++) {
				if (curtShape[i][j] == false) continue;
				int xIndex = (curtY - 56) / unit + i;
				int yIndex = (curtX - 40) / unit + j;
				if (mouseX == xIndex && mouseY == yIndex) {
					changeCur = true;
					return;
				}
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (end) return;
		// TODO Auto-generated method stub
		if(e.getModifiers() == InputEvent.BUTTON1_MASK) {
			for (int i = 0; i < curtShape.length; i++) {
				for (int j = 0; j < curtShape[0].length; j++) {
					if (curtShape[i][j] == false) continue;
					int xIndex = (curtY - 56) / unit + i;
					int yIndex = (curtX - 40) / unit + j;
					if (yIndex == 0 || board[xIndex][yIndex - 1] != null) {
						return;
					}
				}
			}
			curtX -= unit;
			repaint();
		} else if(e.getModifiers() == InputEvent.BUTTON3_MASK) {
//			if (curtX + curtBounds[0] * unit == rightBound) return;
			for (int i = 0; i < curtShape.length; i++) {
				for (int j = 0; j < curtShape[0].length; j++) {
					if (curtShape[i][j] == false) continue;
					int xIndex = (curtY - 56) / unit + i;
					int yIndex = (curtX - 40) / unit + j;
					if (yIndex == col - 1 || board[xIndex][yIndex + 1] != null) {
						return;
					}
				}
			}
			curtX += unit;
			repaint();
		}
			
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if (end) return;
		if(e.getWheelRotation() < 0){
			if (curtId == 3) {
				curtId = 0;
			} else {
				curtId++;
			}
			move();
			
		} else {
			if (curtId == 0) {
				curtId = 3;
			} else {
				curtId--;
			}
			move();
		}
		repaint();
	}
	public void move() {
		int left = curtX + shapes.getBound(curt, curtId)[1] * unit;
		while (left < leftBound) {
			curtX += unit;
			left += unit;
		}
		int right = curtX + shapes.getBound(curt, curtId)[0] * unit;
		while (right > rightBound) {
			curtX -= unit;
			right -= unit;
		}
		int bottom = curtY + shapes.getBound(curt, curtId)[2] * unit;
		while (bottom > bottomBound) {
			curtY -= unit;
			bottom -= unit;
		}
	}

}