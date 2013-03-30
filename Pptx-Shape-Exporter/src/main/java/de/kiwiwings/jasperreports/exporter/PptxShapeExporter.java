/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package de.kiwiwings.jasperreports.exporter;

import java.awt.Dimension;
import java.awt.font.TextAttribute;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.charts.util.DrawChartRenderer;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRCommonElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextUtil;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.xslf.usermodel.LineDash;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFRelation;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSheet;
import org.apache.poi.xslf.usermodel.XSLFSimpleShape;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGroupShape;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPicture;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.tools.conversion.eot.EOTWriter;


/**
 * Exports a JasperReports document to PPTX format.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRPptxExporter.java 5527 2012-08-01 12:04:08Z shertage $
 */
public class PptxShapeExporter extends JRAbstractExporter implements FontResolver {
	private static final Log log = LogFactory.getLog(PptxShapeExporter.class);
	
	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String PPTX_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "pptx";
	
	protected static final String PPTX_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.pptx.";

	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	protected XMLSlideShow ppt;
	protected XSLFSheet slide;
	
	protected JRExportProgressMonitor progressMonitor;

	protected int reportIndex;
	protected int pageIndex;
	protected List<Integer> frameIndexStack;
	protected int elementIndex;
	protected boolean startPage;
	protected String invalidCharReplacement;
	
	
	/**
	 * @see #JRPptxExporter(JasperReportsContext)
	 */
	public PptxShapeExporter() {
		this(DefaultJasperReportsContext.getInstance());
	}

	
	/**
	 *
	 */
	public PptxShapeExporter(JasperReportsContext jasperReportsContext)	{
		super(jasperReportsContext);
	}
	

	/**
	 *
	 */
	public void exportReport() throws JRException {
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		setOffset();
		setExportContext();
		setHyperlinkProducerFactory();
		
		
		
		boolean closeStream = false;
		OutputStream os = null; 
		String excStr = "";
		try	{
			try {
				setInput();
				os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
				excStr = "Error trying to export to output stream : " + jasperPrint.getName();
	
				if (!parameters.containsKey(JRExporterParameter.FILTER)) {
					filter = createFilter(getExporterPropertiesPrefix());
				}
	
				if (!isModeBatch) setPageRange();
	
				File osFile = null;
				if (os == null) {
					osFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
					
					if (osFile == null) {
						String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
						if (fileName == null) {
							throw new JRException("No output specified for the exporter.");
						}
						osFile = new File(fileName);
					}
	
					excStr = "Error trying to export to file : " + osFile;
					os = new FileOutputStream(osFile);
					closeStream = true;
				}
				
				exportReportToStream(os);
			} finally {
				resetExportContext();
				if (closeStream) os.close();	
			}
		} catch (InvalidFormatException e) {
			throw new JRException(excStr, e);
		} catch (IOException e) {
			throw new JRException(excStr, e);
		}
	}


