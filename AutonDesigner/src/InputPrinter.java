import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class InputPrinter {
	static Controller controller;
	static boolean active=false;
	public InputPrinter(Controller controller){
		this.controller= controller;
	}
	public static void print(){
		while(active){
			controller.poll();
			EventQueue queue = controller.getEventQueue();
			Event event = new Event();
			while (queue.getNextEvent(event)) {
				System.out.println(event);
			}
		}
	}
}
