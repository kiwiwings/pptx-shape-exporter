package de.kiwiwings.jasperreports.exporter.customizer;

import org.apache.poi.xslf.usermodel.XSLFSheet;

public interface SheetCustomizer {
	static final String PML_NS = "http://schemas.openxmlformats.org/presentationml/2006/main";
	
	void customize(XSLFSheet sheet);
}
