package pl.youcode.asciigenerator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

public class Image2ASCII {

	private BufferedImage input_image;

	private BufferedImage output_image;

	private String output = "";
	
	private boolean border;

	public Image2ASCII( File image ) throws IOException {

		this.input_image = ImageIO.read( image );
		ColorSpace greyscale = ColorSpace.getInstance( ColorSpace.CS_GRAY );
		ColorConvertOp cco = new ColorConvertOp( greyscale, null );
		this.output_image = cco.filter( this.input_image, null );
		this.input_image = null; // We destroy input image because it isn't need
									// anymore.
		this.border = false;
	}
	
	public void setBorder( boolean border ) {
		
		this.border = border;
		
	}

	public void save() throws IOException {

		this.save( "result.txt", 100, 100 );
	}

	public void save( String filename, int maxWidth, int maxHeight )
			throws IOException {

		System.out.println( String.format( "Saving ASCII into file: %s",
				filename ) );
		if( this.output.isEmpty() ) {
			this.generateASCII( maxWidth, maxHeight );
		}
		FileWriter fw = new FileWriter( filename );
		BufferedWriter out = new BufferedWriter( new OutputStreamWriter(
				new FileOutputStream( filename ), "UTF-8" ) );
		out.write( this.output );
		out.close();
		System.out.println( "Saved!" );
	}

	private void generateASCII( int maxWidth, int maxHeight ) {
		
		// preserve width and aspect ratio, shrink height as necessary
		maxHeight *= (double)output_image.getHeight() / (double)output_image.getWidth();
		
		if( border ) {
			maxWidth -= 2;
			maxHeight -=2;
		}
		
		if( maxWidth < 0 ) maxWidth = 0;
		if( maxHeight < 0 ) maxHeight = 0;
		
		this.output = "";

		BufferedImage original = this.output_image;

		// rescale image
		BufferedImage resized = new BufferedImage( maxWidth, maxHeight,
				BufferedImage.TYPE_INT_RGB );
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR );
		g.drawImage( original, 0, 0, maxWidth, maxHeight, 0, 0, original
				.getWidth(), original.getHeight(), null );
		g.dispose();

		this.output_image = resized;
		original = null;

		int width = this.output_image.getWidth();
		int height = this.output_image.getHeight();
		int last_percent = 0;
		
		if( border ) {
		
			this.output += " ";
			for ( int j = 1; j <= width; j++ ) this.output += "_";
			this.output += " \n|";
			
		}

		
		for ( int i = 0; i < height; i++ ) {
			
			for ( int j = 0; j < width; j++ ) {
				
				String character = " ";
				int pixel = this.output_image.getRGB( j, i );
				int red = ( pixel >> 16 ) & 0xff;
				int green = ( pixel >> 8 ) & 0xff;
				int blue = ( pixel ) & 0xff;
				int grey = (int)( 0.299 * red + 0.587 * green + 0.114 * blue );
				
				if( grey > 0 )    character = " ";
				if( grey >= 8 )   character = "'";
				if( grey >= 16 )  character = ".";
				if( grey >= 32 )  character = "-";
				if( grey >= 48 )  character = ":";
				if( grey >= 64 )  character = "+";
				if( grey >= 80 )  character = ( Math.random() > 0.5 ) ? "/" : "\\";
				if( grey >= 96 )  character = "o";
				if( grey >= 112 ) character = "s";
				if( grey >= 128 ) character = "*";
				if( grey >= 144 ) character = "y";
				if( grey >= 160 ) character = "h";
				if( grey >= 176 ) character = "d";
				if( grey >= 192 ) character = "m";
				if( grey >= 208 ) character = "V";
				if( grey >= 224 ) character = "Q";
				if( grey >= 240 ) character = "M";
				
				this.output += character;

			}
			
			if( border ) this.output += "|\n|";
			else this.output += "\n";
			int percent = ( i / height ) * 100;
			if( percent != last_percent ) {
				
				last_percent = percent;
				System.out.println( String.format( "Progress: %d%%", percent ) );
				
			}
			
		}
		
		if( border ) {
		
		for ( int j = 1; j <= width; j++ ) this.output += "_";
		this.output += "|";
		
		}
		
	}
	
	private String getASCII( int maxWidth, int maxHeight ) {
		
		generateASCII( maxWidth, maxHeight );
		return this.output;
		
	}
}
