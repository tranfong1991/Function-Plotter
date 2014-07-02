public class Interval {
	double start;
	double end;
	
	public Interval(){
		this.start = 0;
		this.end = 10;
	}
	
	public Interval(double start, double end){
		this.start = start;
		this.end = end;
	}
	
	public double getDistance(){
		return this.end - this.start;
	}
}
