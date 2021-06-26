package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Polygon;

public class Test {

	public static void main(String[] args) {
		int oer = 0;
		for (oer =0; oer < 10; oer++ ) {
			System.out.print(oer);
		}
	}
	
	/*
	public List<Polygon> neighbourboard(Board board, int arrayY, int arrayX) {
		List<Polygon> neighbour = new ArrayList<>();
		int limit = (board.board2d[0].length/11)-1;
		
		for (int y = Math.max(0, arrayY-1); y <= Math.min(arrayY+1, limit); y++) {
				for (int x = Math.max(0, arrayX-1); x <= Math.min(arrayX+1, limit); x++) {
					if ( (y != arrayY || x != arrayX) && (y != arrayY-1 && x != arrayX+1) && (y != arrayY +1 && x != arrayX-1 ) ) {
							neighbour.add(board.board2d[y][x]);
					}
				}			
			}
			return neighbour;
	} */
	
}
