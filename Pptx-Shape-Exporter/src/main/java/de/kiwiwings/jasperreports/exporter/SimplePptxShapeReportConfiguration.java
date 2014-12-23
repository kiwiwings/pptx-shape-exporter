package de.kiwiwings.jasperreports.exporter;

import de.kiwiwings.jasperreports.exporter.customizer.SheetCustomizer;
import net.sf.jasperreports.export.SimplePptxReportConfiguration;

public class SimplePptxShapeReportConfiguration extends SimplePptxReportConfiguration implements PptxShapeReportConfiguration {

	private SheetCustomizer[] sheetCustomizer;
	
	@Override
	public SheetCustomizer[] getSheetCustomizer() {
		return sheetCustomizer;
	}

	public void setSheetCustomizer(SheetCustomizer[] sheetCustomizer) {
		this.sheetCustomizer = sheetCustomizer;
	}
}
