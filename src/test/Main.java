package test;

import java.awt.Point;
import java.awt.geom.Line2D;

import behaviours.HqTaxiContractNetResponderBehaviour;

public class Main {

	public static void main(String[] args) {
		System.out.println(Line2D.ptSegDist(1, 1, 4, 4, 15, 16));
		
		String[] test = new String("aa--").split("-");
		System.out.println(test.length);
		
		boolean b = true;
		System.out.println(String.valueOf(b));
		
		System.out.println(angleVectors(0, 10, 10, 10));
	}
	
	public static double pointToLineDistance(Point A, Point B, Point P) {
	    double normalLength = Math.sqrt((B.x-A.x)*(B.x-A.x)+(B.y-A.y)*(B.y-A.y));
	    return Math.abs((P.x-A.x)*(B.y-A.y)-(P.y-A.y)*(B.x-A.x))/normalLength;
	}
	
	public static int angleVectors(int x1, int y1, int x2, int y2){
		double angle = Math.atan2(y2, x2) - Math.atan2(y1,x1);
		return (int) Math.toDegrees(angle);
	}
	
}
