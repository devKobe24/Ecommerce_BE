#!/bin/zsh

echo "➡️ CLEAN product-service"

MYSQL_URL="jdbc:mysql://mysql-product:3306/product_db?allowPublicKeyRetrieval=true&useSSL=false"
MYSQL_USER="root"
MYSQL_PASSWORD="SkyAchieve910424"
FLYWAY_IMAGE="redgate/flyway:11.8.2"
NETWORK="ecommerce-network"
VOLUME_DIR="$(pwd)/product-service/src/main/resources/db/migration"

docker run --rm \
  --network "$NETWORK" \
  -v "$VOLUME_DIR":/flyway/sql \
  --platform linux/amd64 \
  "$FLYWAY_IMAGE" \
  -url="$MYSQL_URL" \
  -user="$MYSQL_USER" \
  -password="$MYSQL_PASSWORD" \
  clean