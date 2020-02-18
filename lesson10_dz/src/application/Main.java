package application;

import java.awt.Color;
import java.lang.ref.WeakReference;
import java.nio.channels.SelectableChannel;
import java.nio.file.attribute.PosixFilePermission;

import com.sun.javafx.image.impl.ByteIndexed.Getter;
import com.sun.javafx.scene.traversal.Direction;

import dogVictim.Dog;
import dogVictim.Owner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Main extends Application {
	Stage primaStage;
	TextField dogNameField = new TextField();
	TextField dogAgeField = new TextField();
	TextField ownerField = new TextField();
	TextField rootField = new TextField();
	TextField systemField = new TextField();

	public void start(Stage primaryStage) {
		try {
//			BorderPane root = new BorderPane();
			GridPane root = new GridPane();
			root.setFocusTraversable(true);

			root.setAlignment(Pos.CENTER);
			root.setHgap(10);
			root.setVgap(10);
			root.setPadding(new Insets(25, 25, 25, 25));

			Label dogNameLabel = new Label("Dog name: ");
			Label dogAgeLabel = new Label("Dog age: ");
			Label ownerLabel = new Label("Owner name: ");
			Label rootLabel = new Label("Root: ");
			Label systemLabel = new Label("System: ");

			Button createDogButton = new Button("Create dog");
			Button getDogButton = new Button("Get dog");
			Button gcButton = new Button("GC");

			HBox rootBox = new HBox();
			rootBox.getChildren().addAll(rootField);
			rootBox.setAlignment(Pos.BOTTOM_CENTER);
			rootBox.setId("rootBox");
			rootBox.setPadding(new Insets(10, 10, 10, 10));

			HBox createdogBox = new HBox();
			createdogBox.getChildren().add(createDogButton);
			createdogBox.setAlignment(Pos.CENTER_RIGHT);

			root.add(dogNameLabel, 0, 0);
			root.add(dogNameField, 1, 0);
			root.add(dogAgeLabel, 0, 1);
			root.add(dogAgeField, 1, 1);
			root.add(ownerLabel, 0, 2);
			root.add(ownerField, 1, 2);
			root.add(createdogBox, 1, 3);

			HBox getDogBox = new HBox();
			getDogBox.getChildren().add(getDogButton);
			getDogBox.setAlignment(Pos.CENTER_RIGHT);

			root.add(getDogBox, 1, 4);
			root.add(gcButton, 0, 4);
			root.add(rootLabel, 0, 5);
			root.add(rootBox, 1, 5);

			root.add(systemLabel, 0, 6);
			root.add(systemField, 1, 6);

			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Dog casher");
			primaryStage.show();

			gcButton.setOnAction(event -> {
				System.gc();
				rootField.setText("");
				dogAgeField.setText("");
				dogNameField.setText("");
				ownerField.setText("");
				systemField.setText("");
			});

			gcButton.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					System.gc();
					rootField.setText("");
					dogAgeField.setText("");
					dogNameField.setText("");
					ownerField.setText("");
					systemField.setText("");
				}
			});

			createDogButton.setOnAction(event -> {
				createDog();
			});

			createDogButton.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					if (!dogNameField.equals("") && !dogAgeField.equals("") && !ownerField.equals("")) {
						createDog();
					}
					createDogButton.impl_traverse(Direction.NEXT);
				}
			});

			getDogButton.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					rootField.setText(String.valueOf(getDog()));
				}
				getDogButton.impl_traverse(Direction.LEFT);
			});

			getDogButton.setOnAction(event -> {
				rootField.setText(String.valueOf(getDog()));
			});

			dogNameField.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					if (!dogNameField.getText().equals("")) {
						dogNameField.impl_traverse(Direction.NEXT);
					}
				}
			});

			dogAgeField.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					if (!dogAgeField.getText().equals("")) {
						dogAgeField.impl_traverse(Direction.NEXT);
					}
				}
			});

			ownerField.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					if (!ownerField.getText().equals("")) {
						ownerField.impl_traverse(Direction.NEXT);
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	WeakReference<Dog> cache = new WeakReference<Dog>(new Dog());

	public Dog getDog() {
		if (cache == null || cache.get() == null) {

			systemField.setText("Dog is not created!");
		} else {
			systemField.setText("Old Dog!");
		}
		return cache.get();
	}

	public void createDog() {
//		if (cache.get() != null) {
//			if (cache.get().getName() == dogNameField.getText()
//					&& cache.get().getAge() == Integer.valueOf(dogAgeField.getText())
//					&& cache.get().getOwner() == new Owner(ownerField.getText())) {
//				systemField.setText("This dog already exists!");
//			}
//		} else if (cache.get().getName() != dogNameField.getText()
//				|| cache.get().getAge() != Integer.valueOf(dogAgeField.getText())
//				|| cache.get().getOwner() != new Owner(ownerField.getText())) {
		cache = new WeakReference<Dog>(new Dog(new Owner(ownerField.getText()), dogNameField.getText(),
				Integer.valueOf(dogAgeField.getText())));
		systemField.setText("New dog created!");
//		}
		rootField.setText("");
	}
}
