#include "include/server.h"

int server() {
	int server_sockfd = 0, client_sockfd = 0;
	struct sockaddr_in server_addr;
	struct sockaddr_in client_addr;

	server_sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (server_sockfd == -1) {
		fprintf(stderr, "Could not create socket\n");
	}

	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	server_addr.sin_port = htons(9734);  // à redéfinir
    
    if (bind(server_sockfd, (struct sockaddr *) &server_addr, sizeof(server_addr)) < 0) {
        fprintf(stderr, "Could not bind socket\n");
    }
    
    listen(server_sockfd, 1);
    
    while(1) {
        fprintf(stdout, "Waiting for clients...\n");
        client_sockfd = accept(server_sockfd, (struct sockaddr*)NULL, NULL);
        fprintf(stdout, "Connection established\n");
        close(client_sockfd);
        fprintf(stdout, "Bye\n");

    }

	return 0;
}
