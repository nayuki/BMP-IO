package p79068.bmpio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;


public final class BmpReader {
	
	public static BmpImage read(InputStream in) throws IOException {
		// BITMAPFILEHEADER (14 bytes)
		if (readInt16(in) != 0x4D42)  // "BM"
			throw new RuntimeException("Invalid BMP signature");
		int fileSize = readInt32(in);
		skipFully(in, 4);  // Skip reserved
		int imageDataOffset = readInt32(in);
		
		// BITMAPINFOHEADER
		BmpImage bmp = new BmpImage();
		int headerSize = readInt32(in);
		int width;
		int height;
		int bitsPerPixel;
		int imageSize;
		int colorsUsed;
		if (headerSize == 40) {
			width  = readInt32(in);
			height = readInt32(in);
			int planes = readInt16(in);
			bitsPerPixel = readInt16(in);
			int compression = readInt32(in);
			imageSize = readInt32(in);
			bmp.horizontalResolution = readInt32(in);
			bmp.verticalResolution   = readInt32(in);
			colorsUsed = readInt32(in);
			int colorsImportant = readInt32(in);
			
			if (width <= 0)
				throw new RuntimeException("Invalid width: " + width);
			if (height <= 0)
				throw new RuntimeException("Invalid height: " + height);
			if (planes != 1)
				throw new RuntimeException("Unsupported planes: " + planes);
			if (compression != 0)
				throw new RuntimeException("Unsupported compression: " + compression);
			
			if (bitsPerPixel == 8) {
				if (colorsUsed == 0)
					colorsUsed = 1 << bitsPerPixel;
				if (colorsUsed > 1 << bitsPerPixel)
					throw new RuntimeException("Invalid colors used: " + colorsUsed);
				
			} else if (bitsPerPixel == 24 || bitsPerPixel == 32) {
				if (colorsUsed != 0)
					throw new RuntimeException("Invalid colors used: " + colorsUsed);
				
			} else
				throw new RuntimeException("Unsupported bits per pixel: " + bitsPerPixel);
			
			if (colorsImportant < 0 || colorsImportant > colorsUsed)
				throw new RuntimeException("Invalid important colors: " + colorsImportant);
			if (imageSize != (width * bitsPerPixel / 8 + 3) / 4 * 4 * height)
				throw new RuntimeException("Invalid image size: " + imageSize);
			
		} else
			throw new RuntimeException("Unsupported BMP header format: " + headerSize + " bytes");
		
		if (14 + headerSize + 4 * colorsUsed > imageDataOffset)
			throw new RuntimeException("Invalid image data offset: " + imageDataOffset);
		if (imageDataOffset + imageSize > fileSize)
			throw new RuntimeException("Invalid file size: " + fileSize);
		
		// Read the image data
		if (bitsPerPixel == 24 || bitsPerPixel == 32)
			bmp.image = readRgb24Or32Image(in, width, height, bitsPerPixel);
		
		else if (bitsPerPixel == 8) {
			int[] palette = new int[colorsUsed];
			for (int i = 0; i < colorsUsed; i++) {
				byte[] entry = new byte[4];
				readFully(in, entry);
				palette[i] = (entry[2] & 0xFF) << 16 | (entry[1] & 0xFF) << 8 | (entry[0] & 0xFF);
			}
			
			bmp.image = readRgb8Image(in, width, height, bitsPerPixel, palette);
			
		} else
			throw new AssertionError();
		
		return bmp;
	}
	
	
	private static Rgb888Image readRgb24Or32Image(InputStream in, int width, int height, int bitsPerPixel) throws IOException {
		BufferedRgb888Image image = new BufferedRgb888Image(width, height);
		int bytesPerPixel = bitsPerPixel / 8;
		byte[] row = new byte[(width * bytesPerPixel + 3) / 4 * 4];
		for (int y = height - 1; y >= 0; y--) {
			readFully(in, row);
			for (int x = 0; x < width; x++) {
				int color =   (row[x * bytesPerPixel + 2] & 0xFF) << 16
				            | (row[x * bytesPerPixel + 1] & 0xFF) <<  8
				            | (row[x * bytesPerPixel + 0] & 0xFF) <<  0;
				image.setRgb888Pixel(x, y, color);
			}
		}
		return image;
	}
	
	
	private static Rgb888Image readRgb8Image(InputStream in, int width, int height, int bitsPerPixel, int[] palette) throws IOException {
		BufferedPalettedRgb888Image image = new BufferedPalettedRgb888Image(width, height, palette);
		int bytesPerPixel = bitsPerPixel / 8;
		byte[] row = new byte[(width * bytesPerPixel + 3) / 4 * 4];
		for (int y = height - 1; y >= 0; y--) {
			readFully(in, row);
			for (int x = 0; x < width; x++)
				image.setRgb888Pixel(x, y, row[x]);
		}
		return image;
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
