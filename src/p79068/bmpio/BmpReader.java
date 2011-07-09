package p79068.bmpio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;


public final class BmpReader {
	
	public static BmpImage read(InputStream in) throws IOException {
		// BITMAPFILEHEADER
		if (readInt16(in) != 0x4D42)
			throw new RuntimeException();
		skipFully(in, 12);
		
		// BITMAPINFOV3HEADER
		if (readInt32(in) != 40)
			throw new RuntimeException();
		int width  = readInt32(in);
		int height = readInt32(in);
		if (readInt16(in) != 1)  // Planes
			throw new RuntimeException();
		int bpp = readInt16(in);  // BitsPerPixel
		if (bpp != 24 && bpp != 32)
			throw new RuntimeException();
		if (readInt32(in) != 0)  // Compression
			throw new RuntimeException();
		
		BmpImage bmp = new BmpImage();
		BufferedRgb888Image image = new BufferedRgb888Image(width, height);
		bmp.image = image;
		skipFully(in, 4);
		
		bmp.horizontalResolution = readInt32(in);
		bmp.verticalResolution   = readInt32(in);
		skipFully(in, 8);
		
		bpp /= 8;  // Now bpp is bytes per pixel
		byte[] row = new byte[(width * bpp + 3) / 4 * 4];
		for (int y = height - 1; y >= 0; y--) {
			readFully(in, row);
			for (int x = 0; x < width; x++) {
				int color = (row[x * bpp + 2] & 0xFF) << 16 | (row[x * bpp + 1] & 0xFF) << 8 | (row[x * bpp] & 0xFF);
				image.setRgb888Pixel(x, y, color);
			}
		}
		
		return bmp;
	}
	
	
	private static void skipFully(InputStream in, int len) throws IOException {
		while (len > 0) {
			long temp = in.skip(len);
			if (temp == 0)
				throw new EOFException();
			len -= temp;
		}
	}
	
	
	private static void readFully(InputStream in, byte[] b) throws IOException {
		int off = 0;
		while (off < b.length) {
			int temp = in.read(b, off, b.length - off);
			if (temp == -1)
				throw new EOFException();
			off += temp;
		}
	}
	
	
	private static int readInt16(InputStream in) throws IOException {
		byte[] b = new byte[2];
		readFully(in, b);
		return (b[0] & 0xFF) | (b[1] & 0xFF) << 8;
	}
	
	
	private static int readInt32(InputStream in) throws IOException {
		byte[] b = new byte[4];
		readFully(in, b);
		return (b[0] & 0xFF) | (b[1] & 0xFF) << 8 | (b[2] & 0xFF) << 16 | (b[3] & 0xFF) << 24;
	}
	
}
