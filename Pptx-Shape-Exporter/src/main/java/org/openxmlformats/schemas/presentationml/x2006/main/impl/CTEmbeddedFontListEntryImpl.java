package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.TypeStore;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry;

public class CTEmbeddedFontListEntryImpl extends XmlComplexContentImpl
  implements CTEmbeddedFontListEntry
{
  private static final QName FONT$0 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "font");
  private static final QName REGULAR$2 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "regular");
  private static final QName BOLD$4 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bold");
  private static final QName ITALIC$6 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "italic");
  private static final QName BOLDITALIC$8 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "boldItalic");

  public CTEmbeddedFontListEntryImpl(SchemaType paramSchemaType)
  {
    super(paramSchemaType);
  }

  public CTTextFont getFont()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTTextFont localCTTextFont = null;
      localCTTextFont = (CTTextFont)get_store().find_element_user(FONT$0, 0);
      if (localCTTextFont == null)
        return null;
      return localCTTextFont;
    }
  }

  public void setFont(CTTextFont paramCTTextFont)
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTTextFont localCTTextFont = null;
      localCTTextFont = (CTTextFont)get_store().find_element_user(FONT$0, 0);
      if (localCTTextFont == null)
        localCTTextFont = (CTTextFont)get_store().add_element_user(FONT$0);
      localCTTextFont.set(paramCTTextFont);
    }
  }

  public CTTextFont addNewFont()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTTextFont localCTTextFont = null;
      localCTTextFont = (CTTextFont)get_store().add_element_user(FONT$0);
      return localCTTextFont;
    }
  }

  public CTEmbeddedFontDataId getRegular()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().find_element_user(REGULAR$2, 0);
      if (localCTEmbeddedFontDataId == null)
        return null;
      return localCTEmbeddedFontDataId;
    }
  }

  public boolean isSetRegular()
  {
    synchronized (monitor())
    {
      check_orphaned();
      return (get_store().count_elements(REGULAR$2) != 0);
    }
  }

  public void setRegular(CTEmbeddedFontDataId paramCTEmbeddedFontDataId)
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().find_element_user(REGULAR$2, 0);
      if (localCTEmbeddedFontDataId == null)
        localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().add_element_user(REGULAR$2);
      localCTEmbeddedFontDataId.set(paramCTEmbeddedFontDataId);
    }
  }

  public CTEmbeddedFontDataId addNewRegular()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().add_element_user(REGULAR$2);
      return localCTEmbeddedFontDataId;
    }
  }

  public void unsetRegular()
  {
    synchronized (monitor())
    {
      check_orphaned();
      get_store().remove_element(REGULAR$2, 0);
    }
  }

  public CTEmbeddedFontDataId getBold()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().find_element_user(BOLD$4, 0);
      if (localCTEmbeddedFontDataId == null)
        return null;
      return localCTEmbeddedFontDataId;
    }
  }

  public boolean isSetBold()
  {
    synchronized (monitor())
    {
      check_orphaned();
      return (get_store().count_elements(BOLD$4) != 0);
    }
  }

  public void setBold(CTEmbeddedFontDataId paramCTEmbeddedFontDataId)
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().find_element_user(BOLD$4, 0);
      if (localCTEmbeddedFontDataId == null)
        localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().add_element_user(BOLD$4);
      localCTEmbeddedFontDataId.set(paramCTEmbeddedFontDataId);
    }
  }

  public CTEmbeddedFontDataId addNewBold()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().add_element_user(BOLD$4);
      return localCTEmbeddedFontDataId;
    }
  }

  public void unsetBold()
  {
    synchronized (monitor())
    {
      check_orphaned();
      get_store().remove_element(BOLD$4, 0);
    }
  }

  public CTEmbeddedFontDataId getItalic()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().find_element_user(ITALIC$6, 0);
      if (localCTEmbeddedFontDataId == null)
        return null;
      return localCTEmbeddedFontDataId;
    }
  }

  public boolean isSetItalic()
  {
    synchronized (monitor())
    {
      check_orphaned();
      return (get_store().count_elements(ITALIC$6) != 0);
    }
  }

  public void setItalic(CTEmbeddedFontDataId paramCTEmbeddedFontDataId)
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().find_element_user(ITALIC$6, 0);
      if (localCTEmbeddedFontDataId == null)
        localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().add_element_user(ITALIC$6);
      localCTEmbeddedFontDataId.set(paramCTEmbeddedFontDataId);
    }
  }

  public CTEmbeddedFontDataId addNewItalic()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().add_element_user(ITALIC$6);
      return localCTEmbeddedFontDataId;
    }
  }

  public void unsetItalic()
  {
    synchronized (monitor())
    {
      check_orphaned();
      get_store().remove_element(ITALIC$6, 0);
    }
  }

  public CTEmbeddedFontDataId getBoldItalic()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().find_element_user(BOLDITALIC$8, 0);
      if (localCTEmbeddedFontDataId == null)
        return null;
      return localCTEmbeddedFontDataId;
    }
  }

  public boolean isSetBoldItalic()
  {
    synchronized (monitor())
    {
      check_orphaned();
      return (get_store().count_elements(BOLDITALIC$8) != 0);
    }
  }

  public void setBoldItalic(CTEmbeddedFontDataId paramCTEmbeddedFontDataId)
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().find_element_user(BOLDITALIC$8, 0);
      if (localCTEmbeddedFontDataId == null)
        localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().add_element_user(BOLDITALIC$8);
      localCTEmbeddedFontDataId.set(paramCTEmbeddedFontDataId);
    }
  }

  public CTEmbeddedFontDataId addNewBoldItalic()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontDataId localCTEmbeddedFontDataId = null;
      localCTEmbeddedFontDataId = (CTEmbeddedFontDataId)get_store().add_element_user(BOLDITALIC$8);
      return localCTEmbeddedFontDataId;
    }
  }

  public void unsetBoldItalic()
  {
    synchronized (monitor())
    {
      check_orphaned();
      get_store().remove_element(BOLDITALIC$8, 0);
    }
  }
}

/* Location:           E:\tmp\mavenRepo\org\apache\poi\ooxml-schemas\1.1\ooxml-schemas-1.1.jar
 * Qualified Name:     org.openxmlformats.schemas.presentationml.x2006.main.impl.CTEmbeddedFontListEntryImpl
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */