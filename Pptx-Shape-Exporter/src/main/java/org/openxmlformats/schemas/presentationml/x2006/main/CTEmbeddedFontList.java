package org.openxmlformats.schemas.presentationml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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
import org.w3c.dom.Node;

public abstract interface CTEmbeddedFontList extends XmlObject
{
  public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(CTEmbeddedFontList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sE130CAA0A01A7CDE5A2B4FEB8B311707").resolveHandle("ctembeddedfontlist240etype");

  public abstract List<CTEmbeddedFontListEntry> getEmbeddedFontList();

  /** @deprecated */
  public abstract CTEmbeddedFontListEntry[] getEmbeddedFontArray();

  public abstract CTEmbeddedFontListEntry getEmbeddedFontArray(int paramInt);

  public abstract int sizeOfEmbeddedFontArray();

  public abstract void setEmbeddedFontArray(CTEmbeddedFontListEntry[] paramArrayOfCTEmbeddedFontListEntry);

  public abstract void setEmbeddedFontArray(int paramInt, CTEmbeddedFontListEntry paramCTEmbeddedFontListEntry);

  public abstract CTEmbeddedFontListEntry insertNewEmbeddedFont(int paramInt);

  public abstract CTEmbeddedFontListEntry addNewEmbeddedFont();

  public abstract void removeEmbeddedFont(int paramInt);

  public static final class Factory
  {
    public static CTEmbeddedFontList newInstance()
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().newInstance(CTEmbeddedFontList.type, null));
    }

    public static CTEmbeddedFontList newInstance(XmlOptions paramXmlOptions)
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().newInstance(CTEmbeddedFontList.type, paramXmlOptions));
    }

    public static CTEmbeddedFontList parse(String paramString)
      throws XmlException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramString, CTEmbeddedFontList.type, null));
    }

    public static CTEmbeddedFontList parse(String paramString, XmlOptions paramXmlOptions)
      throws XmlException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramString, CTEmbeddedFontList.type, paramXmlOptions));
    }

    public static CTEmbeddedFontList parse(File paramFile)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramFile, CTEmbeddedFontList.type, null));
    }

    public static CTEmbeddedFontList parse(File paramFile, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramFile, CTEmbeddedFontList.type, paramXmlOptions));
    }

    public static CTEmbeddedFontList parse(URL paramURL)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramURL, CTEmbeddedFontList.type, null));
    }

    public static CTEmbeddedFontList parse(URL paramURL, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramURL, CTEmbeddedFontList.type, paramXmlOptions));
    }

    public static CTEmbeddedFontList parse(InputStream paramInputStream)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramInputStream, CTEmbeddedFontList.type, null));
    }

    public static CTEmbeddedFontList parse(InputStream paramInputStream, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramInputStream, CTEmbeddedFontList.type, paramXmlOptions));
    }

    public static CTEmbeddedFontList parse(Reader paramReader)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramReader, CTEmbeddedFontList.type, null));
    }

    public static CTEmbeddedFontList parse(Reader paramReader, XmlOptions paramXmlOptions)
      throws XmlException, IOException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramReader, CTEmbeddedFontList.type, paramXmlOptions));
    }

    public static CTEmbeddedFontList parse(XMLStreamReader paramXMLStreamReader)
      throws XmlException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramXMLStreamReader, CTEmbeddedFontList.type, null));
    }

    public static CTEmbeddedFontList parse(XMLStreamReader paramXMLStreamReader, XmlOptions paramXmlOptions)
      throws XmlException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramXMLStreamReader, CTEmbeddedFontList.type, paramXmlOptions));
    }

    public static CTEmbeddedFontList parse(Node paramNode)
      throws XmlException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramNode, CTEmbeddedFontList.type, null));
    }

    public static CTEmbeddedFontList parse(Node paramNode, XmlOptions paramXmlOptions)
      throws XmlException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramNode, CTEmbeddedFontList.type, paramXmlOptions));
    }

    /** @deprecated */
    public static CTEmbeddedFontList parse(XMLInputStream paramXMLInputStream)
      throws XmlException, XMLStreamException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramXMLInputStream, CTEmbeddedFontList.type, null));
    }

    /** @deprecated */
    public static CTEmbeddedFontList parse(XMLInputStream paramXMLInputStream, XmlOptions paramXmlOptions)
      throws XmlException, XMLStreamException
    {
      return ((CTEmbeddedFontList)XmlBeans.getContextTypeLoader().parse(paramXMLInputStream, CTEmbeddedFontList.type, paramXmlOptions));
    }

    /** @deprecated */
    public static XMLInputStream newValidatingXMLInputStream(XMLInputStream paramXMLInputStream)
      throws XmlException, XMLStreamException
    {
      return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(paramXMLInputStream, CTEmbeddedFontList.type, null);
    }

    /** @deprecated */
    public static XMLInputStream newValidatingXMLInputStream(XMLInputStream paramXMLInputStream, XmlOptions paramXmlOptions)
      throws XmlException, XMLStreamException
    {
      return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(paramXMLInputStream, CTEmbeddedFontList.type, paramXmlOptions);
    }
  }
}

/* Location:           E:\tmp\mavenRepo\org\apache\poi\ooxml-schemas\1.1\ooxml-schemas-1.1.jar
 * Qualified Name:     org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */