#ifndef MOTOR_H
#define MOTOR_H

#include "../motorLib/MotorDriver.h"

#define MAX_SPEED 70 //Used to prevent overvoltage of motors
#define MIN_SPEED 0 //Minimum speed at which the robot actually moves
#define MOTOR_L MOTORA
#define MOTOR_R MOTORB

/**
 * @brief Sets the velocity of the specified motor
 * 
 * @param motor The motor to command {MOTOR_R, MOTOR_L}
 * @param velocity The velocity to run the motor at [-100, 100]
 */
void setMotor(char motor, signed char velocity);

/**
 * @brief Makes the robot run forward
 * 
 * @param velocity The velocity to run the motors at [-100, 100]
 */
void runForward(signed char velocity);

/**
 * @brief Makes the robot run backward
 * 
 * @param velocity The velocity to run the motors at [-100, 100]
 */
void runBackward(signed char velocity);

/**
 * @brief Makes the robot turn right forward
 * 
 * @param velocity The velocity to run the motors at [-100, 100]
 */
void turnRightF(signed char velocity);

/**
 * @brief Makes the robot turn left forward
 * 
 * @param velocity The velocity to run the motors at [-100, 100]
 */
void turnLeftF(signed char velocity);

/**
 * @brief Makes the robot turn right backward
 * 
 * @param velocity The velocity to run the motors at [-100, 100]
 */
void turnRightB(signed char velocity);

/**
 * @brief Makes the robot turn left backward
 * 
 * @param velocity The velocity to run the motors at [-100, 100]
 */
void turnLeftB(signed char velocity);

/**
 * @brief Makes the robot rotate on itself clockwise
 * 
 * @param velocity The velocity to run the motors at [-100, 100]
 */
void rotateRight(signed char velocity);

/**
 * @brief Makes the robot rotate on itself anti-clockwise
 * 
 * @param velocity The velocity to run the motors at [-100, 100]
 */
void rotateLeft(signed char velocity);

/**
 * @brief Necessary steps required to make the motors run
 * 
 */
void motorInit();

/**
 * @brief Necessary steps before exiting
 * 
 */
void motorQuit();

#endif