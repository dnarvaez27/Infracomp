ssh -i "Infracomp_Ubuntu.pem" ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com
ssh -i "Infracomp_Cliente.pem" ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com

grep -e " out " -e "Error" out_ServerSS.txt
grep -e " out " -e "Error" server_logs.txt


# ------------------------------------- Artifacts

# Upload files to Cliente
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem bcpg-jdk15on-155.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem bcpkix-jdk15on-155.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem bcprov-jdk15on-155.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem Casos.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem GLoad_1.3.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/

# Upload files to Cliente (No sec)
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem bcpg-jdk15on-155.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/ClienteNoSeguridad/
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem bcpkix-jdk15on-155.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/ClienteNoSeguridad/
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem bcprov-jdk15on-155.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/ClienteNoSeguridad/
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem Casos.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/ClienteNoSeguridad/
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem GLoad_1.3.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/ClienteNoSeguridad/

# Upload files to Servidor
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem bcpg-jdk15on-155.jar ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/Servidor/
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem bcpkix-jdk15on-155.jar ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/Servidor/
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem bcprov-jdk15on-155.jar ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/Servidor/
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem Server.jar ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/Servidor/

# Upload files to Servidor (No Sec)
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem bcpg-jdk15on-155.jar ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/ServidorSinSeguridad/
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem bcpkix-jdk15on-155.jar ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/ServidorSinSeguridad/
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem bcprov-jdk15on-155.jar ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/ServidorSinSeguridad/
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem Server.jar ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/ServidorSinSeguridad/

# ------------------------------------- Data

# Download Files from Client
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/docs/Caso3/data/Carga_400 Carga400.csv
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/docs/Caso3/data/Carga_200 Carga200.csv
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/docs/Caso3/data/Carga_80 Carga80.csv

# Download Files from ClientNoSec
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/ClienteNoSeguridad/docs/Caso3/data/SS_Carga_400 Carga400.csv
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/ClienteNoSeguridad/docs/Caso3/data/SS_Carga_200 Carga200.csv
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/ClienteNoSeguridad/docs/Caso3/data/SS_Carga_80 Carga80.csv

# ------------------------------------- Logs

# Download Logs(Sec)
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/log.txt out_Cliente.txt
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/Servidor/out.txt out_Servidor.txt

# Download Logs(No Sec)
scp -i C:\Users\dnarv\Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/ClienteNoSeguridad/outs_ClienteSS out_ClienteSS.txt
scp -i C:\Users\dnarv\Downloads/Infracomp_Ubuntu.pem ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/ServidorNoSeguridad/outs_ServerSS out_ServerSS.txt
