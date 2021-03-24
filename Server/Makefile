TOOLCHAIN_DIR = /opt/cross-pi-gcc
TOOLCHAIN = arm-linux-gnueabihf
SYSROOT = $(TOOLCHAIN_DIR)/$(TOOLCHAIN)
BUILDTOOLS = $(TOOLCHAIN_DIR)/bin/$(TOOLCHAIN)

CC = gcc
LD = ld
CFLAGS = -g --sysroot=$(SYSROOT)
LDFLAGS = --sysroot=$(SYSROOT) -lc
TARGETFOLDER = bin
APPNAME = RoboPiServer

SUBDIR = src test
OUT_DIR = ./bin
OBJ_DIR = ./build

SRCS = $(wildcard *.c $(foreach fd, $(SUBDIR), $(fd)/*.c))
OBJS = $(addprefix $(OBJ_DIR)/, $(SRCS:c=o))

.PHONY: clean

all: $(APPNAME)

$(APPNAME): $(OBJS)
	$(BUILDTOOLS)-$(LD) $(OBJS) $(LDFLAGS) -o $(OUT_DIR)/$@

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
