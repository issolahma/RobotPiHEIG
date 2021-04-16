#ifndef SERVER_H
#define SERVER_H

#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <pthread.h>
#include <string.h>

#define LISTENING_PORT 9734
#define BUFFER_SIZE 256

int server();
void *session_task(void *ptr);

#endif