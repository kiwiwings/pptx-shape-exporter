package org.openxmlformats.schemas.presentationml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.w3c.dom.Node;

public abstract interface CTEmbeddedFontListEntry extends XmlObject
{
  public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(CTEmbeddedFontListEntry.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sE130CAA0A01A7CDE5A2B4FEB8B311707").resolveHandle("ctembeddedfontlistentry48b4type");

  public abstract CTTextFont getFont();

  public abstract void setFont(CTTextFont paramCTTextFont);

  public abstract CTTextFont addNewFont();

  public abstract CTEmbeddedFontDataId getRegular();

  public abstract boolean isSetRegular();

  public abstract void setRegular(CTEmbeddedFontDataId paramCTEmbeddedFontDataId);

  public abstract CTEmbeddedFontDataId addNewRegular();

  public abstract void unsetRegular();

  public abstract CTEmbeddedFontDataId getBold();

  public abstract boolean isSetBold();

  public abstract void setBold(CTEmbeddedFontDataId paramCTEmbeddedFontDataId);

  public abstract CTEmbeddedFontDataId addNewBold();

  public abstract void unsetBold();

  public abstract CTEmbeddedFontDataId getItalic();

  public abstract boolean isSetItalic();

  public abstract void setItalic(CTEmbeddedFontDataId paramCTEmbeddedFontDataId);

  public abstract CTEmbeddedFontDataId addNewItalic();

  public abstract void unsetItalic();

  public abstract CTEmbeddedFontDataId getBoldItalic();

  public abstract boolean isSetBoldItalic();

  public abstract void setBoldItalic(CTEmbeddedFontDataId paramCTEmbeddedFontDataId);

  public abstract CTEmbeddedFontDataId addNewBoldItalic();

  public abstract void unsetBoldItalic();

  public static final class Factory
  {
    public static CTEmbeddedFontListEntry newInstance()
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().newInstance(CTEmbeddedFontListEntry.type, null));
    }

    public static CTEmbeddedFontListEntry newInstance(XmlOptions paramXmlOptions)
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().newInstance(CTEmbeddedFontListEntry.type, paramXmlOptions));
    }

    public static CTEmbeddedFontListEntry parse(String paramString)
      throws XmlException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramString, CTEmbeddedFontListEntry.type, null));
    }

    public static CTEmbeddedFontListEntry parse(String paramString, XmlOptions paramXmlOptions)
      throws XmlException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramString, CTEmbeddedFontListEntry.type, paramXmlOptions));
    }

    public static CTEmbeddedFontListEntry parse(File paramFile)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramFile, CTEmbeddedFontListEntry.type, null));
    }

    public static CTEmbeddedFontListEntry parse(File paramFile, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramFile, CTEmbeddedFontListEntry.type, paramXmlOptions));
    }

    public static CTEmbeddedFontListEntry parse(URL paramURL)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramURL, CTEmbeddedFontListEntry.type, null));
    }

    public static CTEmbeddedFontListEntry parse(URL paramURL, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramURL, CTEmbeddedFontListEntry.type, paramXmlOptions));
    }

    public static CTEmbeddedFontListEntry parse(InputStream paramInputStream)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramInputStream, CTEmbeddedFontListEntry.type, null));
    }

    public static CTEmbeddedFontListEntry parse(InputStream paramInputStream, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramInputStream, CTEmbeddedFontListEntry.type, paramXmlOptions));
    }

    public static CTEmbeddedFontListEntry parse(Reader paramReader)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramReader, CTEmbeddedFontListEntry.type, null));
    }

    public static CTEmbeddedFontListEntry parse(Reader paramReader, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramReader, CTEmbeddedFontListEntry.type, paramXmlOptions));
    }

    public static CTEmbeddedFontListEntry parse(XMLStreamReader paramXMLStreamReader)
      throws XmlException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramXMLStreamReader, CTEmbeddedFontListEntry.type, null));
    }

    public static CTEmbeddedFontListEntry parse(XMLStreamReader paramXMLStreamReader, XmlOptions paramXmlOptions)
      throws XmlException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramXMLStreamReader, CTEmbeddedFontListEntry.type, paramXmlOptions));
    }

    public static CTEmbeddedFontListEntry parse(Node paramNode)
      throws XmlException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramNode, CTEmbeddedFontListEntry.type, null));
    }

    public static CTEmbeddedFontListEntry parse(Node paramNode, XmlOptions paramXmlOptions)
      throws XmlException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramNode, CTEmbeddedFontListEntry.type, paramXmlOptions));
    }

    /** @deprecated */
    public static CTEmbeddedFontListEntry parse(XMLInputStream paramXMLInputStream)
      throws XmlException, XMLStreamException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramXMLInputStream, CTEmbeddedFontListEntry.type, null));
    }

    /** @deprecated */
    public static CTEmbeddedFontListEntry parse(XMLInputStream paramXMLInputStream, XmlOptions paramXmlOptions)
      throws XmlException, XMLStreamException
    {
      return ((CTEmbeddedFontListEntry)XmlBeans.getContextTypeLoader().parse(paramXMLInputStream, CTEmbeddedFontListEntry.type, paramXmlOptions));
    }

    /** @deprecated */
    public static XMLInputStream newValidatingXMLInputStream(XMLInputStream paramXMLInputStream)
      throws XmlException, XMLStreamException
    {
      return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(paramXMLInputStream, CTEmbeddedFontListEntry.type, null);
    }

    /** @deprecated */
    public static XMLInputStream newValidatingXMLInputStream(XMLInputStream paramXMLInputStream, XmlOptions paramXmlOptions)
      throws XmlException, XMLStreamException
    {
      return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(paramXMLInputStream, CTEmbeddedFontListEntry.type, paramXmlOptions);
    }
  }
}

/* Location:           E:\tmp\mavenRepo\org\apache\poi\ooxml-schemas\1.1\ooxml-schemas-1.1.jar
 * Qualified Name:     org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */