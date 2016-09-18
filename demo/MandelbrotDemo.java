/* 
 * BMP I/O library (Java)
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/bmp-io-library-java
 * 
 * (MIT License)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * - The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 * - The Software is provided "as is", without warranty of any kind, express or
 *   implied, including but not limited to the warranties of merchantability,
 *   fitness for a particular purpose and noninfringement. In no event shall the
 *   authors or copyright holders be liable for any claim, damages or other
 *   liability, whether in an action of contract, tort or otherwise, arising from,
 *   out of or in connection with the Software or the use or other dealings in the
 *   Software.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import io.nayuki.bmpio.AbstractRgb888Image;
import io.nayuki.bmpio.BmpImage;
import io.nayuki.bmpio.BmpWriter;


public final class MandelbrotDemo {
	
	// Run with zero command-line arguments. This program writes Mandelbrot.bmp to the current directory.
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
