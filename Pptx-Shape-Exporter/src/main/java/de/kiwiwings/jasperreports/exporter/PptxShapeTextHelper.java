package de.kiwiwings.jasperreports.exporter;

import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFShape;

public abstract class PptxShapeTextHelper {
	protected JRPrintText text;
	protected JRStyledText styledText;
	protected int offsetX, offsetY;
	protected XSLFShape shape;
	protected XSLFAutoShape shapeBox;
	protected Locale locale;
	protected String invalidCharReplacement;
	protected FontResolver fontResolver;

	
	protected PptxShapeTextHelper(
		  JRPrintText text
		, JRStyledText styledText
		, int offsetX
		, int offsetY
		, Locale locale
		, String invalidCharReplacement
		, FontResolver fontResolver
	) {
		this.text = text;
		this.styledText = styledText;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.locale = locale;
		this.invalidCharReplacement = invalidCharReplacement;
		this.fontResolver = fontResolver;
	}
	
	public int getRotation() {
		switch (text.getRotationValue()) {
			default:
			case NONE: return 0;
			case LEFT: return -90;
			case RIGHT: return 90;
			case UPSIDE_DOWN: return 180;
		}
	}

	public Rectangle2D getBounds() {
		switch (text.getRotationValue()) {
			default:
			case NONE: 
			case UPSIDE_DOWN:
				return new Rectangle2D.Double(
					  text.getX() + offsetX
					, text.getY() + offsetY
					, text.getWidth()
					, text.getHeight()
				);
			case LEFT:
			case RIGHT:
				return new Rectangle2D.Double(
					  text.getX() + offsetX - (text.getHeight() - text.getWidth()) / 2
					, text.getY() + offsetY + (text.getHeight() - text.getWidth()) / 2
					, text.getHeight()
					, text.getWidth()
				);
		}
	}

	public Insets getInsets() {
 		switch (text.getRotationValue()) {
	 		default:
			case NONE:
				return new Insets(
					  text.getLineBox().getTopPadding()
					, text.getLineBox().getLeftPadding()
					, text.getLineBox().getBottomPadding()
					, text.getLineBox().getRightPadding()
				);
			case LEFT:
				return new Insets(
					  text.getLineBox().getLeftPadding()
					, text.getLineBox().getBottomPadding()
					, text.getLineBox().getRightPadding()
					, text.getLineBox().getTopPadding()
				);
			case RIGHT:
				return new Insets(
					  text.getLineBox().getRightPadding()
					, text.getLineBox().getTopPadding()
					, text.getLineBox().getLeftPadding()
					, text.getLineBox().getBottomPadding()
				);
			case UPSIDE_DOWN:
				return new Insets(
					  text.getLineBox().getBottomPadding()
					, text.getLineBox().getRightPadding()
					, text.getLineBox().getTopPadding()
					, text.getLineBox().getLeftPadding()
				);
		}
	}
	
	protected void initShape(XSLFShape shape, XSLFAutoShape shapeBox) {
		this.shape = shape;
		this.shapeBox = shapeBox;
	}
	
	public XSLFShape getShape() {
		return shape;
	}

	public XSLFAutoShape getShapeBox() {
		return shapeBox;
	}

	protected abstract void addLineBreak();

	protected abstract void addTextRun();
	
	protected abstract void setAttributes(Map<Attribute,Object> attr);

	protected abstract void setText(String text);

	protected abstract boolean useLineBreakMeasurer(); 
	
	public void export() {
		String plain = styledText.getText();
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, true);
		AttributedCharacterIterator aci = styledText.getAttributedString().getIterator();

		LineBreakMeasurer measurer = new LineBreakMeasurer(aci, frc);
		float wrappingWidth = (float)getBounds().getWidth();
		switch (text.getRotationValue()) {
			case LEFT:
			case RIGHT: wrappingWidth += 25f; break;
			default:
		}

		for (int last=0;last < plain.length();) {
			if (last > 0) addLineBreak();

			int next = (useLineBreakMeasurer()) 
					? Math.min(measurer.nextOffset(wrappingWidth),plain.length())
					: plain.length();

			while (last < next) {
				boolean addNewLine = false;
				aci.setIndex(last);
				int newline = plain.indexOf('\n', last);
				if (newline == -1) newline = Integer.MAX_VALUE;
				int aciLimit = aci.getRunLimit();

				int limit;
				if (aciLimit < newline) {
					limit = Math.min(aciLimit,next);
				} else if (newline < next) {
					limit = newline;
					addNewLine = true;
				} else {
					limit = next;
				}

				addTextRun();
				setAttributes(aci.getAttributes());
				setText(plain.substring(last, limit));

				if (addNewLine)	{
					next = ++limit;
					break;
				}
				last = limit;
			}
			
			measurer.nextLayout(wrappingWidth, next, false);
			last = next;
		}
	}
}
