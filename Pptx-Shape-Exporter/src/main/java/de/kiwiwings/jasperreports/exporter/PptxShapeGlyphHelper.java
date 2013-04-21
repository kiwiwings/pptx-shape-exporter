package de.kiwiwings.jasperreports.exporter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
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
		  JRPrintText text
		, JRStyledText styledText
		, int offsetX
		, int offsetY
		, Locale locale
		, String invalidCharReplacement
		, XSLFSheet slide
		, FontResolver fontResolver
		, int slideNum
	) {
		super(text, styledText, offsetX, offsetY, locale, invalidCharReplacement, fontResolver, slideNum);
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

	/*
	 * Replace fields with the calculated text fragments 
	 */
	protected AttributedString getAttributedString(StringBuffer strippedText) {
		// taken from http://forums.devx.com/showthread.php?152271-Substring-replacement-on-AttributedString
		Pattern myPattern = getFieldPattern();
		AttributedString as = super.getAttributedString(strippedText);
		if (!myPattern.matcher(strippedText).find()) {
			return as;
		}

		List<Map<Attribute,Object>> attList = new ArrayList<Map<Attribute,Object>>();
		List<int[]> indexList = new ArrayList<int[]>();
		StringBuffer sb = new StringBuffer();
		
		AttributedCharacterIterator aci = as.getIterator();
		for (int begin=0, next=0; begin < strippedText.length(); begin = next+1) {
			aci.setIndex(begin);
			next = aci.getRunLimit();
			attList.add(aci.getAttributes());
			int startIdx = sb.length();
			
			Matcher m = myPattern.matcher(strippedText.substring(begin, next));
			while (m.find()) {
				String field = m.group(1);
				String value = "";
				if ("slidenum".equals(field)) {
					value = Integer.toString(slideNum+1);
				}
				m.appendReplacement(sb, value);
			}
			m.appendTail(sb);
			
			indexList.add(new int[]{startIdx,sb.length()});
		}
		
		
		AttributedString newAs = new AttributedString(sb.toString());
		for (int i=0; i<attList.size(); i++) {
			int index[] = indexList.get(i);
			newAs.addAttributes(attList.get(i), index[0], index[1]);
		}

		strippedText.replace(0, strippedText.length(), sb.toString());
		
		return newAs;
	}
	
	protected void addField(String type) {
		// fields have been already replaced
	}
}

