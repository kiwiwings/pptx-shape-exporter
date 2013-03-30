package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.TypeStore;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry;

public class CTEmbeddedFontListImpl extends XmlComplexContentImpl
  implements CTEmbeddedFontList
{
  private static final QName EMBEDDEDFONT$0 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "embeddedFont");

  public CTEmbeddedFontListImpl(SchemaType paramSchemaType)
  {
    super(paramSchemaType);
  }

  public List<CTEmbeddedFontListEntry> getEmbeddedFontList()
  {
    synchronized (monitor())
    {
      check_orphaned();
      return new AbstractList()
      {
        public CTEmbeddedFontListEntry get(int paramInt)
        {
          return CTEmbeddedFontListImpl.this.getEmbeddedFontArray(paramInt);
        }

        public CTEmbeddedFontListEntry set(int paramInt, CTEmbeddedFontListEntry paramCTEmbeddedFontListEntry)
        {
          CTEmbeddedFontListEntry localCTEmbeddedFontListEntry = CTEmbeddedFontListImpl.this.getEmbeddedFontArray(paramInt);
          CTEmbeddedFontListImpl.this.setEmbeddedFontArray(paramInt, paramCTEmbeddedFontListEntry);
          return localCTEmbeddedFontListEntry;
        }

        public void add(int paramInt, CTEmbeddedFontListEntry paramCTEmbeddedFontListEntry)
        {
          CTEmbeddedFontListImpl.this.insertNewEmbeddedFont(paramInt).set(paramCTEmbeddedFontListEntry);
        }

        public CTEmbeddedFontListEntry remove(int paramInt)
        {
          CTEmbeddedFontListEntry localCTEmbeddedFontListEntry = CTEmbeddedFontListImpl.this.getEmbeddedFontArray(paramInt);
          CTEmbeddedFontListImpl.this.removeEmbeddedFont(paramInt);
          return localCTEmbeddedFontListEntry;
        }

        public int size()
        {
          return CTEmbeddedFontListImpl.this.sizeOfEmbeddedFontArray();
        }
      };
    }
  }

  public CTEmbeddedFontListEntry[] getEmbeddedFontArray()
  {
    synchronized (monitor())
    {
      check_orphaned();
      ArrayList localArrayList = new ArrayList();
      get_store().find_all_element_users(EMBEDDEDFONT$0, localArrayList);
      CTEmbeddedFontListEntry[] arrayOfCTEmbeddedFontListEntry = new CTEmbeddedFontListEntry[localArrayList.size()];
      localArrayList.toArray(arrayOfCTEmbeddedFontListEntry);
      return arrayOfCTEmbeddedFontListEntry;
    }
  }

  public CTEmbeddedFontListEntry getEmbeddedFontArray(int paramInt)
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontListEntry localCTEmbeddedFontListEntry = null;
      localCTEmbeddedFontListEntry = (CTEmbeddedFontListEntry)get_store().find_element_user(EMBEDDEDFONT$0, paramInt);
      if (localCTEmbeddedFontListEntry == null)
        throw new IndexOutOfBoundsException();
      return localCTEmbeddedFontListEntry;
    }
  }

  public int sizeOfEmbeddedFontArray()
  {
    synchronized (monitor())
    {
      check_orphaned();
      return get_store().count_elements(EMBEDDEDFONT$0);
    }
  }

  public void setEmbeddedFontArray(CTEmbeddedFontListEntry[] paramArrayOfCTEmbeddedFontListEntry)
  {
    synchronized (monitor())
    {
      check_orphaned();
      arraySetterHelper(paramArrayOfCTEmbeddedFontListEntry, EMBEDDEDFONT$0);
    }
  }

  public void setEmbeddedFontArray(int paramInt, CTEmbeddedFontListEntry paramCTEmbeddedFontListEntry)
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontListEntry localCTEmbeddedFontListEntry = null;
      localCTEmbeddedFontListEntry = (CTEmbeddedFontListEntry)get_store().find_element_user(EMBEDDEDFONT$0, paramInt);
      if (localCTEmbeddedFontListEntry == null)
        throw new IndexOutOfBoundsException();
      localCTEmbeddedFontListEntry.set(paramCTEmbeddedFontListEntry);
    }
  }

  public CTEmbeddedFontListEntry insertNewEmbeddedFont(int paramInt)
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontListEntry localCTEmbeddedFontListEntry = null;
      localCTEmbeddedFontListEntry = (CTEmbeddedFontListEntry)get_store().insert_element_user(EMBEDDEDFONT$0, paramInt);
      return localCTEmbeddedFontListEntry;
    }
  }

  public CTEmbeddedFontListEntry addNewEmbeddedFont()
  {
    synchronized (monitor())
    {
      check_orphaned();
      CTEmbeddedFontListEntry localCTEmbeddedFontListEntry = null;
      localCTEmbeddedFontListEntry = (CTEmbeddedFontListEntry)get_store().add_element_user(EMBEDDEDFONT$0);
      return localCTEmbeddedFontListEntry;
    }
  }

  public void removeEmbeddedFont(int paramInt)
  {
    synchronized (monitor())
    {
      check_orphaned();
      get_store().remove_element(EMBEDDEDFONT$0, paramInt);
    }
  }
}

/* Location:           E:\tmp\mavenRepo\org\apache\poi\ooxml-schemas\1.1\ooxml-schemas-1.1.jar
 * Qualified Name:     org.openxmlformats.schemas.presentationml.x2006.main.impl.CTEmbeddedFontListImpl
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */