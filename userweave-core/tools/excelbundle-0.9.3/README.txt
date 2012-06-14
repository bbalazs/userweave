
                        ============================
                         Readme for excelbundle 0.9
                        ============================

Introduction
-------------

 In software projects, language translation is often done by non-developers. In 
 Java, strings and other resources are often stored in resource bundles, that
 is, a bunch of .properties files containing the same keys but different values.
 These resource bundles aren't very friendly to the translator.

 The solution is excelbundle. excelbundle is a little tool that converts these
 resource bundles to Excel spreadsheets. Excel files are easier for
 non-developers as they probably have Excel installed, and if not, they can use
 OpenOffice.org or such.
 When received back from the translator, excelbundle can merge the result back
 into the source tree.

What it does
-------------

 excelbundle works by recursively searching the source tree for files matching 
 the pattern *_en.properties. This is because it has to be able to tell the
 difference between resource bundles and properties files used for other 
 things, such as application settings.

 In a constantly developed application, it is often hard to keep all language
 files in sync but there is often at least one language which contains all of
 the right keys and no more. To determine what keys are actually supposed to be
 exported, excelbundle uses the concept of a "reference language". This language
 is what specifies what keys are to be exported. If a key is in the reference
 langauge, it is exported. If it's not, it's not exported. It's also used when
 importing an Excel spreadsheet; if a certain key or file is not in the
 reference language, it is not merged back into the source tree.

Usage
-------------

 Syntax:
   java -jar excelbundle.jar -r <root> (-export|-import) <excel file> [-p]
   -l <languages> [-u <languages>] [-ref <reference language>] [-m <sheet map>]
  
 Explanation:
   -r      - The root of the source tree. In export mode, all paths in the 
             resulting Excel spreadsheet will be relative to this path. In
             import mode, all paths in the source Excel spreadsheet will be
             assumed to be relative to this path.
            
   -export - Exports resource bundles from the specified root to <excel file>.
  
   -import - Import resource bundles from <excel file> to the specified root.
   
   -p      - Only pretend doing an import. When this option is specified in
             import mode, nothing is actually imported. excelbundle only
             displays what would be done if this option was not specified.
  
   -l      - A comma separated list of language codes. In export mode, this
             specifies what languages should be included in the resulting Excel
             spreadsheet. In import mode, this specified what langauges are to
             be merged back into the source tree. To specify the 
             "default language", use "default".
  
   -u      - A comma separated list of language codes. When this is specified,
             only keys which are untranslated in at least one of the specified
             languages are included into the resulting Excel spreadsheet.
            
   -ref    - See description above for detailed explanation. Defaults to "en".
            
   -m      - Specifies a sheet map file to use. A sheet map file can be used to
             categorize different bundles into different sheet of the workbook
             in the resulting Excel spreadsheet. An example is provided in the
             sheetmap.xml.sample file included in the root of the distribution.
  
 Examples:
   
   $ java -jar excelbundle.jar -r myproject/ -export myproject_translate.xls
     -l en,sv -u sv
   
   Exports english and swedish from the directory "myproject" into the Excel
   spreadsheet "myproject_translate.xls" but includes only those keys which are
   untranslated for swedish.
   
   $ java -jar excelbundle.jar -r myproject/ -import myproject_translate.xls
     -l sv
   
   Merges swedish back into the source tree from the file exported in the
   previous example.

Ant tasks
-------------

 excelbundle includes Ant tasks for automating exporting and importing of
 resource bundles. An example build.xml (suprisingly named examplebuild.xml)
 can be found in the root of the distribution.
 
Contact
-------------

 For any questions, contact:
     Emil Eriksson, <shadewind[at]gmail[dot]com>
     