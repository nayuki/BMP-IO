import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import io.nayuki.bmpio.BmpImage;
import io.nayuki.bmpio.BmpReader;
import io.nayuki.bmpio.BmpWriter;


public final class ReadDemo {
	
	// Run with zero command-line arguments. This program reads Demo.bmp and writes Demo2.bmp in the current directory.
	public static void main(String[] args) throws IOException {
		InputStream in = new FileInputStream("Demo.bmp");
		BmpImage bmp;
		try {
			bmp = BmpReader.read(in);
		} finally {
			in.close();
		}
		
		OutputStream out = new FileOutputStream("Demo2.bmp");
		try {
			BmpWriter.write(out, bmp);
		} finally {
			out.close();
		}
	}
	
}