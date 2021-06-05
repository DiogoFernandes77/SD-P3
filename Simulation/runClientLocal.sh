echo -e "\n A executar Hostess"

java -Djava.rmi.server.codebase="file:///home/tabuas/Desktop/uni/4ano/SD/Project/SD-P3/SD-P3/Simulation"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     Simulation.client.HostessClient &


echo -e "\n A executar Piloto"

java -Djava.rmi.server.codebase="file:///home/tabuas/Desktop/uni/4ano/SD/Project/SD-P3/SD-P3/Simulation"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     Simulation.client.PilotClient &



echo -e "\n A executar Passageiros"
for i in $(seq 0 20); 
	do
        java -Djava.rmi.server.codebase="file:///home/tabuas/Desktop/uni/4ano/SD/Project/SD-P3/SD-P3/Simulation"\
	     -Djava.rmi.server.useCodebaseOnly=false\
	     -Djava.security.policy=java.policy\
	     Simulation.client.PassengerClient $i &
     
done
echo "for complete"
     



