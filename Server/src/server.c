#include "include/server.h"

int server(int argc, char *argv[]) {
	int server_sockfd;
	struct sockaddr_in server_addr;
	struct sockaddr_in client_addr;

	server_sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (server_sockfd == -1) {
		fprintf(stderr, "Could not create socket\n");
	}

	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	server_addr.sin_port = htons(9734);  // à redéfinir

	return 0;
}
