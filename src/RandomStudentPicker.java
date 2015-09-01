package randomstudentpicker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * This is a simple little program useful for randomly picking students
 * registered in our class to be called on to answer a question during lecture.
 *
 * @author Richard McKenna
 */
public class RandomStudentPicker extends Application {

    // HERE ARE THE STUDENTS WE'LL PICK FROM
    String COURSE = "CSE219";
    String STUDENT_NAMES_FILE = "./data/CSE219_Fall2015_Students.json";
    String PICK_BUTTON_TEXT = "Pick Random Student";
    ArrayList<String> students = new ArrayList();

    // THESE ARE OUR ONLY CONTROLS, A BUTTON AND A DISPLAY LABEL
    Button pickButton = new Button(PICK_BUTTON_TEXT);
    final Label studentNameLabel = new Label();

    @Override
    public void start(Stage primaryStage) throws IOException {
	// LOAD THE JSON FILE FULL OF NAMES
	JsonObject json = loadJSONFile(STUDENT_NAMES_FILE);
	JsonArray studentsArray = json.getJsonArray("names");
	
	// AND LOAD THE IMAGES FOR THE NAMES
	for (int i = 0; i < studentsArray.size(); i++) {
	    students.add(studentsArray.getString(i));
	}
	// CUSTOMIZE OUR FONTS
	pickButton.setFont(new Font("Serif", 36));
	studentNameLabel.setFont(new Font("Serif", 48));

	// CUSTOMIZE OUR IMAGE VIEW
	String startingStudent = pickRandomStudent();
	studentNameLabel.setText(startingStudent);

	// PUT THEM IN A CONTAINER
	VBox root = new VBox();
	root.setAlignment(Pos.CENTER);
	root.getChildren().add(pickButton);
	root.getChildren().add(studentNameLabel);

	// AND PUT THE CONTAINER IN THE WINDOW (i.e. the "stage")
	Scene scene = new Scene(root, 600, 400);
	primaryStage.setScene(scene);

	// PROVIDE A RESPONSE TO BUTTON CLICKS
	pickButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		Task<Void> task = new Task<Void>() {
		    @Override
		    protected Void call() throws Exception {
			for (int i = 0; i < 15; i++) {
			    String student = pickRandomStudent();

			    // THIS WILL BE DONE ASYNCHRONOUSLY VIA MULTITHREADING
			    Platform.runLater(new Runnable() {
				@Override
				public void run() {
				    studentNameLabel.setText(student);
				}
			    });

			    // SLEEP EACH FRAME
			    try {
				Thread.sleep(100);
			    } catch (InterruptedException ie) {
				ie.printStackTrace();
			    }
			}
			return null;
		    }
		};
		// THIS GETS THE THREAD ROLLING
		Thread thread = new Thread(task);
		thread.start();
	    }
	});
	// OPEN THE WINDOW
	primaryStage.show();
    }

    public String pickRandomStudent() {
	// RANDOMLY SELECT A STUDENT
	int randomIndex = (int) (Math.random() * students.size());
	String student = students.get(randomIndex);
	return student;
    }

    // LOADS A JSON FILE AS A SINGLE OBJECT AND RETURNS IT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    /**
     * This starts our JavaFX application rolling.
     */
    public static void main(String[] args) {
	launch(args);
    }
}
