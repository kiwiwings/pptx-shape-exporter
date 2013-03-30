package de.kiwiwings.jasperreports.exporter;

import java.awt.Font;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Locale;
import java.util.Map;

public interface FontResolver {
	boolean outlineFont(String family, Locale locale);
	boolean embedFont(String family, Locale locale);
	String exportFont(String family, Locale locale);
	Font loadFont(Map<Attribute,Object> attr, Locale locale);
}
