package p79068.bmpio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;


public final class BmpReader {
	
	public static BmpImage read(InputStream in) throws IOException {
		// BITMAPFILEHEADER (14 bytes)
		int fileSize;
		int imageDataOffset;
		if (readInt16(in) != 0x4D42)  // "BM"
			throw new RuntimeException("Invalid BMP signature");
		fileSize = readInt32(in);
		skipFully(in, 4);  // Skip reserved
		imageDataOffset = readInt32(in);
		
		// BITMAPINFOHEADER
		int headerSize = readInt32(in);
		int width;
		int height;
		boolean topToBottom;
		int bitsPerPixel;
		int compression;
		int imageSize;
		int colorsUsed;
		BmpImage bmp = new BmpImage();
		if (headerSize == 40) {
			int planes;
			int colorsImportant;
			width  = readInt32(in);
			height = readInt32(in);
			topToBottom = height < 0;
			height = Math.abs(height);
			planes = readInt16(in);
			bitsPerPixel = readInt16(in);
			compression = readInt32(in);
			imageSize = readInt32(in);
			bmp.horizontalResolution = readInt32(in);
			bmp.verticalResolution   = readInt32(in);
			colorsUsed = readInt32(in);
			colorsImportant = readInt32(in);
			
			if (width <= 0)
				throw new RuntimeException("Invalid width: " + width);
			if (height == 0)
				throw new RuntimeException("Invalid height: " + height);
			if (planes != 1)
				throw new RuntimeException("Unsupported planes: " + planes);
			
			if (bitsPerPixel == 1 || bitsPerPixel == 4 || bitsPerPixel == 8) {
				if (colorsUsed == 0)
					colorsUsed = 1 << bitsPerPixel;
				if (colorsUsed > 1 << bitsPerPixel)
					throw new RuntimeException("Invalid colors used: " + colorsUsed);
				
			} else if (bitsPerPixel == 24 || bitsPerPixel == 32) {
				if (colorsUsed != 0)
					throw new RuntimeException("Invalid colors used: " + colorsUsed);
				
			} else
				throw new RuntimeException("Unsupported bits per pixel: " + bitsPerPixel);
			
			if (compression == 0) {
				if (imageSize == 0)
					imageSize = (width * bitsPerPixel + 31) / 32 * 4 * height;
			} else if (bitsPerPixel == 8 && compression == 1 || bitsPerPixel == 4 && compression == 2) {
				if (topToBottom)
					throw new RuntimeException("Top-to-bottom order not supported for compression = 1 or 2");
			} else
				throw new RuntimeException("Unsupported compression: " + compression);
			
			if (imageSize != (width * bitsPerPixel + 31) / 32 * 4 * height)
				throw new RuntimeException("Invalid image size: " + imageSize);
			if (colorsImportant < 0 || colorsImportant > colorsUsed)
				throw new RuntimeException("Invalid important colors: " + colorsImportant);
			
		} else
			throw new RuntimeException("Unsupported BMP header format: " + headerSize + " bytes");
		
		// Some more checks
		if (14 + headerSize + 4 * colorsUsed > imageDataOffset)
			throw new RuntimeException("Invalid image data offset: " + imageDataOffset);
		if (imageDataOffset > fileSize)
			throw new RuntimeException("Invalid file size: " + fileSize);
		
		// Read the image data
		skipFully(in, imageDataOffset - (14 + headerSize + 4 * colorsUsed));
		if (bitsPerPixel == 24 || bitsPerPixel == 32)
			bmp.image = readRgb24Or32Image(in, width, height, topToBottom, bitsPerPixel);
		
		else {
			int[] palette = new int[colorsUsed];
			for (int i = 0; i < colorsUsed; i++) {
				byte[] entry = new byte[4];
				readFully(in, entry);
				palette[i] = (entry[2] & 0xFF) << 16 | (entry[1] & 0xFF) << 8 | (entry[0] & 0xFF);
			}
			
			if (compression == 0)
				bmp.image = readPalettedImage(in, width, height, topToBottom, bitsPerPixel, palette);
			else
				bmp.image = readRleImage(in, width, height, bitsPerPixel, palette);
		}
		
		skipFully(in, fileSize - (imageDataOffset + imageSize));
		return bmp;
	}
	
	
	private static Rgb888Image readRgb24Or32Image(InputStream in, int width, int height, boolean topToBottom, int bitsPerPixel) throws IOException {
		BufferedRgb888Image image = new BufferedRgb888Image(width, height);
		int bytesPerPixel = bitsPerPixel / 8;
		byte[] row = new byte[(width * bytesPerPixel + 3) / 4 * 4];
		
		int y, end, inc;
		if (topToBottom) {
			y = 0;
			end = height;
			inc = 1;
		} else {
			y = height - 1;
			end = -1;
			inc = -1;
		}
		
		for (; y != end; y += inc) {
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
	
	
	private static Rgb888Image readPalettedImage(InputStream in, int width, int height, boolean topToBottom, int bitsPerPixel, int[] palette) throws IOException {
		BufferedPalettedRgb888Image image = new BufferedPalettedRgb888Image(width, height, palette);
		byte[] row = new byte[(width * bitsPerPixel + 31) / 32 * 4];
		int pixelsPerByte = 8 / bitsPerPixel;
		int mask = (1 << bitsPerPixel) - 1;
		
		int y, end, inc;
		if (topToBottom) {
			y = 0;
			end = height;
			inc = 1;
		} else {
			y = height - 1;
			end = -1;
			inc = -1;
		}
		
		for (; y != end; y += inc) {
			readFully(in, row);
			for (int x = 0; x < width; x++) {
				int index = x / pixelsPerByte;
				int shift = (pixelsPerByte - 1 - x % pixelsPerByte) * bitsPerPixel;
				image.setRgb888Pixel(x, y, (byte)(row[index] >>> shift & mask));
			}
		}
		return image;
	}
	
	
	private static Rgb888Image readRleImage(InputStream in, int width, int height, int bitsPerPixel, int[] palette) throws IOException {
		BufferedPalettedRgb888Image image = new BufferedPalettedRgb888Image(width, height, palette);
		int x = 0;
		int y = height - 1;
		while (true) {
			byte[] b = new byte[2];
			readFully(in, b);
			if (b[0] == 0) {  // Special
				if (b[1] == 0) {  // End of scanline
					x = 0;
					y--;
				} else if (b[1] == 1) {  // End of RLE data
					break;
				} else if (b[1] == 2) {  // Delta code
					readFully(in, b);
					x += b[0] & 0xFF;
					y -= b[1] & 0xFF;
					if (x >= width)
						throw new IndexOutOfBoundsException("x coordinate out of bounds");
				
				} else {  // Literal run
					int n = b[1] & 0xFF;
					b = new byte[(n * bitsPerPixel + 15) / 16 * 2];  // Round up to multiple of 2 bytes
					readFully(in, b);
					for (int i = 0; i < n; i++, x++) {
						if (x == width)  // Ignore image data past end of line
							break;
						
						if (bitsPerPixel == 8)
							image.setRgb888Pixel(x, y, b[i]);
						else if (bitsPerPixel == 4)
							image.setRgb888Pixel(x, y, (byte)(b[i / 2] >>> ((1 - i % 2) * 4) & 0xF));
						else
							throw new AssertionError();
					}
				}
				
			} else {  // Run
				int n = b[0] & 0xFF;
				for (int i = 0; i < n; i++, x++) {
					if (x == width)  // Ignore image data past end of line
						break;
					
					if (bitsPerPixel == 8)
						image.setRgb888Pixel(x, y, b[1]);
					else if (bitsPerPixel == 4)
						image.setRgb888Pixel(x, y, (byte)(b[1] >>> ((1 - i % 2) * 4) & 0xF));
					else
						throw new AssertionError();
				}
			}
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
