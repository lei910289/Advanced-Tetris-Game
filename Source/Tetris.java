import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Tetris extends Frame{
	public static void main(String[] args) {
		new Input();
//		new Tetris(1, 1, 1.0f, 23, 15);
	}

	Tetris(int score, int num, float speed, int row, int col, int size) {
		super("tetrix");
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		}
				);
		setSize(600 + (col - 10) * 28, 800 + (row - 20) * 28);
		add("Center", new MainCav(score, num, speed, row, col, size * 3 + 20));
		show();
	}

	static class Input extends JFrame {
		JTextField jScoreField, jNumField, jSpeedField, jNumRowField, jNumColField, jSizeField;
	    JLabel jLabel1,jLabel2,jLabel3,jLabel4,jLabel5, jLabel6;
	    JPanel jp1,jp2,jp3,jp4,jp5,jp6, jp7;
	    JButton jb1;
	    public Input(){
	    	jNumRowField = new JTextField(12);
	        jNumRowField.setText("20-25(int)");
	        
	        jNumColField = new JTextField(12);
	        jNumColField.setText("10-15(int)");
	        
	        jScoreField = new JTextField(12);
	        jScoreField.setText("1-10(int)");
	        
	        jNumField = new JTextField(12);
	        jNumField.setText("20-50(int)");
	        
	        jSpeedField = new JTextField(12);
	        jSpeedField.setText("0.1-1.0");
	        
	        jSizeField = new JTextField(12);
	        jSizeField.setText("1-4(int)");
	        
	        jLabel1 = new JLabel("  Scoring factor  ");
	        jLabel2 = new JLabel("Number to level up");
	        jLabel3 = new JLabel("      Speed       ");
	        jLabel4 = new JLabel("  number of rows  ");
	        jLabel5 = new JLabel("  number of cols  ");
	        jLabel6 = new JLabel("  size of square  ");
	       
	        jp1 = new JPanel();
	        jp2 = new JPanel();
	        jp3 = new JPanel();
	        jp4 = new JPanel();
	        jp5 = new JPanel();
	        jp6 = new JPanel();
	        jp7 = new JPanel();
	        
	        jb1 = new JButton("Confirm");
	        jb1.addActionListener(new ButtonListener());
	        
	        // set the layout
	        this.setLayout(new GridLayout(6,1));
	        
	        // add three input
	        jp1.add(jLabel1); 
	        jp1.add(jScoreField);
	        
	        jp2.add(jLabel2);
	        jp2.add(jNumField);
	        
	        jp3.add(jLabel3);
	        jp3.add(jSpeedField);
	        
	        jp4.add(jLabel4);
	        jp4.add(jNumRowField);
	        
	        jp5.add(jLabel5);
	        jp5.add(jNumColField);

	        jp6.add(jLabel6);
	        jp6.add(jSizeField);
	        // add button
	        jp7.add(jb1);
	        
	        
	        
	        // to show the frame at middle
	        this.setLocationRelativeTo(null);

	        this.add(jp1);
	        this.add(jp2);
	        this.add(jp3);  
	        this.add(jp4);
	        this.add(jp5);
	        this.add(jp6);
	        this.add(jp7);
	        // set the size
	        
	        this.setSize(300, 300);
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.setVisible(true);
	        this.setTitle("Enter the attributes");
	         
	    }
		class ButtonListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int score = Integer.parseInt(jScoreField.getText());
				int num = Integer.parseInt(jNumField.getText());
				float speed = Float.parseFloat(jSpeedField.getText());
				int row = Integer.parseInt(jNumRowField.getText());
				int col = Integer.parseInt(jNumColField.getText());
				int size = Integer.parseInt(jSizeField.getText());
				dispose();
				new Tetris(score, num, speed, row, col, size);
			}
	         
	    }
	}
	

}

