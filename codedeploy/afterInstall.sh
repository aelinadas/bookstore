#!/bin/bash
sudo chown tomcat:tomcat /var/lib/tomcat9/webapps/ROOT.war

#cleanup log files
sudo rm -rf /var/lib/tomcat9/logs/catalina*
sudo rm -rf /var/lib/tomcat9/logs/*.log
sudo rm -rf /var/lib/tomcat9/logs/*.txt
sudo chown amazon-cloudwatch-agent:amazon-cloudwatch-agent /opt/aws/amazon-cloudwatch-agent/bin/cloudwatch.json
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/opt/aws/amazon-cloudwatch-agent/bin/cloudwatch.json -s