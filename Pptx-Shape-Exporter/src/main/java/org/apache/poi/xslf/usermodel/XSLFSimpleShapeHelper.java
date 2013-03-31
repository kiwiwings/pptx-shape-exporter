package org.apache.poi.xslf.usermodel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import org.apache.poi.util.Units;
import org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;

public class XSLFSimpleShapeHelper extends XSLFSimpleShape {
	XSLFSimpleShapeHelper() {
		super(null, null);
	}

	public static void setFillColorAlpha(XSLFSimpleShape that, Color color, Composite composite) {
		that.setFillColor(color);
		double alpha = getAlpha(color, composite);
		if (alpha < 1) {
			CTShapeProperties spPr = that.getSpPr();
			CTPositiveFixedPercentage pct = spPr.getSolidFill().getSrgbClr().addNewAlpha();
			pct.setVal((int) (alpha * 100000d));
		}
	}

	// returns alpha value between 0 and 1, where 1 is opaque (= no transparancy)
	public static double getAlpha(Color color, Composite composite) {
		int trans = color.getTransparency();
		if ((trans == Color.TRANSLUCENT || trans == Color.BITMASK)) {
			return color.getAlpha() / 255d;
		}
		if (composite instanceof AlphaComposite) {
			AlphaComposite ac = (AlphaComposite) composite;
			return ac.getAlpha();
		}
		return 1d;
	}
	 
	public static void setLineColorAlpha(XSLFSimpleShape that, Color color, Composite composite) {
		that.setLineColor(color);
		double alpha = getAlpha(color, composite);
		if (alpha < 1) {
			CTShapeProperties spPr = that.getSpPr();
			CTPositiveFixedPercentage pct = spPr.getLn().getSolidFill().getSrgbClr().addNewAlpha();
			pct.setVal((int) (alpha * 100000d));
		}
	}
	 
	public static void setLineDashArray(XSLFSimpleShape that, BasicStroke stroke) {
		// float da[] = stroke.getDashArray();

		// Office 2003 doesn't support customized dash lists
		// so we need to emulate it for typical values ...
		// to be continued ...
		// if (da.length == 2 && da[0] == 2 && (da[1] == 2 || da[1] == 3)) {
		that.setLineDash(LineDash.DASH);

		// The following is commented out, so we don't need to include the 10mb
		// ooxml-schemas.jar
		// TODO: extract the 4 Dash-Xml-classes from ooxml-schemas

		// } else {
		// CTLineProperties lnpr = that.getSpPr().getLn();
		// if (lnpr.isSetPrstDash()) lnpr.unsetPrstDash();
		// CTDashStopList list = lnpr.addNewCustDash();
		// for (int i=0; i<=da.length-2; i+=2) {
		// CTDashStop ds = list.addNewDs();
		// ds.setD((int)da[i+0]);
		// ds.setSp((int)da[i+1]);
		// }
		// }
	}

	public static void rotate(XSLFSimpleShape that, double angle) {
		int ooAngle = (int) (angle * 60000);
		that.getSpPr().getXfrm().setRot(ooAngle);
	}

	// libre office fix, when pathlist has width/height of 0
	public static void fixBounds(XSLFFreeformShape that) {
		for (CTPath2D xpath : that.getSpPr().getCustGeom().getPathLst().getPathList()) {
			if (xpath.getH() == 0) xpath.setH(1);
			if (xpath.getW() == 0) xpath.setW(1);
		}
	}

	/**
	 * Set the shape path
	 * 
	 * @param path
	 *            shape outline
	 * @return the number of points written
	 */
	public static int setPath(XSLFFreeformShape that, GeneralPath path) {
		CTPath2D ctPath = CTPath2D.Factory.newInstance();

		Rectangle2D bounds = path.getBounds2D();
		int x0 = Units.toEMU(bounds.getX());
		int y0 = Units.toEMU(bounds.getY());
		PathIterator it = path.getPathIterator(new AffineTransform());
		int numPoints = 0;
		ctPath.setH(Units.toEMU(bounds.getHeight()));
		ctPath.setW(Units.toEMU(bounds.getWidth()));
		while (!it.isDone()) {
			double[] vals = new double[6];
			int type = it.currentSegment(vals);
	
			switch (type) {
			case PathIterator.SEG_MOVETO: {
				CTAdjPoint2D mv = ctPath.addNewMoveTo().addNewPt();
				mv.setX(Units.toEMU(vals[0]) - x0);
				mv.setY(Units.toEMU(vals[1]) - y0);
				numPoints++;
				break;
			}
			case PathIterator.SEG_LINETO: {
				CTAdjPoint2D ln = ctPath.addNewLnTo().addNewPt();
				ln.setX(Units.toEMU(vals[0]) - x0);
				ln.setY(Units.toEMU(vals[1]) - y0);
				numPoints++;
				break;
			}
			case PathIterator.SEG_CUBICTO: {
				CTPath2DCubicBezierTo bez = ctPath.addNewCubicBezTo();
				CTAdjPoint2D p1 = bez.addNewPt();
				p1.setX(Units.toEMU(vals[0]) - x0);
				p1.setY(Units.toEMU(vals[1]) - y0);
				CTAdjPoint2D p2 = bez.addNewPt();
				p2.setX(Units.toEMU(vals[2]) - x0);
				p2.setY(Units.toEMU(vals[3]) - y0);
				CTAdjPoint2D p3 = bez.addNewPt();
				p3.setX(Units.toEMU(vals[4]) - x0);
				p3.setY(Units.toEMU(vals[5]) - y0);
				numPoints += 3;
				break;
			}
			case PathIterator.SEG_QUADTO: {
				CTPath2DQuadBezierTo quad = ctPath.addNewQuadBezTo();
				CTAdjPoint2D p1 = quad.addNewPt();
				p1.setX(Units.toEMU(vals[0]) - x0);
				p1.setY(Units.toEMU(vals[1]) - y0);
				CTAdjPoint2D p2 = quad.addNewPt();
				p2.setX(Units.toEMU(vals[2]) - x0);
				p2.setY(Units.toEMU(vals[3]) - y0);
				numPoints += 2;
				break;
			}
			case PathIterator.SEG_CLOSE: {
				numPoints++;
				ctPath.addNewClose();
				break;
			}
			}
			it.next();
		}
		that.getSpPr().getCustGeom().getPathLst().setPathArray(new CTPath2D[] { ctPath });
		that.setAnchor(bounds);
		return numPoints;
	}    

	public static CTHyperlink addHyperlink(XSLFShape shape) {
		if (shape instanceof XSLFPictureShape) {
			return ((XSLFPictureShape)shape).getNvPr().addNewHlinkClick();
		}
		if (shape instanceof XSLFGroupShape) {
			return ((XSLFGroupShape)shape).getXmlObject().getNvGrpSpPr().getCNvPr().addNewHlinkClick();
		}
		if (shape instanceof XSLFSimpleShape) {
			return ((XSLFSimpleShape)shape).getNvPr().addNewHlinkClick();
		}
		return null;
	}
}