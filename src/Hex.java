package src;

import src.game.TextGame;
import src.game.GUIGame;

public class Hex {
	public static void main(String[] args) {
		switch (args[0]) {
			case "text":
				new TextGame();
				break;
			case "gui":
				new GUIGame();
				break;
		}
	}
}
