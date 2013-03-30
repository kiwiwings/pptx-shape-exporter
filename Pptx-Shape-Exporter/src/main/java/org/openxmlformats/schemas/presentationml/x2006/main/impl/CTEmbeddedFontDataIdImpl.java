package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.TypeStore;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId;

public class CTEmbeddedFontDataIdImpl extends XmlComplexContentImpl
  implements CTEmbeddedFontDataId
{
  private static final QName ID$0 = new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "id");

  public CTEmbeddedFontDataIdImpl(SchemaType paramSchemaType)
  {
    super(paramSchemaType);
  }

  public String getId()
  {
    synchronized (monitor())
    {
      check_orphaned();
      SimpleValue localSimpleValue = null;
      localSimpleValue = (SimpleValue)get_store().find_attribute_user(ID$0);
      if (localSimpleValue == null)
        return null;
      return localSimpleValue.getStringValue();
    }
  }

  public STRelationshipId xgetId()
  {
    synchronized (monitor())
    {
      check_orphaned();
      STRelationshipId localSTRelationshipId = null;
      localSTRelationshipId = (STRelationshipId)get_store().find_attribute_user(ID$0);
      return localSTRelationshipId;
    }
  }

  public void setId(String paramString)
  {
    synchronized (monitor())
    {
      check_orphaned();
      SimpleValue localSimpleValue = null;
      localSimpleValue = (SimpleValue)get_store().find_attribute_user(ID$0);
      if (localSimpleValue == null)
        localSimpleValue = (SimpleValue)get_store().add_attribute_user(ID$0);
      localSimpleValue.setStringValue(paramString);
    }
  }

  public void xsetId(STRelationshipId paramSTRelationshipId)
  {
    synchronized (monitor())
    {
      check_orphaned();
      STRelationshipId localSTRelationshipId = null;
      localSTRelationshipId = (STRelationshipId)get_store().find_attribute_user(ID$0);
      if (localSTRelationshipId == null)
        localSTRelationshipId = (STRelationshipId)get_store().add_attribute_user(ID$0);
      localSTRelationshipId.set(paramSTRelationshipId);
    }
  }
}

/* Location:           E:\tmp\mavenRepo\org\apache\poi\ooxml-schemas\1.1\ooxml-schemas-1.1.jar
 * Qualified Name:     org.openxmlformats.schemas.presentationml.x2006.main.impl.CTEmbeddedFontDataIdImpl
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */