# Esempio di uso di Micronaut Framework

Per avviare il server: `./gradlew run` o `.\gradlew run` su Windows.

Per ottenere una immagine Docker con il server compilato con GraalVM:

```
./gradlew dockerBuildNative
```

Per lanciare un container con l'immagine risultante:

```
docker run --rm -p8080:8080 tictactoe:latest
```
