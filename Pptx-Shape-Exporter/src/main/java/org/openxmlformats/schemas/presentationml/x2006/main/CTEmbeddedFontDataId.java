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
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.w3c.dom.Node;

public abstract interface CTEmbeddedFontDataId extends XmlObject
{
  public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(CTEmbeddedFontDataId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sE130CAA0A01A7CDE5A2B4FEB8B311707").resolveHandle("ctembeddedfontdataid7d67type");

  public abstract String getId();

  public abstract STRelationshipId xgetId();

  public abstract void setId(String paramString);

  public abstract void xsetId(STRelationshipId paramSTRelationshipId);

  public static final class Factory
  {
    public static CTEmbeddedFontDataId newInstance()
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().newInstance(CTEmbeddedFontDataId.type, null));
    }

    public static CTEmbeddedFontDataId newInstance(XmlOptions paramXmlOptions)
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().newInstance(CTEmbeddedFontDataId.type, paramXmlOptions));
    }

    public static CTEmbeddedFontDataId parse(String paramString)
      throws XmlException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramString, CTEmbeddedFontDataId.type, null));
    }

    public static CTEmbeddedFontDataId parse(String paramString, XmlOptions paramXmlOptions)
      throws XmlException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramString, CTEmbeddedFontDataId.type, paramXmlOptions));
    }

    public static CTEmbeddedFontDataId parse(File paramFile)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramFile, CTEmbeddedFontDataId.type, null));
    }

    public static CTEmbeddedFontDataId parse(File paramFile, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramFile, CTEmbeddedFontDataId.type, paramXmlOptions));
    }

    public static CTEmbeddedFontDataId parse(URL paramURL)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramURL, CTEmbeddedFontDataId.type, null));
    }

    public static CTEmbeddedFontDataId parse(URL paramURL, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramURL, CTEmbeddedFontDataId.type, paramXmlOptions));
    }

    public static CTEmbeddedFontDataId parse(InputStream paramInputStream)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramInputStream, CTEmbeddedFontDataId.type, null));
    }

    public static CTEmbeddedFontDataId parse(InputStream paramInputStream, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramInputStream, CTEmbeddedFontDataId.type, paramXmlOptions));
    }

    public static CTEmbeddedFontDataId parse(Reader paramReader)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramReader, CTEmbeddedFontDataId.type, null));
    }

    public static CTEmbeddedFontDataId parse(Reader paramReader, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramReader, CTEmbeddedFontDataId.type, paramXmlOptions));
    }

    public static CTEmbeddedFontDataId parse(XMLStreamReader paramXMLStreamReader)
      throws XmlException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramXMLStreamReader, CTEmbeddedFontDataId.type, null));
    }

    public static CTEmbeddedFontDataId parse(XMLStreamReader paramXMLStreamReader, XmlOptions paramXmlOptions)
      throws XmlException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramXMLStreamReader, CTEmbeddedFontDataId.type, paramXmlOptions));
    }

    public static CTEmbeddedFontDataId parse(Node paramNode)
      throws XmlException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramNode, CTEmbeddedFontDataId.type, null));
    }

    public static CTEmbeddedFontDataId parse(Node paramNode, XmlOptions paramXmlOptions)
      throws XmlException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramNode, CTEmbeddedFontDataId.type, paramXmlOptions));
    }

    /** @deprecated */
    public static CTEmbeddedFontDataId parse(XMLInputStream paramXMLInputStream)
      throws XmlException, XMLStreamException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramXMLInputStream, CTEmbeddedFontDataId.type, null));
    }

    /** @deprecated */
    public static CTEmbeddedFontDataId parse(XMLInputStream paramXMLInputStream, XmlOptions paramXmlOptions)
      throws XmlException, XMLStreamException
    {
      return ((CTEmbeddedFontDataId)XmlBeans.getContextTypeLoader().parse(paramXMLInputStream, CTEmbeddedFontDataId.type, paramXmlOptions));
    }

    /** @deprecated */
    public static XMLInputStream newValidatingXMLInputStream(XMLInputStream paramXMLInputStream)
      throws XmlException, XMLStreamException
    {
      return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(paramXMLInputStream, CTEmbeddedFontDataId.type, null);
    }

    /** @deprecated */
    public static XMLInputStream newValidatingXMLInputStream(XMLInputStream paramXMLInputStream, XmlOptions paramXmlOptions)
      throws XmlException, XMLStreamException
    {
      return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(paramXMLInputStream, CTEmbeddedFontDataId.type, paramXmlOptions);
    }
  }
}

/* Location:           E:\tmp\mavenRepo\org\apache\poi\ooxml-schemas\1.1\ooxml-schemas-1.1.jar
 * Qualified Name:     org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */