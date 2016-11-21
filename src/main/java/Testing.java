import no.ntnu.imt3281.ludo.logic.Ludo;

public class Testing {
	public static void main(String[] args) {
		Ludo ludo = new Ludo();
		
		System.out.println(ludo.getPosition(0, 0));
		System.out.println(ludo.getPieceBoardPos(0, 0));
		System.out.println(ludo.getPosition(0, 1));
		System.out.println(ludo.getPieceBoardPos(0, 1));
		System.out.println(ludo.getPosition(0, 2));
		System.out.println(ludo.getPieceBoardPos(0, 2));
		System.out.println(ludo.getPosition(0, 3));
		System.out.println(ludo.getPieceBoardPos(0, 3));

		

	}
}
