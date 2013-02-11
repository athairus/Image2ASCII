package pl.youcode.asciigenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Program {

	public static void main( String[] args ) {

		System.out.println( "#---------------------------#" );
		System.out.println( "# Image to ASCII conventer   " );
		System.out.println( "#   by: Thelleo              " );
		System.out.println( "#   http://youcode.pl        " );
		System.out.println( "#---------------------------#" );
		boolean run = true;
		do {
			System.out.println( "" );
			System.out.println( "Type path to file which you want to convert." );
			System.out.println( "If you want to exit type: QUIT" );
			BufferedReader bufferRead = new BufferedReader(
					new InputStreamReader( System.in ) );
			try {
				String filename = bufferRead.readLine();
				if( filename.equals( "QUIT" ) ) {
					run = false;
					break;
				}
				try {
					Image2ASCII art = new Image2ASCII( new File( filename ) );
					art.save();
				} catch ( IOException e ) {
					System.out.println( String.format( "Error: %s",
							e.getLocalizedMessage() ) );
				}
			} catch ( IOException e ) {
				System.out.println( String.format( "Error: %s",
						e.getLocalizedMessage() ) );
			}
		} while ( run );
	}

}
