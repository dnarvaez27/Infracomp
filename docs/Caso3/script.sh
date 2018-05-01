# scp -i <*.pem> <upload.txt> ubuntu@<dns-aws>:~/<Destino>


scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem bcpg-jdk15on-155.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem bcpkix-jdk15on-155.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem bcprov-jdk15on-155.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem Casos.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem GLoad_1.3.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem Servidorcs.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/

# scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem Servidorss.jar ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/

# // Download
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/docs/Caso3/data/Carga_400 Carga400.csv
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/docs/Caso3/data/Carga_200 Carga200.csv
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/docs/Caso3/data/Carga_80 Carga80.csv
#
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Cliente.pem ubuntu@ec2-18-220-102-71.us-east-2.compute.amazonaws.com:~/Cliente/log.txt logs.txt
scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Ubuntu.pem ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/Servidor/out.txt out.txt

scp -i /mnt/c/Users/dnarv/Downloads/Infracomp_Ubuntu.pem *** ubuntu@ec2-52-15-203-191.us-east-2.compute.amazonaws.com:~/ServidorNoSeguridad/