	/**
	 *
	 */
	protected void exportReportToStream(OutputStream os) throws JRException, IOException, InvalidFormatException {
		ppt = new XMLSlideShow();

		ppt.setPageSize(new Dimension(jasperPrint.getPageWidth(), jasperPrint.getPageHeight()));

		int nbrBackElem = exportBackground();
		
		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++) {
			setJasperPrint(jasperPrintList.get(reportIndex));
			setExporterHints();

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0) {
				if (isModeBatch) {
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				JRPrintPage page = null;
				for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
					if (Thread.interrupted()) {
						throw new JRException("Current thread interrupted.");
					}

					page = pages.get(pageIndex);

					createSlide(null);//FIXMEPPTX
					
					List<JRPrintElement> list = page.getElements();
					list = list.subList(nbrBackElem, list.size());
					page.setElements(list);
					
					exportPage(page);
				}
			}
		}

		embedFonts();
		
		ppt.write(os);
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException
	{
		frameIndexStack = new ArrayList<Integer>();

		exportElements(page.getElements());
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	protected void createSlide(String name) {
		slide = ppt.createSlide();
	}


	/**
	 *
	 */
	protected void exportElements(List<JRPrintElement> elements) throws JRException
	{
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element;
			for(int i = 0; i < elements.size(); i++)
			{
				elementIndex = i;
				
				element = elements.get(i);
				
				if (filter == null || filter.isToExport(element))
				{
					if (element instanceof JRPrintLine)
					{
						exportLine((JRPrintLine)element);
					}
					else if (element instanceof JRPrintRectangle)
					{
						exportRectangle((JRPrintRectangle)element);
					}
					else if (element instanceof JRPrintEllipse)
					{
						exportEllipse((JRPrintEllipse)element);
					}
					else if (element instanceof JRPrintImage)
					{
						exportImage((JRPrintImage)element);
					}
					else if (element instanceof JRPrintText)
					{
						exportText((JRPrintText)element);
					}
					else if (element instanceof JRPrintFrame)
					{
						exportFrame((JRPrintFrame)element);
					}
					else if (element instanceof JRGenericPrintElement)
					{
						exportGenericElement((JRGenericPrintElement) element);
					}
				}
			}
		}
	}

	protected void exportElementAttributes(JRCommonElement jrShape, JRPen jrPen, XSLFSimpleShape xsShape) {
		if (jrShape.getModeValue() == ModeEnum.OPAQUE && jrShape.getBackcolor() != null) {
			xsShape.setFillColor(jrShape.getBackcolor());
		}
		
		if (jrPen.getLineWidth() > 0) {
			xsShape.setLineWidth(jrPen.getLineWidth());
			xsShape.setLineColor(jrPen.getLineColor());
			switch (jrPen.getLineStyleValue()) {
				case DASHED: xsShape.setLineDash(LineDash.DASH); break;
				case DOTTED: xsShape.setLineDash(LineDash.DOT); break;
				default: xsShape.setLineDash(LineDash.SOLID); break;
			}
		}
	}
	
	
	/**
	 *
	 */
	protected void exportLine(JRPrintLine line) {
		int x1 = line.getX() + getOffsetX();
		int y1 = line.getY() + getOffsetY();
		int x2 = x1 + Math.max(line.getWidth(), 1);
		int y2 = y1 + Math.max(line.getHeight(), 1);

		if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN) {
			// flip vertical
			int tmp = x2; x2 = x1; x1 = tmp;
		}
		
		Line2D myline = new Line2D.Double(x1,y1,x2,y2);
		XSLFAutoShape xfline = PptxGraphics2D.getShape(myline, slide);
		exportElementAttributes(line, line.getLinePen(), xfline);
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle) {
		int x = rectangle.getX() + getOffsetX();
		int y = rectangle.getY() + getOffsetY();
		int width = rectangle.getWidth();
		int height = rectangle.getHeight();
		int radius = rectangle.getRadius();
		
		XSLFAutoShape xfrect;
		if (radius == 0) {
			Rectangle2D myrect = new Rectangle2D.Double(x, y, width, height);
			xfrect = PptxGraphics2D.getShape(myrect, slide);
		} else {
			RoundRectangle2D myrect = new RoundRectangle2D.Double(x, y, width, height, radius, radius);
			xfrect = PptxGraphics2D.getShape(myrect, slide);
		}

		exportElementAttributes(rectangle, rectangle.getLinePen(), xfrect);
	}


	/**
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse) {
		int x = ellipse.getX() + getOffsetX();
		int y = ellipse.getY() + getOffsetY();
		int width = ellipse.getWidth();
		int height = ellipse.getHeight();

		Ellipse2D myoval = new Ellipse2D.Double(x, y, width, height);
		XSLFAutoShape xfOval = PptxGraphics2D.getShape(myoval, slide);

		exportElementAttributes(ellipse, ellipse.getLinePen(), xfOval);
	}
	
	/**
	 *
	 */
	public void exportText(JRPrintText text) {
		JasperReportsContext jrctx = getJasperReportsContext();

		JRStyledText styledText = getStyledText(text);
		Locale locale = getTextLocale(text);
		PptxShapeTextHelper textHelper;
		if (outlineFont(text.getFontName(), locale)) {
			textHelper = new PptxShapeGlyphHelper(jrctx, text, styledText, getOffsetX(), getOffsetY(), locale, invalidCharReplacement, slide, this);
		} else {
			textHelper = new PptxShapeRunHelper(jrctx, text, styledText, getOffsetX(), getOffsetY(), locale, invalidCharReplacement, slide, this);
		}
		
		textHelper.export();

		XSLFShape shape = textHelper.getShape();

		
		String href = getHyperlinkURL(text);
		if (href != null) {
	        PackageRelationship rel = slide.getPackagePart()
        		.addExternalRelationship(href, XSLFRelation.HYPERLINK.getRelation());
	        XmlObject xo = shape.getXmlObject();
	        if (xo instanceof CTShape) {
		        CTShape ct = (CTShape)xo;
    	        ct.getNvSpPr().getCNvPr().addNewHlinkClick().setId(rel.getId());
	        } else if (xo instanceof CTGroupShape) {
		        CTGroupShape ctg = (CTGroupShape)xo;
		        ctg.getNvGrpSpPr().getCNvPr().addNewHlinkClick().setId(rel.getId());
	        }
		}
	}


	/**
	 *
	 */
	public void exportImage(JRPrintImage image) throws JRException {
		int leftPadding = image.getLineBox().getLeftPadding().intValue();
		int topPadding = image.getLineBox().getTopPadding().intValue();//FIXMEDOCX maybe consider border thickness
		int rightPadding = image.getLineBox().getRightPadding().intValue();
		int bottomPadding = image.getLineBox().getBottomPadding().intValue();

		int availableImageWidth = image.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = image.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		Renderable renderer = image.getRenderable();
		
		if (renderer == null || availableImageWidth == 0 || availableImageHeight == 0) return;

		if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE) {
			// Non-lazy image renderers are all asked for their image data at some point.
			// Better to test and replace the renderer now, in case of lazy load error.
			renderer = RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForImageData(renderer, image.getOnErrorTypeValue());
			if (renderer == null) return;
		}
		
		int width = availableImageWidth;
		int height = availableImageHeight;

		double normalWidth = availableImageWidth;
		double normalHeight = availableImageHeight;

		// Image load might fail.
		Renderable tmpRenderer =
			RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForDimension(renderer, image.getOnErrorTypeValue());
		Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension(jasperReportsContext);
		// If renderer was replaced, ignore image dimension.
		if (tmpRenderer == renderer && dimension != null)
		{
			normalWidth = dimension.getWidth();
			normalHeight = dimension.getHeight();
		}

		double cropTop = 0;
		double cropLeft = 0;
		double cropBottom = 0;
		double cropRight = 0;
		
		switch (image.getScaleImageValue()) {
			case FILL_FRAME : {
				width = availableImageWidth;
				height = availableImageHeight;
//					cropTop = 100000 * topPadding / availableImageHeight;
//					cropLeft = 100000 * leftPadding / availableImageWidth;
//					cropBottom = 100000 * bottomPadding / availableImageHeight;
//					cropRight = 100000 * rightPadding / availableImageWidth;
				break;
			}
			case CLIP : {
//					if (normalWidth > availableImageWidth)
//					{
					switch (image.getHorizontalAlignmentValue())
					{
						case RIGHT :
						{
							cropLeft = 100000 * (availableImageWidth - normalWidth) / availableImageWidth;
							cropRight = 0;
//								cropRight = 100000 * rightPadding / availableImageWidth;
							break;
						}
						case CENTER :
						{
							cropLeft = 100000 * (availableImageWidth - normalWidth) / availableImageWidth / 2;
							cropRight = cropLeft;
							break;
						}
						case LEFT :
						default :
						{
//								cropLeft = 100000 * leftPadding / availableImageWidth;
							cropLeft = 0;
							cropRight = 100000 * (availableImageWidth - normalWidth) / availableImageWidth;
							break;
						}
					}
//						width = availableImageWidth;
////						cropLeft = cropLeft / 0.75d;
////						cropRight = cropRight / 0.75d;
//					}
//					else
//					{
//						width = (int)normalWidth;
//					}

//					if (normalHeight > availableImageHeight)
//					{
					switch (image.getVerticalAlignmentValue())
					{
						case TOP :
						{
//								cropTop = 100000 * topPadding / availableImageHeight;
							cropTop = 0;
							cropBottom = 100000 * (availableImageHeight - normalHeight) / availableImageHeight;
							break;
						}
						case MIDDLE :
						{
							cropTop = 100000 * (availableImageHeight - normalHeight) / availableImageHeight / 2;
							cropBottom = cropTop;
							break;
						}
						case BOTTOM :
						default :
						{
							cropTop = 100000 * (availableImageHeight - normalHeight) / availableImageHeight;
							cropBottom = 0;
//								cropBottom = 100000 * bottomPadding / availableImageHeight;
							break;
						}
					}
//						height = availableImageHeight;
//						cropTop = cropTop / 0.75d;
//						cropBottom = cropBottom / 0.75d;
//					}
//					else
//					{
//						height = (int)normalHeight;
//					}

				break;
			}
			case RETAIN_SHAPE :
			default :
			{
				if (availableImageHeight > 0)
				{
					double ratio = normalWidth / normalHeight;

					if( ratio > availableImageWidth / (double)availableImageHeight )
					{
						width = availableImageWidth;
						height = (int)(width/ratio);

						switch (image.getVerticalAlignmentValue())
						{
							case TOP :
							{
								cropTop = 0;
								cropBottom = 100000 * (availableImageHeight - height) / availableImageHeight;
								break;
							}
							case MIDDLE :
							{
								cropTop = 100000 * (availableImageHeight - height) / availableImageHeight / 2;
								cropBottom = cropTop;
								break;
							}
							case BOTTOM :
							default :
							{
								cropTop = 100000 * (availableImageHeight - height) / availableImageHeight;
								cropBottom = 0;
								break;
							}
						}
					}
					else
					{
						height = availableImageHeight;
						width = (int)(ratio * height);

						switch (image.getHorizontalAlignmentValue())
						{
							case RIGHT :
							{
								cropLeft = 100000 * (availableImageWidth - width) / availableImageWidth;
								cropRight = 0;
								break;
							}
							case CENTER :
							{
								cropLeft = 100000 * (availableImageWidth - width) / availableImageWidth / 2;
								cropRight = cropLeft;
								break;
							}
							case LEFT :
							default :
							{
								cropLeft = 0;
								cropRight = 100000 * (availableImageWidth - width) / availableImageWidth;
								break;
							}
						}
					}
				}
			}
		}

