package de.kiwiwings.jasperreports.exporter;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import de.kiwiwings.jasperreports.exporter.customizer.ChartFakeProtect;
import de.kiwiwings.jasperreports.exporter.customizer.ChartGlassPane;
import de.kiwiwings.jasperreports.exporter.customizer.ChartTextLifter;
import de.kiwiwings.jasperreports.exporter.customizer.SheetCustomizer;

// to find missing classes from ooxml-schemas-1.1
// run java with jvm parameter -verbose:class

public class JasperTest {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		File csvFile = new File("src/test/resources/example.csv");
		File jrxmlFile = new File("src/test/resources/example.jrxml");

		JRCsvDataSource dataSource = new JRCsvDataSource(csvFile);
		dataSource.setRecordDelimiter("\r\n");
		dataSource.setFieldDelimiter('\t');
		dataSource.setUseFirstRowAsHeader(true);
		
		FileInputStream fis = new FileInputStream(jrxmlFile);
		JasperReport jrep = JasperCompileManager.compileReport(fis);
		fis.close();
		
		Map<String,Object> hm = new HashMap<String, Object>();
		JasperPrint jprint = JasperFillManager.fillReport(jrep, hm, dataSource);

		PptxShapeExporter exporter = new PptxShapeExporter();
		SimplePptxShapeReportConfiguration configuration = new SimplePptxShapeReportConfiguration();
		configuration.setHasBackground(jrep.getBackground() != null);
//		configuration.setSheetCustomizer(
//			new SheetCustomizer[]{ new ChartTextLifter(), new ChartFakeProtect(), new ChartGlassPane() });
		
		exporter.setConfiguration(configuration);
		exporter.setExporterInput(new SimpleExporterInput(jprint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("jasperdebug.pptx"));
		
		exporter.exportReport();
	}
}
