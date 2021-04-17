#include "include/protocol.h"

void put_response(char *response, int response_code) {
    switch (response_code) {
        case CONN_OK:
            strcpy(response, "CONN_OK");
            break;
        case CONN_ERR:
            strcpy(response, "CONN_ERR");
            break;
        case FWD_OK:
            strcpy(response, "FWD_OK");
            break;
        case BKWD_OK:
            strcpy(response, "BKWD_OK");
            break;
        case STOP_OK:
            strcpy(response, "STOP_OK");
            break;
        case ROTATE_LEFT_OK:
            strcpy(response, "ROTATE_LEFT_OK");
            break;
        case ROTATE_RIGHT_OK:
            strcpy(response, "ROTATE_RIGHT_OK");
            break;
        case DISCONN_OK:
            strcpy(response, "DISCONN_OK");
            break;
        case DISCONN_ERR:
            strcpy(response, "DISCONN_ERR");
            break;
        case PING:
            strcpy(response, "PING");
            break;
        default:
            strcpy(response, "CMD_ERR");
            break;
    }
}

int process_cmd(char *cmd, char *response) {
    int response_code = CMD_ERR;
    if (!strcmp(cmd, "CONN")) {
        response_code = CONN_OK;
    } else if (!strcmp(cmd, "FWD")) {
        response_code = FWD_OK;
    } else if (!strcmp(cmd, "BKWD")) {
        response_code = BKWD_OK;
    } else if (!strcmp(cmd, "STOP")) {
        response_code = STOP_OK;
    } else if (!strcmp(cmd, "ROTATE_LEFT")) {
        response_code = ROTATE_LEFT_OK;
    } else if (!strcmp(cmd, "ROTATE_RIGHT")) {
        response_code = ROTATE_RIGHT_OK;
    } else if (!strcmp(cmd, "DISCONN")) {
        response_code = DISCONN_OK;
    } else if (!strcmp(cmd, "PING")) {
        response_code = PING;
    }
    put_response(response, response_code);
    return 0;
}