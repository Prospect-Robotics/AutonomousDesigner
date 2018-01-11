import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;

public class Wrapper {

	public static void main(String[] args) {
		ArrayList<Shape> map = MapReader.fromfile(new File("map.txt"));
		MapWindow mapWindow = new MapWindow(888,360);
		StepsWindow stepsWindow = new StepsWindow();
		stepsWindow.move(10);
		stepsWindow.turn(90);
		stepsWindow.move(15);
		stepsWindow.move(10);
		stepsWindow.turn(90);
		stepsWindow.move(15);
		stepsWindow.move(10);
		stepsWindow.turn(90);
		stepsWindow.move(15);
		stepsWindow.move(10);
		stepsWindow.turn(90);
		stepsWindow.move(15);
	}

}
