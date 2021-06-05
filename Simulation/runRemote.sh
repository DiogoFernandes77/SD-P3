
sshpass -p diogo-antonio ssh -tt sd306@l040101-ws09.ua.pt<< EOF
   bash -c "ps -ef | grep sd306 | grep java | awk '{print \\\$2}' | xargs kill" &
   exit
EOF

sleep 1

echo "Running Machine 9"
sshpass -p diogo-antonio ssh sd306@l040101-ws09.ua.pt 'cd test/SDP3; echo -e " A iniciar Registry"; rmiregistry -J-Djava.rmi.server.useCodebaseOnly=true 22358 &  java -Djava.rmi.server.codebase="http://l040101-ws09.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.registry.ServerRegisterRemoteObject' &
sleep 1   

sshpass -p diogo-antonio ssh -tt sd306@l040101-ws01.ua.pt<< EOF
   bash -c "ps -ef | grep sd306 | grep java | awk '{print \\\$2}' | xargs kill" &
   exit
EOF

sleep 1

echo "Running Machine 1"
sshpass -p diogo-antonio ssh sd306@l040101-ws01.ua.pt 'cd test/SDP3; echo -e " A iniciar DepartAirp"; java -Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.server.DepartAirp.DepAirp_server' &
    
sleep 1   

sshpass -p diogo-antonio ssh -tt sd306@l040101-ws02.ua.pt<< EOF
   bash -c "ps -ef | grep sd306 | grep java | awk '{print \\\$2}' | xargs kill" &
   exit
EOF

sleep 1

echo "Running Machine 2"
sshpass -p diogo-antonio ssh sd306@l040101-ws02.ua.pt 'cd test/SDP3; echo -e " A iniciar Plane"; java -Djava.rmi.server.codebase="http://l040101-ws02.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.server.Plane.Plane_server' & 
    
    
sleep 1   

sshpass -p diogo-antonio ssh -tt sd306@l040101-ws03.ua.pt<< EOF
   bash -c "ps -ef | grep sd306 | grep java | awk '{print \\\$2}' | xargs kill" &
   exit
EOF

sleep 1
echo "Running Machine 3"
sshpass -p diogo-antonio ssh sd306@l040101-ws03.ua.pt 'cd test/SDP3; echo -e " A iniciar DestAirp"; java -Djava.rmi.server.codebase="http://l040101-ws03.ua.pt/sd306/test/SDP3/"\
    -Djava.rmi.server.useCodebaseOnly=true\
    -Djava.security.policy=java.policy\
    Simulation.server.DestinationAirp.DestAirp_server' 
    
    
  

