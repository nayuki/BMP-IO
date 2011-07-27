package p79068.bmpio;

import java.io.IOException;
import java.io.OutputStream;


public final class BmpWriter {
	
	public static void write(OutputStream out, BmpImage bmp) throws IOException {
		LittleEndianDataOutput out1 = new LittleEndianDataOutput(out);
		
		Rgb888Image image = bmp.image;
		int width = image.getWidth();
		int height = image.getHeight();
		int rowSize = (width * 3 + 3) / 4 * 4;  // 3 bytes per pixel in RGB888, round up to multiple of 4
		int imageSize = rowSize * height;
		
		// BITMAPFILEHEADER
		out1.writeBytes(new byte[]{'B', 'M'}, 0, 2);  // FileType
		out1.writeInt32(14 + 40 + imageSize);         // FileSize
		out1.writeInt16(0);                           // Reserved1
		out1.writeInt16(0);                           // Reserved2
		out1.writeInt32(14 + 40);                     // BitmapOffset
		
		// BITMAPINFOHEADER
		out1.writeInt32(40);                        // Size
		out1.writeInt32(width);                     // Width
		out1.writeInt32(height);                    // Height
		out1.writeInt16(1);                         // Planes
		out1.writeInt16(24);                        // BitsPerPixel
		out1.writeInt32(0);                         // Compression
		out1.writeInt32(imageSize);                 // SizeOfBitmap
		out1.writeInt32(bmp.horizontalResolution);  // HorzResolution
		out1.writeInt32(bmp.verticalResolution);    // VertResolution
		out1.writeInt32(0);                         // ColorsUsed
		out1.writeInt32(0);                         // ColorsImportant
		
		// Image data
		byte[] row = new byte[rowSize];
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				int color = image.getRgb888Pixel(x, y);
				row[x * 3 + 0] = (byte)(color >>>  0);  // Blue
				row[x * 3 + 1] = (byte)(color >>>  8);  // Green
				row[x * 3 + 2] = (byte)(color >>> 16);  // Red
			}
			out1.writeBytes(row, 0, row.length);
		}
		
		out1.flush();
	}
	
}
