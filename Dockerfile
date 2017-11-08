FROM registry.opensource.zalan.do/stups/openjdk:latest

EXPOSE 8086

COPY target/zmon-data-service-1.0-SNAPSHOT.jar /zmon-data-service.jar
COPY target/scm-source.json /
COPY appagent appagent

CMD java $JAVA_OPTS $(java-dynamic-memory-opts) -jar -Dappdynamics.controller.hostName=test-appd.zalando.org -Dappdynamics.controller.port=443 -Dappdynamics.controller.ssl.enabled=true -Dappdynamics.agent.applicationName=zmon -Dappdynamics.agent.tierName=zmon-data-service -Dappdynamics.agent.nodeName=zmon-data-service -Dappdynamics.agent.accountName=customer1 -Dappdynamics.agent.accountAccessKey=9966815f-23ba-4de1-a9fb-19e9a07a49a6 -javaagent:/appagent/javaagent.jar /zmon-data-service.jar
