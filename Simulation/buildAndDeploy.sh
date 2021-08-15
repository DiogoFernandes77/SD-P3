

echo "Distributing intermediate code to the different execution environments."
cp interfaces/*.java Simulation/interfaces/
cp registry/*.java Simulation/registry/
cp server/DepartAirp/*.java Simulation/server/DepartAirp/
cp server/Plane/*.java Simulation/server/Plane/
cp server/DestinationAirp/*.java Simulation/server/DestinationAirp/
cp server/LogPackage/*.java Simulation/server/LogPackage/
cp client/*.java Simulation/client/
cp States/*.java Simulation/States/
cp Parameters.java Simulation
echo "Compressing execution environments."
rm -f Simulation.zip
zip -rq Simulation.zip Simulation

echo "Uploading files to machines"
#echo "Machine 1"
#sshpass -p diogo-antonio ssh sd306@l040101-ws01.ua.pt 'mkdir -p test/SDP3'
#sshpass -p diogo-antonio ssh sd306@l040101-ws01.ua.pt 'rm -rf test/SDP3/*'
#sshpass -p diogo-antonio scp Simulation.zip sd306@l040101-ws01.ua.pt:test/SDP3
#sshpass -p diogo-antonio scp java.policy sd306@l040101-ws01.ua.pt:test/SDP3
#sshpass -p diogo-antonio ssh sd306@l040101-ws01.ua.pt 'cd test/SDP3 ; unzip -uq Simulation.zip; javac $(find . -name "*.java") '
echo "Machine 2"
sshpass -p diogo-antonio ssh sd306@l040101-ws02.ua.pt 'mkdir -p test/SDP3'
sshpass -p diogo-antonio ssh sd306@l040101-ws02.ua.pt 'rm -rf test/SDP3/*'
sshpass -p diogo-antonio scp Simulation.zip sd306@l040101-ws02.ua.pt:test/SDP3
sshpass -p diogo-antonio scp java.policy sd306@l040101-ws02.ua.pt:test/SDP3
sshpass -p diogo-antonio ssh sd306@l040101-ws02.ua.pt 'cd test/SDP3 ; unzip -uq Simulation.zip; javac $(find . -name "*.java")'
echo "Machine 3"
sshpass -p diogo-antonio ssh sd306@l040101-ws03.ua.pt 'mkdir -p test/SDP3'
sshpass -p diogo-antonio ssh sd306@l040101-ws03.ua.pt 'rm -rf test/SDP3/*'
sshpass -p diogo-antonio scp Simulation.zip sd306@l040101-ws03.ua.pt:test/SDP3
sshpass -p diogo-antonio scp java.policy sd306@l040101-ws03.ua.pt:test/SDP3
sshpass -p diogo-antonio ssh sd306@l040101-ws03.ua.pt 'cd test/SDP3 ; unzip -uq Simulation.zip; javac $(find . -name "*.java")'
echo "Machine 4"
sshpass -p diogo-antonio ssh sd306@l040101-ws04.ua.pt 'mkdir -p test/SDP3'
sshpass -p diogo-antonio ssh sd306@l040101-ws04.ua.pt 'rm -rf test/SDP3/*'
sshpass -p diogo-antonio scp Simulation.zip sd306@l040101-ws04.ua.pt:test/SDP3
sshpass -p diogo-antonio scp java.policy sd306@l040101-ws04.ua.pt:test/SDP3
sshpass -p diogo-antonio ssh sd306@l040101-ws04.ua.pt 'cd test/SDP3 ; unzip -uq Simulation.zip; javac $(find . -name "*.java")'
echo "Machine 5"
sshpass -p diogo-antonio ssh sd306@l040101-ws05.ua.pt 'mkdir -p test/SDP3'
sshpass -p diogo-antonio ssh sd306@l040101-ws05.ua.pt 'rm -rf test/SDP3/*'
sshpass -p diogo-antonio scp Simulation.zip sd306@l040101-ws05.ua.pt:test/SDP3
sshpass -p diogo-antonio scp java.policy sd306@l040101-ws05.ua.pt:test/SDP3
sshpass -p diogo-antonio ssh sd306@l040101-ws05.ua.pt 'cd test/SDP3 ; unzip -uq Simulation.zip; javac $(find . -name "*.java")'
echo "Machine 6"
sshpass -p diogo-antonio ssh sd306@l040101-ws06.ua.pt 'mkdir -p test/SDP3'
sshpass -p diogo-antonio ssh sd306@l040101-ws06.ua.pt 'rm -rf test/SDP3/*'
sshpass -p diogo-antonio scp Simulation.zip sd306@l040101-ws06.ua.pt:test/SDP3
sshpass -p diogo-antonio scp java.policy sd306@l040101-ws06.ua.pt:test/SDP3
sshpass -p diogo-antonio ssh sd306@l040101-ws06.ua.pt 'cd test/SDP3 ; unzip -uq Simulation.zip; javac $(find . -name "*.java")'
echo "Machine 7"
sshpass -p diogo-antonio ssh sd306@l040101-ws07.ua.pt 'mkdir -p test/SDP3'
sshpass -p diogo-antonio ssh sd306@l040101-ws07.ua.pt 'rm -rf test/SDP3/*'
sshpass -p diogo-antonio scp Simulation.zip sd306@l040101-ws07.ua.pt:test/SDP3
sshpass -p diogo-antonio scp java.policy sd306@l040101-ws07.ua.pt:test/SDP3
sshpass -p diogo-antonio ssh sd306@l040101-ws07.ua.pt 'cd test/SDP3 ; unzip -uq Simulation.zip; javac $(find . -name "*.java")'
echo "Machine 8"
sshpass -p diogo-antonio ssh sd306@l040101-ws08.ua.pt 'mkdir -p test/SDP3'
sshpass -p diogo-antonio ssh sd306@l040101-ws08.ua.pt 'rm -rf test/SDP3/*'
sshpass -p diogo-antonio scp Simulation.zip sd306@l040101-ws08.ua.pt:test/SDP3
sshpass -p diogo-antonio scp java.policy sd306@l040101-ws08.ua.pt:test/SDP3
sshpass -p diogo-antonio ssh sd306@l040101-ws08.ua.pt 'cd test/SDP3 ; unzip -uq Simulation.zip; javac $(find . -name "*.java")'
echo "Machine 9"
sshpass -p diogo-antonio ssh sd306@l040101-ws09.ua.pt 'mkdir -p test/SDP3'
sshpass -p diogo-antonio ssh sd306@l040101-ws09.ua.pt 'rm -rf test/SDP3/*'
sshpass -p diogo-antonio scp Simulation.zip sd306@l040101-ws09.ua.pt:test/SDP3
sshpass -p diogo-antonio scp java.policy sd306@l040101-ws09.ua.pt:test/SDP3
sshpass -p diogo-antonio ssh sd306@l040101-ws09.ua.pt 'cd test/SDP3 ; unzip -uq Simulation.zip; javac $(find . -name "*.java")'
