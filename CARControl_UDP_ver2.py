# CARControl_UDP_ver2.py
import RPi.GPIO as GPIO
import time
import sys
GPIO.setwarnings(False)

TRIG = 23
ECHO = 24

TRIG_R = 27
ECHO_R = 17

TRIG_L = 18
ECHO_L = 25

RIGHT_FORWARD = 16
RIGHT_BACKWARD = 20
RIGHT_PWM = 21
LEFT_FORWARD = 19
LEFT_BACKWARD = 26
LEFT_PWM = 13
#RIGHT_FORWARD = 19
#RIGHT_BACKWARD = 26
#RIGHT_PWM = 13
#LEFT_FORWARD = 16
#LEFT_BACKWARD = 20
#LEFT_PWM = 21

GPIO.setmode(GPIO.BCM)

GPIO.setup(TRIG, GPIO.OUT)
GPIO.setup(ECHO, GPIO.IN)

GPIO.setup(TRIG_R, GPIO.OUT)
GPIO.setup(ECHO_R, GPIO.IN)

GPIO.setup(TRIG_L, GPIO.OUT)
GPIO.setup(ECHO_L, GPIO.IN)

GPIO.setup(RIGHT_FORWARD,GPIO.OUT)
GPIO.setup(RIGHT_BACKWARD,GPIO.OUT)
GPIO.setup(RIGHT_PWM,GPIO.OUT)
GPIO.output(RIGHT_PWM,0)
RIGHT_MOTOR = GPIO.PWM(RIGHT_PWM,100)
RIGHT_MOTOR.start(0)
RIGHT_MOTOR.ChangeDutyCycle(0)

GPIO.setup(LEFT_FORWARD,GPIO.OUT)
GPIO.setup(LEFT_BACKWARD,GPIO.OUT)
GPIO.setup(LEFT_PWM,GPIO.OUT)
GPIO.output(LEFT_PWM,0)
LEFT_MOTOR = GPIO.PWM(LEFT_PWM,100)
LEFT_MOTOR.start(0)
LEFT_MOTOR.ChangeDutyCycle(0)
    
#RIGHT Motor control
def rightMotor(forward, backward, pwm):
    GPIO.output(RIGHT_FORWARD,forward)
    GPIO.output(RIGHT_BACKWARD,backward)
    RIGHT_MOTOR.ChangeDutyCycle(pwm)

#Left Motor control
def leftMotor(forward, backward, pwm):
    GPIO.output(LEFT_FORWARD,forward)
    GPIO.output(LEFT_BACKWARD,backward)
    LEFT_MOTOR.ChangeDutyCycle(pwm)
    
    
def Forward(speed_L, speed_R):
    rightMotor(1 ,0, speed_R)
    leftMotor(1 ,0, speed_L)
    cmd_text = "FORWARD"
    
def Backward(speed_L, speed_R):
    rightMotor(0 ,1, speed_R)
    leftMotor(0 ,1, speed_L)
    cmd_text = "BACKWARD"

def Stop():
    rightMotor(0 ,0, 0)
    leftMotor(0 ,0, 0)
    
def Poweroff():
    RIGHT_MOTOR.stop()
    LEFT_MOTOR.stop()
    GPIO.cleanup()
    sys.exit()
    
    
if __name__ == '__main__':
    
    #Forward(20,20)
    #time.sleep(2)
    rightMotor(0 ,1, 60)
    leftMotor(0 ,1, 60)
    #time.sleep(2)
    #Stop()
    Poweroff()
    pass


