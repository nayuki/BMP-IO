import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import p79068.bmpio.BmpImage;
import p79068.bmpio.BmpReader;
import p79068.bmpio.BmpWriter;


public final class ReadDemo {
	
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