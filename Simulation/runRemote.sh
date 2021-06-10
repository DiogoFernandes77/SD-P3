


sshpass -p diogo-antonio ssh sd306@l040101-ws09.ua.pt "kill -9 \$(/usr/sbin/lsof -i:22358 -t) 2> /dev/null"
sshpass -p diogo-antonio ssh sd306@l040101-ws09.ua.pt "kill -9 \$(/usr/sbin/lsof -i:22359 -t) 2> /dev/null"
sshpass -p diogo-antonio ssh sd306@l040101-ws04.ua.pt "kill -9 \$(/usr/sbin/lsof -i:22353 -t) 2> /dev/null"
sshpass -p diogo-antonio ssh sd306@l040101-ws04.ua.pt "kill -9 \$(/usr/sbin/lsof -t -u sd306) 2> /dev/null"
sshpass -p diogo-antonio ssh sd306@l040101-ws01.ua.pt "kill -9 \$(/usr/sbin/lsof -i:22350 -t) 2> /dev/null"
sshpass -p diogo-antonio ssh sd306@l040101-ws02.ua.pt "kill -9 \$(/usr/sbin/lsof -i:22351 -t) 2> /dev/null"
sshpass -p diogo-antonio ssh sd306@l040101-ws03.ua.pt "kill -9 \$(/usr/sbin/lsof -i:22352 -t) 2> /dev/null"
sleep 5
echo "everyone dead"
sleep 1

echo "Running Machine 9"
sshpass -p diogo-antonio ssh sd306@l040101-ws09.ua.pt 'cd test/SDP3; echo -e " A iniciar Registry"; rmiregistry -J-Djava.rmi.server.useCodebaseOnly=true 22358 &  java -Djava.rmi.server.codebase="http://l040101-ws09.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.registry.ServerRegisterRemoteObject' &
sleep 1   


   




echo "Running Machine 4"
sshpass -p diogo-antonio ssh sd306@l040101-ws04.ua.pt 'cd test/SDP3; echo -e " A iniciar Logger"; java -Djava.rmi.server.codebase="http://l040101-ws09.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.server.LogPackage.Logger_Server' &









sleep 1

echo "Running Machine 1"
sshpass -p diogo-antonio ssh sd306@l040101-ws01.ua.pt 'cd test/SDP3; echo -e " A iniciar DepartAirp"; java -Djava.rmi.server.codebase="http://l040101-ws09.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.server.DepartAirp.DepAirp_server' &
    



sleep 1

echo "Running Machine 2"
sshpass -p diogo-antonio ssh sd306@l040101-ws02.ua.pt 'cd test/SDP3; echo -e " A iniciar Plane"; java -Djava.rmi.server.codebase="http://l040101-ws09.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.server.Plane.Plane_server' & 
    
    
sleep 1   



echo "Running Machine 3"
sshpass -p diogo-antonio ssh sd306@l040101-ws03.ua.pt 'cd test/SDP3; echo -e " A iniciar DestAirp"; java -Djava.rmi.server.codebase="http://l040101-ws09.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.server.DestinationAirp.DestAirp_server' &




sleep 3
echo "going to run clients now"

sleep 1
echo "Running Machine 5"
sshpass -p diogo-antonio ssh sd306@l040101-ws05.ua.pt 'cd test/SDP3; echo -e " A iniciar Piloto"; java -Djava.rmi.server.codebase="http://l040101-ws09.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.client.PilotClient' &
    
    
sleep 1



sleep 1
echo "Running Machine 6"
sshpass -p diogo-antonio ssh sd306@l040101-ws06.ua.pt 'cd test/SDP3; echo -e " A iniciar Piloto"; java -Djava.rmi.server.codebase="http://l040101-ws09.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.client.HostessClient' &
    
    
sleep 1



sleep 1
echo "Running Machine 7"
for i in $(seq 0 20);
	do
	echo -e " A iniciar Passageiro $i"
	sleep 0.1
	sshpass -p diogo-antonio ssh sd306@l040101-ws07.ua.pt "cd test/SDP3; java -Djava.rmi.server.codebase=\"http://l040101-ws09.ua.pt/sd306/test/SDP3/\"\
    	-Djava.rmi.server.useCodebaseOnly=true\
    	-Djava.security.policy=java.policy\
    	Simulation.client.PassengerClient \"$i\"" & 

done

    
    
  

