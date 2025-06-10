#!/bin/zsh

# ✅ flyway-all.sh - 모든 서비스에 대해 migrate, info, repair, clean 순차 실행

# 🔧 공통 설정
MYSQL_URL="jdbc:mysql://mysql:3306/ecommerce_db?allowPublicKeyRetrieval=true&useSSL=false"
MYSQL_USER="root"
MYSQL_PASSWORD="SkyAchieve910424"
FLYWAY_IMAGE="redgate/flyway:11.8.2"
NETWORK="ecommerce-network"

for COMMAND in migrate info repair clean; do
  for SERVICE in user-service product-service; do
    echo "➡️ ${COMMAND:u} $SERVICE"

    VOLUME_DIR="$(pwd)/$SERVICE/src/main/resources/db/migration"

    if [[ ! -d "$VOLUME_DIR" ]]; then
      echo "⚠️  Migration directory not found for $SERVICE: $VOLUME_DIR"
      continue
    fi

    if [[ "$COMMAND" == "clean" ]]; then
      docker run --rm \
        --network "$NETWORK" \
        --platform linux/amd64 \
        -v "$VOLUME_DIR":/flyway/sql \
        "$FLYWAY_IMAGE" \
        -url="$MYSQL_URL" \
        -user="$MYSQL_USER" \
        -password="$MYSQL_PASSWORD" \
        -cleanDisabled=false \
        clean
    else
      docker run --rm \
        --network "$NETWORK" \
        --platform linux/amd64 \
        -v "$VOLUME_DIR":/flyway/sql \
        "$FLYWAY_IMAGE" \
        -url="$MYSQL_URL" \
        -user="$MYSQL_USER" \
        -password="$MYSQL_PASSWORD" \
        "$COMMAND"
    fi
  done
done