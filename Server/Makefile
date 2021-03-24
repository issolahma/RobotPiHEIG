TOOLCHAIN = arm-linux-gnueabi
CC = gcc
LD = ld
CFLAGS = -g
LDFLAGS = 
TARGETFOLDER = bin/
APPNAME = RoboPiServer

FILES = main.o motor.o server.o protocol.o

all: $(APPNAME)

$(APPNAME): $(FILES)
	$(TOOLCHAIN)-$(LINKER) $(LDFLAGS) $(FILES) -o $(TARGETFOLDER)/$@

%.o: %.c %.h
	$(TOOLCHAIN)-$(CC) $(CFLAGS) $^ -o $@

clean:
	rm -rf /bin
	rm -rf *.o

test:

new_file:
	echo "Entrez le nom des fichiers que vous voulez créer"
	read fileName
	touch src/$$filename.c
