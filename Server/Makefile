TOOLCHAIN = armv8-rpi3-linux-gnueabihf
TOOLCHAIN_DIR = /opt/x-tools/$(TOOLCHAIN)
SYSROOT = $(TOOLCHAIN_DIR)/$(TOOLCHAIN)/sysroot/
BUILDTOOLS = $(TOOLCHAIN_DIR)/bin/$(TOOLCHAIN)

CC = gcc
LD = ld
CFLAGS = -g -Wall
LDFLAGS = -lc
TARGETFOLDER = bin
APPNAME = RoboPiServer
TEST_APPNAME = RoboPiServerTest

SUBDIR = src
TEST_SUBDIR = test
OUT_DIR = ./bin
TEST_OUT_DIR = ./test_bin
OBJ_DIR = ./build
TEST_OBJ_DIR = ./test_build

SRCS = $(wildcard *.c $(foreach dir, $(SUBDIR), $(dir)/*.c))
TEST_SRCS = $(wildcard *.c $(foreach dir, $(TEST_SUBDIR), $(dir)/*.c))
OBJS = $(addprefix $(OBJ_DIR)/, $(SRCS:c=o))
TEST_OBJS = $(addprefix $(TEST_OBJ_DIR)/, $(TEST_SRCS:c=o))

.PHONY: clean

all: $(OUT_DIR)/$(APPNAME) $(TEST_OUT_DIR)/$(TEST_APPNAME)

$(OUT_DIR)/$(APPNAME): $(OBJS)
	$(BUILDTOOLS)-$(CC) $(OBJS) $(LDFLAGS) -o $@

$(OBJ_DIR)/%.o: %.c
	mkdir -p $(@D)
	$(BUILDTOOLS)-$(CC) -o $@ -c $< $(CFLAGS)

$(TEST_OUT_DIR)/$(TEST_APPNAME): $(TEST_OBJS)
	$(BUILDTOOLS)-$(CC) $(TEST_OBJS) $(LDFLAGS) -o $@

$(TEST_OBJ_DIR)/%.o: %.c
	mkdir -p $(@D)
	$(BUILDTOOLS)-$(CC) -o $@ -c $< $(CFLAGS)

clean:
	rm -rf $(OUT_DIR)/*
	rm -rf $(OBJ_DIR)/*
	rm -rf $(TEST_OUT_DIR)/*
	rm -rf $(TEST_OBJ_DIR)/*

test: 

new_file:
	@echo "Entrez le nom des fichiers que vous voulez créer"
	@read fileName; \
	touch src/$$fileName.c src/include/$$fileName.h test/test_$$fileName.c
