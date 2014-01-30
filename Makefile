# FTP Server
#
# Since january 30th, 2014
# Version 1.0
# Durieux Thomas
# Toulet Cyrille


# Config
SRC_PATH    = src
CLASS_PATH  = class

MANIFEST    = MANIFEST.mf

JAVAC       = javac
JARC        = jar

JAVAC_OPT  += -sourcepath $(SRC_PATH)
JAVAC_OPT   = -classpath $(CLASS_PATH)
JAVAC_OPT  += -d $(CLASS_PATH)

# Groups
CLASS       = ftp_server/Main.class
JAR         = ftp_server.jar


# All
all: $(CLASS) $(JAR)

# Subs
ftp_server/Main.class: $(SRC_PATH)/ftp_server/Main.java
	$(JAVAC) $(JAVAC_OPT) $^

ftp_server.jar: $(CLASS)
	$(JARC) cfm $@ $(MANIFEST) -C $(CLASS_PATH) $^


# Phony

.PHONY: clean mrproper

clean:
	rm -f $(CLASS_PATH)/$(CLASS)

mrproper: clean
	rm -f $(JAR)

