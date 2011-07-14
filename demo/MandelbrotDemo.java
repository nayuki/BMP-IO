import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import p79068.bmpio.BmpImage;
import p79068.bmpio.BmpWriter;


public final class MandelbrotDemo {
	
	public static void main(String[] args) throws IOException {
		BmpImage bmp = new BmpImage();
		bmp.image = new MandelbrotImage(512, 512);
		
		File file = new File("Mandelbrot.bmp");
		FileOutputStream out = new FileOutputStream(file);
		BmpWriter.write(out, bmp);
		out.close();
	}
	
}
