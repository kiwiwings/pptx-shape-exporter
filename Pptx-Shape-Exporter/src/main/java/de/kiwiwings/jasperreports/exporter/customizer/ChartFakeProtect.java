package de.kiwiwings.jasperreports.exporter.customizer;

import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSheet;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupLocking;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeLocking;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;

/**
 * This customizer sets the shape locks, so the chart content
 * - especially the text elements - can't be changed. 
 */
public class ChartFakeProtect implements SheetCustomizer {
	public void customize(XSLFSheet sheet) {
		XSLFShape topShapeList[] = sheet.getShapes();
		
		for (XSLFShape topShape : topShapeList) {
			if (!topShape.getShapeName().startsWith("JFreeChart")) continue;
			
			CTGroupLocking grpLocks = ((CTGroupShape)topShape.getXmlObject()).getNvGrpSpPr().getCNvGrpSpPr().addNewGrpSpLocks();
			grpLocks.setNoUngrp(true);

			XSLFShape shapeList[] = ((XSLFGroupShape)topShape).getShapes();
			for (XSLFShape shape : shapeList) {
				if (!(shape instanceof XSLFTextBox)) continue;
				CTShape xshape = (CTShape)shape.getXmlObject();
				CTShapeLocking spLock = xshape.getNvSpPr().getCNvSpPr().addNewSpLocks();
				spLock.setNoSelect(true);
			}
		}
	}
}
