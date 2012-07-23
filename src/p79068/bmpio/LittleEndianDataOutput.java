package p79068.bmpio;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


final class LittleEndianDataOutput {
	
	private DataOutputStream out;
	
	
	
	public LittleEndianDataOutput(OutputStream out) {
		this.out = new DataOutputStream(out);
	}
	
	
	
	public void writeBytes(byte[] b, int off, int len) throws IOException {
		out.write(b);
	}
	
	
	public void writeInt16(int x) throws IOException {
		out.writeShort((x & 0xFF) << 8 | (x & 0xFF00) >>> 8);
	}
	
	
	public void writeInt32(int x) throws IOException {
		out.writeInt((x & 0xFF) << 24 | (x & 0xFF00) << 8 | (x & 0xFF0000) >>> 8 | (x & 0xFF000000) >>> 24);
	}
	
	
	public void flush() throws IOException {
		out.flush();
	}
	
}
