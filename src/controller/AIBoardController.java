package controller;

import model.Board;
import model.Cell;
import model.Orientation;
import model.Ship;

public class AIBoardController extends BoardController {
	private Board playerBoard;

	public AIBoardController(Board playerBoard) {
		super();
		this.playerBoard = playerBoard;
	}

	public boolean placeShipRandomly(Ship ship, Orientation orientation) {
		while (true) {
			Cell cell = null;
			
			if (orientation == Orientation.HORIZONTAL)
				cell = board.getRandomAvailableHorizontal();
			else
				cell = board.getRandomAvailableVertical();

			if (cell == null)
				return false;

			int size = ship.getShipType().getSize();

			if (canPlace(cell.getX(), cell.getY(), size, orientation)) {
				board.removeAvailableCell(cell);

				place(cell.getX(), cell.getY(), ship, orientation);
				return true;
			} else {
				board.removeAvailableCell(cell);
			}
		}
	}

	public Cell getNearbyAvailable(Cell cell, Orientation orientation, int increment) {
		Cell[][] cells = playerBoard.getCells();

		if (orientation == Orientation.VERTICAL) {
			if (cell.getX() + increment < 0 || cell.getX() + increment > 9)
				return null;
			
			Cell nearbyCell = cells[cell.getX() + increment][cell.getY()];

			if (playerBoard.cellHit(nearbyCell)) {
				return getNearbyAvailable(nearbyCell, orientation, increment);
			} else if (playerBoard.availableToShoot(nearbyCell)) {
				return nearbyCell;
			}
			
			return null;
		} else {
			if (cell.getY() + increment < 0 || cell.getY() + increment > 9)
				return null;
			
			Cell nearbyCell = cells[cell.getX()][cell.getY() + increment];
			
			if (playerBoard.cellHit(nearbyCell))
				return getNearbyAvailable(nearbyCell, orientation, increment);
			else if (playerBoard.availableToShoot(nearbyCell))
				return nearbyCell;
			
			return null;
		}
	}

	private AIDestroyShipLogic destroyerLogic;

	public Cell play(Cell hit, boolean destroyed) {
		if (hit != null && destroyerLogic == null) {
			destroyerLogic = new AIDestroyShipLogic(this, hit);
		}

		if (hit != null) {
			playerBoard.addHit(hit);
		}

		if (destroyed) {
			destroyerLogic = null;
		}

		if (destroyerLogic == null) {
			Cell cell = playerBoard.getRandomCell();
			
			playerBoard.removeAvailableCellToShoot(cell);
			return cell;
		} else {
			Cell cell = destroyerLogic.seek(hit);
			
			playerBoard.removeAvailableCellToShoot(cell);
			return cell;
		}
	}
}
