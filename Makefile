help:
	@echo 'Makefile for JNI Example                                                '
	@echo '                                                                        '
	@echo 'Usage:                                                                  '
	@echo '   make jni-compile                 compile native C code               '
	@echo '   make java-compile                compile Java code                   '
	@echo '   make clean                       remove generated shared library     '
	@echo '   make run                         run JNI Example                     '
	@echo '                                                                        '
	@echo "Note: To work from Java IDE you'll probably need only jni-compile target"

jni-compile: net_sukharevd_jni_example_MatrixMultiplier.so

net_sukharevd_jni_example_MatrixMultiplier.so:
	gcc -O3 -fPIC -shared -I $(JAVA_HOME)/include/ \
	-I $(JAVA_HOME)/include/linux/ \
	src/net_sukharevd_jni_example_MatrixMultiplier.c \
	-o net_sukharevd_jni_example_MatrixMultiplier.so

java-compile: src/net/sukharevd/jni/example/%.class

src/net/sukharevd/jni/example/%.class:
	javac src/net/sukharevd/jni/example/*.java

clean:
	[ ! -f net_sukharevd_jni_example_MatrixMultiplier.so ] || \
	rm net_sukharevd_jni_example_MatrixMultiplier.so

run: clean java-compile jni-compile
	java -cp src/ net.sukharevd.jni.example.MatrixMultiplier

.PHONY: clean java-compile jni-compile run help
