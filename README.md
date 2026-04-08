# sensor-measurement-solution

## Prerequisites

 - Git
 - Maven >=3
 - Java 21
 - Docker
 - Docker Compose

## Modules
### - sensor-common
### - sensor-warehouse-service
### - sensor-monitoring-service


Get the application on your PC:

```shell script
git clone https://github.com/Diffouo/sensor-measurement-solution.git
cd sensor-measurement-solution/
mvn clean install
```

## Running the Kafka broker for Dev
> The broker is use for 2 purposes
> - Send sensors from warehouse service to monitoring service through the "sensor-out" Channel 
> - Send sensors that exceed the threshold from monitoring service to "alerts-out" Channel 
>   -   (Each Channel is linked to a specific Kafka Topic)

```shell script
cd /dev-services
docker compose up -d
```

## Warehouse Service

The run the warehouse service, back to sensor-measurement-solution:

```shell script
cd sensor-warehouse-service/
mvn quarkus:dev
```

## Monitoring Service

The run the warehouse service, back to sensor-measurement-solution:

```shell script
cd sensor-monitoring-service/
mvn quarkus:dev
```

## Send UDP Messages using netcat
### Normal Temperature Sensor
No alert is sent out
```shell script
echo "sensor_id=t1; value=30" | nc -u 127.0.0.1 3344
```
### Normal Humidity Sensor
```shell script
echo "sensor_id=h1; value=40" | nc -u 127.0.0.1 3355
```
### Temperature Sensor that exceed the threshold
> You will see a red alert message in the sensor-monitoring-service logs like:
> - Alert triggering for sensor SensorMessageModel[sensorId=t2, value=36.0, sensorType=TEMPERATURE]
```shell script
echo "sensor_id=t2; value=36" | nc -u 127.0.0.1 3344
```

### Humidity Sensor that exceed the threshold
> You will see a red alert message in the sensor-monitoring-service logs like:
> - Alert triggering for sensor SensorMessageModel[sensorId=h2, value=51.0, sensorType=HUMIDITY]
```shell script
echo "sensor_id=h2; value=51" | nc -u 127.0.0.1 3355
```