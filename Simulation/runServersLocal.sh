echo "Distributing intermediate code to the different execution environments."
cp interfaces/*.java Simulation/interfaces/
cp registry/*.java Simulation/registry/
cp server/DepartAirp/*.java Simulation/server/DepartAirp/
cp server/Plane/*.java Simulation/server/Plane/
cp server/DestinationAirp/*.java Simulation/server/DestinationAirp/
cp server/LogPackage/*.java Simulation/server/LogPackage/
cp client/*.java Simulation/client/
cp States/*.java Simulation/States/
cp Parameters_Local.txt Simulation/Parameters.java
cp java.policy Simulation/server/DepartAirp/
cp java.policy Simulation/server/Plane/
cp java.policy Simulation/server/DestinationAirp/
cp java.policy Simulation/server/LogPackage/
cd Simulation
javac $(find . -name "*.java")
cd ..

echo -e " A iniciar Registry"
rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false 22358 &



echo -e "\n A executar Registry"

java -Djava.rmi.server.codebase="file:///home/tony/2_Semester/SD/SD-P3"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     Simulation.registry.ServerRegisterRemoteObject &

sleep 1

echo -e "\n A executar Logger"

java -Djava.rmi.server.codebase="file:///home/tony/2_Semester/SD/SD-P3"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     Simulation.server.LogPackage.Logger_Server &

sleep 1

echo -e "\n A executar DepAirp"

java -Djava.rmi.server.codebase="file:///home/tony/2_Semester/SD/SD-P3"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     Simulation.server.DepartAirp.DepAirp_server &

sleep 1

echo -e "\n A executar Plane"

java -Djava.rmi.server.codebase="file:///home/tony/2_Semester/SD/SD-P3"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     Simulation.server.Plane.Plane_server &

sleep 1

echo -e "\n A executar DestAirp"

java -Djava.rmi.server.codebase="file:///home/tony/2_Semester/SD/SD-P3"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     Simulation.server.DestinationAirp.DestAirp_server &

sleep 1


