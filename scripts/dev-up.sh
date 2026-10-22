#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

require_command() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Error: required command not found: $1" >&2
    exit 1
  fi
}

require_command docker

if ! docker compose version >/dev/null 2>&1; then
  echo "Error: docker compose is not available. Install Docker Desktop or Docker Compose v2." >&2
  exit 1
fi

echo "Starting local development infrastructure..."
docker compose up -d --no-deps postgres kafka keycloak minio

echo "Local development dependencies are running."
echo "- PostgreSQL: localhost:5432"
echo "- Kafka: localhost:9092"
echo "- Keycloak: http://localhost:9080"
echo "- MinIO: http://localhost:9001"
