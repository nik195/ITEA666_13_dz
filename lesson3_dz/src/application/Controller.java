package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Controller {

	String buffDelete;

	@FXML
	private TextField searchField;

	@FXML
	private Button searchButton;

	@FXML
	private ListView<String> generalPane;

	@FXML
	private ChoiceBox<String> comboBox;

	@FXML
	private Button nextButton;

	@FXML
	private Button backButton;

	@FXML
	private TextField comboField;

	@FXML
	private Pane pane;

	@FXML
	private Button renameButton;

	@FXML
	private TextField renameField;

	@FXML
	private Button copyButton;

	@FXML
	private TextField copyField;

	@FXML
	private TextField createField;

	@FXML
	private Button createButton;

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	@FXML
	private Label questionLabel;

	public void initialize() {
		boxClean();

		nextButton.setVisible(false);

		renameButton.setVisible(false);
		renameField.setVisible(false);

		copyButton.setVisible(false);
		copyField.setVisible(false);

		createButton.setVisible(false);
		createField.setVisible(false);

		yesButton.setVisible(false);
		noButton.setVisible(false);
		questionLabel.setVisible(false);

		comboField.setVisible(false);

		File[] roots = File.listRoots();
		for (File file : roots) {
			generalPane.getItems().add(file.getAbsolutePath());
		}

		generalPane.setOrientation(Orientation.VERTICAL);

		generalPane.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		generalPane.setOnMouseClicked(event -> {

			if (event.getButton() == MouseButton.PRIMARY) {
				boxClean();
			}

			if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
				File file = new File(searchField.getText() + "\\" + generalPane.getSelectionModel().getSelectedItem());
				if (file.isDirectory()) {
					searchField.setText(file.getAbsolutePath());
					getItems(file);
				} else if (file.isFile()) {
					Desktop desktop = null;
					if (Desktop.isDesktopSupported()) {
						desktop = Desktop.getDesktop();
					}

					try {
						try {
							desktop.open(file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (event.getButton() == MouseButton.SECONDARY) {
//				comboBox.setVisible(true);
				comboBox.show();
			}
		});

		searchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				comboBox.setVisible(false);
				if (searchField.getText().equals("")) {
					File[] roots = File.listRoots();
					generalPane.getItems().clear();
					for (File a : roots) {
						generalPane.getItems().add(a.getAbsolutePath());
					}
				} else {
					File file = new File(searchField.getText());
					getItems(file);
				}
			}

		});

//		Доработать функционал. Переход на предыдущую директорию
		nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				comboBox.setVisible(false);
				File file = new File(searchField.getText() + comboField.getText() + "\\");
				getItems(file);
				searchField.setText(file.getAbsolutePath() + "\\");
				comboField.setText("");
			}
		});

		backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				File[] roots = File.listRoots();
				boolean flag = false;

				comboBox.setVisible(false);
