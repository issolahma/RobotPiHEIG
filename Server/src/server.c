#include "include/server.h"

const char* WELCOME_MSG = "Welcome to RoboPi!\n";

void *session_task(void *ptr) {
    char buffer[BUFFER_SIZE];
    int n;
    explicit_bzero(buffer, BUFFER_SIZE);
    int client_sockfd = *(int*) ptr;
    n = send(client_sockfd, WELCOME_MSG, strlen(WELCOME_MSG), 0);
    if (n < 0) {
        fprintf(stderr, "Error sending message\n");
        pthread_exit(NULL);
    }
    while(1) {
        n = recv(client_sockfd, buffer, BUFFER_SIZE, MSG_PEEK);
        if (n < 0) {
            fprintf(stderr, "Error reading socket\n");
            pthread_exit(NULL);
        }
        if (n == 0) { // connection is closed
            fprintf(stdout, "Client disconnected\n");
            pthread_exit(NULL);
        }
    }

}

int server() {
	int server_sockfd = 0, client_sockfd = 0;
	struct sockaddr_in server_addr, cli_addr;
	pthread_t session_t;
	server_sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (server_sockfd == -1) {
		fprintf(stderr, "Could not create socket\n");
        return EXIT_FAILURE;
	}

	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	server_addr.sin_port = htons(LISTENING_PORT);  // à redéfinir
    
    if (bind(server_sockfd, (struct sockaddr *) &server_addr, sizeof(server_addr)) < 0) {
        fprintf(stderr, "Could not bind socket\n");
        return EXIT_FAILURE;
    }
    
    listen(server_sockfd, 1);
    
    while(1) {
        fprintf(stdout, "Waiting for clients...\n");
        size_t clilen = sizeof(cli_addr);
        client_sockfd = accept(server_sockfd, (struct sockaddr *) &cli_addr, &clilen);
        if (client_sockfd < 0) {
            fprintf(stderr, "Error on accept");
            return EXIT_FAILURE;
        }
        fprintf(stdout, "Connection established\n");
        pthread_create(&session_t, NULL, session_task, (void*)&client_sockfd);
        pthread_join(session_t, NULL);
        close(client_sockfd);
        close(server_sockfd);
        fprintf(stdout, "Bye\n");
        return EXIT_SUCCESS;
    }

    return 1;
}