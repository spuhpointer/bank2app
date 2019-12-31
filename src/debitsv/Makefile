
SOURCEDIR=.
SOURCES := $(shell find $(SOURCEDIR) -name '*.java')
CLASSES = $(addsuffix .class, $(basename $(SOURCES)))

BINARY=debitsv
PKG=debitsv.jar
MAINCLASS=Debitsv

%.class: %.java
	javac -cp /usr/share/java/enduroxjava.jar:../ubftab/ubftab.jar $<

$(PKG): $(CLASSES)
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Main-Class: $(MAINCLASS)" >> manifest.txt
	@echo "" >> manifest.txt
	jar -cmf manifest.txt $(PKG) $(CLASSES)
	- rm manifest.txt
	exjld -n -o $(BINARY) -m '$(MAINCLASS)' -L/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64 -L /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server $(PKG)

.PHONY: clean
clean:
	- rm *.class manifest.txt $(BINARY) $(PKG)

run:
	LD_LIBRARY_PATH=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server java -classpath /usr/share/java/enduroxjava.jar:./$(PKG) $(MAINCLASS)
