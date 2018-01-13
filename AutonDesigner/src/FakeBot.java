import java.awt.Point;

public class FakeBot {
	static Point.Double position;
	static double angle;
	static double length;
	static double width;
	public FakeBot(Point.Double startPos,double startAngle,double length,double width){
		this.position = startPos;
		this.angle = startAngle;
		this.length = length;
		this.width = width;
	}
	public void rotate(double angle){
		this.angle+=angle;
		if(this.angle>360){
			this.angle-=360;
		}
		else if(this.angle<0){
			this.angle+=360;
		}
	}
}
