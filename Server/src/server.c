#include "include/server.h"
#include "include/protocol.h"

const char *WELCOME_MSG = "Welcome to RoboPi!\n";
int client_connected = 0;
int server_sockfd = 0, client_sockfd = 0;

static void close_socks_on_sigint(int signo) {
    fprintf(stdout, "Received SIGINT signal\n");
    if (close(server_sockfd) < 0) {
        fprintf(stderr, "Error closing server socket\n");
    }
    if (close(client_sockfd) < 0) {
        fprintf(stderr, "Error closing client socket\n");
    }
    signal(SIGINT, SIG_DFL);
    raise(SIGINT);
}

void *session_task(void *ptr) {
    char buffer[BUFFER_SIZE];
    char cmd[CMD_LEN];
    char response[CMD_LEN];
    bzero(cmd, CMD_LEN);
    bzero(response, CMD_LEN);
    int n;
    explicit_bzero(buffer, BUFFER_SIZE);
    /*
    n = send(client_sockfd, WELCOME_MSG, strlen(WELCOME_MSG), 0);
    if (n < 0) {
        fprintf(stderr, "Error sending message\n");
        pthread_exit(NULL);
    }
    buffer[n] = '\0';
    */
    while (1) {
        n = recv(client_sockfd, buffer, BUFFER_SIZE, 0);
        if (n < 0) {
            fprintf(stderr, "Error reading socket\n");
            pthread_exit(NULL);
        }
        if (n == 0) { // connection is closed
            fprintf(stdout, "Client disconnected\n");
            pthread_exit(NULL);
        }
        buffer[n] = '\0';
        fprintf(stdout, "Message received: %s", buffer);
        strcpy(cmd, buffer);
        cmd[strcspn(cmd, "\n")] = 0;

        /* We don't accept commands if there is no application-level connection */
        if (!client_connected) {
            if (!strcmp(cmd, "DISCONN")) {
                put_response(response, DISCONN_ERR);
            } else if (strcmp(cmd, "CONN") != 0) {
                put_response(response, CMD_ERR);
            } else {
                client_connected = 1;
                put_response(response, CONN_OK);
            }
        } else {
            if (!strcmp(cmd, "CONN")) {
                put_response(response, CONN_ERR);
            } else if (!strcmp(cmd, "DISCONN")) {
                client_connected = 0;
                put_response(response, DISCONN_OK);
            } else {
                /* Only then can we process the command */
                process_cmd(cmd, response);
            }
        }
        fprintf(stdout, "Sending message: %s\n", response);
        n = send(client_sockfd, response, CMD_LEN, 0);
        if (n < 0) {
            fprintf(stderr, "Error sending response\n");
        }
        buffer[n] = '\0';
        bzero(cmd, CMD_LEN);
        bzero(response, CMD_LEN);
    }
}

int server() {
    signal(SIGINT, close_socks_on_sigint);
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

    while (1) {
        fprintf(stdout, "Waiting for clients...\n");
        size_t clilen = sizeof(cli_addr);
        client_sockfd = accept(server_sockfd, (struct sockaddr *) &cli_addr, &clilen);
        if (client_sockfd < 0) {
            fprintf(stderr, "Error on accept");
            return EXIT_FAILURE;
        }
        fprintf(stdout, "Connection established\n");
        pthread_create(&session_t, NULL, session_task, (void *) &client_sockfd);
        pthread_join(session_t, NULL);
        close(client_sockfd);
        close(server_sockfd);
        fprintf(stdout, "Bye\n");
        break;
    }
    return 1;
}