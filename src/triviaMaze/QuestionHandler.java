package triviaMaze;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Random;

public class QuestionHandler implements Serializable{
	private static final long serialVersionUID = 1L;
	LinkedList<Question> questions = new LinkedList<Question>();
	private int questionUsed = 0;
	// Use Singleton pattern to coonect and get data instance only one time
	// static variable single_instance of type QuestionHandler 
    private static QuestionHandler single_instance = null;
	public QuestionHandler() {
		addQuestionToList();
	}
	// static method to create instance of Singleton class 
    public static QuestionHandler getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new QuestionHandler(); 
  
        return single_instance; 
    } 
	public Connection connectionDB() throws Exception {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:QuestionForTest.db");
			//Connection conn = DriverManager.getConnection("jdbc:sqlite:Question.db");
			//System.out.println("Connect database successfully");
			return conn;
	   }catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
	   }
		return null;
	}
	public void insertQuestion(String question, String correctAns, String wrAns1, String wrAns2, String wrAns3, int type) {
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectionDB();
			stmt = c.createStatement();
			String sql = "INSERT INTO Question(Question,CorrectAnswer,WrongAnswer1,WrongAnswer2,WrongAnswer3,TypeOfQuestion)"+
							"VALUES ('"+question+"','"+correctAns+"','"+wrAns1+"','"+wrAns2+"','"+wrAns3+"',"+type+")";
			stmt.executeUpdate(sql);
			System.out.println("Inserted records into the table...");
			stmt.close();
		    c.close();
	   }catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
	   }
	}
	public Question getRandomQuestion() {
		int size = questions.size();
		Random r = new Random();
		
		// loop until get a question which is never used
		while(true) {
			// return null if all questions were used
			if (questionUsed == size) {
				System.out.println("Out of question!");
				return null;
			}
			int index = r.nextInt(size);
			Question question = questions.get(index);
			if(question.getStatus() == 0) { //status == 0 means never used
				question.setStatus(1);
				questionUsed++;
				return question;
			}
		}
	}
	public Question getQuestionById(int id) {
		Question question = questions.get(id);
		return question;
	}
	public void resetQuestion() {
		this.single_instance = null;
	}
	public void addObject (Question question) {
		this.questions.add(question);
	}
	public void addQuestionToList() {
		Connection c = null;
		Statement stmt = null;
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
		         String answer = rs.getString("CorrectAnswer");
		         String wrongAns1 = rs.getString("WrongAnswer1");
		         String wrongAns2 = rs.getString("WrongAnswer2");
		         String wrongAns3 = rs.getString("WrongAnswer3");
		         String[] answers = new String[4];
		         answers[0] = answer;
		         answers[1] = wrongAns1;
		         answers[2] = wrongAns2;
		         answers[3] = wrongAns3;
		         
		         int type = rs.getInt("TypeOfQuestion");
		         TypeOfQuestion typeOfQuestion = TypeOfQuestion.MutipleChoice;
		         if (type == 0) {
		        	 randomAnswer(answers,4);
		         }else if( type == 1) {
		        	 typeOfQuestion = TypeOfQuestion.TrueFalse;
		         }
		         else if (type == 2)
		        	 typeOfQuestion = TypeOfQuestion.ShortAnswer;
		         //int status = rs.getInt("Status");
		         int status = 0;
		         this.addObject(new Question(id, question, answer, answers[0], answers[1], answers[2], answers[3], typeOfQuestion, status));
		      }
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	/**
	 * Random answers in string array
	 */
	public void randomAnswer(String[] answers, int bounds) {
		Random r = new Random();
		int rd = r.nextInt(bounds);
		//System.out.println(answers[0] +", "+ answers[1]+", "+ answers[2]+", "+ answers[3]);
		String temp = answers[rd];
		answers[rd] = answers[0];
		answers[0] = temp;
		//System.out.println(answers[0] +", "+ answers[1]+", "+ answers[2]+", "+ answers[3]);
	}
	public String toString() {
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
		        		 	", Wrong Answer 1: " + wrongAns1 + ", Wrong Answer 2: " + wrongAns2 + ", Wrong Answer 2: " + wrongAns3 +
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
}
