package fr.exia.ue5_2.graph;

public class Personne  {

	/**
	 * @see http://www.generatedata.com/?lang=fr
	 */
	private static String[] NAMES = new String[] { "Brody Dale",
			"Jerome Chaney", "Lars Humphrey", "Kato Briggs", "Marshall Forbes",
			"Michael Villarreal", "Ishmael Horne", "Driscoll English",
			"Reed Mullen", "Ishmael Barton", "Nissim Conley", "Matthew Park",
			"Axel Wilder", "Wylie Clements", "Gabriel Todd", "Jackson Sykes",
			"Chandler Flowers", "Gil Rogers", "Neil Dudley", "Merritt Britt",
			"Tate Bradford", "Vladimir Leonard", "Clinton Lynch",
			"Troy Leblanc", "Reece Morse", "Buckminster Hebert", "Xenos Foley",
			"Lawrence Melton", "Thaddeus Bradley", "Basil Rivera",
			"Amos Foster", "Fitzgerald Shaw", "Buckminster Robertson",
			"David Schroeder", "Ryan Singleton", "Carl Long", "Price Pace",
			"Eric Lynn", "Carter Thomas", "Jesse Grant", "Noble Hess",
			"Arsenio Nielsen", "Jackson Walter", "Zeus Joyce", "Henry Avila",
			"Armand Walls", "Aristotle Richards", "Owen Burnett",
			"Garrison Lucas", "Samuel Whitaker", "Coby Barton",
			"Myles Williamson", "Jakeem Brady", "Lee Huffman", "Amal Dean",
			"Tobias William", "Ferris Strickland", "Matthew Carrillo",
			"Louis Mooney", "Howard Waters", "Hashim Adkins",
			"Brenden Nichols", "Caldwell Small", "Alec Warner", "Omar Patton",
			"Theodore Ray", "Garrett Hays", "Nero Larson", "Amal Palmer",
			"Carter Moore", "Graiden Stafford", "Bruce Vasquez",
			"Nehru Mcintosh", "Tyler Nash", "Eaton Rojas", "Quentin Lyons",
			"Daniel Huff", "Joel Burnett", "Damian Stark", "Kenyon Stark",
			"Jonah Fleming", "Solomon Sanders", "Hu Holmes", "Wyatt Mcclure",
			"Kermit Morin", "Quinlan Sosa", "Kaseem Levine", "Wang Spencer",
			"Wesley Molina", "Ivor Sanford", "Harlan Torres", "Hilel Chan",
			"Philip Mcguire", "Scott Trujillo", "Carl Mcdaniel",
			"Hayden Gutierrez", "Patrick Whitley", "Cyrus Cherry",
			"Holmes Burks", "Grant Leon" };

	private static int NAMEC = 0;

	private String name;

	public Personne() {
		name = NAMES[NAMEC++];
		if (NAMEC >= NAMES.length) NAMEC = 0; 
	}
	
	@Override
	public String toString() {
		return name;
	}

}
