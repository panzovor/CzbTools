package czb.tools.filereader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class ExcelFileReader implements CommonReader{

	@Override
	public String readContent(String filepath, String charset) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		File file = new File(filepath);
		Workbook workbook = null;
		String content="";
		if((file=FileCheck.getInstance().checkFile(filepath))!= null){
			String fileType = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
		    InputStream stream = new FileInputStream(filepath);
		    Workbook wb = null;
		    if (fileType.equals("xls")) {
		      wb = new HSSFWorkbook(stream);
		    } else if (fileType.equals("xlsx")) {
		      wb = new XSSFWorkbook(stream);
		    } else {
		      System.out.println("�������excel��ʽ����ȷ");
		    }
		    Sheet sheet1 = wb.getSheetAt(0);
		    for (Row row : sheet1) {
		      for (Cell cell : row) {
		    	  content += "\t";
		      }
		      content = content.trim();
		      content+="\n";
		    }
			return content;
		}else
			return "null";
	}

	@Override
	public String readContent(String filepath) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		File file = new File(filepath);
		Workbook workbook = null;
		String content="";
		if((file=FileCheck.getInstance().checkFile(filepath))!= null){
			String fileType = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
			InputStream stream = new FileInputStream(filepath);
		    Workbook wb = null;
		    if (fileType.equals("xls")) {
		      wb = new HSSFWorkbook(stream);
		    } else if (fileType.equals("xlsx")) {
		      wb = new XSSFWorkbook(stream);
		    } else {
		      System.out.println("�������excel��ʽ����ȷ");
		    }
		    Sheet sheet1 = wb.getSheetAt(0);
		    for (Row row : sheet1) {
		      for (Cell cell : row) {
		    	  content += getValue(cell)+"\t";
		      }
		      content+="\n";
		    }
			return content;
		}else
			return "null";
	}
	
	public String getValue(Cell cell){
		if(cell != null){
			if(cell.getCellType() == cell.CELL_TYPE_BOOLEAN){
				return ""+cell.getBooleanCellValue();
			}else if(cell.getCellType() == cell.CELL_TYPE_NUMERIC){
				return ""+cell.getNumericCellValue();
			}else if(cell.getCellType() == cell.CELL_TYPE_STRING){
				return ""+cell.getStringCellValue();
			}else {
				return ""+cell.getNumericCellValue();
			}
		}else{
			System.out.println("cell is null");
			return null;
		}
		
	}

}
