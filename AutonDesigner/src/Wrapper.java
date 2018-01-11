import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;

public class Wrapper {

	public static void main(String[] args) {
		ArrayList<Shape> map = MapReader.fromfile(new File("map.txt"));
		MapWindow mapWindow = new MapWindow();
	}

}
