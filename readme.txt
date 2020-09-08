# HBSIS Java Senior Challenge
## Teste Java Microservices

### Pré-requisitos

Base de dados: Postgresql (https://www.postgresql.org/)
Message Broker: Rabbitmq (https://www.rabbitmq.com/)
Maven: Rabbitmq (https://maven.apache.org/)
Java: JDK (https://www.oracle.com/java/technologies/javase-downloads.html)

### Instruções para build

No arquivo /weather-register/weather-register-service/src/main/resources/application.properties está informado configuração para conexão com o banco de dados:

```
spring.datasource.url = jdbc:postgresql://localhost:5432/postgres
spring.datasource.username = postgres
spring.datasource.password = postgres
```

Caso seja necessário, altere para conectar onde desejar.

Realize o build do projeto utilizando Maven.

### Instruções para execução

Execute o serviço responsável por cadastrar previsões do tempo:
 
```
java -jar weather-register/weather-register-service/target/weather-register-service-1.0.0-SNAPSHOT.jar
```

Execute o serviço responsável por consultar previsões do tempo:

```
java -jar weather-consultant/weather-consultant-service/target/weather-consultant-service-1.0.0-SNAPSHOT.jar
```

### Documentação da API

Cadastrar previsão do tempo:

- Executar POST no endereço http://localhost:8080/api/v1/weather
- Body: {"lat": 35,"lon": 128}
    - Onde: "lat" é a latitute do local; e "lon" é a longitude;
    
- Exemplo de retorno:
```
{
    "id": "b6721ab0-4eed-43c7-9526-fc48b8c97e5b",
    "createdAt": "2020-09-08T11:36:27.246724",
    "attrs": {
        "coord": {
            "lon": 128,
            "lat": 35
        },
        "weather": [
            {
                "id": 721,
                "main": "Haze",
                "description": "haze",
                "icon": "50n"
            }
        ],
        "base": "stations",
        "main": {
            "temp": 297.15,
            "feels_like": 300.25,
            "temp_min": 297.15,
            "temp_max": 297.15,
            "pressure": 1016,
            "humidity": 83
        },
        "visibility": 6000,
        "wind": {
            "speed": 1.5,
            "deg": 120
        },
        "clouds": {
            "all": 75
        },
        "dt": 1599575688,
        "sys": {
            "type": 1,
            "id": 5507,
            "country": "KR",
            "sunrise": 1599512753,
            "sunset": 1599558352
        },
        "timezone": 32400,
        "id": 1838294,
        "name": "Shisen",
        "cod": 200
    }
}
```

### Overview da arquitetura e design

Há dois microserviços: weather-consultant-service; weather-register-service;

O processo de comunicação entre os serviços é assíncrono.

O serviço weather-consultant-service é responsável por realizar consultas da previsão do tempo na api pública Open Weather(https://openweathermap.org/api). Este serviço é acionado atráves de mensagem recebida na fila "hbis.weather.request". O retorno da consulta é devolvido pela mesma fila.

O serviço weather-consultant-service expoem um client para consulta de seu serviço, disponível no módulo weather-consultant/weather-consultant-client, sendo necessário incorporá-lo no projeto que se deseja realizar consulta de previsão do tempo.

Dependência Maven:
```
<dependencies>
    ...
    <dependency>
        <groupId>br.com.hbsis</groupId>
        <artifactId>weather-consultant-client</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ...
</dependencies>
```

O serviço weather-register-service é responsável por registrar as consultas da previsão do tempo na base de dados. Esse serviço é acionado atráves de uma requição HTTP, de método POST. 

Utilizando o client exposto para consulta de previsão do tempo, a previsão do tempo é consultada e seu retorno é registrado em bando de dados, ou caso ocorra algum problema, o registro não é realizado e o problema é informado.