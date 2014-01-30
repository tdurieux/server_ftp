# FTP Server
#
# Since january 30th, 2014
# Version 1.0
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
CLASS      += lille1/car2014/durieux_toulet/common/*.class
CLASS      += lille1/car2014/durieux_toulet/ftp_server/*.class
#CLASS      += lille1/car2014/durieux_toulet/exception/*.class
CLASS      += lille1/car2014/durieux_toulet/ftp_server/*.class

JAR         = ftp_server.jar

CONFIG      = lille1/car2014/durieux_toulet/config/*.ini

MANIFEST    = MANIFEST.mf

# All
all: $(CLASS) $(JAR)

# Generic
lille1/car2014/durieux_toulet/%.class: $(SRC_PATH)/lille1/car2014/durieux_toulet/%.java
	$(JAVAC) $(JAVAC_OPT) $^

# Specific
ftp_server.jar: $(CLASS)
	#$(JARC) cfm $@ $(MANIFEST) -C $(CLASS_PATH) $^
	$(JARC) cfm $@ $(MANIFEST)
	#TODO Add files to archive
	#$(JARC) uf $@ -C $(SRC_PATH) $(CONFIG)

# Phony
.PHONY: clean mrproper

clean:
	cd $(CLASS_PATH) && rm -f $(CLASS)

mrproper: clean
	rm -f $(JAR)

