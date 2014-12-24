package de.kiwiwings.jasperreports.exporter;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.export.PptxReportConfiguration;
import net.sf.jasperreports.export.annotations.ExporterProperty;
import de.kiwiwings.jasperreports.exporter.customizer.SheetCustomizer;

public interface PptxShapeReportConfiguration extends PptxReportConfiguration {

	/**
	 * Property that provides a customizer for the slide/sheets
	 */
	public static final String PROPERTY_SHEET_CUSTOMIZER = JRPropertiesUtil.PROPERTY_PREFIX +  "export.pptx." + "sheet_customizer";
	
	/**
	 * Flag if a background band is in the Report
	 */
	public static final String PROPERTY_HAS_BACKGROUND = JRPropertiesUtil.PROPERTY_PREFIX +  "export.pptx." + "background";
	
	/**
	 * @see #PROPERTY_SHEET_CUSTOMIZER
	 */
	@ExporterProperty(
		value=PROPERTY_SHEET_CUSTOMIZER, 
		booleanDefault=false
	)
	public SheetCustomizer[] getSheetCustomizer();
	

	/**
	 * @see #PROPERTY_HAS_BACKGROUND
	 */
	@ExporterProperty(
		value=PROPERTY_HAS_BACKGROUND, 
		booleanDefault=true,
		intDefault=0
	)
	public boolean hasBackground();
}
