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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.charts.util.DrawChartRenderer;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRCommonElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTypeSniffer;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;
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
import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFRelation;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSheet;
import org.apache.poi.xslf.usermodel.XSLFSimpleShape;
import org.apache.poi.xslf.usermodel.XSLFSimpleShapeHelper;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPicture;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.tools.conversion.eot.EOTWriter;

import de.kiwiwings.jasperreports.exporter.customizer.SheetCustomizer;


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
	public static final String PPTX_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "pptx";

	public static class PptxShapeExportParameter extends JRExporterParameter {
		protected PptxShapeExportParameter(String name) { super(name); }
	}
	
	public static final JRExporterParameter SHEET_CUSTOMIZER = new PptxShapeExportParameter("sheet customizer");
	public static final String PROPERTY_REPLACE_INVALID_CHARS = JRProperties.PROPERTY_PREFIX + "export.xml.replace.invalid.chars";
	protected static final String PPTX_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.pptx.";

	protected XMLSlideShow ppt;
	protected XSLFSheet slide;
	protected Map<Integer,XSLFSheet> slideList = new HashMap<Integer,XSLFSheet>();
	
	protected JRExportProgressMonitor progressMonitor;

	protected int reportIndex;
	protected int pageIndex;
	protected List<Integer> frameIndexStack;
	protected int elementIndex;
	protected boolean startPage;
	protected String invalidCharReplacement;

	protected SheetCustomizer sheetCustomizer[] = {};
	
	
	/**
	 * @see #JRPptxExporter()
	 */
	public PptxShapeExporter() {}
	

	/**
	 *
	 */
	public void exportReport() throws JRException {
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		if (parameters.containsKey(SHEET_CUSTOMIZER)) {
			sheetCustomizer = (SheetCustomizer[])parameters.get(SHEET_CUSTOMIZER);
		}
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
			slideList.clear();

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages == null || pages.size() == 0) continue;
			
			if (isModeBatch) {
				startPageIndex = 0;
				endPageIndex = pages.size() - 1;
			}

			// pre-create pages for hyperlinks between them
			for (int i=startPageIndex; i <= endPageIndex; i++) {
				createSlide(null);//FIXMEPPTX
				slideList.put(i,slide);
			}
			
			
			JRPrintPage page = null;
			for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
				if (Thread.interrupted()) {
					throw new JRException("Current thread interrupted.");
				}

				page = pages.get(pageIndex);
				slide = slideList.get(pageIndex);
				
				List<JRPrintElement> list = page.getElements();
				list = list.subList(nbrBackElem, list.size());
				page.setElements(list);
				
				exportPage(page);
				
				for (SheetCustomizer sc : sheetCustomizer) {
					sc.customize(slide);
				}
			}
		}

		embedFonts();
		
		ppt.write(os);
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException {
		frameIndexStack = new ArrayList<Integer>();

		exportElements(page.getElements());
		
		if (progressMonitor != null) {
			progressMonitor.afterPageExport();
		}
	}


	protected void createSlide(String name) {
		slide = ppt.createSlide();
	}


	/**
	 *
	 */
	protected void exportElements(List<JRPrintElement> elements) throws JRException	{
		if (elements == null) return;
		elementIndex = -1;

		for (JRPrintElement element : elements) {
			elementIndex++;
			if (filter != null && !filter.isToExport(element)) continue;

			if (element instanceof JRPrintLine)	{
				exportLine((JRPrintLine)element);
			} else if (element instanceof JRPrintRectangle) {
				exportRectangle((JRPrintRectangle)element);
			} else if (element instanceof JRPrintEllipse) {
				exportEllipse((JRPrintEllipse)element);
			} else if (element instanceof JRPrintImage) {
				exportImage((JRPrintImage)element);
			} else if (element instanceof JRPrintText) {
				exportText((JRPrintText)element);
			} else if (element instanceof JRPrintFrame) {
				exportFrame((JRPrintFrame)element);
			} else if (element instanceof JRGenericPrintElement) {
				exportGenericElement((JRGenericPrintElement) element);
			}
		}
	}

	protected void exportElementAttributes(
		  XSLFSimpleShape xsShape   // the target shape
		, JRCommonElement jrShape   // the source shape
		, JRPen jrPen               // the outline of the shape
		, JRBoxContainer jrBox      // box around the shape, if applicable
		, XSLFShape hyperlinkTarget // a shape or group of shape as hyperlink object
	) {
		assert(xsShape != null && jrShape != null);

		if (jrShape.getKey() != null) {
			XSLFSimpleShapeHelper.setShapeName(xsShape, jrShape.getKey());
		}
		
		if ((jrShape.getModeValue() == null || jrShape.getModeValue() == ModeEnum.OPAQUE) && jrShape.getBackcolor() != null) {
			xsShape.setFillColor(jrShape.getBackcolor());
		}

		if (jrPen != null && jrPen.getLineWidth() > 0) {
			xsShape.setLineWidth(jrPen.getLineWidth());
			xsShape.setLineColor(jrPen.getLineColor());
			switch (jrPen.getLineStyleValue()) {
				case DASHED: xsShape.setLineDash(LineDash.DASH); break;
				case DOTTED: xsShape.setLineDash(LineDash.DOT); break;
				default: xsShape.setLineDash(LineDash.SOLID); break;
			}
		}

		// TODO: set border to box around the shape
		
		setHyperlinkURL(jrShape, hyperlinkTarget);
	}

	protected void setHyperlinkURL(JRCommonElement jrShape, XSLFShape hyperlinkTarget) {
		if (hyperlinkTarget == null || !(jrShape instanceof JRPrintHyperlink)) return;
		
		// http://openxmldeveloper.org/discussions/formats/f/15/t/304.aspx
		
		JRPrintHyperlink link = (JRPrintHyperlink)jrShape; 
		JRHyperlinkProducer customHandler = getHyperlinkProducer(link);
		PackageRelationship rel = null;
		String action = null;
		
		if (customHandler != null) {
			String href = customHandler.getHyperlink(link);
			if (href != null) {
				rel = slide.getPackagePart()
					.addExternalRelationship(href, XSLFRelation.HYPERLINK.getRelation());
			}
		}
		
		switch(link.getHyperlinkTypeValue()) {
			case REFERENCE : {
				String href = link.getHyperlinkReference();
				if (href != null) {
					rel = slide.getPackagePart()
						.addExternalRelationship(href, XSLFRelation.HYPERLINK.getRelation());
				}
				break;
			}
			case LOCAL_PAGE : {
				XSLFSheet targetSlide = slideList.get(link.getHyperlinkPage());
		        rel = slide.getPackagePart()
	        		.addRelationship(targetSlide.getPackagePart().getPartName()
    				, TargetMode.INTERNAL, XSLFRelation.SLIDE.getRelation());
		        action = "ppaction://hlinksldjump";
				break;
			}
			case REMOTE_PAGE : {
				String href = link.getHyperlinkReference();
				if (href != null) {
					rel = slide.getPackagePart()
						.addExternalRelationship(href, XSLFRelation.HYPERLINK.getRelation());
				}
				Integer page = link.getHyperlinkPage();
				if (page != null) {
					action = "ppaction://hlinkpres?slideindex="+page;
					if (link.getHyperlinkParameters() != null) {
						List<JRPrintHyperlinkParameter> plist = link.getHyperlinkParameters().getParameters();
						if (plist != null) {
							for (JRPrintHyperlinkParameter p : plist) {
								if ("slidetitle".equals(p.getName())) {
									action += "&slidetitle="+p.getValue().toString();
									break;
								}
							}
						}
					}
				}
				break;
			}
			
			case NONE :
			default :
			case LOCAL_ANCHOR :
			case REMOTE_ANCHOR :
				// not implemented ... only pages (not elements) can be referenced in powerpoint
				break;
		}

		if (rel != null) {
			CTHyperlink hlink = XSLFSimpleShapeHelper.addHyperlink(hyperlinkTarget);
			if (hlink == null) {
				slide.getPackagePart().removeRelationship(rel.getId());
			} else {
				hlink.setId(rel.getId());
				if (action != null) {
					hlink.setAction(action);
				}
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
		exportElementAttributes(xfline, line, line.getLinePen(), null, xfline);
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

		exportElementAttributes(xfrect, rectangle, rectangle.getLinePen(), null, xfrect);
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

		exportElementAttributes(xfOval, ellipse, ellipse.getLinePen(), null, xfOval);
	}
	
	/**
	 *
	 */
	public void exportText(JRPrintText text) {
		JRStyledText styledText = getStyledText(text);
		Locale locale = getTextLocale(text);
		PptxShapeTextHelper textHelper;
		if (outlineFont(text.getFontName(), locale)) {
			textHelper = new PptxShapeGlyphHelper(text, styledText, getOffsetX(), getOffsetY(), locale, invalidCharReplacement, slide, this, pageIndex);
		} else {
			textHelper = new PptxShapeRunHelper(text, styledText, getOffsetX(), getOffsetY(), locale, invalidCharReplacement, slide, this, pageIndex);
		}
		
		textHelper.export();

		exportElementAttributes(textHelper.getShapeBox(), text, null, text, textHelper.getShape());
	}


	/**
	 *
	 */
	public void exportImage(JRPrintImage image) throws JRException {
		int leftPadding = image.getLineBox().getLeftPadding().intValue();
		int topPadding = image.getLineBox().getTopPadding().intValue();//FIXMEDOCX maybe consider border thickness
		int rightPadding = image.getLineBox().getRightPadding().intValue();
		int bottomPadding = image.getLineBox().getBottomPadding().intValue();

		double availableImageWidth = image.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		double availableImageHeight = image.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		JRRenderable renderer = image.getRenderer();
		
		if (renderer == null || availableImageWidth == 0 || availableImageHeight == 0) return;

		if (renderer.getType() == JRRenderable.TYPE_IMAGE) {
			// Non-lazy image renderers are all asked for their image data at some point.
			// Better to test and replace the renderer now, in case of lazy load error.
			renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, image.getOnErrorTypeValue());
			if (renderer == null) return;
		}
		
		double normalWidth = availableImageWidth;
		double normalHeight = availableImageHeight;

		// Image load might fail.
		JRRenderable tmpRenderer =
				JRImageRenderer.getOnErrorRendererForDimension(renderer, image.getOnErrorTypeValue());
		Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension();
		// If renderer was replaced, ignore image dimension.
		if (tmpRenderer == renderer && dimension != null) {
			normalWidth = dimension.getWidth();
			normalHeight = dimension.getHeight();
		}

		double cropTop = 0;
		double cropLeft = 0;
		double cropBottom = 0;
		double cropRight = 0;
		
		switch (image.getScaleImageValue()) {
			case FILL_FRAME :
//				width = availableImageWidth;
//				height = availableImageHeight;
				break;

			default :
			case RETAIN_SHAPE :
				if (availableImageWidth > 0 && availableImageHeight > 0) {
					if (normalWidth > availableImageWidth) {
						normalHeight /= normalWidth/availableImageWidth;
						normalWidth = availableImageWidth;
					}
					if (normalHeight > availableImageHeight) {
						normalWidth /= normalHeight/availableImageHeight;
						normalHeight = availableImageHeight;
					}
				}
				// nobreak
			
			case CLIP : 
				if (availableImageWidth > 0 && availableImageHeight > 0) {
					double reducedWidth = getOffsetInPercent(normalWidth,availableImageWidth);
					switch (image.getHorizontalAlignmentValue()) {
						default :
						case LEFT : cropRight = reducedWidth; break;
						case RIGHT : cropLeft = reducedWidth; break;
						case CENTER : cropLeft = cropRight = reducedWidth/2d; break;
					}
					double reducedHeight = getOffsetInPercent(normalHeight,availableImageHeight);
					switch (image.getVerticalAlignmentValue()) {
						default :
						case BOTTOM : cropTop = reducedHeight; break;
						case TOP : cropBottom = reducedHeight; break;
						case MIDDLE : cropTop = cropBottom = reducedHeight/2; break;
					}
				}
				break;
		}

		XSLFSimpleShape backgroundShape;
		XSLFShape hyperlinkShape;
		
		if (renderer instanceof DrawChartRenderer) {
			Rectangle2D rect = new Rectangle2D.Double(image.getX(), image.getY(), image.getWidth(), image.getHeight());
			PptxGraphics2D grx2 = new PptxGraphics2D(rect, this, slide);
			// Background color is applied to whole Image
			backgroundShape = grx2.getShape(rect);
			renderer.render(grx2, rect);

			// hyperlinks are only available for visible elements of the chart
			XSLFGroupShape grp = grx2.getShapeGroup();
			CTNonVisualDrawingProps cp = grp.getXmlObject().getNvGrpSpPr().getCNvPr();
			cp.setName(cp.getName().replaceFirst("[^ ]+", "JFreeChart"));
			hyperlinkShape = grp;
		} else {
			//	if (image.isLazy())//FIXMEDOCX learn how to link images				
			JRRenderable imgRenderer = image.getRenderer();
			if (imgRenderer.getType() == JRRenderable.TYPE_SVG) {
				imgRenderer =
					new JRWrappingSvgRenderer(
						imgRenderer,
						new Dimension(image.getWidth(), image.getHeight()),
						ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
					);
			}

			String mimeType = JRTypeSniffer.getImageMimeType(renderer.getImageType());
			int xsMime = XSLFPictureData.PICTURE_TYPE_JPEG;
			if (JRRenderable.MIME_TYPE_JPEG.equals(mimeType)) xsMime = XSLFPictureData.PICTURE_TYPE_JPEG;
			else if (JRRenderable.MIME_TYPE_GIF.equals(mimeType)) xsMime = XSLFPictureData.PICTURE_TYPE_GIF;
			else if (JRRenderable.MIME_TYPE_PNG.equals(mimeType)) xsMime = XSLFPictureData.PICTURE_TYPE_PNG;
			else if (JRRenderable.MIME_TYPE_TIFF.equals(mimeType)) xsMime = XSLFPictureData.PICTURE_TYPE_TIFF;
			
			byte pictureData[] = imgRenderer.getImageData();
			
			int idx = ppt.addPicture(pictureData, xsMime);
			backgroundShape = slide.createPicture(idx);
			hyperlinkShape = backgroundShape;
			CTPicture ct = (CTPicture)backgroundShape.getXmlObject();

			if (cropTop != 0 || cropRight != 0 || cropBottom != 0 || cropLeft != 0) {
//				CTRelativeRect rrect = ct.getBlipFill().getStretch().getFillRect();
				CTRelativeRect rrect = ct.getBlipFill().addNewSrcRect();
				rrect.setT((int)cropTop);
				rrect.setR((int)cropRight);
				rrect.setB((int)cropBottom);
				rrect.setL((int)cropLeft);
			}
			
			
			Rectangle2D rect = new Rectangle2D.Double(
				  image.getX() + getOffsetX() + leftPadding
				, image.getY() + getOffsetY() + topPadding
				, availableImageWidth
				, availableImageHeight
			);
			backgroundShape.setAnchor(rect);
		}

		exportElementAttributes(backgroundShape, image, image.getLinePen(), image, hyperlinkShape);
	}

	protected static double getOffsetInPercent(double normalSize, double availableSize) {
		return -100000d*(availableSize-normalSize)/normalSize;
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
		exportElementAttributes(shape, frame, frame.getLineBox().getPen(), frame, null);

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
		if(jasperPrint.hasProperties() && jasperPrint.getPropertiesMap().containsProperty(PROPERTY_REPLACE_INVALID_CHARS)) {
			// allows null values for the property
			invalidCharReplacement = jasperPrint.getProperty(PROPERTY_REPLACE_INVALID_CHARS);
		} else {
			invalidCharReplacement = JRProperties.getProperty(PROPERTY_REPLACE_INVALID_CHARS, jasperPrint);
		}
	}
	
	protected void embedFonts() throws JRException, IOException, InvalidFormatException {
		List<FontFamily> ferList = 
			ExtensionsEnvironment
			.getExtensionsRegistry()
			.getExtensions(FontFamily.class);
		if (ferList == null || ferList.size() == 0) return;
		
		FontFactory fontfac = FontFactory.getInstance();
		EOTWriter conv = new EOTWriter(true); // generate MicroTypeExpress (mtx) fonts

		CTEmbeddedFontList fontList = null;
		
		boolean anyFontsEmbedded = false;
		for (FontFamily ff : ferList) {
			if (!"embed".equals(ff.getExportFont(PPTX_EXPORTER_KEY))) {
				// don't export font
				continue;
			}
			anyFontsEmbedded = true;
			
			// "<p:font typeface=\"AllianzLogo\" pitchFamily=\"49\" charset=\"2\"/><p:regular r:id=\"rId13\"/>"
			FontFace ffTypes[] = { ff.getNormalFace(), ff.getBoldFace(), ff.getItalicFace(), ff.getBoldItalicFace() };

			CTEmbeddedFontListEntry fntEntry = null;
			
			for (int i=0; i<4; i++) {
				if (ffTypes[i] == null) continue;
				String fileRelPath = ffTypes[i].getFile();
				String fntDataName = fileRelPath.replaceAll(".*/(.*).ttf", "$1.fntdata");
				byte fontBytes[] = RepositoryUtil.getBytes(fileRelPath);
				
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

		if (anyFontsEmbedded) {
			CTPresentation pres = ppt.getCTPresentation();
			pres.setEmbedTrueTypeFonts(true);
			pres.setSaveSubsetFonts(true);
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
						String str1 = jpr1.getText();
						String str2 = jpr2.getText();
						if (!str1.equals(str2)) break;

						// Special case - slide number fields in outline fonts
						// have to be rendered on every page
						if (str1.contains("{fld:slidenum}")
							&& outlineFont(jpr1.getFontName(), getTextLocale(jpr1))) break;
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
		FontInfo fontInfo = JRFontUtil.getFontInfo(family, locale);
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
		FontInfo fontInfo = JRFontUtil.getFontInfo(family, locale);
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

