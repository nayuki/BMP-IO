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
import io.nayuki.bmpio.Rgb888Image;


public final class SimpleDemo {
	
	// Run with zero command-line arguments. This program writes Demo.bmp to the current directory.
	public static void main(String[] args) throws IOException {
		File file = new File("Demo.bmp");
		
		/* 
		 * A colorful test image (512 x 576):
		 *   
		 *   [black, red, green, blue, yellow, cyan, magenta, white squares at 64x64]
		 *   
		 *   [  black-gray-white   ] [black-red-green-yellow]
		 *   [ gradient at 256x256 ] [ gradient at 256x256  ]
		 *   
		 *   [black-green-blue-cyan] [black-blue-red-magenta]
		 *   [ gradient at 256x256 ] [ gradient at 256x256  ]
		 */
		Rgb888Image image = new AbstractRgb888Image(512, 64 + 512) {
			public int getRgb888Pixel(int x, int y) {
				if (y < 64)
					return (new int[]{0x000000, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0x00FFFF, 0xFF00FF, 0xFFFFFF})[x / 64];
				else {
					y -= 64;
					int a = x % 256;
					int b = y % 256;
					if (y < 256) {
						if (x < 256)
							return ((a + b) / 2) * 0x010101;
						else
							return b << 16 | a << 8;
					} else {
						if (x < 256)
							return b << 8 | a << 0;
						else
							return b << 0 | a << 16;
					}
				}
			}
		};
		
		BmpImage bmp = new BmpImage();
		bmp.image = image;
		FileOutputStream out = new FileOutputStream(file);
		try {
			BmpWriter.write(out, bmp);
		} finally {
			out.close();
		}
	}
	
}
