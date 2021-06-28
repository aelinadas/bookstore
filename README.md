# Bookstore

### PROJECT DESCRIPTION

Bookstore is a web application that allows booksellers to sign up and sell their books on the platform and gives customer the opportunity to sign up and purchase.
Sellers and Customers are given the priviledge to update their profile and change their password.
The application is configured to be hosted on AWS cloud platform.
DNS has been set along with Route 53 to access the application via the domain name.
The entire infrastructure is built as code using **Terraform**.

---

### ARCHITECTURE

<img alt="Architecture" src="https://github.com/aelinadas/bookstore/blob/main/images/Infrastructure.png" />

---

### CI/CD Pipeline

Using CircleCI, CI/CD pipelines are set that validates the code by running unit test cases and allows only the valid code to be merged with main branch.
Upon merge, CircleCI creates code artifact and uploads the same to S3 bucket and later invokes AWS CodeDeploy to deploy the latest version of the code in tomcat server.

---

### AMAZON MACHINE IMAGE (AMI)

Using **Packer by HashiCorp**, AMI is built and deployed with required customization. Please find details on the AMI used for this project in this [repository](https://github.com/aelinadas/amazon-machine-image).

<img alt="ami" src="https://github.com/aelinadas/bookstore/blob/main/images/AMI.png" />

---

### INFRASTRUCTURE

Using **Terraform**, infrastructure for hosting the web application on AWS is built. Please find details regarding infrastructure in this [repository](https://github.com/aelinadas/aws-infrastructure).

---

### AWS Lambda

To adopt microservices architecture, generation of reset password link feature is configured to run as a Lambda function. Please find details regarding the Lambda function in this [repository](https://github.com/aelinadas/aws-lambda).

<img alt="Lambda" src="https://github.com/aelinadas/bookstore/blob/main/images/Lambda.png" />

---

### MONTORING and LOGGING

1. Centralized logging is implemented to collect the logs from all the EC2 instances and eliminated loss of log data when EC2 instances are brought down
2. Utilized statsD to collect custom metrics like Database Access time, S3 data access time, number of API request, etc. and passed it to **AWS CloudWatch Agent**
3. Configured CPU Utilization alarms to dynamically increase or decrease the EC2 instances based on the load using **AWS Auto Scaling**

---

### TESTING

1. Developed unit test cases using **Mockito**
2. Load tested the application using **Apache JMeter**

---

### TECHNOLOGY AND TOOLS

| Category | AWS Resources, Framework & Technologies |
| --- | --- |
| Programming Languages | Java, TSQL, HCL, HTML, CSS, JS |
| Web Application | Spring MVC, Hibernate, MySQL, AWS-SDK |
| Infrastructure | Terraform, VPC, RDS, S3, ELB, Auto Scaling, Route53, AWS Lambda, DynamoDB |
| Montoring & Logging | statsD, AWS Cloud-Watch Agent, Cloud-Watch Alarm, Log4js |
| Notification | SNS, SES |
| Security | BCrypt, RDS Encryption, SSL/TLS |
| CI/CD Pipeline | Circle CI |
| Testing Framework| Mockito, Apache JMeter |
| Web Server | Apache Tomcat |
