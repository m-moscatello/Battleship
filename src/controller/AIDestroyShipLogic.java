package controller;

import model.Cell;
import model.Orientation;

public class AIDestroyShipLogic {
	private AIBoardController controller;
	
	private Orientation orientation = Orientation.HORIZONTAL;
	private int increment = 1;
	
	private Cell originalHit;
	private Cell currentCell;

	public AIDestroyShipLogic(AIBoardController controller, Cell startingHit) {
		this.controller = controller;
		this.originalHit = startingHit;
	}

	protected Cell seek(Cell hit) {
		if (hit != null)
			currentCell = hit;
		else
			currentCell = originalHit;

		while (true) {
			currentCell = controller.getNearbyAvailable(currentCell, orientation, increment);

			if (currentCell == null) {
				changeDirection();
				currentCell = originalHit;
			} else {
				return currentCell;
			}
		}
	}

	protected void changeDirection() {
		if (orientation == Orientation.VERTICAL && increment < 0) {
			orientation = Orientation.HORIZONTAL;
			increment = 1;
			return;
		}

		if (increment > 0) {
			increment = -1;
			return;
		} else {
			orientation = Orientation.VERTICAL;
			increment = 1;
			return;
		}
	}
}