//				if (searchField.getText().equals("C:\\") || searchField.getText().equals("E:\\")
//						|| searchField.getText().equals("D:\\") || searchField.getText().equals("Z:\\")) {
				for (int i = 0; i < roots.length; i++) {
					if (searchField.getText().equals(roots[i].getAbsolutePath())) {
						searchField.setText("");
						flag = true;
						generalPane.getItems().clear();

						for (File file : roots) {
							generalPane.getItems().add(file.getAbsolutePath());
						}
						break;
					} else {
						continue;
					}
				}
				if (!flag) {
					File file1 = new File(searchField.getText());
					File file = new File(file1.getParent());
					getItems(file);
					searchField.setText(file.getAbsolutePath());
				}
				flag = false;
			}
		});

		comboBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (comboBox.getSelectionModel().getSelectedItem().equals("Удалить")) {
					File fileForDelete = new File(
							searchField.getText() + "\\" + generalPane.getSelectionModel().getSelectedItem());
					if (fileForDelete.isFile()) {
						fileForDelete.delete();
					} else if (fileForDelete.isDirectory()) {
						buffDelete = searchField.getText() + "\\" + generalPane.getSelectionModel().getSelectedItem();
						while (fileForDelete.exists()) {
							directoryClean(fileForDelete.getAbsolutePath());
						}
					}
//					boxClean();

					File file = new File(searchField.getText());
					getItems(file);

				} else if (comboBox.getSelectionModel().getSelectedItem().equals("Создать")) {
					createField.setVisible(true);
					createButton.setVisible(true);

				} else if (comboBox.getSelectionModel().getSelectedItem().equals("Переименовать")) {
					renameField.setVisible(true);
					renameButton.setVisible(true);

				} else if (comboBox.getSelectionModel().getSelectedItem().equals("Открыть")) {

					File file = new File(
							searchField.getText() + "\\" + generalPane.getSelectionModel().getSelectedItem());
					if (file.isDirectory()) {
						searchField.setText(file.getAbsolutePath());
						getItems(file);
					}

					if (file.isFile()) {
						Desktop desktop = null;
						if (Desktop.isDesktopSupported()) {
							desktop = Desktop.getDesktop();
						}

						try {
							try {
								desktop.open(file);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					comboField.setText("");
					boxClean();
				} else if (comboBox.getSelectionModel().getSelectedItem().equals("Скопировать")) {
					copyButton.setText("Скопировать");

					File file = new File(
							searchField.getText() + "\\" + generalPane.getSelectionModel().getSelectedItem());

//					if (file.isFile()) {
					comboField.setText(file.getAbsolutePath());
					copyField.setText(generalPane.getSelectionModel().getSelectedItem());
//					} else if (file.isDirectory()) {

//					}

					copyButton.setVisible(true);
				} else if (comboBox.getSelectionModel().getSelectedItem().equals("Вырезать")) {

					copyButton.setText("Вставить");

					File file = new File(
							searchField.getText() + "\\" + generalPane.getSelectionModel().getSelectedItem());

//					if (file.isFile()) {
					comboField.setText(file.getAbsolutePath());
					copyField.setText(generalPane.getSelectionModel().getSelectedItem());
//					} else if (file.isDirectory()) {

//					}

					copyButton.setVisible(true);
				}
			}

		});

		copyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // copyButtonHandler

			@Override
			public void handle(MouseEvent event) {

				File file = new File(comboField.getText());
				Path path = Paths.get(file.getAbsolutePath());

				File fileCopy = new File(searchField.getText() + "\\" + copyField.getText());
				Path pathCopy = Paths.get(fileCopy.getAbsolutePath());

				if (fileCopy.exists()) {
					yesButton.setVisible(true);
					noButton.setVisible(true);
					questionLabel.setVisible(true);
				} else {
					try {
						if (copyButton.getText().equals("Скопировать")) {
							if (file.isFile()) {
								Files.copy(path, pathCopy);
							} else if (file.isDirectory()) {
								copyDir(file.getAbsolutePath(), fileCopy.getAbsolutePath());
							}
						} else if (copyButton.getText().equals("Вставить")) {
							Files.move(path, pathCopy);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					comboField.setText("");

					boxClean();
					comboField.setText("");
					copyField.setText("");
					copyButton.setVisible(false);

					getItems(new File(searchField.getText()));
				}
			}
		});

		yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // yesButtonHandler

			@Override
			public void handle(MouseEvent event) {
				File file = new File(comboField.getText());
				Path path = Paths.get(file.getAbsolutePath());

				File fileCopy = new File(searchField.getText() + "\\" + copyField.getText());
				Path pathCopy = Paths.get(fileCopy.getAbsolutePath());

				try {
					if (copyButton.getText().equals("Скопировать")) {
						if (file.isFile()) {
							Files.copy(path, pathCopy, StandardCopyOption.REPLACE_EXISTING);
						} else if (file.isDirectory()) {
							buffDelete = fileCopy.getAbsolutePath();
							for (int i = 0; i < 2;) {
								if (i == 0) {
									while (fileCopy.exists()) {
										directoryClean(fileCopy.getAbsolutePath());
									}
									i++;
								}
								if (i == 1) {
									if (file.isDirectory() && !fileCopy.exists()) {
										copyDir(file.getAbsolutePath(), fileCopy.getAbsolutePath());
									}
									i++;
								}
							}
						}
					} else if (copyButton.getText().equals("Вставить")) {
						Files.move(path, pathCopy, StandardCopyOption.REPLACE_EXISTING);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

//				boxClean();
				comboField.setText("");
				copyField.setText("");
				copyButton.setVisible(false);

				getItems(new File(searchField.getText()));
			}
		});

		noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // noButtonHandler

			@Override
			public void handle(MouseEvent event) {

				boxClean();
			}
		});

		createButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // createButtonHandler

			@Override
			public void handle(MouseEvent event) {

				File file = new File(searchField.getText() + "\\" + createField.getText());

				try {
					if (!file.exists()) {
						file.createNewFile();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				boxClean();

				getItems(new File(file.getParent()));
			}
		});

		renameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // renameButtonHandler

			@Override
			public void handle(MouseEvent event) {

				String url = generalPane.getSelectionModel().getSelectedItem();
				String name = url.substring(url.indexOf("."), url.length());

				File file = new File(searchField.getText() + "\\" + generalPane.getSelectionModel().getSelectedItem());
				File renameFile = new File(searchField.getText() + "\\" + renameField.getText() + name);

				file.renameTo(renameFile);

				boxClean();

				getItems(new File(file.getParent()));
			}
		});

