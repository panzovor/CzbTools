package czb.tools.filereader;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface CommonReader {
	public String readContent(String filepath)throws FileNotFoundException, IOException;
	public String readContent(String filepath, String charset)throws FileNotFoundException, IOException;
}
