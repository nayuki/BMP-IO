package p79068.bmpio;


// A convenience base class with width and height fields and getters.
public abstract class AbstractRgb888Image implements Rgb888Image {
	
	protected int width;
	protected int height;
	
	
	
	public AbstractRgb888Image(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	
	
	public int getWidth() {
		return width;
	}
	
	
	public int getHeight() {
		return height;
	}
	
}
