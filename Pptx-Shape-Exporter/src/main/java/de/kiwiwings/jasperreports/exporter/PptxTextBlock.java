package de.kiwiwings.jasperreports.exporter;

import java.awt.Graphics2D;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.jfree.text.TextBlock;
import org.jfree.text.TextBlockAnchor;
import org.jfree.text.TextLine;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.Size2D;
import org.jfree.ui.TextAnchor;
import org.jfree.ui.VerticalAlignment;

@SuppressWarnings("serial")
public class PptxTextBlock extends TextBlock {
	double lineSpacing = 1d;
	VerticalAlignment blockAlignment = VerticalAlignment.TOP;
	Size2D limits = null;

    public double getLineSpacing() {
		return lineSpacing;
	}

	public void setLineSpacing(double lineSpacing) {
		this.lineSpacing = lineSpacing;
	}
	
	public VerticalAlignment getBlockAlignment() {
		return blockAlignment;
	}

	public void setBlockAlignment(VerticalAlignment blockAlignment) {
		this.blockAlignment = blockAlignment;
	}
	
	public void setLimits(Size2D limits) {
		this.limits = limits;
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
            height += dimension.getHeight()*(iterator.hasNext()?lineSpacing:1f);
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
                     final float anchorX, float anchorY, 
                     final TextBlockAnchor anchor,
                     final float rotateX, final float rotateY, 
                     final double angle) {
    
        final Size2D d = calculateDimensions(g2);
        if (limits != null) {
        	// can't use offsets for bounding box anchor (= top left)
        	if (VerticalAlignment.CENTER.equals(blockAlignment)) {
        		anchorY += (float)Math.max(0, (limits.height-d.height)/2.0f);
        	} else if (VerticalAlignment.BOTTOM.equals(blockAlignment)) {
        		anchorY += (float)Math.max(0, limits.height-d.height);
        	} // else ... do nothing 
        	
        	d.setHeight(Math.max(d.getHeight(), limits.getHeight()));
        	d.setWidth(Math.max(d.getWidth(), limits.getWidth()));
        }
        
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

