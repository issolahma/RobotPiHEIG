TOOLCHAIN = armv8-rpi3-linux-gnueabihf
TOOLCHAIN_DIR = /opt/x-tools/$(TOOLCHAIN)
SYSROOT = $(TOOLCHAIN_DIR)/$(TOOLCHAIN)/sysroot/ #TOOLCHAIN_DIR inclut déjà TOOLCHAIN
BUILDTOOLS = $(TOOLCHAIN_DIR)/bin/$(TOOLCHAIN)

CC = gcc
LD = ld
AR = ar
CFLAGS = -g -Wall
LDFLAGS = -lc -l -lbcm2835 -lm
ARFLAGS = -crs
TARGETFOLDER = bin
APPNAME = RoboPiServer
LIBNAME = libMotor.a
LIBDIR = src/motorLib

SUBDIR = src test
OUT_DIR = ./bin
OBJ_DIR = ./build

SRCS = $(wildcard *.c $(foreach dir, $(SUBDIR), $(dir)/*.c)) #Les tests seront un exécutable à part, pas besoin de les ajouter 
OBJS = $(addprefix $(OBJ_DIR)/, $(SRCS:c=o))

LIBSRCS = $(wildcard $(LIBDIR)/*.c)
LIBOBJS = $(addprefix $(OBJ_DIR)/, $(LIBSRCS:.c=.o))

.PHONY: clean
.PHONY: lib

all: $(OUT_DIR)/$(APPNAME)

$(OUT_DIR)/$(APPNAME): $(OBJS)
	$(BUILDTOOLS)-$(CC) $(LIBNAME) $(OBJS) $(LDFLAGS) -o $@

$(OBJ_DIR)/%.o: %.c
	mkdir -p $(@D)
	$(BUILDTOOLS)-$(CC) -o $@ -c $< $(CFLAGS)

clean:
	rm -rf $(OUT_DIR)/*
	rm -rf $(OBJ_DIR)/*

test:

new_file:
	@echo "Entrez le nom des fichiers que vous voulez créer"
	@read fileName; \
	touch src/$$fileName.c src/include/$$fileName.h test/test_$$fileName.c

lib: $(LIBNAME)

$(LIBNAME): $(LIBOBJS)
	$(TOOLCHAIN)-$(AR) $(ARFLAGS) $(LIBNAME) $(LIBOBJS) 
	