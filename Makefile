# FTP Server
#
# Since january 30th, 2014
# Version february 11th, 2014
# Durieux Thomas
# Toulet Cyrille


# Config
SRC_PATH    = src
CLASS_PATH  = class
#JAVAC       = /usr/lib/jvm/j2sdk1.7-oracle/bin/javac
JAVAC       = javac
JAVA        = java
JARC        = jar
JAVAC_OPT   = -sourcepath $(SRC_PATH)
JAVAC_OPT  += -classpath $(CLASS_PATH)
JAVAC_OPT  += -d $(CLASS_PATH)
JUNIT       = /usr/share/java/junit4.jar

# Groups
CLASS       = lille1/car2014/durieux_toulet/logs/*.class
CLASS      += lille1/car2014/durieux_toulet/ftp_server/*.class
CLASS      += lille1/car2014/durieux_toulet/exception/*.class
CLASS      += lille1/car2014/durieux_toulet/config/*.class
CLASS      += test/lille1/car2014/durieux_toulet/ftp_server/JUnitRunner.class

JAR         = ftp_server.jar

FILES       = lille1/car2014/durieux_toulet/config/*.ini
FILES      += lille1/car2014/durieux_toulet/logs/server_ftp.*

MANIFEST    = MANIFEST.mf

# All
all: $(CLASS) $(JAR)

# Generic
lille1/car2014/durieux_toulet/%.class: $(SRC_PATH)/lille1/car2014/durieux_toulet/%.java
	$(JAVAC) $(JAVAC_OPT) $^

test/lille1/car2014/durieux_toulet/%.class: $(SRC_PATH)/test/lille1/car2014/durieux_toulet/%.java
	$(JAVAC) $(JAVAC_OPT) -cp $(JUNIT) $^

# Specific
ftp_server.jar: $(CLASS)
	$(JARC) cfm $@ $(MANIFEST)
	cd $(CLASS_PATH) && $(JARC) uf ../$@ $^
	cd $(SRC_PATH) && $(JARC) uf ../$@ $(FILES)

# Phony
.PHONY: clean mrproper tests

tests: clean all
	cd $(CLASS_PATH) && $(JAVA) -cp $(JUNIT) test.lille1.car2014.durieux_toulet.ftp_server.JUnitRunner

clean:
	cd $(CLASS_PATH) && rm -f $(CLASS)

mrproper: clean
	rm -f $(JAR)

