ifndef VERBOSE
.SILENT:
endif

RESOURCES := $(shell find . -name *.java -print)
all: hex

hex: $(RESOURCES)
	javac $(RESOURCES)

text: hex
	java src/Hex text

gui: hex
	java src/Hex gui
