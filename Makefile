# FTP Server
#
# Since january 30th, 2014
# Version february 2nd, 2014
# Durieux Thomas
# Toulet Cyrille


# Config
SRC_PATH    = src
CLASS_PATH  = class
JAVAC       = javac
JARC        = jar
JAVAC_OPT   = -sourcepath $(SRC_PATH)
JAVAC_OPT  += -classpath $(CLASS_PATH)
JAVAC_OPT  += -d $(CLASS_PATH)

# Groups
CLASS       = lille1/car2014/durieux_toulet/logs/*.class
CLASS      += lille1/car2014/durieux_toulet/ftp_server/*.class
CLASS      += lille1/car2014/durieux_toulet/exception/*.class
CLASS      += lille1/car2014/durieux_toulet/config/*.class

JAR         = ftp_server.jar

FILES       = lille1/car2014/durieux_toulet/config/*.ini
FILES      += lille1/car2014/durieux_toulet/logs/server_ftp.*

MANIFEST    = MANIFEST.mf

# All
all: $(CLASS) $(JAR)

# Generic
lille1/car2014/durieux_toulet/%.class: $(SRC_PATH)/lille1/car2014/durieux_toulet/%.java
	$(JAVAC) $(JAVAC_OPT) $^

# Specific
ftp_server.jar: $(CLASS)
	$(JARC) cfm $@ $(MANIFEST)
	cd $(CLASS_PATH) && $(JARC) uf ../$@ $^
	cd $(SRC_PATH) && $(JARC) uf ../$@ $(FILES)

# Phony
.PHONY: clean mrproper

clean:
	cd $(CLASS_PATH) && rm -f $(CLASS)

mrproper: clean
	rm -f $(JAR)

