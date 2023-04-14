# Projeto Comex
para executar, entre na pasta raiz do projeto:
- faça o build:
`` 
$ mvn clean package -Dambiente.teste=local``
- Suba os conteiners do docker:
``$ docker-compose up --build -d``
- abrir o browser e acessar: 
  - para interagir: http://localhost:8080 
  - para visualizar o resultado: http://localhost:7900/?autoconnect=1&resize=scale&password=secret