//			insertPageAnchor();
//			if (image.getAnchorName() != null)
//			{
//				tempBodyWriter.write("<text:bookmark text:name=\"");
//				tempBodyWriter.write(image.getAnchorName());
//				tempBodyWriter.write("\"/>");
//			}


//			boolean startedHyperlink = startHyperlink(image,false);

		if (renderer instanceof DrawChartRenderer) {
			Rectangle2D rect = new Rectangle2D.Double(image.getX(), image.getY(), image.getWidth(), image.getHeight());
			PptxGraphics2D grx2 = new PptxGraphics2D(rect, this, slide);
			renderer.render(jasperReportsContext, grx2, rect);
		} else {
			//	if (image.isLazy())//FIXMEDOCX learn how to link images				
			Renderable imgRenderer = image.getRenderable();
			if (imgRenderer.getTypeValue() == RenderableTypeEnum.SVG) {
				imgRenderer =
					new JRWrappingSvgRenderer(
						imgRenderer,
						new Dimension(image.getWidth(), image.getHeight()),
						ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
					);
			}

			ImageTypeEnum mimeType = imgRenderer.getImageTypeValue();
			if (mimeType == null) mimeType = ImageTypeEnum.JPEG;
			int xsMime;
			switch (mimeType) {
				default:
				case UNKNOWN:
				case JPEG: xsMime = XSLFPictureData.PICTURE_TYPE_JPEG; break;
				case GIF: xsMime = XSLFPictureData.PICTURE_TYPE_GIF; break;
				case PNG: xsMime = XSLFPictureData.PICTURE_TYPE_PNG; break;
				case TIFF: xsMime = XSLFPictureData.PICTURE_TYPE_TIFF; break;
			}
			
			byte pictureData[] = imgRenderer.getImageData(jasperReportsContext);
			
			int idx = ppt.addPicture(pictureData, xsMime);
			XSLFPictureShape shape = slide.createPicture(idx);
			CTPicture ct = (CTPicture)shape.getXmlObject();

			if (cropTop != 0 || cropRight != 0 || cropBottom != 0 || cropLeft != 0) {
				CTRelativeRect rrect = ct.getBlipFill().getStretch().getFillRect();
				rrect.setT((int)cropTop);
				rrect.setR((int)cropRight);
				rrect.setB((int)cropBottom);
				rrect.setL((int)cropLeft);
			}
			
			String href = getHyperlinkURL(image);
			if (href != null) {
		        PackageRelationship rel = slide.getPackagePart()
	        		.addExternalRelationship(href, XSLFRelation.HYPERLINK.getRelation());
				ct.getNvPicPr().getCNvPr().addNewHlinkClick().setId(rel.getId());
			}
			
			Rectangle2D rect = new Rectangle2D.Double(
				  image.getX() + getOffsetX() + leftPadding
				, image.getY() + getOffsetY() + topPadding
				, availableImageWidth
				, availableImageHeight
			);
			shape.setAnchor(rect);

			exportElementAttributes(image, image.getLinePen(), shape);
		}
	}


	/**
	 *
	 */
	protected void exportFrame(JRPrintFrame frame) throws JRException {
		Rectangle2D rect = new Rectangle2D.Double(
			  frame.getX() + getOffsetX()
			, frame.getY() + getOffsetY()
			, frame.getWidth()
			, frame.getHeight());
		XSLFAutoShape shape = PptxGraphics2D.getShape(rect, slide);
		exportElementAttributes(frame, frame.getLineBox().getPen(), shape);

		setFrameElementsOffset(frame, false);

		frameIndexStack.add(Integer.valueOf(elementIndex));

		exportElements(frame.getElements());

		frameIndexStack.remove(frameIndexStack.size() - 1);
		
		restoreElementOffsets();
	}


	/**
	 *
	 */
	protected void exportGenericElement(JRGenericPrintElement element) {
		if (log.isDebugEnabled()) {
			log.debug("No PPTX-Shape generic element handler for " + element.getGenericType());
		}
	}

	protected String getHyperlinkURL(JRPrintHyperlink link) {
		String href = null;
		JRHyperlinkProducer customHandler = getHyperlinkProducer(link);
		if (customHandler == null) {
			switch(link.getHyperlinkTypeValue()) {
				case REFERENCE :
					if (link.getHyperlinkReference() != null) {
						href = link.getHyperlinkReference();
					}
					break;
				case LOCAL_ANCHOR :
//					if (link.getHyperlinkAnchor() != null)
//					{
//						href = "#" + link.getHyperlinkAnchor();
//					}
					break;
				case LOCAL_PAGE :
//					if (link.getHyperlinkPage() != null)
//					{
//						href = "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
//					}
					break;
				case REMOTE_ANCHOR :
					if (
						link.getHyperlinkReference() != null &&
						link.getHyperlinkAnchor() != null
					) {
						href = link.getHyperlinkReference() + "#" + link.getHyperlinkAnchor();
					}
					break;
				case REMOTE_PAGE :
//					if (
//						link.getHyperlinkReference() != null &&
//						link.getHyperlinkPage() != null
//						)
//					{
//						href = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
//					}
					break;
				case NONE :
				default : break;
			}
		} else {
			href = customHandler.getHyperlink(link);
		}

		return href;
	}


	/**
	 *
	 */
	protected String getExporterPropertiesPrefix() { //FIXMEPPTX move this to abstract exporter
		return PPTX_EXPORTER_PROPERTIES_PREFIX;
	}

	/**
	 *
	 */
	protected String getExporterKey() {
		return PPTX_EXPORTER_KEY;
	}

	protected void setExporterHints() {
		if(jasperPrint.hasProperties() && jasperPrint.getPropertiesMap().containsProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS)) {
			// allows null values for the property
			invalidCharReplacement = jasperPrint.getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS);
		} else {
			invalidCharReplacement = getPropertiesUtil().getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS, jasperPrint);
		}
	}
	
	protected void embedFonts() throws JRException, IOException, InvalidFormatException {
		RepositoryUtil ru = RepositoryUtil.getInstance(jasperReportsContext);
		
		List<FontFamily> ferList = jasperReportsContext.getExtensions(FontFamily.class);
		if (ferList == null || ferList.size() == 0) return;

		FontFactory fontfac = FontFactory.getInstance();
		EOTWriter conv = new EOTWriter(true); // generate MicroTypeExpress (mtx) fonts

		ppt.getCTPresentation().setEmbedTrueTypeFonts(true);
		ppt.getCTPresentation().setSaveSubsetFonts(true);
		
		CTEmbeddedFontList fontList = null;
		
		for (FontFamily ff : ferList) {
			if (!"embed".equals(ff.getExportFont(PPTX_EXPORTER_KEY))) {
				// don't export font
				continue;
			}
			
			// "<p:font typeface=\"AllianzLogo\" pitchFamily=\"49\" charset=\"2\"/><p:regular r:id=\"rId13\"/>"
			FontFace ffTypes[] = { ff.getNormalFace(), ff.getBoldFace(), ff.getItalicFace(), ff.getBoldItalicFace() };

			CTEmbeddedFontListEntry fntEntry = null;
			
			for (int i=0; i<4; i++) {
				if (ffTypes[i] == null) continue;
				String fileRelPath = ffTypes[i].getFile();
				String fntDataName = fileRelPath.replaceAll(".*/(.*).ttf", "$1.fntdata");
				byte fontBytes[] = ru.getBytesFromLocation(fileRelPath);
				
				Font fonts[] = fontfac.loadFonts(fontBytes);
				assert(fonts != null && fonts.length == 1);
				
				WritableFontData eotFont = conv.convert(fonts[0]);

				PackagePartName partName = PackagingURIHelper.createPartName("/ppt/fonts/"+fntDataName);
				PackagePart part = ppt.getPackage().createPart(partName, "application/x-fontdata");
				OutputStream partOs = part.getOutputStream();
				eotFont.copyTo(partOs);
				partOs.close();

				PackageRelationship prs = ppt.getPackagePart().addRelationship(partName, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/font");
				
				if (fontList == null) {
					fontList = ppt.getCTPresentation().addNewEmbeddedFontLst();
				}
				if (fntEntry == null) {
					fntEntry = fontList.addNewEmbeddedFont();
					CTTextFont fnt = fntEntry.addNewFont();
					fnt.setTypeface(ff.getName());
					// TODO set charset / pitchFamily
				}
				
				CTEmbeddedFontDataId fntId = null;
				switch (i) {
				case 0: fntId = fntEntry.addNewRegular(); break;
				case 1: fntId = fntEntry.addNewBold(); break;
				case 2: fntId = fntEntry.addNewItalic(); break;
				case 3: fntId = fntEntry.addNewBoldItalic(); break;
				}
				
				fntId.setId(prs.getId());
			}
			
		}
	}


	protected int exportBackground() throws JRException {
		if (jasperPrintList == null || jasperPrintList.size() == 0) {
			return 0;
		}

		setJasperPrint(jasperPrintList.get(0));
		
		List<JRPrintPage> pages = jasperPrint.getPages();
		List<JRPrintElement> backElem = new ArrayList<JRPrintElement>();
		if (pages.size() >= 2) {
			// determine number of background elements
			List<JRPrintElement> pEl1 = pages.get(0).getElements();
			List<JRPrintElement> pEl2 = pages.get(1).getElements();
			for (int i=0; i<pEl1.size(); i++) {
				JRPrintElement el1 = pEl1.get(i);
				if (pEl2.size() <= i) break;
				JRPrintElement el2 = pEl2.get(i);
				// reference identity doesn't work here
				// so we need to check for something in common
				// maybe bounds?
				if (el1.getX() == el2.getX()
					&& el1.getY() == el2.getY()
					&& el1.getWidth() == el2.getWidth()
					&& el1.getHeight() == el2.getHeight()
					&& el1.getClass().equals(el2.getClass())
				) {
					if (el1 instanceof JRTemplatePrintText) {
						JRTemplatePrintText jpr1 = (JRTemplatePrintText)el1;
						JRTemplatePrintText jpr2 = (JRTemplatePrintText)el2;
						JRStyledTextUtil jstu = JRStyledTextUtil.getInstance(getJasperReportsContext());
						String str1 = jstu.getTruncatedText(jpr1);
						String str2 = jstu.getTruncatedText(jpr2);
						if (!str1.equals(str2)) break;
					}
					
					backElem.add(el1);
				} else {
					break;
				}
			}
		}

		if (backElem.size() > 0) {
			XSLFSlideMaster master = ppt.getSlideMasters()[0];
			slide = master;
			exportElements(backElem);
		}
		
		return backElem.size();
	}

	public boolean outlineFont(String family, Locale locale) {
		return "curves".equals(exportFont(family, locale, true));
	}

	public boolean embedFont(String family, Locale locale) {
		return "embed".equals(exportFont(family, locale, true));
	}

	protected String exportFont(String family, Locale locale, boolean returnSpecial) {
		FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(family, locale);
		if (fontInfo == null) return null;

		String mappedFont = fontInfo.getFontFamily().getExportFont(JRPptxExporter.PPTX_EXPORTER_KEY);
		boolean isSpecial = ("curves".equals(mappedFont) || "embed".equals(mappedFont));
		
		return (mappedFont != null && (!isSpecial || returnSpecial)) 
			? mappedFont
			: family;
	}
	
	public String exportFont(String family, Locale locale) {
		return exportFont(family, locale, false);
	}

	public java.awt.Font loadFont(Map<Attribute,Object> attr, Locale locale) {
		String family = (String)attr.get(TextAttribute.FAMILY);
		FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(family, locale);
		if (fontInfo == null || fontInfo.getFontFamily() == null) {
			return java.awt.Font.getFont(attr);
		}

		FontFamily ffamily = fontInfo.getFontFamily();
		FontFace fface = null;
		if (fface == null
			&& TextAttribute.WEIGHT_BOLD.equals(attr.get(TextAttribute.WEIGHT))
			&& TextAttribute.POSTURE_OBLIQUE.equals(attr.get(TextAttribute.POSTURE))
		) {
			fface = ffamily.getBoldItalicFace();
		}
		
		if (fface == null
			&& TextAttribute.WEIGHT_BOLD.equals(attr.get(TextAttribute.WEIGHT))
		) {
			fface = ffamily.getBoldFace();
		}

		if (fface == null
			&& TextAttribute.POSTURE_OBLIQUE.equals(attr.get(TextAttribute.POSTURE))
		) {
			fface = ffamily.getItalicFace();
		}

		if (fface == null) {
			fface = ffamily.getNormalFace();
		}

		if (fface == null) {
			return java.awt.Font.getFont(attr);
		}
		
		attr.remove(TextAttribute.FAMILY);
		return fface.getFont().deriveFont(attr);
	}
}

