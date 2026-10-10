#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

if ! command -v java >/dev/null 2>&1; then
  echo "Error: Java is required for this project. Install a supported JDK (Java 25 for the repo toolchain)." >&2
  exit 1
fi

if ! command -v docker >/dev/null 2>&1; then
  echo "Error: Docker is required for local infrastructure. Install Docker and Docker Compose." >&2
  exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
  echo "Error: docker compose is not available. Install Docker Compose v2." >&2
  exit 1
fi

if [[ "${1:-}" == "--verify" ]]; then
  echo "Running project verification..."
  ./gradlew test
else
  echo "Development environment prerequisites are available."
  echo "Run the following command to start local infrastructure:"
  echo "  ./scripts/dev-up.sh"
  echo "Run the following command to launch a service:"
  echo "  ./scripts/run-service.sh api-gateway"
fi

./scripts/dev-up.sh

echo "Development setup is ready."
