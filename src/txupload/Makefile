
SOURCEDIR=.
SOURCES := $(shell find $(SOURCEDIR) -name '*.java')
CLASSES = $(addsuffix .class, $(basename $(SOURCES)))


PKG=txuploadcl.jar
MAINCLASS=Txuploadcl

%.class: %.java
	javac -cp /usr/share/java/enduroxjava.jar:../ubftab/ubftab.jar $<

$(PKG): $(CLASSES)
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Main-Class: $(MAINCLASS)" >> manifest.txt
	@echo "" >> manifest.txt
	jar -cmf manifest.txt $(PKG) $(CLASSES)
	- rm manifest.txt


.PHONY: clean
clean:
	- rm *.class manifest.txt $(BINARY) $(PKG)

