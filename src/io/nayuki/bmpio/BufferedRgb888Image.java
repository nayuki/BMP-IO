package io.nayuki.bmpio;


public final class BufferedRgb888Image extends AbstractRgb888Image {
	
	private int[] pixels;
	
	
	
	public BufferedRgb888Image(int width, int height) {
		super(width, height);
		if (width > Integer.MAX_VALUE / height)
			throw new IllegalArgumentException("Image dimensions too large");
		pixels = new int[width * height];
	}
	
	
	
	public int getRgb888Pixel(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IndexOutOfBoundsException();
		return pixels[y * width + x];
	}
	
	
	public void setRgb888Pixel(int x, int y, int color) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IndexOutOfBoundsException();
		pixels[y * width + x] = color;
	}
	
}
