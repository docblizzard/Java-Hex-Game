package application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.shape.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application{

	@Override
	 public void start(Stage stage)
	    {	
		  	stage.setTitle("Let's play a game of Hex!");
			Player player1 = new Player(true, false, Color.RED, "RED");
			Player player2 = new Player(false, false, Color.BLUE, "BLUE");
			ArrayList<Polygon> explored = new ArrayList<>();		 	
			
			// Create main menu
			Font font = new Font(20);
		 	Scene scene1;
		 	
		 	// Button Start
			Button button1 = new Button("Play a new game");
			button1.setPrefSize(300, 80);
			button1.setFont(font);
			
			// Button Quit
			Button button2 = new Button("Quit");
			button2.setPrefSize(300, 80);
			button2.setFont(font);
			button2.setOnAction(e-> System.exit(0));
			//Main menu group
		 	VBox group1 = new VBox();
		 	group1.setSpacing(30);
		 	group1.setAlignment(Pos.CENTER);
		 	group1.getChildren().addAll(button1, button2);

		 	// Initialize the main menu
		 	scene1 = new Scene(group1,500, 600);
		 	stage.setScene(scene1);
		 	stage.show(); 
		 	
		 	// Create decoration
		 	Group decor = group();
	        // Create the Hexagon, it's properties and adds them to the Board
			Scene scene2;
			Group group2 = new Group();
		 	Board board = new Board(121);

	 	
		 	for (int y = 0; y < 11; y++) {
		 		for (int x = 0; x < 11; x++) {
		 			Polygon polygon = new Polygon( (540 +(x * 45)-(y*45)), 75+(x*30)+(30*y), (570+(x * 45)-(y*45)), 75+(x*30)+(30*y), (585+(x * 45)-(y*45)), 45+(x*30)+(30*y),
		 										   (570+(x * 45)-(y*45)), 15+(x*30)+(30*y), (540+(x * 45)-(y*45)), 15+(x*30)+(30*y),
		 										   (525+(x * 45)-(y*45)), 45+(x*30)+(30*y), (540+(x * 45)-(y*45)), 75+(x*30)+(30*y));
		 			polygon.setStrokeWidth(1.5);
		 			polygon.setFill(Color.WHITE);
		 			polygon.setStroke(Color.BLACK);
		 			polygon.setId("WHITE");
		 			
		 			polygon.setOnMouseClicked(mouseEvent ->{
		 				if ((player1.turn) && polygon.getId().equals("WHITE")){
		 					polygon.setFill(player1.color);	 						
		 					player1.turn = false;
		 					player2.turn = true;
		 					polygon.setId("RED");	 					
		 				}
		 				else if (polygon.getId().equals("WHITE")){
		 						polygon.setFill(player2.color);
		 						player2.turn = false;
		 						player1.turn = true;
		 						polygon.setId("BLUE");		
		 				}
		 				// Pathfinding
		 				for (int ite = 0; ite < 11; ite++) {
		 					if (!(player1.turn)) {
		 						pathfinding(board,ite,0,player1, explored);
		 					}
		 					else {
		 						pathfinding(board,0,ite,player2, explored);
		 					}
		 				}
		 				explored.clear();
		 				
		 				// If there is a winner
		 				if (player1.winner || player2.winner) {			
		 					group2.setEffect(new GaussianBlur());
		 					VBox winmenu = new VBox(5);
		 					
		 					// TEXT 
		 					Label wintext = new Label("The " + player1.displaywinner(player1, player2) + " player won !");		 					
		 					wintext.setFont(font);
		 					winmenu.getChildren().add(wintext);
		 					
		 					// CENTER TEXT AND BUTTONS
		 					winmenu.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
		 		            winmenu.setAlignment(Pos.CENTER);
		 		            winmenu.setPadding(new Insets(50));
		 		            winmenu.setSpacing(20);
		 		            
		 		            // Popup window
		 		            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
		 		            
		 		            // Create a Replay and quit button
		 		            Button replay = new Button("Play again");
		 		            replay.setOnAction(e -> {
		 		            	board.resetboard(board, player1, player2);
		 		            	group2.setEffect(null);
		 		                popupStage.hide();
			 			 		//stage.setScene(scene2);	
			 			 		//stage.centerOnScreen();
		 		            });
		 		            replay.setPrefSize(200, 50);
		 					replay.setFont(font);
		 		            winmenu.getChildren().add(replay);
		 		            winmenu.getChildren().add(button2);
		 		            button2.setPrefSize(200, 50);
		 		            
		 		            
		 		            // Initialize the new window
		 		          	popupStage.initOwner(stage);
		 		          	popupStage.initModality(Modality.APPLICATION_MODAL);
		 		            popupStage.setScene(new Scene(winmenu));
		 		            popupStage.show();
		 				}
		 				
		 			});
		 			board.board2d[y][x] = polygon;
		 			group2.getChildren().add(polygon);
		 			board.hover(polygon, player1, player2) ;					
		 			
		 		}
		 		
		 	}
		 	
		 	// ADD THE BOARD GAME IN GROUP 2
		 	group2.getChildren().addAll(decor);
	        scene2 = new Scene(group2,1100, 700);

	        // event to start game
	        button1.setOnAction(e-> {
				stage.setScene(scene2);
				stage.centerOnScreen();
			});
			

	    }	
		// PATHFINDING RECURSIVE
		public void pathfinding(Board board, int arrayY, int arrayX,Player player, ArrayList<Polygon> explored) {
			List<Polygon> neighbour =  neighbourboard(board, arrayY, arrayX);
			System.out.print(neighbour.size());
			
			List<Polygon> winNodes = new ArrayList<>();
			Color color = player.color;
			if (color.equals(Color.BLUE)) {
				for (int x = 0; x < 11; x++) {
					winNodes.add(board.board2d[10][x]);
				}
			}
			else for (int y = 0; y < 11; y++) {
					winNodes.add(board.board2d[y][10]);
			}
			if (board.board2d[arrayY][arrayX].getId().equals(player.ID)) {				
				for (Polygon neigh : neighbour) {
					if ( neigh.getFill().equals(color) && !(explored.contains(neigh)) ) {
						explored.add(neigh);
						if(winNodes.contains(neigh)) {
							System.out.print("You have a bridge");
							player.winner = true;
						}
						else for (int y = 0; y < 11; y++) {
							for (int x = 0; x < 11; x++) {
								if ( neigh == board.board2d[y][x]) {	
									System.out.print("Neighbour is in [" +y +"]["+x +"]\n");
									pathfinding(board, y, x,player, explored);
								}		
							}	
						}
					}
				}	
			}
		}
		// FIND NEIGHBOUR
		public List<Polygon> neighbourboard(Board board, int arrayY, int arrayX) {
			List<Polygon> neighbour = new ArrayList<>();
			int limit = (board.board2d[0].length/11)-1;
			
			for (int y = Math.max(arrayY - 1, 0); y <= Math.min(arrayY + 1, limit); y++) {
				for (int x = Math.max(arrayX - 1, 0); x <= Math.min(arrayX + 1, limit); x++) {
					if ( !(y == arrayY && x == arrayX) && !(y == arrayY - 1  && x == arrayX + 1) &&  !(y == arrayY + 1  && x == arrayX - 1 ) ){
						neighbour.add(board.board2d[y][x]);
					}
				}
			}
			return neighbour; 
		} 
		
		public void winner(Stage stage,Player player, Scene scene) {
			if (player.turn) {
				stage.setScene(scene);
				stage.show();
			}
		}
		
		public Group group() {
			Group group = new Group();
			
			Line line1 = new Line((540+(0 * 45)-(11*45)), 7+(0*30)+(30*11),(540+(0 * 45)-(0*45)), 7+(0*30)+(30*0));
			line1.setStrokeWidth(6);
			line1.setStroke(Color.RED);
			group.getChildren().add(line1);
			
			Line line2 = new Line((560+(11 * 45)-(11*45)), 30+(11*30)+(30*11),(560+(11 * 45)-(0*45)), 30+(11*30)+(30*0));
			line2.setStrokeWidth(6);
			line2.setStroke(Color.RED);
			group.getChildren().add(line2);
		
			Line line3 = new Line((560+(0 * 45)-(0*45)), 0+(0*30)+(30*0),(560+(11 * 45)-(0*45)), 0+(11*30)+(30*0));
			line3.setStrokeWidth(6);
			line3.setStroke(Color.BLUE);
			group.getChildren().add(line3);
			
			Line line4 = new Line((550+(0 * 45)-(11*45)), 30+(0*30)+(30*11), (550+(11 * 45)-(11*45)), 30+(11*30)+(30*11));
			line4.setStrokeWidth(6);
			line4.setStroke(Color.BLUE);
			group.getChildren().add(line4);
			return group;
		}
		
}
class Board{
	int size_board = 121;
	Polygon[][] board2d = new Polygon[size_board][size_board];
	public Board(int size) {
		this.size_board = (size/11);
	}
	// Reset the board
	public void resetboard(Board board, Player player1, Player player2) {
		player1.winner = false;
		player2.winner = false;
		for (int y = 0; y < 11; y++ ) {
			for ( int x = 0; x < 11; x++) {
				board.board2d[y][x].setFill(Color.WHITE);
				board.board2d[y][x].setStroke(Color.BLACK);
				board.board2d[y][x].setId("WHITE");
			}
		}
	}
	// SET UP HOVER COLORING
	public void hover(Polygon poly, Player player, Player player2) {		
		poly.setOnMouseEntered(e -> {
			if (poly.getId().equals("WHITE") && (player.turn)) {
					poly.setFill(player.color);
			}
			else if (poly.getId().equals("WHITE") && (player2.turn)) {
				poly.setFill(player2.color);	
			}
		});			
		poly.setOnMouseExited(e -> {
			String tempo = poly.getId();
			switch(tempo) {
				case "WHITE":
					poly.setFill(Color.WHITE);
					break;
				case "BLUE":
					poly.setFill(Color.BLUE);
					break;
				case "RED":
					poly.setFill(Color.RED);
					break;
				default:
					break;
			}
		});
	}
}

class Player {
	String ID = "Color";
	boolean turn;
	boolean winner;
	Color color;
	public Player(boolean turn, boolean winner, Color color, String ID) {
		this.turn = turn;
		this.winner = winner;
		this.color = color;
		this.ID = ID;
	}
	public String displaywinner(Player player1, Player player2) {
		if (player1.winner) {
			return player1.ID;
		}
		else {
			return player2.ID;
		}
	}
}