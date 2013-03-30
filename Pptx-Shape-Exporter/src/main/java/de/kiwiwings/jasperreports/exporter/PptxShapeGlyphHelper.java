package de.kiwiwings.jasperreports.exporter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFSheet;
import org.jfree.text.TextBlock;
import org.jfree.text.TextBlockAnchor;
import org.jfree.text.TextFragment;
import org.jfree.text.TextLine;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.Size2D;
import org.jfree.ui.TextAnchor;


public class PptxShapeGlyphHelper extends PptxShapeTextHelper {
	@SuppressWarnings("serial")
	static class MyTextBlock extends TextBlock {
		double lineSpacing = 1d;

	    public double getLineSpacing() {
			return lineSpacing;
		}

		public void setLineSpacing(double lineSpacing) {
			this.lineSpacing = lineSpacing;
		}

		/**
	     * Returns the width and height of the text block.
	     * 
	     * @param g2  the graphics device.
	     * 
	     * @return The width and height.
	     */
	    public Size2D calculateDimensions(final Graphics2D g2) {
	        double width = 0.0;
	        double height = 0.0;
	        @SuppressWarnings("unchecked")
			final Iterator<TextLine> iterator = getLines().iterator();
	        while (iterator.hasNext()) {
	            final TextLine line = iterator.next();
	            final Size2D dimension = line.calculateDimensions(g2);
	            width = Math.max(width, dimension.getWidth());
	            height = height + dimension.getHeight()*lineSpacing;
	        }
	        return new Size2D(width, height);
	    }
	    
	    /**
	     * Draws the text block, aligning it with the specified anchor point and 
	     * rotating it about the specified rotation point.
	     * 
	     * @param g2  the graphics device.
	     * @param anchorX  the x-coordinate for the anchor point.
	     * @param anchorY  the y-coordinate for the anchor point.
	     * @param anchor  the point on the text block that is aligned to the 
	     *                anchor point.
	     * @param rotateX  the x-coordinate for the rotation point.
	     * @param rotateY  the x-coordinate for the rotation point.
	     * @param angle  the rotation (in radians).
	     */
	    public void draw(final Graphics2D g2,
	                     final float anchorX, final float anchorY, 
	                     final TextBlockAnchor anchor,
	                     final float rotateX, final float rotateY, 
	                     final double angle) {
	    
	        final Size2D d = calculateDimensions(g2);
	        
	        final float[] offsets;
	        try {
		        Method m = TextBlock.class.getDeclaredMethod("calculateOffsets"
		        		, new Class<?>[]{TextBlockAnchor.class,Double.TYPE,Double.TYPE});
		        m.setAccessible(true);
		        offsets = (float[])m.invoke(this, anchor, d.getWidth(), d.getHeight());
	        } catch (Exception e) {
	        	e.printStackTrace(System.err);
	        	return;
	        }
	        
	        
	        @SuppressWarnings("unchecked")
			final Iterator<TextLine> iterator = getLines().iterator();
	        float yCursor = 0.0f;
	        while (iterator.hasNext()) {
	            TextLine line = (TextLine) iterator.next();
	            Size2D dimension = line.calculateDimensions(g2);
	            float lineOffset = 0.0f;
	            if (getLineAlignment() == HorizontalAlignment.CENTER) {
	                lineOffset = (float) (d.getWidth() - dimension.getWidth()) 
	                    / 2.0f;   
	            }
	            else if (getLineAlignment() == HorizontalAlignment.RIGHT) {
	                lineOffset = (float) (d.getWidth() - dimension.getWidth());   
	            }
	            line.draw(
	                g2, anchorX + offsets[0] + lineOffset, anchorY + offsets[1] + yCursor,
	                TextAnchor.TOP_LEFT, rotateX, rotateY, angle
	            );
	            yCursor = yCursor + (float)(dimension.getHeight()*lineSpacing);
	        }
	        
	    }
	}
	
	protected PptxGraphics2D gctx;
	protected MyTextBlock textBox;
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
		initShape(gctx.getGroup());
		textBox = new MyTextBlock();
		textPara = new TextLine();
		textBox.addLine(textPara);
		
		switch (text.getHorizontalAlignmentValue()) {
			default:
			case JUSTIFIED:
			case LEFT: textBox.setLineAlignment(HorizontalAlignment.LEFT); break;
			case CENTER: textBox.setLineAlignment(HorizontalAlignment.CENTER); break;
			case RIGHT: textBox.setLineAlignment(HorizontalAlignment.RIGHT); break;
		}
		
		textBox.setLineSpacing(text.getLineSpacingFactor());

		gctx.getGroup().setRotation(getRotation());
		
		if (text.getModeValue() == null || text.getModeValue() == ModeEnum.OPAQUE) {
			// TODO: extend TextBlock.draw to draw background box
			Rectangle2D rect = getBounds();
			gctx.setPaint(text.getBackcolor());
			gctx.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		}
	}

	public void export() {
		super.export();
		Rectangle2D rect = getBounds();
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

		// Super-/SubScript can't be sent by style object 
		
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

