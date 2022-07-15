docker run --net net18  -eLISTENERPORT=62000 \
  -eTARGETPORT=62000 -eTARGETADDRESS=172.18.0.0 \
  -eHQADDRESS="http://172.17.0.1:8080" \
  -eSEQUENCE=1000 -eSENDER=1 \
  --ip 172.18.1.11 -m 128Mb --rm \
  --name ctnode1 pdp2021/ctnode