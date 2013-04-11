package de.kiwiwings.jasperreports.exporter;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import de.kiwiwings.jasperreports.exporter.customizer.ChartFakeProtect;
import de.kiwiwings.jasperreports.exporter.customizer.ChartGlassPane;
import de.kiwiwings.jasperreports.exporter.customizer.ChartTextLifter;
import de.kiwiwings.jasperreports.exporter.customizer.SheetCustomizer;

// to find missing classes from ooxml-schemas-1.1
// run java with jvm parameter -verbose:class

public class JasperTest {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
//		ClassLoader cl = JasperTest.class.getClassLoader();
//		File xlsFile = new File(cl.getResource("npstest.xls").toURI());
//		File jrxmlFile = new File(cl.getResource("jaspertest.jrxml").toURI());
		File xlsFile = new File("src/test/resources/npstest.xls");
		File jrxmlFile = new File("src/test/resources/jaspertest1.jrxml");
		
		
		
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		String sqlurl = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};"+
				"DBQ="+xlsFile.getCanonicalPath();
		Connection lCon = DriverManager.getConnection(sqlurl);

		if (false) testXls(lCon);

		Map<String,Object> hm = new HashMap<String, Object>();
		JasperReport jrep = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());
		JasperPrint jprint = JasperFillManager.fillReport(jrep, hm, lCon);

		FileOutputStream fos = new FileOutputStream("jasperdebug.pptx");
		JRAbstractExporter exporter = new PptxShapeExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jprint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);
//		exporter.setParameter(PptxShapeExporter.USE_GLASS_PANE, Boolean.TRUE);
		exporter.setParameter(PptxShapeExporter.SHEET_CUSTOMIZER,
			new SheetCustomizer[]{ new ChartTextLifter(), new ChartFakeProtect(), new ChartGlassPane() });
		exporter.exportReport();
		fos.close();
		
	}
	
	private static void testXls(Connection lCon) throws Exception {
      Statement lStmt = lCon.createStatement();
      ResultSet lRs = lStmt.executeQuery("select * from [NPS$]");
      ResultSetMetaData rsmd = lRs.getMetaData();
      int ccnt = rsmd.getColumnCount();
		for (int i=1; i<=ccnt; i++) {
			System.out.println(rsmd.getColumnName(i));
		}
      lStmt.close();		
	}
}
