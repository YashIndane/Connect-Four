package com.internshala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	private static final int COLUMNS=7;
	private  static  final int ROWS=6;
	private static final int CIRCLE_DIAMETER=80;
	private static final String disccolor1="#24303E";
	private static final String disccolor2="#4CAA88";



	private static String PLAYER_ONE;
	private static String PLAYER_TWO;

	private boolean isplayeroneturn=true;
	private Disc[][] inserteddiscsarray=new Disc[ROWS][COLUMNS];


	@FXML
	public TextField playeronetextfield,playertwotextfield;

	@FXML
	public Button setnamesbutton;



	@FXML
	public GridPane rootGridPane;
	@FXML
	public Pane insertedDiscPane;
	@FXML
	public Label playerone;

	private boolean isallowedtoinsert=true;

	public void createplayground(){
		Shape recteanglewithholes=creategamestructuralgrid();
		rootGridPane.add(recteanglewithholes ,0, 1 );
		List<Rectangle> rectangleList =createclickablecolumns();
		for(Rectangle rectangle:rectangleList) {
			rootGridPane.add(rectangle, 0, 1);
		}
		setnamesbutton.setOnAction(event -> {
			setnames();
		});


	}

	private void setnames() {
		String p1=playeronetextfield.getText();
		PLAYER_ONE=p1;

		String p2=playertwotextfield.getText();
		PLAYER_TWO=p2;

		playerone.setText(p1);

	}

	private Shape creategamestructuralgrid(){
		Shape recteanglewithholes=new Rectangle((COLUMNS+1)*CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);
		for(int row=0;row<ROWS;row++){
			for(int col=0;col<COLUMNS;col++){
				Circle circle=new Circle();
				circle.setRadius(CIRCLE_DIAMETER/2);
				circle.setCenterX(CIRCLE_DIAMETER/2);
				circle.setCenterY(CIRCLE_DIAMETER/2);
				circle.setSmooth(true);


				circle.setTranslateX(col*(CIRCLE_DIAMETER+5)+ CIRCLE_DIAMETER/4);
				circle.setTranslateY(row*(CIRCLE_DIAMETER+5)+ CIRCLE_DIAMETER/4);
				recteanglewithholes=Shape.subtract(recteanglewithholes,circle);



			}
		}

		recteanglewithholes.setFill(Color.WHITE);
		return recteanglewithholes;

	}
	private List<Rectangle> createclickablecolumns(){
		List<Rectangle> rectangleList=new ArrayList<>();
		for(int col=0;col<COLUMNS;col++) {
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5)+ CIRCLE_DIAMETER/4);
			rectangle.setOnMouseEntered(event->rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event->rectangle.setFill(Color.TRANSPARENT));
			final int column=col;
			rectangle.setOnMouseClicked(event-> {
						if (isallowedtoinsert) {
							isallowedtoinsert=false;
							insertdisc(new Disc(isplayeroneturn), column);
						}
					});
			rectangleList.add(rectangle);
		}
		return rectangleList;

	}
	private void insertdisc(Disc disc,int column){
		int row=ROWS-1;
		while(row>=0){
			if(getdiscifpresent(row,column)==null)
				break;
			row--;
		}
		if (row<0)
			return;
		inserteddiscsarray[row][column]=disc;
		insertedDiscPane.getChildren().addAll(disc);
		disc.setTranslateX(column*(CIRCLE_DIAMETER+5)+ CIRCLE_DIAMETER/4);

		int currentrow=row;

		TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
		translateTransition.setToY(row*(CIRCLE_DIAMETER+5)+ CIRCLE_DIAMETER/4);
		translateTransition.setOnFinished(event -> {
			isallowedtoinsert=true;
			if(gameended(currentrow,column)){
				gameover();
				return;

			}
			isplayeroneturn=!isplayeroneturn;
			playerone.setText(isplayeroneturn? PLAYER_ONE:PLAYER_TWO);
		});
		translateTransition.play();

	}

	private boolean gameended(int row,int column){

		List<Point2D> verticalpoints =IntStream.rangeClosed(row-3,row+3).mapToObj(r-> new Point2D(r,column)).collect(Collectors.toList());
		List<Point2D> horizontalpoints =IntStream.rangeClosed(column-3,column+3).mapToObj(col-> new Point2D(row,col)).collect(Collectors.toList());

		Point2D startpoint1=new Point2D(row-3,column+3);
		List<Point2D> diagonal1points=IntStream.rangeClosed(0,6).mapToObj(i->startpoint1.add(i,-i)).collect(Collectors.toList());


		Point2D startpoint2=new Point2D(row-3,column-3);
		List<Point2D> diagonal2points=IntStream.rangeClosed(0,6).mapToObj(i->startpoint2.add(i,i)).collect(Collectors.toList());
		boolean isended=checkcombinations(verticalpoints)||checkcombinations(horizontalpoints)||checkcombinations(diagonal1points)||checkcombinations(diagonal2points);

		return isended;
	}

	private boolean checkcombinations(List<Point2D> points) {
		int chain=0;
		for(Point2D point:points){

			int rowindexforarray= (int) point.getX();
			int columnindexforarray= (int) point.getY();
			Disc disc=getdiscifpresent(rowindexforarray,columnindexforarray);

			if(disc !=null&& disc.isplayeroneturn==isplayeroneturn){
				chain++;
				if(chain==4){
					return  true;
				}
			}else {
				chain=0;
			}
		}
        return  false;
	}

	private Disc getdiscifpresent(int row,int column) {
		if (row >= ROWS || row < 0 || column >= COLUMNS || column < 0)
			return null;
			return inserteddiscsarray[row][column];


	}

	private  void gameover(){
      String winner=isplayeroneturn? PLAYER_ONE:PLAYER_TWO;
		System.out.println("winner is"+winner);
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect Four");
		alert.setHeaderText("The Winner is"+winner);
		alert.setContentText("Want to play again?");
		ButtonType yesbtn=new ButtonType("Yes");
		ButtonType nobtn=new ButtonType("No, Exit");
		alert.getButtonTypes().setAll(yesbtn,nobtn);

		Platform.runLater(()->{
			Optional<ButtonType> btnclicked= alert.showAndWait();
			if( btnclicked.isPresent()&& btnclicked.get()==yesbtn){
				resetgame();

			}else {
				Platform.exit();
				System.exit(0);
			}

		});

	}

	public  void resetgame() {
		insertedDiscPane.getChildren().clear();
		for(int row=0;row<inserteddiscsarray.length;row++){
			for (int col=0;col<inserteddiscsarray[row].length;col++){
				inserteddiscsarray[row][col]=null;
			}
		}
		isplayeroneturn=true;
		playerone.setText(PLAYER_ONE);

		createplayground();
	}

	private static class Disc extends Circle{
		private final boolean isplayeroneturn;

		public Disc( boolean isplayeroneturn){
			this.isplayeroneturn=isplayeroneturn;
			setRadius(CIRCLE_DIAMETER/2);
			setFill(isplayeroneturn? Color.valueOf(disccolor1):Color.valueOf(disccolor2));
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterY(CIRCLE_DIAMETER/2);

		}
	}




	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
