package org.apache.poi.xslf.usermodel;

import java.awt.Color;
import java.util.UUID;

import org.apache.poi.xslf.model.CharacterPropertyFetcher;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextField;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.STSchemeColorVal;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;

public class XSLFTextField extends XSLFTextRun {
	
    private final CTTextField _r;
	
	XSLFTextField(CTTextField r, XSLFTextParagraph p) {
		super(null,p);
		this._r = r;
		_r.setId("{"+UUID.randomUUID().toString()+"}");
	}

    public String getText(){
        return _r.getT();
    }
    
    String getRenderableText(){
        String txt = _r.getT();
        TextCap cap = getTextCap();
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < txt.length(); i++) {
            char c = txt.charAt(i);
            if(c == '\t') {
                // TODO: finish support for tabs
                buf.append("  ");
            } else {
                switch (cap){
                    case ALL:
                        buf.append(Character.toUpperCase(c));
                        break;
                    case SMALL:
                        buf.append(Character.toLowerCase(c));
                        break;
                    default:
                        buf.append(c);
                }
            }
        }

        return buf.toString();
    }

    public void setText(String text){
        _r.setT(text);
    }
    
    public String getId(String id){
    	return _r.getId();
    }
    
    public void setId(String id){
    	_r.setId(id);
    }
    
    @Override
    public CTRegularTextRun getXmlObject() {
    	throw new RuntimeException("XSLFTextField (aka Fake-XSLFTextRun) doesn't support getXmlObject()" +
    			" - use getXmlObjectField() instead!");
    }
    
    public CTTextField getXmlObjectField(){
        return _r;
    }
    
    @Override
    protected CTTextCharacterProperties getRPr(){
        return _r.isSetRPr() ? _r.getRPr() : _r.addNewRPr();
    }

    @Override
    public XSLFHyperlink createHyperlink(){
        XSLFHyperlink link = new XSLFHyperlink(_r.getRPr().addNewHlinkClick(), this);
        return link;
    }

    @Override
    public XSLFHyperlink getHyperlink(){
        if(!_r.getRPr().isSetHlinkClick()) return null;
        return new XSLFHyperlink(_r.getRPr().getHlinkClick(), this);
    }

    @Override
    public Color getFontColor(){
        final XSLFTheme theme = getParentParagraph().getParentShape().getSheet().getTheme();
        CTShapeStyle style = getParentParagraph().getParentShape().getSpStyle();
        final CTSchemeColor phClr = style == null ? null : style.getFontRef().getSchemeClr();

        CharacterPropertyFetcher<Color> fetcher = new CharacterPropertyFetcher<Color>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                CTSolidColorFillProperties solidFill = props.getSolidFill();
                if(solidFill != null) {
                    boolean useCtxColor =
                            (solidFill.isSetSchemeClr() && solidFill.getSchemeClr().getVal() == STSchemeColorVal.PH_CLR)
                            || isFetchingFromMaster;
                    Color c = new XSLFColor(solidFill, theme, useCtxColor ? phClr : null).getColor();
                    setValue(c);
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue();
    }    
    
    /**
     * @return font size in points or -1 if font size is not set.
     */
    @Override
    public double getFontSize(){
        double scale = 1;
        CTTextNormalAutofit afit = getParentParagraph().getParentShape().getTextBodyPr().getNormAutofit();
        if(afit != null) scale = (double)afit.getFontScale() / 100000;

        CharacterPropertyFetcher<Double> fetcher = new CharacterPropertyFetcher<Double>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                if(props.isSetSz()){
                    setValue(props.getSz()*0.01);
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue() == null ? -1 : fetcher.getValue()*scale;
    }

    /**
     *
     * @return the spacing between characters within a text run,
     * If this attribute is omitted than a value of 0 or no adjustment is assumed.
     */
    @Override
    public double getCharacterSpacing(){

        CharacterPropertyFetcher<Double> fetcher = new CharacterPropertyFetcher<Double>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                if(props.isSetSpc()){
                    setValue(props.getSpc()*0.01);
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue() == null ? 0 : fetcher.getValue();
    }

    /**
     * @return  font family or null if not set
     */
    @Override
    public String getFontFamily(){
        final XSLFTheme theme = getParentParagraph().getParentShape().getSheet().getTheme();

        CharacterPropertyFetcher<String> visitor = new CharacterPropertyFetcher<String>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                CTTextFont font = props.getLatin();
                if(font != null){
                    String typeface = font.getTypeface();
                    if("+mj-lt".equals(typeface)) {
                        typeface = theme.getMajorFont();
                    } else if ("+mn-lt".equals(typeface)){
                        typeface = theme.getMinorFont();
                    }
                    setValue(typeface);
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(visitor);

        return  visitor.getValue();
    }

    @Override
    public byte getPitchAndFamily(){
        CharacterPropertyFetcher<Byte> visitor = new CharacterPropertyFetcher<Byte>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                CTTextFont font = props.getLatin();
                if(font != null){
                    setValue(font.getPitchFamily());
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(visitor);

        return  visitor.getValue() == null ? 0 : visitor.getValue();
    }

    /**
     * @return whether a run of text will be formatted as strikethrough text. Default is false.
     */
    @Override
    public boolean isStrikethrough() {
        CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher<Boolean>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                if(props.isSetStrike()){
                    setValue(props.getStrike() != STTextStrikeType.NO_STRIKE);
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue() == null ? false : fetcher.getValue();
    }

    /**
     * @return whether a run of text will be formatted as a superscript text. Default is false.
     */
    @Override
    public boolean isSuperscript() {
        CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher<Boolean>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                if(props.isSetBaseline()){
                    setValue(props.getBaseline() > 0);
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue() == null ? false : fetcher.getValue();
    }

    /**
     * @return whether a run of text will be formatted as a superscript text. Default is false.
     */
    @Override
    public boolean isSubscript() {
        CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher<Boolean>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                if(props.isSetBaseline()){
                    setValue(props.getBaseline() < 0);
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue() == null ? false : fetcher.getValue();
    }

    /**
     * @return whether a run of text will be formatted as a superscript text. Default is false.
     */
    @Override
    public TextCap getTextCap() {
        CharacterPropertyFetcher<TextCap> fetcher = new CharacterPropertyFetcher<TextCap>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                if(props.isSetCap()){
                    int idx = props.getCap().intValue() - 1;
                    setValue(TextCap.values()[idx]);
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue() == null ? TextCap.NONE : fetcher.getValue();
    }

    /**
     * @return whether this run of text is formatted as bold text
     */
    @Override
    public boolean isBold(){
        CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher<Boolean>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                if(props.isSetB()){
                    setValue(props.getB());
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue() == null ? false : fetcher.getValue();
    }

    /**
     * @return whether this run of text is formatted as italic text
     */
    @Override
    public boolean isItalic(){
        CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher<Boolean>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                if(props.isSetI()){
                    setValue(props.getI());
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue() == null ? false : fetcher.getValue();
    }

    /**
     * @return whether this run of text is formatted as underlined text
     */
    @Override
    public boolean isUnderline(){
        CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher<Boolean>(getParentParagraph().getLevel()){
            public boolean fetch(CTTextCharacterProperties props){
                if(props.isSetU()){
                    setValue(props.getU() != STTextUnderlineType.NONE);
                    return true;
                }
                return false;
            }
        };
        fetchCharacterProperty(fetcher);
        return fetcher.getValue() == null ? false : fetcher.getValue();
    }

    
    /*
     * It's a pity, that all the other methods which use fetchCharacterProperty
     * need to be copied, just because this is a private method in XSLFTextRun ...
     */
    private boolean fetchCharacterProperty(CharacterPropertyFetcher<?> fetcher){
        boolean ok = false;

        if(_r.isSetRPr()) ok = fetcher.fetch(getRPr());

        if(!ok) {
            XSLFTextShape shape = getParentParagraph().getParentShape();
            ok = shape.fetchShapeProperty(fetcher);
            if(!ok){
                CTPlaceholder ph = shape.getCTPlaceholder();
                if(ph == null){
                    // if it is a plain text box then take defaults from presentation.xml
                    XMLSlideShow ppt = shape.getSheet().getSlideShow();
                    CTTextParagraphProperties themeProps = ppt.getDefaultParagraphStyle(getParentParagraph().getLevel());
                    if(themeProps != null) {
                        fetcher.isFetchingFromMaster = true;
                        ok = fetcher.fetch(themeProps);
                    }
                }
                if (!ok) {
                    CTTextParagraphProperties defaultProps =  getParentParagraph().getDefaultMasterStyle();
                    if(defaultProps != null) {
                        fetcher.isFetchingFromMaster = true;
                        ok = fetcher.fetch(defaultProps);
                    }
                }
            }
        }

        return ok;
    }

}