//		comboField.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//
//			@Override
//			public void handle(MouseEvent event) {
//				comboBox.setVisible(true);
//
//			}
//		});

		searchField.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				boxClean();

			}
		});

		pane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				boxClean();
				comboField.setText("");
				copyField.setText("");
				copyButton.setVisible(false);
			}
		});
	}

	private void getItems(File file) {
		generalPane.getItems().clear();
		int i = 0;
		String[] allFilesFolder = new String[file.listFiles().length];

		for (File a : file.listFiles()) {
			if (file.isDirectory() && !file.isFile()) {
				allFilesFolder[i] = a.getName();
				i++;
			}
		}
		for (File a : file.listFiles()) {
			if (file.isFile() && !file.isDirectory()) {
				allFilesFolder[i] = a.getName();
				i++;
			}
		}
		generalPane.getItems().addAll(allFilesFolder);

	}

	private void boxClean() {
		comboBox.setVisible(false);

		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("Создать", "Переименовать", "Удалить", "Открыть", "Скопировать", "Вырезать");

//		comboBox.getSelectionModel().clearSelection();

//		comboField.setText("");

		comboField.setVisible(false);

//		copyField.setText("");

		copyField.setVisible(false);

//		copyButton.setVisible(false);

		createField.setText("");
		createField.setVisible(false);
		createButton.setVisible(false);

		renameField.setText("");
		renameField.setVisible(false);
		renameButton.setVisible(false);

		yesButton.setVisible(false);
		noButton.setVisible(false);
		questionLabel.setVisible(false);
	}

	private void directoryClean(String path) {

		for (File file : new File(path).listFiles()) {
			if (buffDelete == "") {
			}

			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory() && file.list().length != 0) {
				String newPath = file.getAbsolutePath();
				directoryClean(newPath);
			} else if (file.isDirectory() && file.list().length == 0) {
				file.delete();
				if (!(file.getParent().length() < buffDelete.length())) {
					directoryClean(file.getParent());
				}

			}
		}
		if (new File(buffDelete).list().length == 0 && new File(buffDelete).exists()) {
			new File(buffDelete).delete();
			buffDelete = "";
		}
	}

	private void copyDir(String source, String destination) {

		File file = new File(source);
		File copy = new File(destination);

		if (file.isDirectory() && !copy.exists()) {
			copy.mkdir();
			File newFile;
			String fileName, copyName;
			for (String a : file.list()) {
				fileName = file.getAbsolutePath() + "\\" + a;

				copyName = copy.getAbsolutePath() + "\\" + a;

				newFile = new File(fileName);
				if (newFile.isDirectory()) {
					copyDir(fileName, copyName);
				} else {
					Path path = Paths.get(fileName);
					Path copyPath = Paths.get(copyName);
					try {
						Files.copy(path, copyPath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
//		else if (file.isDirectory() && copy.exists()) {
//			while (copy.exists()) {
//				directoryClean(copy.getAbsolutePath());
//			}
//		copyDir(source, destination);
//		}
	}

}
