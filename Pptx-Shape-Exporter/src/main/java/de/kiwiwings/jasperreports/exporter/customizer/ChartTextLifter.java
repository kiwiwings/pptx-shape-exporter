package de.kiwiwings.jasperreports.exporter.customizer;

import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSheet;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;

/**
 * This customizer will raise all text elements of JFreeChart groups,
 * so they won't be (partly) hidden by other shapes 
 */

public class ChartTextLifter implements SheetCustomizer {
	public void customize(XSLFSheet sheet) {
		// use workaround for complex xpath expression, otherwise saxon would be needed
		// selectPath("//p:grpSp[starts-with(p:nvGrpSpPr/p:cNvPr/@name,'JFreeChart')]");
		XSLFShape shapeList[] = sheet.getShapes();
				
		for (XSLFShape shape : shapeList) {
			if (!shape.getShapeName().startsWith("JFreeChart")) continue;
			XmlCursor curEnd = shape.getXmlObject().newCursor();
			curEnd.toEndToken();

			String firstMovedId = null;
			XmlCursor curNext = shape.getXmlObject().newCursor();
			curNext.selectPath("declare namespace p='"+PML_NS+"' ./p:sp");
			
			while (curNext.toNextSelection()) {
				CTShape cts = (CTShape)curNext.getObject();
				String shapeName = cts.getNvSpPr().getCNvPr().getName();
				
				if (shapeName.startsWith("TextBox")) {
					if (firstMovedId == null) firstMovedId = shapeName;
					else if (firstMovedId.equals(shapeName)) break;

					curNext.moveXml(curEnd);
					curNext.toPrevSibling();
				}
			}
			
			curEnd.dispose();
			curNext.dispose();
		}

		// the cursor manipulations have changed the order of the shapes,
		// therefore we need to update the cached poi shape informations
		sheet.importContent(sheet);
	}
}
