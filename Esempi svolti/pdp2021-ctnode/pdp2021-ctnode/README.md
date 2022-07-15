# Come impostare l'indirizzo ip dei container

Per prima cosa, creare alcune reti:

`docker network create --subnet=172.18.0.0/16 net18`
`docker network create --subnet=172.19.0.0/16 net19`
`docker network create --subnet=172.20.0.0/16 net20`

Nelle reti così create, è possbile assegnare l'ip del container:

```
mim@mim:~/workspace/ct-node$ docker run --net net18 --ip 172.18.1.4 -m 256Mb --rm --name ctnode pdp2021/ctnode
eth0 172.18.1.4
...

mim@mim:~/workspace/ct-node$ docker run --net net19 --ip 172.19.0.4 -m 256Mb --rm --name ctnode pdp2021/ctnode
eth0 172.19.0.4
...
```
