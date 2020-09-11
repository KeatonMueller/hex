RESOURCES := $(shell find . -name *.java -print)
all: hex

hex: $(RESOURCES)
	javac src/Hex.java

run: hex
	java src/Hex
