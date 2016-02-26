import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import p79068.bmpio.AbstractRgb888Image;
import p79068.bmpio.BmpImage;
import p79068.bmpio.BmpWriter;


public final class MandelbrotDemo {
	
	public static void main(String[] args) throws IOException {
		BmpImage bmp = new BmpImage();
		bmp.image = new MandelbrotImage(512, 512);
		
		File file = new File("Mandelbrot.bmp");
		FileOutputStream out = new FileOutputStream(file);
		try {
			BmpWriter.write(out, bmp);
		} finally {
			out.close();
		}
	}
	
	
	
	// Represents an image of the Mandelbrot set. This shows that an image object does not
	// need to store pixel data explicitly; the pixel values can be computed on the fly.
	private final static class MandelbrotImage extends AbstractRgb888Image {
		
		private double xMin = -1.9;
		private double xMax =  0.5;
		private double yMin = -1.2;
		private double yMax =  1.2;
		
		private int iterations = 1000;
		
		
		
		public MandelbrotImage(int width, int height) {
			super(width, height);
		}
		
		
		
		public int getRgb888Pixel(int x, int y) {
			double real = xMin + (x + 0.5) / width  * (xMax - xMin);
			double imag = yMax - (y + 0.5) / height * (yMax - yMin);
			return isInMandelbrotSet(real, imag) ? 0x000000 : 0xFFFFFF;
		}
		
		
		private boolean isInMandelbrotSet(double real, double imag) {
			double zr = 0;
			double zi = 0;
			for (int i = 0; i < iterations; i++) {
				if (zr * zr + zi * zi > 4)
					return false;
				double temp = zr * zr - zi * zi + real;
				zi = 2 * zr * zi + imag;
				zr = temp;
			}
			return true;
		}
		
	}
	
}
