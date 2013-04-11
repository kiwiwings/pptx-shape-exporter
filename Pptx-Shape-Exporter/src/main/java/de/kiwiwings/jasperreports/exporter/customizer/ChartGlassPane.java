package de.kiwiwings.jasperreports.exporter.customizer;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSheet;

import de.kiwiwings.jasperreports.exporter.PptxGraphics2D;

/**
 * Another chart fake protection
 * A nearly transparent shape in front of the other chart shapes
 * disables text editing
 */
public class ChartGlassPane implements SheetCustomizer {

	public void customize(XSLFSheet sheet) {
		XSLFShape topShapeList[] = sheet.getShapes();
		
		for (XSLFShape topShape : topShapeList) {
			if (!topShape.getShapeName().startsWith("JFreeChart")) continue;
			
			XSLFGroupShape jfree = (XSLFGroupShape)topShape;
			
			Rectangle2D rect = jfree.getAnchor().getBounds2D();

			PptxGraphics2D gctx = new PptxGraphics2D(null, sheet, jfree);
			gctx.setPaint(new Color(0x01ffffff,true));
			gctx.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		}
	}

}
