package com.internshala.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("game.fxml"));
	    GridPane rootGridPane=loader.load();
	    controller=loader.getController();
	    controller.createplayground();
	    MenuBar menuBar=createmenu();
	    menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
	    Pane menuPane= (Pane) rootGridPane.getChildren().get(0);
	    menuPane.getChildren().add(menuBar);


	    Scene scene=new Scene(rootGridPane);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("connect four");
	    primaryStage.setResizable(false);
	    primaryStage.show();
    }

    private MenuBar createmenu(){
	    Menu file=new Menu("FILE");
	    MenuItem resetgame=new MenuItem("Reset Game");
	    resetgame.setOnAction(event -> {controller.resetgame();});
	    MenuItem  newgame=new MenuItem("New Game");
	    newgame.setOnAction(event -> {controller.resetgame();});
	    MenuItem exitgame=new MenuItem("Exit Game");
	    exitgame.setOnAction(event -> {exitGame();});
	    SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
	    file.getItems().addAll(newgame,resetgame,separatorMenuItem,exitgame);


	    Menu help=new Menu("HELP");
	    MenuItem about=new MenuItem("About Connect4");
	    about.setOnAction(event -> {ag();});
	    MenuItem aboutme=new MenuItem("About me");
	    aboutme.setOnAction(event -> {am();});
	    SeparatorMenuItem separatorMenuItem1=new SeparatorMenuItem();
	    help.getItems().addAll(about,separatorMenuItem1,aboutme);

	   




	    MenuBar menuBar=new MenuBar();
	    menuBar.getMenus().addAll(file,help);
	    return menuBar;



    }

	private void am() {
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About the developer");
		alert.setHeaderText("Yash Indane");
		alert.setContentText("I like to develop java applications");
		alert.show();
	}

	private void ag() {
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About Connect4");
		alert.setHeaderText("How to play?");
		alert.setContentText("Connect four is a two player connection game"+
				"in which the players first choose a color and then take turns dropping"+
				"colored discs from the top into a seven-column, six-row vertically suspended grid"+
				"The pieces fall straight down"+"occupying the next available space within the column. The objective of the game is to be the first to form a horizontal,"+
				"vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves."
				);
		alert.show();



	}

	private void exitGame() {
		Platform.exit();
		System.exit(0);
	}

	private void Resetgame() {
    	//TO DO
	}


	public static void main(String[] args) {
        launch(args);
    }
}
