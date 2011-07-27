package p79068.bmpio;


public final class BufferedRgb888Image implements Rgb888Image {
	
	private int width;
	
	private int height;
	
	private int[] pixels;
	
	
	
	public BufferedRgb888Image(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	
	
	public int getWidth() {
		return width;
	}
	
	
	public int getHeight() {
		return height;
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
