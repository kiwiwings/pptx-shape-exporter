package de.kiwiwings.jasperreports.exporter;

import java.awt.Color;
import java.awt.Insets;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.poi.xslf.usermodel.TextAlign;
import org.apache.poi.xslf.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XSLFSheet;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;


public class PptxShapeRunHelper extends PptxShapeTextHelper {
	
	protected XSLFTextBox textBox;
	protected XSLFTextParagraph textPara;
	protected XSLFTextRun textRun;
	
	
	public PptxShapeRunHelper(
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
		textBox = slide.createTextBox();
		textBox.setAnchor(this.getBounds());
		textPara = textBox.addNewTextParagraph();

		Insets in = getInsets();
		
		textBox.setTopInset(in.top);
		textBox.setRightInset(in.right);
		textBox.setBottomInset(in.bottom);
		textBox.setLeftInset(in.left);
		textBox.setAnchor(getBounds());
		textBox.setRotation(getRotation());
		
		switch (text.getVerticalAlignmentValue()) {
			default: 
			case TOP: textBox.setVerticalAlignment(VerticalAlignment.TOP); break;
			case MIDDLE: textBox.setVerticalAlignment(VerticalAlignment.MIDDLE); break;
			case BOTTOM: textBox.setVerticalAlignment(VerticalAlignment.BOTTOM); break;
		}
	
		switch (text.getHorizontalAlignmentValue()) {
			default:
			case LEFT: textPara.setTextAlign(TextAlign.LEFT); break;
			case CENTER: textPara.setTextAlign(TextAlign.CENTER); break;
			case RIGHT: textPara.setTextAlign(TextAlign.RIGHT); break;
			case JUSTIFIED: textPara.setTextAlign(TextAlign.JUSTIFY); break;
		}
		
		switch (text.getParagraph().getLineSpacing()) {
			default:
			case SINGLE: textPara.setLineSpacing(100); break;
			case DOUBLE: textPara.setLineSpacing(200); break;
			case ONE_AND_HALF: textPara.setLineSpacing(150); break;
		}
	}

	protected void addLineBreak() {
		textRun = textPara.addLineBreak();
	}

	protected void addTextRun() {
		textRun = textPara.addNewTextRun();
	}
	
	protected void setText(String text) {
		textRun.setText(text);
	}
	
	protected boolean useLineBreakMeasurer() {
		return false;
	}

	
	protected void setAttributes(Map<Attribute,Object> attr) {
		JRStyle style = text.getStyle();

		//FIXMEDOCX the text locale might be different from the report locale, resulting in different export font
		if (attr.containsKey(TextAttribute.FAMILY)) {
			String ff = fontResolver.exportFont((String)attr.get(TextAttribute.FAMILY), locale);
			textRun.setFontFamily(ff);
		} else if (style != null && style.getFontName() != null) {
			String ff = fontResolver.exportFont(style.getFontName(), locale);
			textRun.setFontFamily(ff);
		}

		if (attr.containsKey(TextAttribute.SIZE)) {
			textRun.setFontSize((Float)attr.get(TextAttribute.SIZE));
		} else if (style != null && style.getFontSize() != null) {
			textRun.setFontSize(style.getFontSize());
		}
		if (textRun.getFontSize() == 0) {
			// only the special EMPTY_CELL_STYLE would have font size zero
			textRun.setFontSize(0.5f);
		}

		if (attr.containsKey(TextAttribute.WEIGHT)
			&& TextAttribute.WEIGHT_BOLD.equals(attr.get(TextAttribute.WEIGHT))) {
			textRun.setBold(true);
		} else if (style != null && style.isBold() != null && style.isBold()) {
			textRun.setBold(true);
		}

		if (attr.containsKey(TextAttribute.POSTURE)
			&& TextAttribute.POSTURE_OBLIQUE.equals(attr.get(TextAttribute.POSTURE))) {
			textRun.setItalic(true);
		} else if (style != null && style.isItalic() != null && style.isItalic()) {
			textRun.setItalic(true);
		}

		if (attr.containsKey(TextAttribute.UNDERLINE)
			&& TextAttribute.UNDERLINE_ON.equals(attr.get(TextAttribute.UNDERLINE))) {
			textRun.setUnderline(true);
		} else if (style != null && style.isUnderline() != null && style.isUnderline()) {
			textRun.setUnderline(true);
		}

		if (attr.containsKey(TextAttribute.STRIKETHROUGH)
			&& TextAttribute.STRIKETHROUGH_ON.equals(attr.get(TextAttribute.STRIKETHROUGH))) {
			textRun.setStrikethrough(true);
		} else if (style != null && style.isStrikeThrough() != null && style.isStrikeThrough()) {
			textRun.setStrikethrough(true);
		}
		
		if (text.getModeValue() == null || text.getModeValue() == ModeEnum.OPAQUE) {
			textBox.setFillColor(text.getBackcolor());
		}
		
		if (attr.containsKey(TextAttribute.FOREGROUND)) {
			textRun.setFontColor((Color)attr.get(TextAttribute.FOREGROUND));
		} else if (style != null) {
			textRun.setFontColor(style.getForecolor());
		}

		if (attr.containsKey(TextAttribute.SUPERSCRIPT)) {
			Integer ss = (Integer)attr.get(TextAttribute.SUPERSCRIPT);
			if (TextAttribute.SUPERSCRIPT_SUPER.equals(ss)) {
				textRun.setSuperscript(true);
			} else if (TextAttribute.SUPERSCRIPT_SUB.equals(ss)) {
				textRun.setSubscript(true);
			}
		}
	}
}

