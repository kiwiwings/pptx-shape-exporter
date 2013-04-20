package de.kiwiwings.jasperreports.exporter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFSheet;
import org.jfree.text.TextBlockAnchor;
import org.jfree.text.TextFragment;
import org.jfree.text.TextLine;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.Size2D;
import org.jfree.ui.VerticalAlignment;


public class PptxShapeGlyphHelper extends PptxShapeTextHelper {
	protected PptxGraphics2D gctx;
	protected PptxTextBlock textBox;
	protected TextLine textPara;
	protected TextFragment textRun;
	protected Font font;
	protected Paint paint;
	protected float baselineOffset = 0f;
	
	public PptxShapeGlyphHelper(
		  JasperReportsContext jasperReportsContext
		, JRPrintText text
		, JRStyledText styledText
		, int offsetX
		, int offsetY
		, Locale locale
		, String invalidCharReplacement
		, XSLFSheet slide
		, FontResolver fontResolver
	) {
		super(jasperReportsContext, text, styledText, offsetX, offsetY, locale, invalidCharReplacement, fontResolver);
		gctx = new PptxGraphics2D(getBounds(), fontResolver, slide);
		initShape(gctx.getShapeGroup());
		textBox = new PptxTextBlock();
		textPara = new TextLine();
		textBox.addLine(textPara);
		
		switch (text.getHorizontalAlignmentValue()) {
			default:
			case JUSTIFIED: /* justified .. not implemented yet */
			case LEFT: textBox.setLineAlignment(HorizontalAlignment.LEFT); break;
			case CENTER: textBox.setLineAlignment(HorizontalAlignment.CENTER); break;
			case RIGHT: textBox.setLineAlignment(HorizontalAlignment.RIGHT); break;
		}
		
		textBox.setLineSpacing(text.getLineSpacingFactor());
		
		switch (text.getVerticalAlignmentValue()) {
			default:
			case TOP: textBox.setBlockAlignment(VerticalAlignment.TOP); break;
			case MIDDLE: textBox.setBlockAlignment(VerticalAlignment.CENTER); break;
			case BOTTOM: textBox.setBlockAlignment(VerticalAlignment.BOTTOM); break;
		}

		gctx.getShapeGroup().setRotation(getRotation());
	}

	public void export() {
		super.export();
		Rectangle2D rect = getBounds();
		textBox.setLimits(new Size2D(rect.getWidth(), rect.getHeight()));
		gctx.setLocale(locale);
		textBox.draw(gctx, (float)rect.getX(), (float)rect.getY(), TextBlockAnchor.TOP_LEFT);
	}
	
	protected void initShape(XSLFGroupShape shape) {
		XSLFAutoShape shapeBox = PptxGraphics2D.getShape(getBounds(), shape);
		super.initShape(shape, shapeBox);
	}
	
	protected void addLineBreak() {
		textPara = new TextLine();
		textBox.addLine(textPara);
	}

	protected void addTextRun() {
	}
	
	protected void setText(String text) {
		textRun = new TextFragment(text, font, paint, baselineOffset);
		textPara.addFragment(textRun);
	}

	protected void setAttributes(Map<Attribute,Object> attr) {
		attr = new HashMap<Attribute,Object>(attr);
		
		JRStyle style = text.getStyle();
		
		if (!attr.containsKey(TextAttribute.FAMILY) && style != null && style.getFontName() != null) {
			attr.put(TextAttribute.FAMILY, style.getFontName());
		}
		if (attr.containsKey(TextAttribute.FAMILY)) {
			String ff = fontResolver.exportFont((String)attr.get(TextAttribute.FAMILY), locale);
			attr.put(TextAttribute.FAMILY, ff);
		}
		

		if (!attr.containsKey(TextAttribute.SIZE) && style != null && style.getFontSize() != null) {
			attr.put(TextAttribute.SIZE, style.getFontSize());
		}
		if (attr.containsKey(TextAttribute.SIZE) && 0 == (Float)attr.get(TextAttribute.SIZE)) {
			// only the special EMPTY_CELL_STYLE would have font size zero
			attr.put(TextAttribute.SIZE, (Float)0.5f);
		}
		
		if (!attr.containsKey(TextAttribute.WEIGHT) && style != null && style.isBold() != null && style.isBold()) {
			attr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		}

		if (!attr.containsKey(TextAttribute.POSTURE) && style != null && style.isItalic() != null && style.isItalic()) {
			attr.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		}		

		if (!attr.containsKey(TextAttribute.UNDERLINE) && style != null && style.isUnderline() != null && style.isUnderline()) {
			attr.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		}		
		
		if (!attr.containsKey(TextAttribute.STRIKETHROUGH) && style != null && style.isStrikeThrough() != null && style.isStrikeThrough()) {
			attr.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		}		

		// Super-/SubScript can't be set by style object 
		
		font = fontResolver.loadFont(attr, locale);

		paint = Color.BLACK;
		if (attr.containsKey(TextAttribute.FOREGROUND)) {
			paint = (Color)attr.get(TextAttribute.FOREGROUND);
		} else if (style != null) {
			paint = style.getForecolor();
		}
	}

	protected boolean useLineBreakMeasurer() {
		return true;
	}
}

