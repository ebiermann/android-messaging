android-messaging
=================
This project demonstrates the use of the Stomp Protocol(stomp.github.com/) on an Android device.

You have to manually add stompj-0.5.jar as a dependency for android-messaging-client.

To run this demo follow these steps:
1) Run 'mvn install' in folder android-messaging
2) Download and install JBoss AS7 (7.1.1.Final)
3) Deploy EAR from android-messaging-ear/target to your JBoss AS7
4) Copy the JBoss configuration from android-messaging-parent/src/main/resources to $JBOSS_HOME/standalone/configuration
5) Launch standalone.{sh|bat}
6) Start activity MessagingActivity