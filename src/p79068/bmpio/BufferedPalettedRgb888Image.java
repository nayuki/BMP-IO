package p79068.bmpio;


public final class BufferedPalettedRgb888Image extends AbstractRgb888Image {
	
	private int[] palette;
	
	private byte[] pixels;
	
	
	
	public BufferedPalettedRgb888Image(int width, int height, int[] palette) {
		super(width, height);
		this.palette = palette.clone();
		pixels = new byte[width * height];
	}
	
	
	
	public int getRgb888Pixel(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IndexOutOfBoundsException();
		return palette[pixels[y * width + x] & 0xFF];
	}
	
	
	public void setRgb888Pixel(int x, int y, byte colorIndex) {
		if (x < 0 || x >= width || y < 0 || y >= height || (colorIndex & 0xFF) >= palette.length)
			throw new IndexOutOfBoundsException();
		pixels[y * width + x] = colorIndex;
	}
	
}
