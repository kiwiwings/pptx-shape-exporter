Powerpoint Export for Jasper-Reports

Differences to Standard-PPTXExporter:
- JFreeChart elements are rendered as powerpoint shapes instead of blury pixel images
- Font-Embedding / Font-as-curves
- Move common elements to background
- Use of apache poi ooxml instead of custom pptx archive


Font-Embedding
... is implemented by the google snftly code library (https://code.google.com/p/sfntly/).
Currently only non-MTX (MicroType Express) will be embedded, but that
can be simpled changed in the embedFont method. Be aware that not all truetype fonts
can be converted to MTX.
If you need to publish the powerpoint to an unknown readers, you've
might be better off, rendering the text (the logo?) as curves, as
font embedding works by installing the font into the windows fonts directory,
which is not always allowed - especially with coporate environments *sigh*
As jasperreports and also snftly don't work with OpenType fonts, you might need
to convert them first with fontforge (fontforge.sourceforge.net)

To specify which font embedding method to be used, specify it in the fonts.xml,
where "none","curves" or "embed" are the options:

<fontFamilies>
	<fontFamily name="Allianz-Positiv">
		<normal>fonts/AllianzPositiv/Allianz-Positiv.ttf</normal>
		<pdfEncoding>Identity-H</pdfEncoding>
		<pdfEmbedded>true</pdfEmbedded>
		<exportFonts>
			<export key="net.sf.jasperreports.pptx">curves</export>
		</exportFonts>
	</fontFamily>
</fontFamilies>

The option:
- none: Font faces are simply referenced, but not defined at all
- embed: the font is embedded in the pptx as EOT (embedded open type - true type container)
- curves: the font outline is rendered as shape



Background
JasperReports copies the elements of the background band to every page
as the first elements. The exporter compares the first elements until
they differ. The common elements will be created on the master slide.
Everything beyond the common elements will be created on every page.
So if you put variables (like the page number) on the background page,
make sure, to move the common elements first.


Apache POI ooxml instead of PptxZip
As the Graphics2d implementation already made heavy use of poi ooxml,
it was straightforward also to implement the other exporter functions
with poi ooxml instead of the various helper classes.


ToDo:
the exporter is still work in progress ... well currently our needed features are inside,
but there's still a lot todo:
- Font as curves with text transformation (outside jfreechart) haven't been tested at all
- Image cropping and the generic element handling  hasn't been implemented,
  i.e. left out on copy-&-paste from the standard exporter
- Hyperlinking different elements
- Find out howto identify the pfm (???) font classifcation and the pitch for a more detailed
  embedded font-list
- ...

Contact:
- http://bit.ly/kiwimail

License:
- I don't care ... the classes use apache poi, jasperreports, copied elements from other
  sources (mentioned below) ... probably Apache License (http://www.apache.org/licenses/LICENSE-2.0)
  fits best ...


Interesting stuff:
The project originated, because we are generating heaps of slides including
diagramms and those diagramms looked blury, although we've raised the DPI setting.
The first implementation was using the freehep EMF vector graphics library as
suggested by this post:
- http://community.jaspersoft.com/questions/518695/excel-image-quality-awful#node-576441
- http://community.jaspersoft.com/sites/default/files/bug_files/jexcelapiexporter.java
... but the problem is, EMF graphics look horrible in Powerpoint, because they aren't
antialiased, furthermore there's no font embedding possible. So a different solution
was needed. After searching a bit for "vector graphics powerpoint", I've found out, that
there's already a quite complete implementation in apache poi for hslf:
http://poi.apache.org/apidocs/org/apache/poi/hslf/model/PPGraphics2D.html
... strangely, it's not anymore in version 3.9, maybe under a different name??

To get good results, I've decided to map the java awt elements as best as possible
to pptx shapes and therefore I haven't used a AWT GeneralPath element for every element.
Especially dodgy is the transformation of AWT angles to pptx angles, because the 
first are skewed (45 degrees point always to the right top edge) whereas the second are
depending on the bounding box. After hours of searching for the wrong keywords, I've
specified a more geographical search and promptly found the solution on:
http://www.onlinemathe.de/forum/Problem-bei-Winkelberechnungen-einer-Ellipse

The latest version of google snftly was backported (i.e. all the @overwrite annotations
on interface were removed) to java 1.5, because that's the environment neccessary for my
workplace (websphere 6.1).
Furthermore the library has been provided, because there's no maven repo hosting it...

The minimized apache poi ooxml doesn't contain all the ooxml schema classes.
When elements of the ooxml-schemas-1.1 artifact are referenced, these need to be integrated 
in the maven build. You'll need to run the test class with the vm option -verbose:class and
check the log for references to the ooxml-schemas.jar. These classes need to be added
in the shade-maven-plugin.


Other links - my personal bookmark history:
- After finding a lots of link describing the reversation of a affine transformation wouldn't
  be possible at all, I've found this quite useful source:
  https://java.net/svn/javagraphics~svn/trunk/src/com/bric/geom/TransformUtils.java
  This is necessary, when rotated text elements need to be defined the powerpoint way,
  i.e. bounding box and rotation attribute

- I was looking for a way of generating fntdata files, here I finally found the links to google snftly
  http://typophile.com/node/89218

- https://code.google.com/p/sfntly/wiki/MicroTypeExpress

- When you play around with preset geometry elements, you need to know the default values of
  the extension element ... but you need to play around with them anyway ...
  http://msdn.microsoft.com/en-us/library/documentformat.openxml.presentation.font.aspx

- Looking for the ooxml schema is quite confusing, as wikipedia points to the container and
  not the document schemas, what you need to search for is "Ecma TC45 OOXML Standard Schemas - Draft 1.3.zip"
  http://www.ecma-international.org/news/TC45_current_work/TC45-2006-50_draft14.htm

- http://jasperreports.sourceforge.net/sample.reference/jfreechart/index.html
- http://www.jfree.org/phpBB2/viewtopic.php?p=67654
- http://community.jaspersoft.com/questions/511011/vectorial-images-pdf-report-wo-raster
- http://blogs.msdn.com/b/openspecification/archive/2011/11/14/how-to-use-the-presetshapedefinitions-xml-file-and-fun-with-drawingml.aspx