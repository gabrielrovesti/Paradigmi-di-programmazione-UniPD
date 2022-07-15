docker run --net net19  -eLISTENERPORT=62000 \
  -eTARGETPORT=62000 -eTARGETADDRESS=172.19.0.0 \
  -eHQADDRESS="http://172.17.0.1:8080" \
  -eSEQUENCE=3000 -eSENDER=1 \
  --ip 172.19.1.10 -m 128Mb --rm \
  --name ctnode3 pdp2021/ctnode