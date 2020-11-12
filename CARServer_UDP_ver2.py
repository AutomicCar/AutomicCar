# CARServer_UDP_ver2.py
import CARControl_UDP_ver2
from socket import *
from time import ctime
import RPi.GPIO as GPIO
import sys



#CARControl.setup()

ctrCmd = ["F","B","S","P"]
cmd = []

HOST = ''
PORT = 8011
BUFSIZE = 1024
ADDR = (HOST,PORT)

# tcpSerSock = socket(AF_INET, SOCK_STREAM)
# tcpSerSock.bind(ADDR)
# tcpSerSock.listen(5)

# 소켓 생성 (UDP = SOCK_DGRAM, TCP = SOCK_STREAM)
udpSerSock = socket(AF_INET, SOCK_DGRAM)

# 포트 설정
udpSerSock.bind(ADDR)
    
# 준비 완료 화면에 표시
print('udp echo server ready')

while True:
        print('Waiting for connection')
        data, addr = udpSerSock.recvfrom(BUFSIZE)
        print('...connected from :', addr)
        try:
                while True:
                    data = ''
                    data = udpSerSock.recv(BUFSIZE)
                    data = data.decode('utf-8')
                    print(data)
                    cmd = data.split(',')
                    if len(cmd[1])>=3:
                        cmd[1]="100"
                    if len(cmd[2])>=3:
                        cmd[2]="100"    
                    if not data:
                        break
                    if cmd[0] == ctrCmd[0]:
                        CARControl_UDP_ver2.Forward(int(cmd[1]), int(cmd[2]))
                        print("Forward")
                    if cmd[0] == ctrCmd[1]:
                        CARControl_UDP_ver2.Backward(int(cmd[1]), int(cmd[2]))
                        print("Backward")
                    if cmd[0] == ctrCmd[2]:
                        CARControl_UDP_ver2.Stop()
                        print("Stop")
                    if cmd[0] == ctrCmd[3]:
                        print("Poweroff")
                        CARControl_UDP_ver2.Poweroff()
                        break
                                       
        except KeyboardInterrupt:
            print("Terminated by Keborad Interrupt")
            GPIO.cleanup()
udpSerSock.close();



