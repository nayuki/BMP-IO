import p79068.bmpio.Rgb888Image;


/**
 * The Mandelbrot set as an Rgb888Image. This shows that an image does not need to be explicitly stored; its pixels can be computed on the fly.
 */
final class MandelbrotImage implements Rgb888Image {
	
	private double xMin = -1.9;
	private double xMax =  0.5;
	private double yMin = -1.2;
	private double yMax =  1.2;
	
	private int iterations = 1000;
	
	private int width;
	private int height;
	
	
	
	public MandelbrotImage(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	
	
	public int getWidth() {
		return width;
	}
	
	
	public int getHeight() {
		return height;
	}
	
	
	public int getRgb888Pixel(int x, int y) {
		if (isInMandelbrotSet(xMin + (x + 0.5) / width * (xMax - xMin), yMax - (y + 0.5) / height * (yMax - yMin)))
			return 0x000000;
		else
			return 0xFFFFFF;
	}
	
	
	private boolean isInMandelbrotSet(double x, double y) {
		double zr = 0;
		double zi = 0;
		for (int i = 0; i < iterations; i++) {
			if (zr * zr + zi * zi > 4)
				return false;
			double temp = zr * zr - zi * zi + x;
			zi = 2 * zr * zi + y;
			zr = temp;
		}
		return true;
	}
	
}
