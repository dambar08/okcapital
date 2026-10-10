#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

SERVICE="${1:-api-gateway}"
shift || true

if [[ ! -d "$ROOT_DIR/$SERVICE" ]]; then
  echo "Unknown service: $SERVICE" >&2
  echo "Available services:" >&2
  find "$ROOT_DIR" -maxdepth 1 -mindepth 1 -type d -printf '%f\n' | sort | grep -E '(-service|gateway)$' >&2 || true
  exit 1
fi

if ! command -v java >/dev/null 2>&1; then
  echo "Error: Java is required to run Spring Boot services." >&2
  exit 1
fi

echo "Starting $SERVICE with Gradle bootRun..."
exec ./gradlew ":$SERVICE:bootRun" -Dspring-boot.run.arguments="--spring.profiles.active=local" "$@"
