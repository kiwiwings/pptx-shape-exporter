package de.kiwiwings.jasperreports.exporter;

import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	protected int slideNum;
	protected final Pattern fieldPattern;

	
	protected PptxShapeTextHelper(
		  JRPrintText text
		, JRStyledText styledText
		, int offsetX
		, int offsetY
		, Locale locale
		, String invalidCharReplacement
		, FontResolver fontResolver
		, int slideNum
	) {
		this.text = text;
		this.styledText = styledText;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.locale = locale;
		this.invalidCharReplacement = invalidCharReplacement;
		this.fontResolver = fontResolver;
		this.slideNum = slideNum;
		this.fieldPattern = Pattern.compile("\\{fld:([^}]+)\\}");
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
	
	protected abstract void addField(String type);
	
	/*
	 * Request the text field content
	 * In case of modified content a new AttributedString is returned and
	 * the StringBuffer is always filled with the corresponding text
	 */
	protected AttributedString getAttributedString(StringBuffer strippedText) {
		strippedText.append(styledText.getText());
		return styledText.getAttributedString();
	}
	
	public void export() {
		StringBuffer strippedText = new StringBuffer();
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, true);
		AttributedCharacterIterator aci = getAttributedString(strippedText).getIterator();
		
		LineBreakMeasurer measurer = new LineBreakMeasurer(aci, frc);
		float wrappingWidth = (float)getBounds().getWidth();
		switch (text.getRotationValue()) {
			// TODO: wrappingWidth is still mostly too small
			case LEFT:
			case RIGHT: wrappingWidth += 25f; break;
			default:
		}

		for (int last=0;last < strippedText.length();) {
			if (last > 0) addLineBreak();

			int next = (useLineBreakMeasurer()) 
					? Math.min(measurer.nextOffset(wrappingWidth),strippedText.length())
					: strippedText.length();

			while (last < next) {
				boolean addNewLine = false;
				aci.setIndex(last);
				int newline = strippedText.indexOf("\n", last);
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

				String fragment = strippedText.substring(last, limit);
				exportFragment(fragment, aci.getAttributes());

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

	protected void exportFragment(String fragment, Map<Attribute,Object> attributes) {
		Pattern fldPat = getFieldPattern();
		StringBuffer sb = new StringBuffer();
		Matcher m = fldPat.matcher(fragment);
		while (m.find()) {
			String field = m.group(1);
			sb.setLength(0);
			m.appendReplacement(sb, "");
			if (sb.length()>0) {
				addTextRun();
				setAttributes(attributes);
				setText(sb.toString());
			}
			addField(field);
			setAttributes(attributes);
		}
		
		sb.setLength(0);
		m.appendTail(sb);
		if (sb.length()>0) {
			addTextRun();
			setAttributes(attributes);
			setText(sb.toString());
		}
	}
	
	protected Pattern getFieldPattern() {
		return fieldPattern;
	}
}
