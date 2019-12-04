package admin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import triviaMaze.QuestionHandler;
import triviaMaze.TypeOfQuestion;

public class AdminWindow {
	static boolean isDataGood = true; //to check data condition before insert
	public static Connection connectionDB() throws Exception {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:QuestionForTest.db");
			//System.out.println("Connect database successfully");
			return conn;
	   }catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
	   }
		return null;
	}
	public static void insertQuestion(String question, String correctAns, String wrAns1, String wrAns2, String wrAns3, int type) {
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectionDB();
			stmt = c.createStatement();
			String sql = "INSERT INTO Question(Question,CorrectAnswer,WrongAnswer1,WrongAnswer2,WrongAnswer3,TypeOfQuestion)"+
							"VALUES ('"+question+"','"+correctAns+"','"+wrAns1+"','"+wrAns2+"','"+wrAns3+"',"+type+")";
			stmt.executeUpdate(sql);
			//System.out.println("Inserted records into the table...");
			stmt.close();
		    c.close();
	   }catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
	   }
	}
	public static String selectData() {
		Connection c = null;
		Statement stmt = null;
		String result = "";
		try {
			c = connectionDB();
			stmt = c.createStatement();
			String sql = "SELECT * FROM Question";
			ResultSet rs = stmt.executeQuery(sql);

		      // Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		         int id  = rs.getInt("QuestionID");
		         String question = rs.getString("Question");
		         String correctAns = rs.getString("CorrectAnswer");
		         String wrongAns1 = rs.getString("WrongAnswer1");
		         String wrongAns2 = rs.getString("WrongAnswer2");
		         String wrongAns3 = rs.getString("WrongAnswer3");
		         int type = rs.getInt("TypeOfQuestion");
		         result += "QuestionID: " + id + ", Question: " + question + ", Correct Answer: " + correctAns +
		        		 	", Wrong Answer 1: " + wrongAns1 + ", Wrong Answer 2: " + wrongAns2 + ", Wrong Answer 3: " + wrongAns3 +
		        		 	", Type Of Question: " + type +"\n";
		      }
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		return result;
	}	
	
	public static void main(String[] args) {
		//Creating the Frame
        JFrame frame = new JFrame("Adding Question");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(940, 600);
        
        // Title
        JPanel panelText = new JPanel();
        JLabel lbTitle = new JLabel("Question Table:");
        
        // Show question table
        JTextArea ta = new JTextArea(5,80);
		ta.append(selectData());
		panelText.add(lbTitle);
		panelText.add(new JScrollPane(ta));
		
		// Adding question
        JPanel panel = new JPanel();
        JLabel lbAddQuestion = new JLabel("------------------------------------------------------------------------------------------ Adding Question ------------------------------------------------------------------------------------------");
        JLabel lbTypeOfQuestion = new JLabel("Type Of Question :");
        JRadioButton rdMultiple = new JRadioButton("Multiple Choice");
        rdMultiple.setSelected(true);
        JRadioButton rdTrueFalse = new JRadioButton("True/False");
        panel.add(lbAddQuestion);
        panel.add(lbTypeOfQuestion);
        panel.add(rdMultiple);
        panel.add(rdTrueFalse);
        ButtonGroup grd = new ButtonGroup();
        grd.add(rdMultiple);
        grd.add(rdTrueFalse);
        JTextArea taQuestion = new JTextArea(5,80);
        panel.add(new JScrollPane(taQuestion));
         
        JLabel lbCorrectAns = new JLabel("Correct Answer :");
        JTextField tfCorrectAns = new JTextField(70);
        panel.add(lbCorrectAns);
        panel.add(tfCorrectAns);
        
        JLabel lbWrongAns1 = new JLabel("Wrong Answer 1:");
        JTextField tfWrongAns1 = new JTextField(70);
        panel.add(lbWrongAns1);
        panel.add(tfWrongAns1);
        
        JLabel lbWrongAns2 = new JLabel("Wrong Answer 2:");
        JTextField tfWrongAns2 = new JTextField(70);
        
        panel.add(lbWrongAns2);
        panel.add(tfWrongAns2);
        
        JLabel lbWrongAns3 = new JLabel("Wrong Answer 3:");
        JTextField tfWrongAns3 = new JTextField(70);
        panel.add(lbWrongAns3);
        panel.add(tfWrongAns3);
        
        // True False panel
        JPanel panelTrueFalse = new JPanel();
        JLabel lbTF = new JLabel("Chose the correct answer: ");
        JRadioButton rdTrue = new JRadioButton("True");
        JRadioButton rdFalse = new JRadioButton("False");
        ButtonGroup grTF = new ButtonGroup();
        grTF.add(rdTrue);
        grTF.add(rdFalse);
        //lbTF
        panelTrueFalse.add(lbTF);
        panelTrueFalse.add(rdTrue);
        rdTrue.setSelected(true);
        panelTrueFalse.add(rdFalse);
        
        panelTrueFalse.setVisible(false);
        panel.add(panelTrueFalse);
        // Click radio True/False event
        rdTrueFalse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(rdTrueFalse.isSelected()) {
					panelTrueFalse.setVisible(true);
					tfCorrectAns.setText("");
					tfWrongAns1.setText("");
					tfWrongAns2.setText("");
					tfWrongAns3.setText("");
					tfCorrectAns.setEditable(false);
					tfWrongAns1.setEditable(false);
		        	tfWrongAns2.setEditable(false);
		        	tfWrongAns3.setEditable(false);
		        }else {
		        	panelTrueFalse.setVisible(false);
		        	tfCorrectAns.setEditable(true);
					tfWrongAns1.setEditable(true);
		        	tfWrongAns2.setEditable(true);
		        	tfWrongAns3.setEditable(true);
		        }
			}
		});
        
        //Panel button
        JPanel panelButton = new JPanel();
        JButton btAdd = new JButton("Add Question");
        btAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int typeOfQuestion = 0;
				String question = null;
				String answer = null;
				String wrongAns1 = null;
				String wrongAns2 = null;
				String wrongAns3 = null;
				
				question = taQuestion.getText();
				answer = tfCorrectAns.getText();
				wrongAns1 = tfWrongAns1.getText();
				wrongAns2 = tfWrongAns2.getText();
				wrongAns3 = tfWrongAns3.getText();
				if (rdMultiple.isSelected()) {
					typeOfQuestion = 0;
					if (question.equals(null) || answer.equals(null) || wrongAns1.equals(null) || wrongAns2.equals(null) || wrongAns3.equals(null)||
						question.equals("") || answer.equals("") || wrongAns1.equals("") || wrongAns2.equals("") || wrongAns3.equals("")) {
						isDataGood = false;
					}else {
						isDataGood = true;
					}
				}
				else if (rdTrueFalse.isSelected()) {
					typeOfQuestion = 1;
					if (question.equals(null) || question.equals("")) {
						isDataGood = false;
					}else {
						if(rdTrue.isSelected())
							answer = "true";
						else
							answer = "false";
						isDataGood = true;
					}
				}
				if(isDataGood) {
					insertQuestion(question, answer,wrongAns1,wrongAns2,wrongAns3,typeOfQuestion);
					ta.setText(null);				
					ta.append(selectData());
					// Reset value
					rdMultiple.setSelected(true);
					taQuestion.setText(null);
					tfCorrectAns.setText(null);
					tfWrongAns1.setText(null);
					tfWrongAns2.setText(null);
					tfWrongAns3.setText(null);
				} else
				{
					JOptionPane.showMessageDialog(null, "All the text fields need to be filled!");
				}
			}
		});
        JButton btReset = new JButton("Reset");
        btReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Reset variable
				rdMultiple.setSelected(true);
				taQuestion.setText(null);
				tfCorrectAns.setText(null);
				tfWrongAns1.setText(null);
				tfWrongAns2.setText(null);
				tfWrongAns3.setText(null);
				
			}
		});
        panelButton.add(btAdd);
        panelButton.add(btReset);

        
		//insertQuestion("Where can you find London bridge today", "USA","England","France","Gemany",TypeOfQuestion.MutipleChoice.ordinal());
		
        //Adding Components to the frame.
		frame.getContentPane().add(BorderLayout.NORTH, panelText);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.SOUTH, panelButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);	
		
	}

}
