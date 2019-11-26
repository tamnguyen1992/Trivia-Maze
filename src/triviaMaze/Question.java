package TrivialMaze;

public class Question {
	private int id;
	private String question;
	private String correctAnswer;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	//private String hint;
	private TypeOfQuestion type;
	private int status;
	private QuestionHandler questionHandler;
	public Question() {
		questionHandler = QuestionHandler.getInstance();
		Question temp = questionHandler.getRandomQuestion();
		if (temp != null) {
			this.id = temp.getId();
			this.question = temp.getQuestion();
			this.correctAnswer = temp.getCorrectAnswer();
			this.answer1 = temp.getAnswer1();
			this.answer2 = temp.getAnswer2();
			this.answer3 = temp.getAnswer3();
			this.answer4 = temp.getAnswer4();
			//this.hint = hint;
			this.type = temp.getType();
			this.status = temp.getStatus();
		}
	}
	public Question(int id, String question, String correctAnswer, String answer1, String answer2, String answer3, String answer4, TypeOfQuestion type, int status) {
		this.id = id;
		this.question = question;
		this.correctAnswer = correctAnswer;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		//this.hint = hint;
		this.type = type;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public String getQuestion() {
		return question;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public String getAnswer1() {
		return answer1;
	}
	public String getAnswer2() {
		return answer2;
	}
	public String getAnswer3() {
		return answer3;
	}
	public String getAnswer4() {
		return answer4;
	}
	public TypeOfQuestion getType() {
		return type;
	}
	public int getStatus() {
		return this.status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
    public String toString() {
    	String result = "";
    	result += "QuestionID: " + id + ", Question: " + question + ", Correct Answer: " + answer1 +
    		 	", Wrong Answer 1: " + answer2 + ", Wrong Answer 2: " + answer3 + ", Wrong Answer 2: " + answer4 +
    		 	", Type Of Question: " + type + ", Status: " + status;
    	return result;
    }
}
