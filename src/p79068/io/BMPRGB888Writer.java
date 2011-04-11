/*
How to use this class

This class only provides methods to write an uncompressed RGB 24-bit (8 bits per channel) Windows bitmap image.
The methods take these arguments:
File path string / File path object / Output stream
Image pixel data
Image width
Image height
Image horizontal resolution (optional)
Image vertical resolution (optional)

The pixel data must be in this form:
 The array is byte[3][width*height]
 image[0] holds the pixels for the red channel, image[1] the green channel, and image[2] the blue channel
 The pixel with coordinates (x,y) is located in image[channel_number][y*width+x]
*/

/*
Windows Bitmap (BMP) - File Format

All values must be serialized in little endian.
This is an abbreviated specification.

Offset (bytes), Length (bytes), Description
 File Header
   0, 2, Magic number: 0x4D42
   2, 4, File length: file header length + info header length + image length
   6, 2, Reserved: 0
   8, 2, Reserved: 0
  10, 4, Image offset: file header length + info header length
 Info header
  14, 4, Info header length
  18, 4, Image width (pixels)
  22, 4, Image height (pixels)
  26, 2, Planes: 1
  28, 2, Color depth (bits per pixel): 24
  30, 4, Compression: 0 (Uncompressed)
  34, 4, Image length
  38, 4, Horizontal resolution (pixels per metre)
  42, 4, Vertical resolution (pixels per metre)
  46, 4, Number of palette entries used: 0
  50, 4, Number of palette entries important: 0
 Bitmap
  54, ..., Image pixel values

Scanlines are serialized upward from the bottom.
Each scanline must have a length divisible by 4.
For each RGB 8-8-8 pixel, the byte order is {blue, green, red}.
*/


package p79068.io;

import java.io.*;


public class BMPRGB888Writer {
	
	private static final int resolutiondefault = 3780; // Measured in pixels per metre
	

	public static void write(String file, byte[][] image, int width, int height) throws IOException {
		write(file, image, width, height, resolutiondefault, resolutiondefault);
	}
	
	public static void write(String file, byte[][] image, int width, int height, int resx, int resy) throws IOException {
		write(new File(file), image, width, height, resx, resy);
	}
	
	
	public static void write(File file, byte[][] image, int width, int height) throws IOException {
		write(file, image, width, height, resolutiondefault, resolutiondefault);
	}
	
	public static void write(File file, byte[][] image, int width, int height, int resx, int resy) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		write(out, image, width, height, resx, resy);
		out.close();
	}
	
	
	public static void write(OutputStream out, byte[][] image, int width, int height) throws IOException {
		write(out, image, width, height, resolutiondefault, resolutiondefault);
	}
	
	public static void write(OutputStream out, byte[][] image, int width, int height, int resx, int resy) throws IOException {
		writeBMPHeader(out, width, height, resx, resy);
		int linelen = (width * 3 + 3) / 4 * 4;
		byte[] line = new byte[linelen];
		byte[] r = image[0], g = image[1], b = image[2];
		for (int y = height - 1, yy = y * width; y >= 0; y--, yy -= width) {
			for (int x = 0, xx = 0; x < width; x++, xx += 3) {
				line[xx] = b[yy + x];
				line[xx + 1] = g[yy + x];
				line[xx + 2] = r[yy + x];
			}
			out.write(line);
		}
	}
	
	
	private static void writeBMPHeader(OutputStream out, int width, int height, int resx, int resy) throws IOException {
		int imglen = (width * 3 + 3) / 4 * 4 * height;
		byte[] header = new byte[54];
		// BITMAPFILEHEADER
		header[0] = (byte)'B'; // bfType
		header[1] = (byte)'M';
		header[2] = (byte)(54 + imglen); // bfSize
		header[3] = (byte)(54 + imglen >>> 8);
		header[4] = (byte)(54 + imglen >>> 16);
		header[5] = (byte)(54 + imglen >>> 24);
		header[6] = 0; // bfReserved1
		header[7] = 0;
		header[8] = 0; // bfReserved2
		header[9] = 0;
		header[10] = 54; // bfOffBits
		header[11] = 0;
		header[12] = 0;
		header[13] = 0;
		// BITMAPINFOHEADER
		header[14] = 40; // biSize
		header[15] = 0;
		header[16] = 0;
		header[17] = 0;
		header[18] = (byte)width; // biWidth
		header[19] = (byte)(width >>> 8);
		header[20] = (byte)(width >>> 16);
		header[21] = (byte)(width >>> 24);
		header[22] = (byte)height; // biHeight
		header[23] = (byte)(height >>> 8);
		header[24] = (byte)(height >>> 16);
		header[25] = (byte)(height >>> 24);
		header[26] = 1; // biPlanes
		header[27] = 0;
		header[28] = 24; // biBitCount
		header[29] = 0;
		header[30] = 0; // biCompression
		header[31] = 0;
		header[32] = 0;
		header[33] = 0;
		header[34] = (byte)imglen; // biSizeImage
		header[35] = (byte)(imglen >>> 8);
		header[36] = (byte)(imglen >>> 16);
		header[37] = (byte)(imglen >>> 24);
		header[38] = (byte)resx; // biXPelsPerMeter
		header[39] = (byte)(resx >>> 8);
		header[40] = (byte)(resx >>> 16);
		header[41] = (byte)(resx >>> 24);
		header[42] = (byte)resy; // biYPelsPerMeter
		header[43] = (byte)(resy >>> 8);
		header[44] = (byte)(resy >>> 16);
		header[45] = (byte)(resy >>> 24);
		header[46] = 0; // biClrUsed
		header[47] = 0;
		header[48] = 0;
		header[49] = 0;
		header[50] = 0; // biClrImportant
		header[51] = 0;
		header[52] = 0;
		header[53] = 0;
		out.write(header);
	}
	
	
	private BMPRGB888Writer() {}
}